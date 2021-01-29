package core;

import org.openqa.selenium.remote.SessionId;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

class TempFileIO {

    private static File tempFile;
    private static BrowserConnectionData browserData;

    public static BrowserConnectionData getBrowserData() {
        return browserData;
    }

    private static File getTempFile() {
        if (tempFile != null) return tempFile;

        String tempPathStr = (LocalUtils.isStringNullOrEmpty(EnvironmentVariables.temporaryDirectory)) ?
                System.getProperty("java.io.tmpdir") :
                EnvironmentVariables.temporaryDirectory;

        File tempFolder = new File(tempPathStr);
        if (tempFolder.exists()) {
            System.out.println("FATAL ERROR! No TEMP folder found. Please set it up in core.EnvironmentVariables.java");
            System.exit(1);
        }

        tempFile = new File(tempFolder, EnvironmentVariables.temporaryFileName);

        return tempFile;
    }

    public static Boolean isTempFileExist() {
        return getTempFile().exists();
    }

    public static BrowserConnectionData loadBrowserConnData() {
        if (!isTempFileExist()) return null;

        BufferedReader reader = null;

        String urlStr = null;
        String sessionIdStr = null;

        try {
            reader = new BufferedReader(new FileReader(tempFile));
            urlStr = reader.readLine();
            sessionIdStr = reader.readLine();
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

        createBrowserData(urlStr, sessionIdStr);
        return getBrowserData();
    }

    public static void saveBrowserConnData(BrowserConnectionData data) {
        if (isTempFileExist()) {
            tempFile.delete();
        }

        browserData = data;

        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter(tempFile));
            writer.write(browserData.remoteAddress.toString());
            writer.newLine();
            writer.write(browserData.sessionId.toString());
        } catch (IOException e) {
            printError("write");
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                printError("write");
            }
        }
    }

    private static void createBrowserData(String urlStr, String sessionIdStr) {
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
    }

    private static void printError(String action) {
        System.out.println("FATAL ERROR! Could not " + action + " the temp file!");
    }

    static class BrowserConnectionData {
        URL remoteAddress;
        SessionId sessionId;
    }
}
