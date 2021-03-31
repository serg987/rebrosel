package core;

class EnvironmentVariables {

    // Current hardware:
    public static final String OS = System.getProperty("os.name");
    public static final boolean IS_WINDOWS = OS.toLowerCase().contains("windows");
    public static final boolean IS_MAC = OS.toLowerCase().contains("mac os");

    public static final String nameOfFrameworkForLogging = "REBROSEL";
    public static final String temporaryFileName = "reubrowsel-browser-driver-connection.tmp";
    public static final String temporaryDirectory = ""; // set it up if system TEMP folder does not work
                                                        // or custom folder is needed

}
