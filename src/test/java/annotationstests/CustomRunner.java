package annotationstests;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

public class CustomRunner extends BlockJUnit4ClassRunner {

    /**
     * Creates a BlockJUnit4ClassRunner to run {@code klass}
     *
     * @param klass
     * @throws InitializationError if the test class is malformed.
     */
    public CustomRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }
    @Override
    protected Statement withBeforeClasses(Statement statement) {
        System.out.println("New added before Class )))");
        TestClass clazz = getTestClass();
        System.out.println(getTestClass().getName());

        List<FrameworkMethod> initializationMethod = getTestClass()
                .getAnnotatedMethods(RebroselFrameworkInitialization.class);
        statement = new RunBefores(statement, initializationMethod, null);

        getTestClass().getAnnotatedMethods(RebroselFrameworkInitialization.class).stream().map(FrameworkMethod::getName).forEach(System.out::println);
        getTestClass().getAnnotatedMethods(OnBrowserStart.class).stream().map(FrameworkMethod::getName).forEach(System.out::println);
        System.out.println(getTestClass().getAnnotatedMethods());
        Arrays.stream(getTestClass().getAnnotations()).map(a -> a.annotationType().getName()).forEach(System.out::println);
        return super.withBeforeClasses(statement);
    }
}
