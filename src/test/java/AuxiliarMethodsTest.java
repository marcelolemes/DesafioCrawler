import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static com.axreng.backend.crawler.service.auxiliar.AuxiliarMethods.*;

public class AuxiliarMethodsTest {
    @Test
    void test_valid_url() {
        Assertions.assertTrue(validUrl("http://hiring.axreng.com/"));
    }

    @Test
    void test_invalid_url() {
        String url = "http://hiring.axreng.com";
        Assertions.assertEquals("Link inválido encontrado. " + url,
                Assertions.assertThrows(IllegalArgumentException.class,
                        () -> validUrl(url)).getMessage());
    }

    @Test
    void test_invalid_relative_url() {
        String url = "";
        Assertions.assertEquals("Link inválido " + url,
                Assertions.assertThrows(IllegalArgumentException.class,
                        () -> relativeToAbsoluteUrl("", url)).getMessage());
    }

    @Test
    void test_invalid_html_content() {
        Assertions.assertNotNull(linkProcessor("http://hiring.axreng.com/"));

    }

}
