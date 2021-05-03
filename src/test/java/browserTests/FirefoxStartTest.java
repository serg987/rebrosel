package browserTests;

import core.annotations.BrowserInitialization;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(core.runner.RebroselRunner.class)
public class FirefoxStartTest extends ParentStartTest {

    public FirefoxStartTest() {
        expectedToInclude = "https://www.google.com/gmail/";
    }

    @BrowserInitialization
    public static WebDriver browserInit() {
        Path currentPath = Paths.get("");

        // Set up FF
        String absFFDriverPath = currentPath.toAbsolutePath().toString() + "/" +
                "src/main/resources/drivers/geckodriver.exe";
        System.setProperty("webdriver.gecko.driver", absFFDriverPath);

        return new FirefoxDriver();
    }
}

