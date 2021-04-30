package browserTests;

import core.annotations.BrowserInitialization;
import core.annotations.OnBrowserStart;
import core.annotations.RebroselWebDriver;
import org.junit.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(core.runner.RebroselRunner.class)
public class ChromeStartTest {

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
        ChromeDriver webDriver = new ChromeDriver();

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
        String expectedToInclude = "https://www.google.com/gmail/";
        Assert.assertTrue("Url does not have expected part: " + expectedToInclude,
                driver.getCurrentUrl().contains(expectedToInclude));
    }
}

