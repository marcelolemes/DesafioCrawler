import com.axreng.backend.crawler.service.CrawlerService;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicInteger;

public class ServiceTest {
    @Test
    void test_should_not_throw_exception() throws URISyntaxException {
        CrawlerService crawlerService = CrawlerService.build();

        crawlerService.run(1, new AtomicInteger(1), "http://hiring.axreng.com/",
                "four");
    }


}
