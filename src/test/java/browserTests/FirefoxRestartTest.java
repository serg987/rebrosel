package browserTests;

import org.junit.runner.RunWith;

@RunWith(core.runner.RebroselRunner.class)
public class FirefoxRestartTest extends ParentRestartTest {

    public FirefoxRestartTest() {
        expectedToInclude = "https://www.google.com/";
    }
}

