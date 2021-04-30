import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;

public class BrowserTests {
    private JUnitCore junit;

    @Before
    public void before() {
        junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));

    }

    @Test
    public void chrome() {
        Assert.assertTrue("Test was not successful",
                junit.run(browserTests.ChromeStartTest.class).wasSuccessful());
        Assert.assertTrue("Test was not successful",
                junit.run(browserTests.ChromeRestartTest.class).wasSuccessful());
    }

    @Test
    public void firefox() {
        Assert.assertTrue("Test was not successful",
                junit.run(browserTests.FirefoxStartTest.class).wasSuccessful());
        Assert.assertTrue("Test was not successful",
                junit.run(browserTests.FirefoxRestartTest.class).wasSuccessful());
    }

    @Test
    public void edge() {
        Assert.assertTrue("Test was not successful",
                junit.run(browserTests.EdgeStartTest.class).wasSuccessful());
        Assert.assertTrue("Test was not successful",
                junit.run(browserTests.EdgeRestartTest.class).wasSuccessful());
    }

    //TODO make the same for opera
}
