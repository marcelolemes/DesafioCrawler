package com.axreng.backend.crawler.controller;

import com.axreng.backend.Main;
import com.axreng.backend.crawler.service.CrawlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

import static spark.Spark.get;
import static spark.Spark.post;

public class CrawlerController {
    static Logger logger = LoggerFactory.getLogger(CrawlerController.class);

    public static void runController() throws MalformedURLException, ExecutionException, InterruptedException {
        get("/crawl/:id", (req, res) ->
                "GET /crawl/" + req.params("id"));

        post("/crawl", (req, res) ->
                "POST /crawl" + System.lineSeparator() + req.body());
        CrawlerService.
                process(
                        System.getenv("BASE_URL"),
                        System.getenv("KEYWORD"));
    }
}
