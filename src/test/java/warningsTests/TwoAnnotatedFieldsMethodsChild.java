package warningsTests;

import core.annotations.BrowserInitialization;
import core.annotations.OnBrowserStart;
import core.annotations.RebroselWebDriver;
import core.runner.RebroselRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(RebroselRunner.class)
public class TwoAnnotatedFieldsMethodsChild extends TwoAnnotatedFieldsMethodsParent {

    @RebroselWebDriver
    static WebDriver driver;


    @BrowserInitialization
    public static WebDriver init()
    {
        return null;
    }

    @OnBrowserStart
    public static void start() {

    }

    @Test
    public void test() {

    }
}
