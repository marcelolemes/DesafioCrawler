package com.axreng.backend;

import com.axreng.backend.crawler.controller.CrawlerController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;


public class Main {
    static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws MalformedURLException, ExecutionException, InterruptedException {
        CrawlerController.runController();
    }
}
