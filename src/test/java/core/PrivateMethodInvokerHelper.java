package core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class PrivateMethodInvokerHelper {
    public static Object invokePrivateMethod(Object obj, String methodName, List<Object> parameters) {
        Method method = null;
        Class[] parameterTypes = null;
        if (parameters != null) {
            parameterTypes = new Class[parameters.size()];
            for (int i = 0; i < parameters.size(); i++) {
                parameterTypes[i] = parameters.get(i).getClass(); // streams are not working here
            }
        }

        try {
            method = obj.getClass().getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        Object out = null;

        if (method != null) {
            method.setAccessible(true);
            
            try {
                out = method.invoke(obj, parameters == null ? null : parameters.toArray());
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return out;
    }
}
