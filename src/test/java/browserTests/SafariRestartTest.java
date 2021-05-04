package browserTests;

import org.junit.runner.RunWith;

@RunWith(core.runner.RebroselRunner.class)
public class SafariRestartTest extends ParentRestartTest {

    public SafariRestartTest() {
        expectedToInclude = "https://www.google.com/gmail/";
    }
}

