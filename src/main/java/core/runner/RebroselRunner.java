package core.runner;

import core.LogHelper;
import core.TempFileIO;
import core.WebDriverKiller;
import core.annotations.BrowserInitialization;
import core.annotations.OnBrowserStart;
import core.annotations.RebroselWebDriver;
import org.junit.internal.runners.statements.Fail;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static core.WebDriverHelper.*;

public class RebroselRunner extends BlockJUnit4ClassRunner {

    private WebDriver webDriver;
    private TestClass testClass;
    private FrameworkMethod browserInitMethod;
    private FrameworkMethod onStartBrowserMethod;
    private boolean isErrorInInitialization = false;

    /**
     * Creates a BlockJUnit4ClassRunner to run {@code klass}
     *
     * @param klass
     * @throws InitializationError if the test class is malformed.
     */
    public RebroselRunner(Class<?> klass) throws InitializationError {
        super(klass);
        validate();
    }

    @Override
    protected Statement withBeforeClasses(Statement statement) {
        Statement initializationStatement = initializeFramework();
        isErrorInInitialization = initializationStatement != null;
        if (isErrorInInitialization) {
            return initializationStatement;
        }

        return super.withBeforeClasses(statement);
    }

    /**
     *
     * Prevent {@code @AfterClass} from execution if there was an error in initialization
     * (both in {@code @BrowserInitialization} and {@code @OnBrowserStart}
     */
    @Override
    protected Statement withAfterClasses(Statement statement) {
        if (!isErrorInInitialization) return super.withAfterClasses(statement);
        return statement;
    }

    private void validate() throws InitializationError {
        List<Throwable> errors = new ArrayList<>();

        testClass = getTestClass();
        verifyOnStartBrowserMethod(testClass, errors);
        verifyBrowserInitializationMethod(testClass, errors);
        verifyWebDriverField(testClass, errors);

        if (!errors.isEmpty()) {
            isErrorInInitialization = true;
            throw new InitializationError(errors);
        }
    }

    private Statement initializeFramework() {
        LogHelper.logMessage("Initializing framework...");
        TestClass testClass = getTestClass();
        browserInitMethod = testClass.getAnnotatedMethods(BrowserInitialization.class).get(0);
        List<FrameworkMethod> onStartBrowserMethods = testClass.getAnnotatedMethods(OnBrowserStart.class);
        onStartBrowserMethod = onStartBrowserMethods.size() > 0 ? onStartBrowserMethods.get(0) : null;
        Statement statement = setInitializedWebDriver();

        return statement;
    }

    private void verifyBrowserInitializationMethod(TestClass clazz, List<Throwable> errors) {
        List<FrameworkMethod> methods = clazz.getAnnotatedMethods(BrowserInitialization.class);
        if (methods.isEmpty()) errors.add(new Exception("No methods annotated with @BrowserInitialization found."));
        if (methods.size() > 1) errors.add(new Exception("Only one method annotated with Annotation " +
                "@BrowserInitialization is allowed."));
        if (methods.size() == 1) verifyMethodPublicStaticNoArgsReturnsWebDriver(methods.get(0), errors);
    }

    private void verifyOnStartBrowserMethod(TestClass clazz, List<Throwable> errors) {
        List<FrameworkMethod> methods = clazz.getAnnotatedMethods(OnBrowserStart.class);
        if (methods.size() > 1) errors.add(new Exception("Only one method annotated with Annotation " +
                "@BrowserInitialization is allowed."));
        if (!methods.isEmpty()) validatePublicVoidNoArgMethods(OnBrowserStart.class, true, errors);
    }

    private void verifyMethodPublicStaticNoArgsReturnsWebDriver(FrameworkMethod method,
                                                                List<Throwable> errors) {
        int modifier = method.getMethod().getModifiers();
        StringBuilder builder = new StringBuilder("Method '" + method.getName() + "' should");
        List<String> errorStrings = new ArrayList<>();
        if (!Modifier.isStatic(modifier)) {
            errorStrings.add("be static");
        }
        if (!Modifier.isPublic(modifier)) {
            errorStrings.add("be public");
        }
        if (!method.getReturnType().equals(WebDriver.class)) {
            errorStrings.add("return org.openqa.selenium.WebDriver");
        }
        if (method.getMethod().getParameterTypes().length > 0) {
            errorStrings.add("have no arguments");
        }
        addError(builder, errorStrings, errors);
    }

