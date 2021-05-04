# Rebrosel #

### REuse BROwser with SELenium ###

Rebrosel allows to execute Selenium automation tests in the same browser session even after tests are done. It was 
created for using in debugging, working with production environment and to make simple iterative development of 
automation code on the same page without running the whole test. 
It is a small (~19 Kb) library written in pure Java, having only JUnit and Selenium as dependecies, so it can be used 
in scratches as well as in existing frameworks. The author consider it mostly like a debug tool and simple code 
checking in prod environment when it is hard to run the whole script due to captcha, two-factor authorization and other 
reasons.

Main implementation is based on TARUN LALWANI's (https://github.com/tarunlalwani) post 
https://tarunlalwani.com/post/reusing-existing-browser-session-selenium-java/ 


### Prehistory ###

One of the author's friend was given an interview homework to write a web automation code for Salesforce application in 
prod. Salesforce is a pretty challenging web application to automate. There are custom tags which sometimes break 
locator identification and two-factor authorization with blocking a user for log in after several times in a certain 
period of time. It means that the tester has a few attempts to check the automation code, if the browser session is 
over (what actually happens once the Selenium test is done, even if the webdriver is not killed).

After that there was an idea to make the connection of tests to the same browser session in a simple, user-friendly way.
Unfortunately, it was not that simple. All webdrivers have their own nuances while reconnecting to existing browsers, 
and webdriver executables remain running if the driver is not explicitly killed. For some actions OS commands are used 
with creating CLI proccesses. So it makes this framework highly dependable on browsers and OSs. As of version 0.1a it 
is supposed to work only on Windows and macOS. See tested configurations in "Known issues".   

### Get started ###
First of all, Rebrosel jar should be set as a dependency of the current project. It can be done either in IDE project 
settings, or in a build tool. For example, in Gradle the following lines should be added in `build.gradle` file:
```Groovy

```

To get started with the Rebrosel, 3 mandatory things should be done:
1. Test class must be annotated with `@RunWith(RebroselRunner.class)`
2. The only one method with `@BrowserInitialization` annotation, having `public static` 
   modifiers and return WebDriver class variable should be created. 
3. The only one field of WebDriver class having `@RebroselWebDriver` annotation having 
   `static` modifier is allowed.
   
Method with `@OnBrowserStart` annotation is optional, but still must be the only one and have `public static` 
modifiers and be `void`.

Of course, at least one `@Test` annotated method (so it can be empty) is needed to start JUnit execution. 

If there are any issues, Rebrosel will put errors in console.

Inheritance is supported. Though still the only one method with `@BrowserInitialization`, one with `@OnBrowserStart` 
and one field with `@RebroselWebDriver` annotations are allowed across the 
inherited classes.

Simple example:
```Java
@RunWith(core.runner.RebroselRunner.class)
public class Test {

    @RebroselWebDriver
    static WebDriver driver;
    
    @BrowserInitialization
    public static WebDriver browserInit() {

        System.setProperty("webdriver.chrome.driver", {PATH_TO_CHROME_DRIVER});

        return new ChromeDriver();
    }

    @Test
    public void test1() {
    }
}
```



### Execution flow ###
The framework just enhances the JUnit execution by adding 2 annotations: `@BrowserInitialization` and `@OnBrowserStart`.
Both are executed before `@BeforeClass` annotated method in a test. After a webdriver is initialized either by 
connecting to a previous session or by initializing a new one, the native JUnit execution is started. The only 
difference - if there's an issue during initialization (in methods annotated with `@BrowserInitialization` or 
`@OnBrowserStart`), method with `@AfterClass` annotation will not be executed.

`@BrowserInitialization` annotation is used to initiate new WebDriver instance and the method annotated with it must 
return WebDriverClass. Method with `@OnBrowserStart` annotation is not mandatory to have, but if it is in the code, it 
will be executed right away after one with `@BrowserInitialization` annotation.

If both methods were run without exceptions, the browser session information will be saved to a temporary file and be 
used later to try to connect to this existing session.
<img src="mermaid-diagram.png" width="300" alt="Flowchart diagram"/>

### Known issues ###
Rebrosel has been tested on: 
 - Windows 10 x64 1903, browsers:
   - Chrome 90.0.4430.93 (ChromeDriver 90.0.4430.24),
   - FireFox 88.0 (GeckoDriver 0.29.1),
   - Edge 90.0.818.51 (EdgeDriver 90.0.818.51),
   - Opera 76.0.4017.94 (OperaDriver 90.0.4430.85);
 - macOS 10.14.6:
   - Chrome 90.0.4430.93 (ChromeDriver 90.0.4430.24),
   - FireFox 88.0 (GeckoDriver 0.29.1),
   - Edge 90.0.818.51 (EdgeDriver 90.0.818.51),
   - Opera 76.0.1 (OperaDriver 90.0.4430.85),
   - Safari 14.1.
  
Known issues:    
- Opera browser should be fully killed to make framework restart the browser (issue mostly confusing on Mac). 
  Operadriver does not throw any exceptions if the browser is not ready for some reasons. So the framework checks the 
  working browser in running processes. 
- Do not include `test.java.browserTests/*` and `test.java.methodOrderTests/*` into tests on compile. Those tests are 
  running inside `test.java.BrowserTests`, `test.java.core.StartWithDeletedFileTest.java` and 
  `test.java.MethodOrderVerificationTest.java` - this will create failures if run separately
- If another type of browser is initialized in `@BrowserInitialization` and the previous one is still running, the 
  framework most likely will not complain and will try to run tests on a wrong browser. Example: the 
  previous test used Chrome, Chrome is still running, and the new test initializes Edge, the framework 
  will connect to existing Chrome. Currently, there is no way to check what browser is running in RemoteWebDriver if 
  it is connected with SessionId and Url.
- if there is an issue on initialization of WebDriver, the webdriver executable will be running, and the used port will 
be blocked. There is no way to figure out what port is used: WebDriver instance is not initialized due to Exception, 
  and we cannot get port from it; webdriver executable writes to a process console so get it even from there is not 
  possible. Well, probably there are still some ways, but complexity is not worth it. Frankly, this behavior is the 
  same for any framework using Selenium WebDriver.
  



                                        