package warningsTests;

import core.annotations.BrowserInitialization;
import core.annotations.OnBrowserStart;
import core.annotations.RebroselWebDriver;
import core.runner.RebroselRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(RebroselRunner.class)
public class OneModifierTest {
    @RebroselWebDriver
    public WebDriver driver;

    @BrowserInitialization
    public static String init() {
        return "";
    }

    @OnBrowserStart
    private static void start() {

    }

    @Test
    public void test() {

    }
}
