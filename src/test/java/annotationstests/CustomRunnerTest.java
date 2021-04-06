package annotationstests;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(CustomRunnerNew.class)
public class CustomRunnerTest {
    @RebroselWebDriver
    static String driver;

    @BrowserInitialization
    public static String browserInit() {
        System.out.println("driver: " + driver);
        System.out.println("In BrowserInit");
        return "Browser Initialized";
    }

    @OnBrowserStart
    public static String onBrowserStart() {
        System.out.println("driver: " + driver);
        System.out.println("In onBrowserStart");
        return "Browser Started";
    }

    @BeforeClass
    public static void beforeClassInChild() {
        System.out.println("driver: " + driver);
        System.out.println("BeforeClass in Child");
    }

    @Before
    public void before() {
        System.out.println("driver: " + driver);
        System.out.println("Before in child");
    }

    @Test
    public void test() {
        System.out.println("driver: " + driver);
        System.out.println("Test");
    }
}
