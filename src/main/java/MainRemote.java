import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.remote.http.W3CHttpCommandCodec;
import org.openqa.selenium.remote.http.W3CHttpResponseCodec;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

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
        System.out.println(getDriverBrowserName(driver));




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
    public static RemoteWebDriver createDriverFromSession(final SessionId sessionId, URL command_executor){
        // the whole method is taken from https://tarunlalwani.com/post/reusing-existing-browser-session-selenium-java/
        // poster - TARUN LALWANI https://github.com/tarunlalwani
        CommandExecutor executor = new HttpCommandExecutor(command_executor) {

            @Override
            public Response execute(Command command) throws IOException {
                Response response;
                if (command.getName() == "newSession") {
                    response = new Response();
                    response.setSessionId(sessionId.toString());
                    response.setStatus(0);
                    response.setValue(Collections.<String, String>emptyMap());

                    try {
                        Field commandCodec;
                        commandCodec = this.getClass().getSuperclass().getDeclaredField("commandCodec");
                        commandCodec.setAccessible(true);
                        commandCodec.set(this, new W3CHttpCommandCodec());

                        Field responseCodec;
                        responseCodec = this.getClass().getSuperclass().getDeclaredField("responseCodec");
                        responseCodec.setAccessible(true);
                        responseCodec.set(this, new W3CHttpResponseCodec());
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                } else {
                    response = super.execute(command);
                }
                return response;
            }
        };

        return new RemoteWebDriver(executor, new DesiredCapabilities());
    }


    public static URL getAddressOfRemoteServer(WebDriver driver) {
        HttpCommandExecutor executor = invokeMethodInWebDriver(driver, "getCommandExecutor");

        return executor.getAddressOfRemoteServer();
    }

    public static SessionId getSessionId(WebDriver driver) {
        return invokeMethodInWebDriver(driver, "getSessionId");
    }

    private static <T> T invokeMethodInWebDriver(WebDriver driver, String methodName) {
        T out = null;
        Method[] methods = driver.getClass().getMethods();

        java.util.Optional<Method> invokingMethodOpt = Arrays.stream(methods).filter(method -> {
            String[] methodWords = method.getName().split("\\.");
            return methodWords[methodWords.length - 1].equals(methodName);
        }).findFirst();

        if (invokingMethodOpt.isPresent()) {
            Method invokingMethod = invokingMethodOpt.get();
            try {
                out = (T) invokingMethod.invoke(driver);
            } catch (IllegalAccessException | InvocationTargetException e) {
                System.out.println("Error while invoking " + methodName + "() in " + driver.toString());
            }
        }

        return out;
    }

    private static String getDriverBrowserName(WebDriver driver) {
        return Arrays.stream(driver.getClass().getName().split("\\."))
                .reduce((s1, s2) -> s2).get()
                .replace("Driver", "");
    }
}


