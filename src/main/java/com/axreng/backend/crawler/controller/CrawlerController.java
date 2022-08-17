package com.axreng.backend.crawler.controller;

import com.axreng.backend.crawler.service.CrawlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import static spark.Spark.get;
import static spark.Spark.post;

public class CrawlerController {
    static Logger logger = LoggerFactory.getLogger(CrawlerController.class);

    public static void runController() throws MalformedURLException, ExecutionException, InterruptedException, URISyntaxException {
        get("/crawl/:id", (req, res) ->
                "GET /crawl/" + req.params("id"));

        post("/crawl", (req, res) ->
                "POST /crawl" + System.lineSeparator() + req.body());
        CrawlerService crawlerService;
        crawlerService =
                CrawlerService.build();


        AtomicInteger counter = new AtomicInteger(1);
        new Thread(() -> crawlerService.
                run(counter,
                        System.getenv("BASE_URL"),
                        System.getenv("KEYWORD"))).start();
        new Thread(() -> crawlerService.
                run(counter,
                        System.getenv("BASE_URL"),
                        System.getenv("KEYWORD"))).start();
        new Thread(() -> crawlerService.
                run(counter,
                        System.getenv("BASE_URL"),
                        System.getenv("KEYWORD"))).start();
        new Thread(() -> crawlerService.
                run(counter,
                        System.getenv("BASE_URL"),
                        System.getenv("KEYWORD"))).start();
        new Thread(() -> crawlerService.
                run(counter,
                        System.getenv("BASE_URL"),
                        System.getenv("KEYWORD"))).start();
        logger.warn("Execução encerrada!");
    }

}
