package annotationstests;

import org.junit.internal.runners.statements.Fail;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CustomRunnerNew extends BlockJUnit4ClassRunner {

    /**
     * Creates a BlockJUnit4ClassRunner to run {@code klass}
     *
     * @param klass
     * @throws InitializationError if the test class is malformed.
     */
    public CustomRunnerNew(Class<?> klass) throws InitializationError {
        super(klass);
        validate();
    }
    @Override
    protected Statement withBeforeClasses(Statement statement)  {
       // System.out.println("New added before Class )))");
        //statement =
        initializeFramework();

        try {
            throw new IllegalArgumentException("aaaa");
        } catch (IllegalArgumentException e) {
            statement = new Fail(e);
        }


/*        List<FrameworkMethod> initializationMethod = getTestClass()
                .getAnnotatedMethods(RebroselFrameworkInitialization.class);
        statement = new RunBefores(statement, initializationMethod, null);

        getTestClass().getAnnotatedMethods(RebroselFrameworkInitialization.class).stream().map(FrameworkMethod::getName).forEach(System.out::println);
        getTestClass().getAnnotatedMethods(OnBrowserStart.class).stream().map(FrameworkMethod::getName).forEach(System.out::println);
        System.out.println(getTestClass().getAnnotatedMethods());
        Arrays.stream(getTestClass().getAnnotations()).map(a -> a.annotationType().getName()).forEach(System.out::println);*/
        return super.withBeforeClasses(statement);
    }

    private void validate() throws InitializationError {
        TestClass testClass = getTestClass();
        verifyBrowserInitializationMethod(testClass);
        verifyOnStartBrowserMethod(testClass);
        verifyWebDriverField(testClass);
    }

    private Statement initializeFramework() {
        System.out.println("Initializing framework...");
        TestClass testClass = getTestClass();
        FrameworkMethod browserInitMethod = testClass.getAnnotatedMethods(BrowserInitialization.class).get(0);
        List<FrameworkMethod> onStartBrowserMethods = testClass.getAnnotatedMethods(OnBrowserStart.class);
        FrameworkMethod onStartBrowserMethod = onStartBrowserMethods.size() > 0 ? onStartBrowserMethods.get(0) : null;
        //Statement statement = invokeStaticMethod(testClass, browserInitMethod);
        //statement = invokeStaticMethod(testClass, onStartBrowserMethod);
        //setWebDriverField(testClass, "aaaa");
        return null;
    }

    private void verifyBrowserInitializationMethod(TestClass clazz) throws InitializationError {
        List<FrameworkMethod> methods = clazz.getAnnotatedMethods(BrowserInitialization.class);
        if (methods.isEmpty()) throw
                new InitializationError("No methods annotated with @BrowserInitialization found.");
        if (methods.size() > 1) throw
                new InitializationError("Only one method annotated with Annotation " +
                        "@BrowserInitialization is allowed.");
        verifyMethodPublicStaticNoArgsReturnsString(methods.get(0));
    }

    private void verifyOnStartBrowserMethod(TestClass clazz) throws InitializationError {
        List<FrameworkMethod> methods = clazz.getAnnotatedMethods(OnBrowserStart.class);
        if (methods.size() > 1) throw
                new InitializationError("Only one method annotated with Annotation " +
                        "@BrowserInitialization is allowed.");
        if (!methods.isEmpty()) verifyMethodPublicStaticNoArgsReturnsString(methods.get(0));
    }

    private void verifyMethodPublicStaticNoArgsReturnsString(FrameworkMethod method) throws InitializationError {
        int modifier =  method.getMethod().getModifiers();
        StringBuilder builder = new StringBuilder("Method '" + method.getName() + "' should");
        List<String> errors = new ArrayList<>();
        if (!Modifier.isStatic(modifier)) {
            errors.add("be static");
        }
        if (!Modifier.isPublic(modifier)) {
            errors.add("be public");
        }
        if (!method.getReturnType().equals(String.class)) {
            errors.add("return String");
        }
        if (method.getMethod().getParameterTypes().length > 0) {
            errors.add("have no arguments");
        }
        throwError(builder, errors);
    }

    private void verifyWebDriverField(TestClass clazz) throws InitializationError {
        List<FrameworkField> fields = clazz.getAnnotatedFields(RebroselWebDriver.class);
        if (fields.isEmpty()) throw
                new InitializationError("No fields annotated with @RebroselWebDriver found.");
        if (fields.size() > 1) throw
                new InitializationError("Only one field annotated with Annotation " +
                        "@RebroselWebDriver is allowed.");
        Field field = fields.get(0).getField();
        Class fieldClass = field.getType();
        StringBuilder builder = new StringBuilder("Fields annotated with @RebroselWebDriver should");
        List<String> errors = new ArrayList<>();
        int modifier = field.getModifiers();
        if (!Modifier.isStatic(modifier)) errors.add("be static");
        if (fieldClass != String.class) errors.add("be a String class");
        throwError(builder, errors);
    }

    private void throwError(StringBuilder error, List<String> errors) throws InitializationError {
        if (!errors.isEmpty()) {
            Iterator<String> iterator = errors.iterator();
            while (iterator.hasNext()) {
                error.append(" ").append(iterator.next()).append(iterator.hasNext() ? "," : ".");
            }
            throw new InitializationError(error.toString());
        }
    }

    private void setWebDriverField(TestClass clazz, String str) {
        Field field = clazz.getAnnotatedFields(RebroselWebDriver.class).get(0).getField();
        boolean defaultAccessible = field.isAccessible();
        field.setAccessible(true);
        try {
            field.set(clazz.getJavaClass().newInstance(), str);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        field.setAccessible(defaultAccessible);
    }

    private Statement invokeStaticMethod(TestClass clazz, FrameworkMethod method) {
        Object obj;
        try {
            obj = method.getDeclaringClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return new Fail(e);
        }
        String str;
        try {
            str = (String) method.getMethod().invoke(obj);
        } catch (IllegalArgumentException | ReflectiveOperationException e) {
            return new Fail(e);
        }
        setWebDriverField(clazz, str);

        return methodInvoker(method, obj);
    }
}
