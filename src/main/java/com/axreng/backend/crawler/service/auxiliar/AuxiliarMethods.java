package com.axreng.backend.crawler.service.auxiliar;

import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

public interface AuxiliarMethods {

    static boolean validUrl(String url) {
        boolean matchFound = FixedStrings.getHttpUrlMatcher(url).find();
        if (matchFound && url.endsWith("/")) {
            try {
                new URL(url);
                return true;
            } catch (MalformedURLException ex) {
                throw new IllegalArgumentException("Link inválido" + url);
            }
        } else {
            throw new IllegalArgumentException("Link inválido encontrado. " + url);
        }
    }

    static String relativeToAbsoluteUrl(String baseUrl, String uri) {
        try {
            return String.valueOf((new URL(new URL(baseUrl), uri)));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Link inválido " + uri);
        }
    }

    static String linkProcessor(String uri){
        HttpClient client = HttpClient.newHttpClient();
        StringBuilder html = new StringBuilder();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .build();
            html.append(client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .get());
        } catch (RuntimeException|ExecutionException e) {
            LoggerFactory.getLogger(AuxiliarMethods.class).
                    error("Não foi possível conectar, verifique a URL! " + uri);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return html.toString();
    }

    static String validateStringLink(String inputUrl) {

        inputUrl = inputUrl.replaceAll(FixedStrings.spaceRegex, "");
        for (String removeString : FixedStrings.StringRemoveList) {
            inputUrl = inputUrl.replace(removeString, "");
        }

        return inputUrl;
    }
}
