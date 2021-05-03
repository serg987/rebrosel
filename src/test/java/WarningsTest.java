import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class WarningsTest {
    private static JUnitCore junit;

    @BeforeClass
    public static void beforeClass() {
        junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
    }

    @Test
    public void noAnnotatedFieldsMethods() {
        Result result = junit.run(warningsTests.NoAnnotatedFieldsMethods.class);
        Assert.assertFalse("Test was not successful",
                result.wasSuccessful());
        checkNumberOfFailures(result, 2);
        List<String> failures = result.getFailures().stream().map(Failure::getException).map(Throwable::getMessage)
                .collect(Collectors.toList());
        String checkingString = "No methods annotated with @BrowserInitialization found.";
        Assert.assertTrue("No error '" + checkingString + "' found", failures.contains(checkingString));
        checkingString = "No fields annotated with @RebroselWebDriver found.";
        Assert.assertTrue("No error '" + checkingString + "' found", failures.contains(checkingString));
    }

    @Test
    public void twoAnnotatedFieldsMethodsNoInheritance() {
        Result result = junit.run(warningsTests.TwoAnnotatedFieldsMethodsNoInheritance.class);
        checkTwoAnnotatedFieldsMethodsResults(result);
    }

    @Test
    public void twoAnnotatedFieldsMethodsInheritance() {
        Result result = junit.run(warningsTests.TwoAnnotatedFieldsMethodsChild.class);
        checkTwoAnnotatedFieldsMethodsResults(result);
    }

    @Test
    public void allModifiersTest() {
        Result result = junit.run(warningsTests.AllModifiersTest.class);
        checkNumberOfFailures(result, 5);
        List<String> failures = result.getFailures().stream().map(Failure::getException).map(Throwable::getMessage)
                .collect(Collectors.toList());
        String checkingString = "Method 'init' should be static, be public, return org.openqa.selenium.WebDriver.";
        checkFailuresContains(failures, checkingString);
        checkingString = "Method start() should be static";
        checkFailuresContains(failures, checkingString);
        checkingString = "Method start() should be public";
        checkFailuresContains(failures, checkingString);
        checkingString = "Method start() should be void";
        checkFailuresContains(failures, checkingString);
        checkingString = "Field driver annotated with @RebroselWebDriver should be static, be org.openqa.selenium.WebDriver class.";
        checkFailuresContains(failures, checkingString);
    }

    @Test
    public void oneModifierTest() {
        Result result = junit.run(warningsTests.OneModifierTest.class);
        checkNumberOfFailures(result, 3);
        List<String> failures = result.getFailures().stream().map(Failure::getException).map(Throwable::getMessage)
                .collect(Collectors.toList());
        String checkingString = "Method 'init' should return org.openqa.selenium.WebDriver.";
        checkFailuresContains(failures, checkingString);
        checkingString = "Method start() should be public";
        checkFailuresContains(failures, checkingString);
        checkingString = "Field driver annotated with @RebroselWebDriver should be static.";
        checkFailuresContains(failures, checkingString);
    }

    private void checkFailuresContains(List<String> failures, String checkingString) {
        Assert.assertTrue("No error '" + checkingString + "' found", containsInIterable(failures, checkingString));
    }

    private void checkNumberOfFailures(Result result, int i) {
        Assert.assertEquals("Number of failures is not " + i, i, result.getFailureCount());
    }


    private void checkTwoAnnotatedFieldsMethodsResults(Result result) {
        Assert.assertFalse("Test was not successful",
                result.wasSuccessful());
        checkNumberOfFailures(result, 3);
        List<String> failures = result.getFailures().stream().map(Failure::getException).map(Throwable::getMessage)
                .collect(Collectors.toList());
        String checkingString = "Only one method with Annotation @BrowserInitialization is allowed.";
        checkFailuresContains(failures, checkingString);
        checkingString = "Only one field with Annotation @RebroselWebDriver is allowed.";
        checkFailuresContains(failures, checkingString);
        checkingString = "Only one method with Annotation @OnBrowserStart is allowed.";
        checkFailuresContains(failures, checkingString);
    }

    private boolean containsInIterable(Iterable<String> iterable, String contains) {
        Iterator<String> iterator = iterable.iterator();
        boolean found = false;
        while (iterator.hasNext() && !found) {
            found = iterator.next().contains(contains);
        }
        return found;
    }
}
