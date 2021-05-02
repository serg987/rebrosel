package core;

import org.junit.Test;

import static org.junit.Assert.*;

public class WebDriverKillerTest {

    @Test
    public void killWebDriver() {
        final String testUrl = "http://localhost:26538";
        final String sessionID = "0871996af02bcf5a3ae3101caea8579a";
        final String browserName = "chrome";
        WebDriverKiller.killWebDriver(TempFileIO.createBrowserData(testUrl,sessionID, browserName));
    }

    @Test
    public void isOperaBrowserWorking() {
        boolean isOpera = WebDriverKiller.isOperaBrowserWorking();
        System.out.println(isOpera);
    }
}