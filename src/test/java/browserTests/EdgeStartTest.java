package browserTests;

import core.annotations.BrowserInitialization;
import core.annotations.OnBrowserStart;
import core.annotations.RebroselWebDriver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(core.runner.RebroselRunner.class)
public class EdgeStartTest {

    @RebroselWebDriver
    static WebDriver driver;

    @BrowserInitialization
    public static WebDriver browserInit() {
        Path currentPath = Paths.get("");

        // Set up Edge
        String absEdgeDriverPath = currentPath.toAbsolutePath().toString() + "/" + "src/main/resources/drivers/msedgedriver.exe";
        System.setProperty("webdriver.edge.driver", absEdgeDriverPath);
        ChromeOptions options = new ChromeOptions();
        // Set up the binary path to exe file
        options.setBinary("C:/Windows/SystemApps/Microsoft.MicrosoftEdge_8wekyb3d8bbwe/MicrosoftEdge.exe");
        EdgeOptions edgeOptions = new EdgeOptions();
        EdgeDriver webDriver = new EdgeDriver(edgeOptions);

        return webDriver;
    }

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
        String expectedToInclude = "https://accounts.google.com/signin";
        Assert.assertTrue("Url does not have expected part: " + expectedToInclude,
                driver.getCurrentUrl().contains(expectedToInclude));
    }

    public static void killWebDriver() {
        driver.quit();
    }
}

