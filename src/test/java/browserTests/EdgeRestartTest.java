package browserTests;

import org.junit.runner.RunWith;

@RunWith(core.runner.RebroselRunner.class)
public class EdgeRestartTest extends ParentRestartTest {
    public EdgeRestartTest() {
        expectedToInclude = "https://accounts.google.com/signin/";
    }
}

