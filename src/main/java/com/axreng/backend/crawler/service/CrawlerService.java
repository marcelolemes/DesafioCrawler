package com.axreng.backend.crawler.service;

import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicInteger;

public interface CrawlerService {
    static CrawlerServiceImpl build() throws URISyntaxException {
        return new CrawlerServiceImpl();
    }

    void run(int limit, AtomicInteger counter, String uri, String keyword);
}
