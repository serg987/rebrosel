package core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.remote.http.W3CHttpCommandCodec;
import org.openqa.selenium.remote.http.W3CHttpResponseCodec;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;

public class WebDriverHelper {
    public static RemoteWebDriver createDriverFromSession(final SessionId sessionId, URL command_executor){
        // the whole method is taken from https://tarunlalwani.com/post/reusing-existing-browser-session-selenium-java/
        // poster - TARUN LALWANI https://github.com/tarunlalwani
        CommandExecutor executor = new HttpCommandExecutor(command_executor) {

            @Override
            public Response execute(Command command) throws IOException {
                Response response;
                if (command.getName() == "newSession") {
                    response = new Response();
                    response.setSessionId(sessionId.toString());
                    response.setStatus(0);
                    response.setValue(Collections.<String, String>emptyMap());

                    try {
                        Field commandCodec;
                        commandCodec = this.getClass().getSuperclass().getDeclaredField("commandCodec");
                        commandCodec.setAccessible(true);
                        commandCodec.set(this, new W3CHttpCommandCodec());

                        Field responseCodec;
                        responseCodec = this.getClass().getSuperclass().getDeclaredField("responseCodec");
                        responseCodec.setAccessible(true);
                        responseCodec.set(this, new W3CHttpResponseCodec());
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                } else {
                    response = super.execute(command);
                }
                return response;
            }
        };

        return new RemoteWebDriver(executor, new DesiredCapabilities());
    }


    public static URL getAddressOfRemoteServer(WebDriver driver) {
        HttpCommandExecutor executor = invokeMethodInWebDriver(driver, "getCommandExecutor");

        return executor.getAddressOfRemoteServer();
    }

    public static SessionId getSessionId(WebDriver driver) {
        return invokeMethodInWebDriver(driver, "getSessionId");
    }

    private static <T> T invokeMethodInWebDriver(WebDriver driver, String methodName) {
        T out = null;
        Method[] methods = driver.getClass().getMethods();

        java.util.Optional<Method> invokingMethodOpt = Arrays.stream(methods).filter(method -> {
            String[] methodWords = method.getName().split("\\.");
            return methodWords[methodWords.length - 1].equals(methodName);
        }).findFirst();

        if (invokingMethodOpt.isPresent()) {
            Method invokingMethod = invokingMethodOpt.get();
            try {
                out = (T) invokingMethod.invoke(driver);
            } catch (IllegalAccessException | InvocationTargetException e) {
                System.out.println("Error while invoking " + methodName + "() in " + driver.toString());
            }
        }

        return out;
    }

    private static String getDriverBrowserName(WebDriver driver) {
        return Arrays.stream(driver.getClass().getName().split("\\."))
                .reduce((s1, s2) -> s2).get()
                .replace("Driver", "");
    }
}
