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
        System.out.println("New added before Class )))");
      //  statement = initializeFramework();


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
    }

    private Statement initializeFramework() {
        System.out.println("Initializing framework...");
        TestClass testClass = getTestClass();
        FrameworkMethod browserInitMethod = verifyAndGetBrowserInitializationMethod(testClass);
        FrameworkMethod onStartBrowserMethod = verifyAndGetOnStartBrowserMethod(testClass);
        Statement statement = invokeStaticMethod(testClass, browserInitMethod);
        statement = invokeStaticMethod(testClass, onStartBrowserMethod);
        verifyWebDriverField(testClass);
        //setWebDriverField(testClass, "aaaa");
        return statement;
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


    private FrameworkMethod verifyAndGetBrowserInitializationMethod(TestClass clazz) {
        List<FrameworkMethod> methods = clazz.getAnnotatedMethods(BrowserInitialization.class);
        if (methods.isEmpty()) System.out.println("No methods annotated with @BrowserInitialization found.");
        if (methods.size() > 1) System.out.println("Only one method annotated with Annotation @BrowserInitialization is allowed.");
        FrameworkMethod method = methods.get(0);
        boolean isMethodProper = isMethodPublicStaticReturnsString(method.getMethod());
        return isMethodProper ? method : null;
    }

    private FrameworkMethod verifyAndGetOnStartBrowserMethod(TestClass clazz) {
        List<FrameworkMethod> methods = clazz.getAnnotatedMethods(OnBrowserStart.class);
        if (methods.size() > 1) System.out.println("Only one method annotated with Annotation @OnBrowserStart is allowed.");
        if (methods.isEmpty()) return null;
        FrameworkMethod method = methods.get(0);
        boolean isMethodProper = isMethodPublicStaticReturnsString(method.getMethod());
        return isMethodProper ? method : null;
    }

    private boolean isMethodPublicStaticReturnsString(Method method) {
        int modifier =  method.getModifiers();
        if (!Modifier.isStatic(modifier)) {
            System.out.println("Method '" + method.getName() + "' should be static");
        }
        if (!Modifier.isPublic(modifier)) {
            System.out.println("Method '" + method.getName() + "' should be public");
        }
        if (!method.getReturnType().equals(String.class)) {
            System.out.println("Method '" + method.getName() + "' should return String");
        }
        return true;
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
        if (!errors.isEmpty()) {
            Iterator<String> iterator = errors.iterator();
            while (iterator.hasNext()) {
                builder.append(" ").append(iterator.next()).append(iterator.hasNext() ? "," : ".");
            }
            throw new InitializationError(builder.toString());
        }
    }

    private void verifyWebDriverField(TestClass clazz) {
        List<FrameworkField> fields = clazz.getAnnotatedFields(RebroselWebDriver.class);
        if (fields.isEmpty()) System.out.println("No fields annotated with @RebroselWebDriver found.");
        if (fields.size() > 1) System.out.println("Only one field annotated with Annotation @RebroselWebDriver is allowed.");
        Field field = fields.get(0).getField();
        int modifier = field.getModifiers();
        if (!Modifier.isStatic(modifier)) System.out.println("Fields annotated with @RebroselWebDriver should be static.");
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
