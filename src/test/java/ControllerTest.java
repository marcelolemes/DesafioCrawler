import com.axreng.backend.crawler.service.CrawlerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicInteger;

public class ControllerTest {
    @Test
    void test_valid_url() throws URISyntaxException {
        CrawlerService.build().
                run(1,new AtomicInteger(1), "http://hiring.axreng.com/",
                        "four");

    }


}
