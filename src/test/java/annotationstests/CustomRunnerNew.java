package annotationstests;

import org.junit.internal.runners.statements.RunBefores;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
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
    }
    @Override
    protected Statement withBeforeClasses(Statement statement)  {
        System.out.println("New added before Class )))");
        initializeFramework();


/*        List<FrameworkMethod> initializationMethod = getTestClass()
                .getAnnotatedMethods(RebroselFrameworkInitialization.class);
        statement = new RunBefores(statement, initializationMethod, null);

        getTestClass().getAnnotatedMethods(RebroselFrameworkInitialization.class).stream().map(FrameworkMethod::getName).forEach(System.out::println);
        getTestClass().getAnnotatedMethods(OnBrowserStart.class).stream().map(FrameworkMethod::getName).forEach(System.out::println);
        System.out.println(getTestClass().getAnnotatedMethods());
        Arrays.stream(getTestClass().getAnnotations()).map(a -> a.annotationType().getName()).forEach(System.out::println);*/
        return super.withBeforeClasses(statement);
    }

    private void initializeFramework() {
        System.out.println("Initializing framework...");
        TestClass testClass = getTestClass();
        Method browserInitMethod = verifyAndGetBrowserInitializationMethod(testClass);
        Method onStartBrowserMethod = verifyAndGetOnStartBrowserMethod(testClass);
        verifyWebDriverField(testClass);
        setWebDriverField(testClass, "aaaa");
    }

    private Method verifyAndGetBrowserInitializationMethod(TestClass clazz) {
        List<FrameworkMethod> methods = clazz.getAnnotatedMethods(BrowserInitialization.class);
        if (methods.isEmpty()) System.out.println("No methods annotated with @BrowserInitialization found.");
        if (methods.size() > 1) System.out.println("Only one method annotated with Annotation @BrowserInitialization is allowed.");
        Method method = methods.get(0).getMethod();
        boolean isMethodProper = isMethodPublicStaticReturnsString(method);
        return isMethodProper ? method : null;
    }

    private Method verifyAndGetOnStartBrowserMethod(TestClass clazz) {
        List<FrameworkMethod> methods = clazz.getAnnotatedMethods(OnBrowserStart.class);
        if (methods.size() > 1) System.out.println("Only one method annotated with Annotation @OnBrowserStart is allowed.");
        if (methods.isEmpty()) return null;
        Method method = methods.get(0).getMethod();
        boolean isMethodProper = isMethodPublicStaticReturnsString(method);
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
        return false;
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
}
