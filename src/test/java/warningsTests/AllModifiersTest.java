package warningsTests;

import core.annotations.BrowserInitialization;
import core.annotations.OnBrowserStart;
import core.annotations.RebroselWebDriver;
import core.runner.RebroselRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RebroselRunner.class)
public class AllModifiersTest {
    @RebroselWebDriver
    String driver;

    @BrowserInitialization
    private String init() {
        return "";
    }

    @OnBrowserStart
    private String start() {
        return "";
    }

    @Test
    public void test() {

    }
}
