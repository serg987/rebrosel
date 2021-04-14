package core.runner;

import core.LogHelper;
import core.annotations.BrowserInitialization;
import core.annotations.OnBrowserStart;
import core.annotations.RebroselWebDriver;
import org.junit.internal.runners.statements.Fail;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.*;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RebroselRunner extends BlockJUnit4ClassRunner {

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
    protected Statement withBeforeClasses(Statement statement)  {
        Statement initializationStatement = initializeFramework();

        if (initializationStatement != null) return initializationStatement;

        return super.withBeforeClasses(statement);
    }

    private void validate() throws InitializationError {
        List<Throwable> errors = new ArrayList<Throwable>();

        TestClass testClass = getTestClass();
        verifyOnStartBrowserMethod(testClass, errors);
        verifyBrowserInitializationMethod(testClass, errors);
        verifyWebDriverField(testClass, errors);

        if (!errors.isEmpty()) {
            throw new InitializationError(errors);
        }
    }

    private Statement initializeFramework() {
        LogHelper.logMessage("Initializing framework...");
        TestClass testClass = getTestClass();
        FrameworkMethod browserInitMethod = testClass.getAnnotatedMethods(BrowserInitialization.class).get(0);
        List<FrameworkMethod> onStartBrowserMethods = testClass.getAnnotatedMethods(OnBrowserStart.class);
        FrameworkMethod onStartBrowserMethod = onStartBrowserMethods.size() > 0 ? onStartBrowserMethods.get(0) : null;
        //Statement statement = invokeStaticMethod(testClass, browserInitMethod);
        //statement = invokeStaticMethod(testClass, onStartBrowserMethod);
        //setWebDriverField(testClass, "aaaa");
        return null;
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
        int modifier =  method.getMethod().getModifiers();
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

    private void setWebDriverField(TestClass clazz, WebDriver driver) {
        Field field = clazz.getAnnotatedFields(RebroselWebDriver.class).get(0).getField();
        boolean defaultAccessible = field.isAccessible();
        field.setAccessible(true);
        try {
            field.set(clazz.getJavaClass().newInstance(), driver);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        field.setAccessible(defaultAccessible);
    }

    private Statement setWebDriverAndInvokeStaticMethod(TestClass clazz, FrameworkMethod method) {
        Object obj;
        try {
            obj = method.getDeclaringClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return new Fail(e);
        }
        WebDriver driver;
        try {
            driver = (WebDriver) method.getMethod().invoke(obj);
        } catch (IllegalArgumentException | ReflectiveOperationException e) {
            return new Fail(e);
        }
        setWebDriverField(clazz, driver);

        return methodInvoker(method, obj);
    }
}
