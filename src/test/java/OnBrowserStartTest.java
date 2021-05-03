import core.annotations.BrowserInitialization;
import core.annotations.OnBrowserStart;
import core.annotations.RebroselWebDriver;
import org.junit.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(core.runner.RebroselRunner.class)
public class OnBrowserStartTest {

    private static final String url = "https://www.google.com";

    @RebroselWebDriver
    static WebDriver driver;

    @BrowserInitialization
    public static WebDriver browserInit() {
        Path currentPath = Paths.get("");

        // Set up Chrome
        String chromeDriverPath = "src/main/resources/drivers/chromedriver.exe";
        String absChromeDriverPath = currentPath.toAbsolutePath().toString() + "/" + chromeDriverPath;
        System.out.println(absChromeDriverPath);
        System.setProperty("webdriver.chrome.driver", absChromeDriverPath);

        return new ChromeDriver();
    }

    @OnBrowserStart
    public static void onBrowserStart() {
        driver.get(url);
    }

    @Test
    public void test1() {
        System.out.println("Checking just started browser");
        Assert.assertTrue("Url does not have expected part: " + url,
                driver.getCurrentUrl().contains(url));
        RemoteWebDriver remoteWebDriver = (RemoteWebDriver) driver;
        System.out.println("Browser: '" + remoteWebDriver.getCapabilities().getBrowserName() + "'");
        driver.quit();
    }
}

