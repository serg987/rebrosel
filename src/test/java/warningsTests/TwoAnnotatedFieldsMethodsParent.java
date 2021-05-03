package warningsTests;

import core.annotations.BrowserInitialization;
import core.annotations.OnBrowserStart;
import core.annotations.RebroselWebDriver;
import org.openqa.selenium.WebDriver;

public class TwoAnnotatedFieldsMethodsParent {

    @RebroselWebDriver
    static WebDriver driver1;


    @BrowserInitialization
    public static WebDriver init1()
    {
        return null;
    }

    @OnBrowserStart
    public static void start1() {

    }
}
