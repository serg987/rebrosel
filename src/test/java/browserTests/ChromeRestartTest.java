package browserTests;

import core.annotations.BrowserInitialization;
import core.annotations.RebroselWebDriver;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;


@RunWith(core.runner.RebroselRunner.class)
public class ChromeRestartTest {

    @RebroselWebDriver
    static WebDriver driver;

    @BrowserInitialization
    public static WebDriver browserInit() {
        return null;
    }

    @Test
    public void test1() {
        System.out.println("Checking restarted browser");
        String expectedToInclude = "https://www.google.com/gmail/";
        Assert.assertTrue("Url does not have expected part: " + expectedToInclude,
                driver.getCurrentUrl().contains(expectedToInclude));
        RemoteWebDriver remoteWebDriver = (RemoteWebDriver) driver;
        System.out.println("Browser: '" + remoteWebDriver.getCapabilities().getBrowserName() + "'");
        driver.quit();
    }
}

