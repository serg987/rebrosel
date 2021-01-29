import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

public class FileTests {
    @Test
    public void testFileCreation() {
        Class clazz = null;

        try {
            clazz = Class.forName("core.TempFileIO");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Arrays.stream(clazz.getMethods()).map(Method::getName).forEach(System.out::println);
    }


}
