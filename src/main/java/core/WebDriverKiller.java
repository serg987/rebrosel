package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import static core.EnvironmentVariables.IS_WINDOWS;
import static core.EnvironmentVariables.IS_MAC;

public class WebDriverKiller {

    public static void killWebDriver(TempFileIO.BrowserConnectionData browserData) {
        if (IS_WINDOWS) {
            killWebDriverInWindows(browserData);
        } else if (IS_MAC) {
            killWebDriverInMac(browserData);
        }
    }

    private static void killWebDriverInWindows(TempFileIO.BrowserConnectionData browserData) {
        String port = Arrays.stream(browserData.remoteAddress.toString().split(":"))
                .reduce((a, b) -> b).orElse("99999");
        System.out.println("Port: " + port);
        String portToFind = ":" + port;
        String webDriverProcess = executeCommandAndFindStringContains("netstat -ano", portToFind);
        System.out.println("Found process: " + webDriverProcess);
        if (!webDriverProcess.isEmpty()) {
            String pid = Arrays.stream(webDriverProcess.split(" ")).reduce((a, b) -> b).orElse("");
            System.out.println("PID: " + pid);
            executeCommandAndFindStringContains("taskkill /F /PID " + pid, "");
        }
    }

    private static void killWebDriverInMac(TempFileIO.BrowserConnectionData browserData) {

    }

    public static boolean isOperaBrowserWorking() {
        if (IS_WINDOWS) {
            return isOperaBrowserWorkingInWindows();
        } else if (IS_MAC) {
           return isOperaBrowserWorkingInMac();
        }
        return false;
    }

    private static boolean isOperaBrowserWorkingInWindows() {
        String opera = executeCommandAndFindStringContains("tasklist", "opera.exe");
        return opera.contains("opera.exe");
    }

    private static boolean isOperaBrowserWorkingInMac() {
        return false;
    }

    private static String executeCommandAndFindStringContains(String command, String find) {
        String out = "";
        Process process = null;
        BufferedReader reader = null;
        try {
            process = Runtime.getRuntime().exec(command);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            out = reader.lines().filter(s -> s.contains(find)).findFirst().orElse("");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (process.isAlive()) process.destroy();

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
