package com.axreng.backend.crawler.controller;

import com.axreng.backend.crawler.service.CrawlerService;
import com.axreng.backend.crawler.service.auxiliar.MagicNumbers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicInteger;

import static spark.Spark.get;
import static spark.Spark.post;

public class CrawlerController {
    static Logger logger = LoggerFactory.getLogger(CrawlerController.class);

    public static void runController() throws  URISyntaxException {
        AtomicInteger counter = new AtomicInteger(1);

        get("/crawl/status", (req, res) ->
                (counter.get())-1+" Registros encontrados e contando...");

        post("/crawl", (req, res) ->
                "POST /crawl" + System.lineSeparator() + req.body());

        CrawlerService crawlerService;
        crawlerService =
                CrawlerService.build();



        for (int threadsNumber = MagicNumbers.ZERO.getValue();
             threadsNumber <= MagicNumbers.REPETICOES_THREAD_LOOP.getValue();
             threadsNumber++) {

            new Thread(() -> crawlerService.
                    run(counter,
                            System.getenv("BASE_URL"),
                            System.getenv("KEYWORD"))).start();
        }

        //logger.warn("Execução encerrada!");
    }

}
