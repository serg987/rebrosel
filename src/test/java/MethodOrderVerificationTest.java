import core.PrivateMethodInvokerHelper;
import core.TempFileIO;
import methodOrderTests.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;

import java.util.Arrays;
import java.util.List;

public class MethodOrderVerificationTest {

    private static final String[] stringsForColdStart = {"Browser init", "Browser start", "Before class", "Before test", "Test 1",
            "After test", "Before test", "Test 2", "After test", "After class"};
    private static final String[] stringsForFailInBrowserInit = {"Browser init"};
    private static final String[] stringsForWithoutOnStart = {"Browser init", "Before class", "Before test", "Test 1",
            "After test", "Before test", "Test 2", "After test", "After class"};
    private static final String[] stringsForFailInBefore = {"Browser init", "Browser start", "Before class", "Before test",
            "After test", "Before test", "After test", "After class"};
    private static final String[] stringsForFailInBeforeClass = {"Browser init", "Browser start", "Before class", "After class"};
    private static final String[] stringsForFailInOnStart = {"Browser init", "Browser start"};
    private static final String[] stringsForFailInTest2 = {"Browser init", "Browser start", "Before class", "Before test", "Test 1",
            "After test", "Before test", "Test 2", "After test", "After class"};

    private static JUnitCore junit;
    static final TempFileIO tempFileIO = new TempFileIO();

    @BeforeClass
    public static void beforeClass() {
        junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
    }

    @Test
    public void coldStart() {
        PrivateMethodInvokerHelper.invokePrivateMethod(tempFileIO,
                "deleteTempFileIfExists",
                null);
        Assert.assertTrue("Test was not successful",
                junit.run(MethodOrderPassTest.class).wasSuccessful());
        List<String> actualStrings = MethodOrderPassTest.getStrings();

        Assert.assertEquals("Order of execution is different", Arrays.asList(stringsForColdStart), actualStrings);
    }

    @Test
    public void failInBrowserInit() {
        PrivateMethodInvokerHelper.invokePrivateMethod(tempFileIO,
                "deleteTempFileIfExists",
                null);
        Assert.assertFalse("Test was not successful",
                junit.run(MethodOrderFailInBrowserInitTest.class).wasSuccessful());
        List<String> actualStrings = MethodOrderFailInBrowserInitTest.getStrings();

        Assert.assertEquals("Order of execution is different", Arrays.asList(stringsForFailInBrowserInit), actualStrings);
    }

    @Test
    public void withoutOnStart() {
        PrivateMethodInvokerHelper.invokePrivateMethod(tempFileIO,
                "deleteTempFileIfExists",
                null);
        Assert.assertTrue("Test was not successful",
                junit.run(MethodOrderPassWOOnStartTest.class).wasSuccessful());
        List<String> actualStrings = MethodOrderPassWOOnStartTest.getStrings();

        Assert.assertEquals("Order of execution is different", Arrays.asList(stringsForWithoutOnStart), actualStrings);
    }

    @Test
    public void failInOnStart() {
        PrivateMethodInvokerHelper.invokePrivateMethod(tempFileIO,
                "deleteTempFileIfExists",
                null);
        Assert.assertFalse("Test was not successful",
                junit.run(MethodOrderFailInOnStartTest.class).wasSuccessful());
        List<String> actualStrings = MethodOrderFailInOnStartTest.getStrings();

        Assert.assertEquals("Order of execution is different", Arrays.asList(stringsForFailInOnStart), actualStrings);
    }

    @Test
    public void failInBefore() {
        PrivateMethodInvokerHelper.invokePrivateMethod(tempFileIO,
                "deleteTempFileIfExists",
                null);
        Assert.assertFalse("Test was not successful",
                junit.run(MethodOrderFailInBeforeTest.class).wasSuccessful());
        List<String> actualStrings = MethodOrderFailInBeforeTest.getStrings();

        Assert.assertEquals("Order of execution is different", Arrays.asList(stringsForFailInBefore), actualStrings);
    }

    @Test
    public void failInTest2() {
        PrivateMethodInvokerHelper.invokePrivateMethod(tempFileIO,
                "deleteTempFileIfExists",
                null);
        Assert.assertFalse("Test was not successful",
                junit.run(MethodOrderFailInTest2Test.class).wasSuccessful());
        List<String> actualStrings = MethodOrderFailInTest2Test.getStrings();

        Assert.assertEquals("Order of execution is different", Arrays.asList(stringsForFailInTest2), actualStrings);
    }

    @Test
    public void failInBeforeClass() {
        PrivateMethodInvokerHelper.invokePrivateMethod(tempFileIO,
                "deleteTempFileIfExists",
                null);
        Assert.assertFalse("Test was not successful",
                junit.run(MethodOrderFailInBeforeClassTest.class).wasSuccessful());
        List<String> actualStrings = MethodOrderFailInBeforeClassTest.getStrings();

        Assert.assertEquals("Order of execution is different", Arrays.asList(stringsForFailInBeforeClass), actualStrings);
    }
}

