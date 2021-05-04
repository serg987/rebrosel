package browserTests;

import core.annotations.BrowserInitialization;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.nio.file.Path;
import java.nio.file.Paths;

import static browserTests.DriversForTests.*;

@RunWith(core.runner.RebroselRunner.class)
public class ChromeStartTest extends ParentStartTest {

    public ChromeStartTest() {
        expectedToInclude = "https://www.google.com/gmail/";
    }

    @BrowserInitialization
    public static WebDriver browserInit() {
        Path currentPath = Paths.get("");

        // Set up Chrome
        String chromeDriverPath = CHROME_DRIVER_PATH;
        String absChromeDriverPath = currentPath.toAbsolutePath().toString() + "/" + chromeDriverPath;
        System.out.println(absChromeDriverPath);
        System.setProperty("webdriver.chrome.driver", absChromeDriverPath);

        return new ChromeDriver();
    }
}

