import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static core.WebDriverHelper.*;

public class MainRemote {
    public static void main(String[] args) throws MalformedURLException {

        // Set up Chrome
        Path currentPath = Paths.get("");
        String chromeDriverPath = "src/main/resources/drivers/chromedriver.exe";
        String absChromeDriverPath = currentPath.toAbsolutePath().toString() + "/" + chromeDriverPath;
        System.out.println(absChromeDriverPath);
        System.setProperty("webdriver.chrome.driver", absChromeDriverPath);
        //ChromeDriver driver = new ChromeDriver();

        // Set up FF
        String absFFDriverPath = currentPath.toAbsolutePath().toString() + "/" + "src/main/resources/drivers/geckodriver.exe";
        System.setProperty("webdriver.gecko.driver", absFFDriverPath);
        //FirefoxDriver driver = new FirefoxDriver();

        // Set up Edge
        String absEdgeDriverPath = currentPath.toAbsolutePath().toString() + "/" + "src/main/resources/drivers/msedgedriver.exe";
        System.setProperty("webdriver.edge.driver", absEdgeDriverPath);
        ChromeOptions options = new ChromeOptions();
        options.setBinary("C:/Windows/SystemApps/Microsoft.MicrosoftEdge_8wekyb3d8bbwe/MicrosoftEdge.exe");
        EdgeOptions edgeOptions = new EdgeOptions();
        EdgeDriver driver = new EdgeDriver(edgeOptions);

        // Set up Opera
        String absOperaDriverPath = currentPath.toAbsolutePath().toString() + "/" + "src/main/resources/drivers/operadriver.exe";
        System.setProperty("webdriver.opera.driver", absOperaDriverPath);
       // OperaDriver driver = new OperaDriver();

      //  HttpCommandExecutor executor = (HttpCommandExecutor) driver.getCommandExecutor();

      //  URL url = executor.getAddressOfRemoteServer();
        URL url = getAddressOfRemoteServer(driver);
       // SessionId sessionId = driver.getSessionId();
        SessionId sessionId = getSessionId(driver);



        System.out.println(url);
        System.out.println(sessionId);
       // System.out.println(getDriverBrowserName(driver));




        RemoteWebDriver driver1 = createDriverFromSession(sessionId, url);

       // driver1.quit();

       // System.out.println(((HttpCommandExecutor)driver.getCommandExecutor()).getAddressOfRemoteServer());
       //System.out.println(driver.getSessionId());


        driver1.get("http://www.google.com");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}


