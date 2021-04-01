import org.junit.Test;
import org.openqa.selenium.NoSuchSessionException;

public class Tries {

    @Test
    public void test1() {
        try {
            throw new org.openqa.selenium.NoSuchSessionException("aaaa");
        } catch (NoSuchSessionException e) {
            System.out.println(e.getClass().getCanonicalName());
            System.out.println(e.getMessage());
        }
    }
}
