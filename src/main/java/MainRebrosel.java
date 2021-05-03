import core.annotations.BrowserInitialization;
import core.annotations.OnBrowserStart;
import core.annotations.RebroselWebDriver;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(core.runner.RebroselRunner.class)
public class MainRebrosel {

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
        //ChromeDriver webDriver = new ChromeDriver();

        // Set up FF
        String absFFDriverPath = currentPath.toAbsolutePath().toString() + "/" + "src/main/resources/drivers/geckodriver.exe";
        System.setProperty("webdriver.gecko.driver", absFFDriverPath);
        FirefoxDriver webDriver = new FirefoxDriver();

        // Set up Edge
        String absEdgeDriverPath = currentPath.toAbsolutePath().toString() + "/" + "src/main/resources/drivers/msedgedriver.exe";
        System.setProperty("webdriver.edge.driver", absEdgeDriverPath);
        ChromeOptions options = new ChromeOptions();
        options.setBinary("C:/Windows/SystemApps/Microsoft.MicrosoftEdge_8wekyb3d8bbwe/MicrosoftEdge.exe");
        EdgeOptions edgeOptions = new EdgeOptions();
        //EdgeDriver webDriver = new EdgeDriver(edgeOptions);

        // Set up Opera
        String absOperaDriverPath = currentPath.toAbsolutePath().toString() + "/" + "src/main/resources/drivers/operadriver.exe";
        System.setProperty("webdriver.opera.driver", absOperaDriverPath);
        //OperaDriver webDriver = new OperaDriver();

        return webDriver;
    }

    @OnBrowserStart
    public static void onBrowserStart() {
        System.out.println("In onBrowserStart()");
        driver.get("http://www.google.com");
    }

    @BeforeClass
    public static void beforeClass() {
        System.out.println("driver: " + driver);
        System.out.println("BeforeClass");
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

    @Test
    public void test2() {
        driver.get("http://www.yahoo.com");
    }
}
