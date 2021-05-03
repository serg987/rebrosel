package browserTests;

import org.junit.runner.RunWith;

@RunWith(core.runner.RebroselRunner.class)
public class ChromeRestartTest extends ParentRestartTest {

    public ChromeRestartTest() {
        expectedToInclude = "https://www.google.com/gmail/";
    }
}

