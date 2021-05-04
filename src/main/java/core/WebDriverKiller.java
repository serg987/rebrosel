package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import static core.EnvironmentVariables.IS_WINDOWS;
import static core.EnvironmentVariables.IS_MAC;

public class WebDriverKiller {

    public static void killWebDriver(TempFileIO.BrowserConnectionData browserData) {
        if (browserData == null) return;
        boolean wasKilled = true;
        if (IS_WINDOWS) {
            wasKilled = killWebDriverInWindows(browserData);
        } else if (IS_MAC) {
            wasKilled = killWebDriverInMac(browserData);
        }
        if (!wasKilled) LogHelper.logMessage("No process was found handling webdriver port. Nothing to kill.");
    }

    private static boolean killWebDriverInWindows(TempFileIO.BrowserConnectionData browserData) {
        String port = Arrays.stream(browserData.remoteAddress.toString().split(":"))
                .reduce((a, b) -> b).orElse("99999");
        String portToFind = ":" + port;
        String webDriverProcess = executeCommandAndFindStringContains("netstat -ano", portToFind);
        if (!webDriverProcess.isEmpty()) {
            String pid = Arrays.stream(webDriverProcess.split(" ")).reduce((a, b) -> b).orElse("");
            LogHelper.logMessage("Found process with PID=" + pid + " handling port " + port
                    + ". Will kill it.");
            executeCommandAndFindStringContains("taskkill /F /PID " + pid, "");
            return true;
        }
        return false;
    }

    private static boolean killWebDriverInMac(TempFileIO.BrowserConnectionData browserData) {
        String port = Arrays.stream(browserData.remoteAddress.toString().split(":"))
                .reduce((a, b) -> b).orElse("99999");
        String webDriverProcess = executeCommandAndFindStringContains("lsof -i tcp:" + port, port);
        if (!webDriverProcess.isEmpty()) {
            String pid = webDriverProcess.split(" ")[1];
            LogHelper.logMessage("Found process with PID=" + pid + " handling port " + port
                    + ". Will kill it.");
            executeCommandAndFindStringContains("kill -15 " + pid, "");
            return true;
        }
        return false;
    }

    public static boolean isOperaBrowserWorking() {
        boolean isWorking = false;
        if (IS_WINDOWS) {
            isWorking = isOperaBrowserWorkingInWindows();
        } else if (IS_MAC) {
           isWorking = isOperaBrowserWorkingInMac();
        }
        if (!isWorking) LogHelper.logMessage("Opera browser was not found as working. Will restart it.");

        return isWorking;
    }

    private static boolean isOperaBrowserWorkingInWindows() {
        String opera = executeCommandAndFindStringContains("tasklist", "opera.exe");
        return opera.contains("opera.exe");
    }

    private static boolean isOperaBrowserWorkingInMac() {
        String opera = executeCommandAndFindStringContains("ps -A", "opera.app");
        return !opera.isEmpty();
    }

    private static String executeCommandAndFindStringContains(String command, String find) {
        String out = "";
        Process process = null;
        BufferedReader reader = null;
        try {
            process = Runtime.getRuntime().exec(command);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            out = reader.lines().filter(s -> s.toLowerCase().contains(find)).findFirst().orElse("");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (process != null && process.isAlive()) process.destroy();

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
        }
        return out;
    }


}
