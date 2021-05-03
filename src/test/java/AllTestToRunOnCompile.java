import core.FileTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Categories.class)
@Suite.SuiteClasses({core.FileTests.class, core.StartWithDeletedFileTest.class, BrowserTests.class,
        MethodOrderVerificationTest.class, OnBrowserStartTest.class, WarningsTest.class})
public class AllTestToRunOnCompile {

}
