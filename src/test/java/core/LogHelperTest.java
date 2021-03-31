package core;

import org.junit.Test;

public class LogHelperTest{

    @Test
    public void testLogMessage() {
        LogHelper.logMessage("Message in plain text");
    }

    @Test
    public void testLogError() {
        LogHelper.logError("Error. Should be in Red");
    }
}