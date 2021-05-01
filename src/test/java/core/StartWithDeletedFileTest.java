package core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;

public class StartWithDeletedFileTest {
    private static JUnitCore junit;

    @BeforeClass
    public static void beforeClass() {
        junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
    }

    @Before
    public void beforeTest() {
        TempFileIO tempFileIO = new TempFileIO();
        TempFileIO.BrowserConnectionData data = (TempFileIO.BrowserConnectionData) PrivateMethodInvokerHelper
                .invokePrivateMethod(tempFileIO, "loadBrowserConnData", null);
        if (data != null) {
            System.out.println("Found file for webdriver: " + data.browserName + "; url: " + data.remoteAddress);
            WebDriverKiller.killWebDriver(data);
        } else {
            System.out.println("No file was found. Nothing to kill");
        }
        PrivateMethodInvokerHelper.invokePrivateMethod(tempFileIO,
                    "deleteTempFileIfExists",
                    null);
    }

    @Test
    public void chrome() {
        Assert.assertTrue("Test was not successful",
                junit.run(browserTests.ChromeStartTest.class).wasSuccessful());
        browserTests.ChromeStartTest.killWebDriver();
    }

    @Test
    public void firefox() {
        Assert.assertTrue("Test was not successful",
                junit.run(browserTests.FirefoxStartTest.class).wasSuccessful());
        browserTests.FirefoxStartTest.killWebDriver();
    }

    @Test
    public void edge() {
        Assert.assertTrue("Test was not successful",
                junit.run(browserTests.EdgeStartTest.class).wasSuccessful());
        browserTests.EdgeStartTest.killWebDriver();
    }

    @Test
    public void opera() {
        Assert.assertTrue("Test was not successful",
                junit.run(browserTests.OperaStartTest.class).wasSuccessful());
        browserTests.OperaStartTest.killWebDriver();
    }
}
