package browserTests;

import core.annotations.BrowserInitialization;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.nio.file.Path;
import java.nio.file.Paths;

import static core.DriversForTests.CHROME_DRIVER_PATH;

@RunWith(core.runner.RebroselRunner.class)
public class SafariStartTest extends ParentStartTest {

    public SafariStartTest() {
        expectedToInclude = "https://www.google.com/gmail/";
    }

    @BrowserInitialization
    public static WebDriver browserInit() {

        return new SafariDriver();
    }
}

