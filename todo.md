# Todos:

- get rid of 'WebDriverHelper.java uses unchecked or unsafe operations.' while compiling
  
- ~~do something with Opera driver trowing no exceptions if browser is closed~~ - done
  
- kill the previous webdriver executable if browser is closed (platform - depending) - done for Win
  
- get rid of 'Deprecated Gradle features were used in this build, making it incompatible with Gradle 7.0.'
  
- ~~check if the new browser is not the same as was stored and then start a new one.~~ - no need to, it will connect to the last opened and stored webdriver
  
- ~~make browser methods executable~~  - done
  
- move draft of the runner to actual framework and adapt it - WIP
  
~~- change the flow - now if browser is definitely just started, it checks??? for the browser again~~ - Done

~~- BUG: something wrong with Opera initialization~~ - Done; just wrong webdriver version
  
~~- BUG: seems like Opera does not save its name to tmp file~~ - Done; just wrong webdriver version
  
~~- check if native JUnit methods (@BeforeX, @Test, etc) verification is not broken by local validate();~~ - Done - 
tests has been written

~~- fill statements - statement should be changed only if something happened~~ - Done
  
~~- interrupt test execution on errors in initialization~~ - Done
  
- Intellij offers to execute methods with custom annotations - seems like it is any custom runner issue in Intellij 
  (see example https://intellij-support.jetbrains.com/hc/en-us/community/posts/206184769-Custom-JUnit-test-runner-method-execution-problem)
  anyway, didn't find a way to fix it yet
~~- Check how to handle if one browser is working, but it is needed to open another one. Open both and store data for all
  available???~~ - NOT IMPLEMENTED  - no easy way to figure out what driver is requested to launch unless it is launched
  
~~- change saving the file method - all-at-once and assuming ^^^ is true~~ - won't fix
  
~~- failed local tests are green even on failure~~ - Done
  
~~- figure out why tempfile is rewritten with empty browser name (try with a wrong webdriver)~~ - Done/won't fix
  ~~- kill WebDriver if initializing Browser was not successful~~ - is not possible. WebDriver is not initialized to 
  get port and Webdriver exec writes to process console, so cannot get it even from there
  ~~- Do not save file if browser is not running~~ - Done
  
~~- Try to get what browser is connected with RemoteWebDriver if it is possible then it is possible to kill it and 
restart a proper one~~ - Didn't find a way

~~- test @OnBrowserStart~~ - Done

~~- Test with deleting files (kill WebDriver before deleting the file)~~ - Done

- Handle test classes inheritance