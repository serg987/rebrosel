# Rebrosel

REuse BROwser with SELenium

Main implementation is based on TARUN LALWANI's (https://github.com/tarunlalwani) post 
https://tarunlalwani.com/post/reusing-existing-browser-session-selenium-java/ 

- if there's an issue during initialization (in methods annotated with `@BrowserInitialization` or `@OnBrowserStar`), 
  method with annotation @AfterClass will not be executed.

Known issues:
- do not include `test.java.browserTests/*` and `test.java.methodOrderTests/*` into tests on compile. Those tests are 
  running inside `test.java.BrowserTests`, `test.java.core.StartWithDeletedFileTest.java` and 
  `test.java.MethodOrderVerificationTest.java` - this will create failures if run separately
- if different browser type WebDriver is initialized in `@BrowserInitialization` and the previous one is still running 
the framework most likely will not complain and will try to run tests on a wrong browser. Example: the 
  previous test used Chrome, Chrome is still running, and the new test initializes Edge, the framework 
  will connect to existing Chrome. Currently, there is no way to check what browser is running in RemoteWebDriver if 
  it is connected with SessionId and Url.
- if there was an issue on initialization of WebDriver, the webdriver executable will be running and the used port will 
be blocked. There is no way to figure out what port is used: WebDriver instance is not initialized due to Exception, 
  and webdriver executable writes to a process console so even get it from there is not possible. Well, probably 
  there are still some ways, but complexity is not worth it. Frankly, this behavior is the same for any framework using 
  Selenium WebDriver.
  



                                        