package com.axreng.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import static spark.Spark.*;


public class Main {
    static Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws URISyntaxException, ExecutionException, InterruptedException {
        get("/crawl/:id", (req, res) ->
                "GET /crawl/" + req.params("id"));
        post("/crawl", (req, res) ->
                "POST /crawl" + System.lineSeparator() + req.body());


        logger.info("Aqui");
        Scrap.process(System.getenv("BASE_URL"));


    }
}
