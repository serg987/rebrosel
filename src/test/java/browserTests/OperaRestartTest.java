package browserTests;

import org.junit.runner.RunWith;

@RunWith(core.runner.RebroselRunner.class)
public class OperaRestartTest extends ParentRestartTest {

    public OperaRestartTest() {
        expectedToInclude = "https://www.google.com/gmail/";
    }
}