package core;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class TempFileIO {

    private static File tempFile;
    private static BrowserConnectionData browserData;

    public static BrowserConnectionData getBrowserData() {
        return browserData;
    }

    private static File getTempFile() {
        if (tempFile != null) return tempFile;

        String tempPathStr = (EnvironmentVariables.temporaryDirectory == null ||
                EnvironmentVariables.temporaryDirectory.isEmpty()) ?
                System.getProperty("java.io.tmpdir") :
                EnvironmentVariables.temporaryDirectory;

        File tempFolder = new File(tempPathStr);
        if (!tempFolder.exists()) {
            System.out.println("FATAL ERROR! No TEMP folder found. Please set it up in core.EnvironmentVariables.java");
            System.exit(1);
        }

        tempFile = new File(tempFolder, EnvironmentVariables.temporaryFileName);

        return tempFile;
    }

    private static Boolean isTempFileExist() {
        return getTempFile().exists();
    }

    static BrowserConnectionData loadBrowserConnData() {
        if (!isTempFileExist()) return null;

        BufferedReader reader = null;

        String urlStr = null;
        String sessionIdStr = null;
        String browserName = null;

        try {
            reader = new BufferedReader(new FileReader(tempFile));
            urlStr = reader.readLine();
            sessionIdStr = reader.readLine();
            browserName = reader.readLine();
        } catch (IOException e) {
            printError("read");
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
               printError("read");
            }
        }

        return createBrowserData(urlStr, sessionIdStr, browserName);
    }

    private static void saveBrowserConnData() {
        deleteTempFileIfExists();

        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter(tempFile));
            writer.write(browserData.remoteAddress.toString());
            writer.newLine();
            writer.write(browserData.sessionId.toString());
            writer.newLine();
            writer.write(browserData.browserName);
        } catch (IOException e) {
            printError("write");
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                    LogHelper.logMessage("Browser connection data was saved.");
                }
            } catch (IOException e) {
                printError("write");
            }
        }
    }

    static BrowserConnectionData createBrowserData(String urlStr, String sessionIdStr, String browserName) {
        URL url = null;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            // do nothing
        }
        SessionId sessionId = new SessionId(sessionIdStr);

        browserData = new BrowserConnectionData();
        browserData.remoteAddress = url;
        browserData.sessionId = sessionId;
        browserData.browserName = browserName;

        return browserData;
    }

    private static void printError(String action) {
        LogHelper.logError("FATAL ERROR! Could not " + action + " the temp file!");
    }

    private static void deleteTempFileIfExists() {
        if (isTempFileExist()) {
            tempFile.delete();
        }
    }

    private static void getBrowserData(RemoteWebDriver driver) {
        URL url = WebDriverHelper.getAddressOfRemoteServer(driver);
        SessionId sessionId = WebDriverHelper.getSessionId(driver);
        String browserName = driver.getCapabilities().getBrowserName();
        TempFileIO.createBrowserData(url.toString(),
                sessionId.toString(),
                browserName);
    }

    public static void saveBrowserDataToFile(RemoteWebDriver driver) {
        if (driver != null) {
            getBrowserData(driver);
            saveBrowserConnData();
        }
    }

    static class BrowserConnectionData {
        URL remoteAddress;
        SessionId sessionId;
        String browserName;
    }
}
