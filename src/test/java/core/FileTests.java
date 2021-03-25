package core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileTests {
    private static final String testUrl = "http://localhost:12784";
    private static final String sessionID = "0871996af02bcf5a3ae3101caea8579a";

    @Before
    public void beforeTest() {
        System.out.println(" *** Test started ***");
    }

    @Test
    public void printErrorTest() {
        TempFileIO tempFileIO = new TempFileIO();

        List<Object> parameters = new ArrayList<>();
        parameters.add("Something");

        PrivateMethodInvokerHelper.invokePrivateMethod(tempFileIO, "printError", parameters);
    }

    @Test
    public void createBrowserDataTest() {
        TempFileIO tempFileIO = new TempFileIO();
        List<Object> parameters = new ArrayList<>();
        parameters.add(testUrl);
        parameters.add(sessionID);

        PrivateMethodInvokerHelper.invokePrivateMethod(tempFileIO, "createBrowserData", parameters);

        TempFileIO.BrowserConnectionData data = (TempFileIO.BrowserConnectionData) PrivateMethodInvokerHelper
                .invokePrivateMethod(tempFileIO, "getBrowserData", null);
        System.out.println("URL: " + data.remoteAddress.toString());
        System.out.println("SessionID: " + data.sessionId.toString());
        Assert.assertTrue("URL is not as expected", data.remoteAddress.toString().equals(testUrl));
        Assert.assertTrue("SessionID is not as expected", data.sessionId.toString().equals(sessionID));
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

        PrivateMethodInvokerHelper.invokePrivateMethod(tempFileIO, "createBrowserData", parameters);

        TempFileIO.saveBrowserConnData();

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

        PrivateMethodInvokerHelper.invokePrivateMethod(tempFileIO, "createBrowserData", parameters);

        TempFileIO.saveBrowserConnData();

        TempFileIO.BrowserConnectionData data = TempFileIO.loadBrowserConnData();
        System.out.println("URL: " + data.remoteAddress.toString());
        System.out.println("SessionID: " + data.sessionId.toString());
        Assert.assertTrue("URL is not as expected", data.remoteAddress.toString().equals(testUrl));
        Assert.assertTrue("SessionID is not as expected", data.sessionId.toString().equals(sessionID));

        String testUrlNew = testUrl + "/something/new";
        String sessionIDNew = sessionID + "new";
        parameters = new ArrayList<>();
        parameters.add(testUrlNew);
        parameters.add(sessionIDNew);

        PrivateMethodInvokerHelper.invokePrivateMethod(tempFileIO, "createBrowserData", parameters);

        TempFileIO.saveBrowserConnData();

        data = TempFileIO.loadBrowserConnData();
        System.out.println("URL: " + data.remoteAddress.toString());
        System.out.println("SessionID: " + data.sessionId.toString());
        Assert.assertTrue("URL is not as expected", data.remoteAddress.toString().equals(testUrlNew));
        Assert.assertTrue("SessionID is not as expected", data.sessionId.toString().equals(sessionIDNew));
    }
}
