package methodOrderTests;

import core.annotations.BrowserInitialization;
import core.annotations.OnBrowserStart;
import core.annotations.RebroselWebDriver;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(core.runner.RebroselRunner.class)
public class MethodOrderFailInBeforeTest {
    @RebroselWebDriver
    static WebDriver driver;

    static final List<String> strings = new ArrayList<>();

    @BrowserInitialization
    public static WebDriver browserInit() {
        strings.add("Browser init");
        return null;
    }

    @OnBrowserStart
    public static void browserStart() {
        strings.add("Browser start");
    }

    @BeforeClass
    public static void beforeClass() {
        strings.add("Before class");
    }

    @Before
    public void before() {
        strings.add("Before test");
        int[] ints = new int[1];
        System.out.println(ints[2]);
    }

    @Test
    public void test1() {
        strings.add("Test 1");
    }

    @Test
    public void test2() {
        strings.add("Test 2");
    }

    @AfterClass
    public static void afterClass() {
        strings.add("After class");
    }

    @After
    public void after() {
        strings.add("After test");
    }

    public static List<String> getStrings() {
        return strings;
    }
}
