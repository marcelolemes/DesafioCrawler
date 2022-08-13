package com.axreng.backend;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Scrap {

    public static List<String> request() throws ExecutionException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(System.getenv("BASE_URL")))
                .build();
        StringBuilder html = new StringBuilder();
        html.append(client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .get());
        List letterSoup = List.of(html.toString().split(" "));

        return letterSoup;

    }


    public Scrap() throws URISyntaxException {
    }
}
