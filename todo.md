# Todos:

- get rid of 'WebDriverHelper.java uses unchecked or unsafe operations.' while compiling
- ~~do something with Opera driver trowing no exceptions if browser is closed~~ - done
- kill the previous webdriver executable if browser is closed (platform - depending) - done for Win
- get rid of 'Deprecated Gradle features were used in this build, making it incompatible with Gradle 7.0.'
- ~~check if the new browser is not the same as was stored and then start a new one.~~ - no need to, it will connect to the last opened and stored webdriver
- ~~make browser methods executable~~  - done
- move draft of the runner to actual framework and adapt it - WIP
- change the flow - now if browser is definitely just started, it checks??? for the browser again
~~- BUG: something wrong with Opera initialization~~ - Done; just wrong webdriver version
~~- BUG: seems like Opera does not save its name to tmp file~~ - Done; just wrong webdriver version
- check if native JUnit methods (@BeforeX, @Test, etc) verification is not broken by local validate(); 
~~- fill statements - statement should be changed only if something happened~~ - Done 
~~- interrupt test execution on errors in initialization~~ - Done
- Intellij offers to execute methods with custom annotations
- Check how to handle if one browser is working, but it is needed to open another one. Open both and store data for all 
  available???
- change saving the file method - all-at-once and assuming ^^^ is true 