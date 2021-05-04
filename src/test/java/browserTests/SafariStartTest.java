package browserTests;

import core.annotations.BrowserInitialization;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;

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

