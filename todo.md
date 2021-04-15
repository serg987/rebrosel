# Todos:

- get rid of 'WebDriverHelper.java uses unchecked or unsafe operations.' while compiling
- ~~do something with Opera driver trowing no exceptions if browser is closed~~ - done
- kill the previous webdriver executable if browser is closed (platform - depending) - done for Win
- get rid of 'Deprecated Gradle features were used in this build, making it incompatible with Gradle 7.0.'
- ~~check if the new browser is not the same as was stored and then start a new one.~~ - no need to, it will connect to the last opened and stored webdriver
- ~~make browser methods executable~~  - done
- move draft of the runner to actual framework and adapt it
  - BUG: something wrong with Opera initialization
  - BUG: seems like Opera does not save its name to tmp file
  - check if native JUnit methods (@BeforeX, @Test, etc) verification is not broken by local validate(); 
- fill statements - statement should be changed only if something happened
- interrupt test execution on errors in initialization
