package browserTests;

import core.DriversForTests;
import core.annotations.BrowserInitialization;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.opera.OperaDriver;

import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(core.runner.RebroselRunner.class)
public class OperaStartTest extends ParentStartTest {

    public OperaStartTest() {
        expectedToInclude = "https://www.google.com/gmail/";
    }

    @BrowserInitialization
    public static WebDriver browserInit() {
        Path currentPath = Paths.get("");

        // Set up Opera
        String absOperaDriverPath = DriversForTests.OPERA_DRIVER_PATH;
        System.setProperty("webdriver.opera.driver", absOperaDriverPath);

        return new OperaDriver();
    }
}

