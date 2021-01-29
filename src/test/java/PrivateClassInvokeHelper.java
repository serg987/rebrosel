import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PrivateClassInvokeHelper {

    public static final String srcRootFolderPath = "src/main/java";
    public static final File projectPath = new File(Paths.get("").toString());
    public static final File srcRootFolder = new File(projectPath, srcRootFolderPath);

    public static Object getObjectFromClassPath(String path) {
        File classFile = new File(srcRootFolder, path);

        Class loadedClass = null;

        try {
            loadedClass = ClassLoader.getSystemClassLoader().loadClass("core.TempFileIO");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found");
        }

        System.out.println(loadedClass.getName());

        return null;
    }
}
