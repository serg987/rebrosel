package annotationstests;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class LocalBeforeTest extends BeforeTest {


    @OnBrowserStart
    public void onBrowserStart() {
        System.out.println("On browser Start");
    }

    @BeforeClass
    public static void beforeClassInChild() {
        System.out.println("BeforeClass in Child");
    }

    @Before
    public void before() {
        System.out.println("Before in child");
    }

    @Test
    public void test() {
        System.out.println("Test");
    }

}
