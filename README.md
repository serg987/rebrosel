# Rebrosel

REuse BROwser with SELenium

Main implementation is based on TARUN LALWANI's (https://github.com/tarunlalwani) post 
https://tarunlalwani.com/post/reusing-existing-browser-session-selenium-java/ 

Known issues:
- do not include `test.java.browserTests/*` into tests on compile. Those tests are running inside 
  `test.java.BrowserTests` and will create failures if run separately
- if different browser type WebDriver is initialized in `@BrowserInitialization` and the previous one is still running 
the framework most likely will not complain and will try to run tests on a wrong browser. Example: the 
  previous test used Chrome, Chrome is still running, and the new test initializes Edge, the framework 
  will connect to existing Chrome. Currently there is no way to check what browser is running in RemoteWebDriver if 
  it is connected with SessionId and Url.
  



                                        