    private void verifyWebDriverField(TestClass clazz,
                                      List<Throwable> errors) {
        List<FrameworkField> fields = clazz.getAnnotatedFields(RebroselWebDriver.class);
        if (fields.isEmpty()) errors.add(new Exception("No fields annotated with @RebroselWebDriver found."));
        if (fields.size() > 1) errors.add(new Exception("Only one field annotated with Annotation " +
                "@RebroselWebDriver is allowed."));
        if (fields.size() == 1) {
            Field field = fields.get(0).getField();
            Class fieldClass = field.getType();
            StringBuilder builder = new StringBuilder("Fields annotated with @RebroselWebDriver should");
            List<String> errorStrings = new ArrayList<>();
            int modifier = field.getModifiers();
            if (!Modifier.isStatic(modifier)) errorStrings.add("be static");
            if (fieldClass != WebDriver.class) errorStrings.add("be a org.openqa.selenium.WebDriver class");
            addError(builder, errorStrings, errors);
        }
    }

    private void addError(StringBuilder error,
                          List<String> errorStrings,
                          List<Throwable> errors) {
        if (!errorStrings.isEmpty()) {
            Iterator<String> iterator = errorStrings.iterator();
            while (iterator.hasNext()) {
                error.append(" ").append(iterator.next()).append(iterator.hasNext() ? "," : ".");
            }
            errors.add(new Exception(error.toString()));
        }
    }

    private static Statement setWebDriverField(TestClass clazz, WebDriver driver) {
        Field field = clazz.getAnnotatedFields(RebroselWebDriver.class).get(0).getField();
        boolean defaultAccessible = field.isAccessible();
        field.setAccessible(true);
        try {
            field.set(clazz.getJavaClass().newInstance(), driver);
        } catch (IllegalAccessException | InstantiationException e) {
            return new Fail(e);
        }
        field.setAccessible(defaultAccessible);

        return null;
    }

    private Statement killWebdriverAndRestartBrowser() {
        WebDriverKiller.killWebDriver(TempFileIO.getBrowserData());
        Statement statement = restartBrowserAndDoOnStart();
        if (statement == null) {
            TempFileIO.saveBrowserDataToFile((RemoteWebDriver) webDriver);
        }
        return statement;
    }

    private Statement restartBrowserAndDoOnStart() {
        Object obj;
        try {
            obj = browserInitMethod.getDeclaringClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return new Fail(e);
        }
        try {
            webDriver = (WebDriver) browserInitMethod.getMethod().invoke(obj);
        } catch (IllegalArgumentException | ReflectiveOperationException e) {
            return new Fail(e);
        }
        Statement statement = setWebDriverField(testClass, webDriver);
        if (onStartBrowserMethod != null) {
            try {
                onStartBrowserMethod.getMethod().invoke(obj);
            } catch (IllegalArgumentException | ReflectiveOperationException e) {
                return new Fail(e);
            }
        }

        return statement;
    }

    private Statement setInitializedWebDriver() {
        Statement statement = null;
        boolean isBrowserColdStarted = false;

        webDriver = loadBrowserSessionFromFileIfExists();

        // Opera webdriver doesn't throw any exception if browser is not working
        // so it should be treated differently
        if (webDriver == null || (isLoadedBrowserOpera() && !WebDriverKiller.isOperaBrowserWorking())) {
            statement = killWebdriverAndRestartBrowser();
            if (statement != null) {
                return statement;
            }
            isBrowserColdStarted = true;
        }

        boolean isBrowserKilled = false;

        // Trying to get whether other browsers are working by catching exceptions from their webdrivers.
        try {
            if (webDriver != null && !isBrowserColdStarted) webDriver.getCurrentUrl();
        } catch (UnreachableBrowserException e) {
            LogHelper.logMessage("Webdriver is not reachable.");
            isBrowserKilled = true;
        } catch (WebDriverException e) {
            String message = Arrays.stream(e.getMessage().split("\\n")).findFirst().orElse("");
            isBrowserKilled = message.equalsIgnoreCase("chrome not reachable")
                    || message.equalsIgnoreCase("Failed to decode response from marionette")
                    || message.equalsIgnoreCase("Failed to write request to stream")
                    || ((e.getClass().getCanonicalName().contains("NoSuchSessionException")
                    && (message
                    .equalsIgnoreCase("Tried to run command without establishing a connection"))
                    || message.equalsIgnoreCase("invalid session id")));
            if (!isBrowserKilled) throw e;
        }

        if (isBrowserKilled) {
            LogHelper.logMessage("Will start a new browser session");
            statement = killWebdriverAndRestartBrowser();
        } else {
            setWebDriverField(testClass, webDriver);
        }

        if (statement == null) {
            LogHelper.logMessage("Framework initialization is done. You can continue to work.");
        } else {
            LogHelper.logError("Framework initialization is failed.");
        }
        return statement;
    }
}
