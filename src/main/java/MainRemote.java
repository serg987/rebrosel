import core.LogHelper;
import core.WebDriverKiller;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
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
import java.util.Arrays;

import static core.WebDriverHelper.*;

public class MainRemote {
    private static RemoteWebDriver initializeWebDriver() {
        // Set up Chrome
        Path currentPath = Paths.get("");
        String chromeDriverPath = "src/main/resources/drivers/chromedriver.exe";
        String absChromeDriverPath = currentPath.toAbsolutePath().toString() + "/" + chromeDriverPath;
        System.out.println(absChromeDriverPath);
        System.setProperty("webdriver.chrome.driver", absChromeDriverPath);
        //ChromeDriver webDriver = new ChromeDriver();

        // Set up FF
        String absFFDriverPath = currentPath.toAbsolutePath().toString() + "/" + "src/main/resources/drivers/geckodriver.exe";
        System.setProperty("webdriver.gecko.driver", absFFDriverPath);
        //FirefoxDriver webDriver = new FirefoxDriver();

        // Set up Edge
        String absEdgeDriverPath = currentPath.toAbsolutePath().toString() + "/" + "src/main/resources/drivers/msedgedriver.exe";
        System.setProperty("webdriver.edge.driver", absEdgeDriverPath);
        ChromeOptions options = new ChromeOptions();
        options.setBinary("C:/Windows/SystemApps/Microsoft.MicrosoftEdge_8wekyb3d8bbwe/MicrosoftEdge.exe");
        EdgeOptions edgeOptions = new EdgeOptions();
        //EdgeDriver webDriver = new EdgeDriver(edgeOptions);

        // Set up Opera
        String absOperaDriverPath = currentPath.toAbsolutePath().toString() + "/" + "src/main/resources/drivers/operadriver.exe";
        System.setProperty("webdriver.opera.driver", absOperaDriverPath);
        OperaDriver webDriver = new OperaDriver();

        //  HttpCommandExecutor executor = (HttpCommandExecutor) driver.getCommandExecutor();

        //  URL url = executor.getAddressOfRemoteServer();
        URL url = getAddressOfRemoteServer(webDriver);
        // SessionId sessionId = driver.getSessionId();
        SessionId sessionId = getSessionId(webDriver);


        System.out.println(url);
        System.out.println(sessionId);
        // System.out.println(getDriverBrowserName(driver));
        return webDriver;
    }

    private static RemoteWebDriver initializeFramework() {
        RemoteWebDriver driver = loadBrowserSessionFromFileIfExists();

        // Opera webdriver doesn't throw any exception if browser is not working
        // so it should be treated differently
        if (driver == null || (isLoadedBrowserOpera() && !WebDriverKiller.isOperaBrowserWorking())) {
            driver = killWebdriverAndRestartBrowser();
        }

        boolean isBrowserKilled = false;

        // Trying to get whether other browsers are working by catching exceptions from their webdrivers.
        try {
            driver.getCurrentUrl();
        } catch (UnreachableBrowserException e) {
            LogHelper.logMessage("Webdriver is not reachable.");
            isBrowserKilled = true;
        } catch (WebDriverException e) {
            String message = Arrays.stream(e.getMessage().split("\\n")).findFirst().orElse("");
            isBrowserKilled = message.equalsIgnoreCase("chrome not reachable")
                    || message.equalsIgnoreCase("Failed to decode response from marionette")
                    || message.equalsIgnoreCase("Failed to write request to stream")
                    || (e.getClass().getCanonicalName().contains("NoSuchSessionException")
                        && message
                            .equalsIgnoreCase("Tried to run command without establishing a connection"));
            if (!isBrowserKilled) throw e;
        }

        if (isBrowserKilled) {
            LogHelper.logMessage("Will start a new browser session");
            driver = killWebdriverAndRestartBrowser();
        }

        LogHelper.logMessage("Framework initialization is done. You can continue to work.");
        return driver;
    }

    private static RemoteWebDriver killWebdriverAndRestartBrowser() {
        WebDriverKiller.killWebDriver();
        RemoteWebDriver driver = initializeWebDriver();
        saveBrowserDataToFile(driver);
        return driver;
    }

    public static void main(String[] args) throws MalformedURLException {

        WebDriver driver = initializeFramework();


        //System.out.println(driver.getCapabilities().asMap());
        // System.out.println(driver.getCapabilities().getPlatform());
        //  System.out.println(driver.getCapabilities().getVersion());
        // System.out.println(driver.getCapabilities().getBrowserName());


        // RemoteWebDriver driver1 = createDriverFromSession(sessionId, url);

        // driver1.quit();

        // System.out.println(((HttpCommandExecutor)driver.getCommandExecutor()).getAddressOfRemoteServer());
        //System.out.println(driver.getSessionId());


        driver.get("http://www.google.com");
        //driver.get("http://www.yahoo.com");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}


