package browserTests;

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
        String absEdgeDriverPath = currentPath.toAbsolutePath().toString() + "/" + "src/main/resources/drivers/msedgedriver.exe";
        System.setProperty("webdriver.edge.driver", absEdgeDriverPath);
        ChromeOptions options = new ChromeOptions();
        // Set up the binary path to exe file
        options.setBinary("C:/Windows/SystemApps/Microsoft.MicrosoftEdge_8wekyb3d8bbwe/MicrosoftEdge.exe");
        EdgeOptions edgeOptions = new EdgeOptions();

        return new EdgeDriver(edgeOptions);
    }
}

