package browserTests;

import core.annotations.OnBrowserStart;
import core.annotations.RebroselWebDriver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

public class ParentStartTest {

    protected String expectedToInclude;

    @RebroselWebDriver
    static WebDriver driver;

    @OnBrowserStart
    public static void onBrowserStart() {
        driver.get("http://www.google.com");
    }

    @BeforeClass
    public static void beforeClass() {
        driver.get("https://www.google.com/imghp");
    }

    @Before
    public void before() {
        driver.get("https://www.gmail.com");
    }

    @Test
    public void test1() {
        System.out.println("Checking just started browser");
        Assert.assertTrue("Url does not have expected part: " + expectedToInclude,
                driver.getCurrentUrl().contains(expectedToInclude));
    }

    public static void killWebDriver() {
        driver.quit();
    }
}
