package annotationstests;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import java.lang.invoke.MethodHandles;

@RunWith(CustomRunner.class)
public class BeforeTest {

    private static BeforeTest beforeTest;

    @RebroselFrameworkInitialization
    private static boolean init() {
        System.out.println("Running RebroselFrameworkInitialization");
        return false;
    }

/*    public BeforeTest() {
        System.out.println("BeforeTest constructor:" + beforeTest.getClass().getCanonicalName());
    }*/

    @BeforeClass
    public static void beforeClassInParent() {
        new BeforeTest().aaa();
        System.out.println("BeforeTest constructor:" + beforeTest.getClass().getCanonicalName());
        System.out.println("BeforeClass in Parent");
        System.out.println("BeforeClass in Parent: " + new Object(){}.getClass().getCanonicalName());
    }

    private static void executeOnBrowserStart() {
        System.out.println("");
    }

    protected void aaa() {
        beforeTest = this;
    }
}
