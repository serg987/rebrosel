package core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FileTests {
    private static final String testUrl = "http://localhost:12784";
    private static final String sessionID = "0871996af02bcf5a3ae3101caea8579a";
    private static final String browserName = "chrome";

    @Before
    public void beforeTest() {
        System.out.println(" *** Test started ***");
    }

    @Test
    public void printErrorTest() {
        TempFileIO tempFileIO = new TempFileIO();

        List<Object> parameters = new ArrayList<>();
        parameters.add("do anything - it is a test of");

        PrivateMethodInvokerHelper.invokePrivateMethod(tempFileIO, "printError", parameters);
    }

    @Test
    public void createBrowserDataTest() {
        TempFileIO tempFileIO = new TempFileIO();
        List<Object> parameters = new ArrayList<>();
        parameters.add(testUrl);
        parameters.add(sessionID);
        parameters.add(browserName);

        PrivateMethodInvokerHelper.invokePrivateMethod(tempFileIO, "createBrowserData", parameters);

        TempFileIO.BrowserConnectionData data = (TempFileIO.BrowserConnectionData) PrivateMethodInvokerHelper
                .invokePrivateMethod(tempFileIO, "getBrowserData", null);
        System.out.println("URL: " + data.remoteAddress.toString());
        System.out.println("SessionID: " + data.sessionId.toString());
        System.out.println("BrowserName: " + data.browserName);
        Assert.assertEquals("URL is not as expected", data.remoteAddress.toString(), testUrl);
        Assert.assertEquals("SessionID is not as expected", data.sessionId.toString(), sessionID);
        Assert.assertEquals("BrowserName is not as expected", data.browserName, browserName);
    }

    @Test
    public void checkLoadingNotExistingFile() {
        TempFileIO tempFileIO = new TempFileIO();
        PrivateMethodInvokerHelper.invokePrivateMethod(tempFileIO, "deleteTempFileIfExists", null);

        TempFileIO.BrowserConnectionData data = TempFileIO.loadBrowserConnData();

        System.out.println("Data returned: " + data + ". Should be null.");

        Assert.assertNull(data);
    }

    @Test
    public void deleteTempFileTest() {
        TempFileIO tempFileIO = new TempFileIO();
        PrivateMethodInvokerHelper.invokePrivateMethod(tempFileIO, "deleteTempFileIfExists", null);
        boolean exists = (boolean) PrivateMethodInvokerHelper
                .invokePrivateMethod(tempFileIO, "isTempFileExist", null);
        System.out.println("Temp file exists: " + exists + ". Should be false.");
        Assert.assertFalse(exists);
    }

    @Test
    public void isTempFileExistTest() {
        TempFileIO tempFileIO = new TempFileIO();
        PrivateMethodInvokerHelper.invokePrivateMethod(tempFileIO, "deleteTempFileIfExists", null);

        boolean exists = (boolean) PrivateMethodInvokerHelper
                .invokePrivateMethod(tempFileIO, "isTempFileExist", null);
        System.out.println("Temp file exists: " + exists + ". Should be false.");
        Assert.assertFalse(exists);

        List<Object> parameters = new ArrayList<>();
        parameters.add(testUrl);
        parameters.add(sessionID);
        parameters.add(browserName);

        PrivateMethodInvokerHelper.invokePrivateMethod(tempFileIO, "createBrowserData", parameters);

        PrivateMethodInvokerHelper.invokePrivateMethod(tempFileIO, "saveBrowserConnData", null);

        exists = (boolean) PrivateMethodInvokerHelper
                .invokePrivateMethod(tempFileIO, "isTempFileExist", null);
        System.out.println("Temp file exists: " + exists + ". Should be true.");
        Assert.assertTrue(exists);

    }

    @Test
    public void saveAndLoad() {
        TempFileIO tempFileIO = new TempFileIO();
        PrivateMethodInvokerHelper.invokePrivateMethod(tempFileIO, "deleteTempFileIfExists", null);

        List<Object> parameters = new ArrayList<>();
        parameters.add(testUrl);
        parameters.add(sessionID);
        parameters.add(browserName);

        PrivateMethodInvokerHelper.invokePrivateMethod(tempFileIO, "createBrowserData", parameters);

        PrivateMethodInvokerHelper.invokePrivateMethod(tempFileIO, "saveBrowserConnData", null);

        TempFileIO.BrowserConnectionData data = TempFileIO.loadBrowserConnData();
        System.out.println("URL: " + data.remoteAddress.toString());
        System.out.println("SessionID: " + data.sessionId.toString());
        System.out.println("BrowserName: " + data.browserName);
        Assert.assertEquals("URL is not as expected", data.remoteAddress.toString(), testUrl);
        Assert.assertEquals("SessionID is not as expected", data.sessionId.toString(), sessionID);
        Assert.assertEquals("BrowserName is not as expected", data.browserName, browserName);

        String testUrlNew = testUrl + "/something/new";
        String sessionIDNew = sessionID + "new";
        String browserNameNew = browserName + "new";
        parameters = new ArrayList<>();
        parameters.add(testUrlNew);
        parameters.add(sessionIDNew);
        parameters.add(browserNameNew);

        PrivateMethodInvokerHelper.invokePrivateMethod(tempFileIO, "createBrowserData", parameters);

        PrivateMethodInvokerHelper.invokePrivateMethod(tempFileIO, "saveBrowserConnData", null);

        data = TempFileIO.loadBrowserConnData();
        System.out.println("URL: " + data.remoteAddress.toString());
        System.out.println("SessionID: " + data.sessionId.toString());
        System.out.println("BrowserName: " + data.browserName);
        Assert.assertEquals("URL is not as expected", data.remoteAddress.toString(), testUrlNew);
        Assert.assertEquals("SessionID is not as expected", data.sessionId.toString(), sessionIDNew);
        Assert.assertEquals("BrowserName is not as expected", data.browserName, browserNameNew);
    }
}
