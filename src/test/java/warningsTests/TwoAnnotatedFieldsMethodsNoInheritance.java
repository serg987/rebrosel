package warningsTests;

import core.annotations.BrowserInitialization;
import core.annotations.OnBrowserStart;
import core.annotations.RebroselWebDriver;
import core.runner.RebroselRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(RebroselRunner.class)
public class TwoAnnotatedFieldsMethodsNoInheritance {

    @RebroselWebDriver
    static WebDriver driver;

    @RebroselWebDriver
    static WebDriver driver1;


    @BrowserInitialization
    public static WebDriver init()
    {
        return null;
    }

    @BrowserInitialization
    public static WebDriver init1()
    {
        return null;
    }

    @OnBrowserStart
    public static void start() {

    }

    @OnBrowserStart
    public static void start1() {

    }

    @Test
    public void test() {

    }
}
