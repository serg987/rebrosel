package browserTests;

import core.DriversForTests;
import core.annotations.BrowserInitialization;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(core.runner.RebroselRunner.class)
public class EdgeStartTest extends ParentStartTest {

    public EdgeStartTest() {
        expectedToInclude = "https://accounts.google.com/signin";
    }

    @BrowserInitialization
    public static WebDriver browserInit() {
        Path currentPath = Paths.get("");

        // Set up Edge
        String absEdgeDriverPath = currentPath.toAbsolutePath().toString() + "/" + DriversForTests.EDGE_DRIVER_PATH;
        System.setProperty("webdriver.edge.driver", absEdgeDriverPath);
        ChromeOptions options = new ChromeOptions();
        // Set up the binary path to exe file
      //  options.setBinary(DriversForTests.EDGE_EXEC_PATH);
        EdgeOptions edgeOptions = new EdgeOptions();

        return new EdgeDriver(edgeOptions);
    }
}

