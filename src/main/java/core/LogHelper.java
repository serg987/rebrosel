package core;

import static core.EnvironmentVariables.nameOfFrameworkForLogging;

public class LogHelper {
    // as for now just pass everything to console

    public static void logMessage(String str) {
        String message = "[" + nameOfFrameworkForLogging + "] " + str;
        System.out.println(message);
    }

    public static void logError(String str) {
        String message = "\u001B[31m[" + nameOfFrameworkForLogging + "] [ERROR] " + str + "\u001B[0m";
        System.out.println(message);
    }
}
