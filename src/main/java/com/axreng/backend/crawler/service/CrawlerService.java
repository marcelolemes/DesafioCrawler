package com.axreng.backend;

import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scrap {
    final static Set<String> processedLinks = new HashSet<>();
    static Logger logger = LoggerFactory.getLogger(Scrap.class);

    public static Set<String> baseUrlLinkExtract(String baseUrl, String keyword) throws ExecutionException, InterruptedException {
        Set<String> innerLinks = new HashSet<>();
        String html = linkProcessor(baseUrl);

        String urlPattern1 = "(href=)+[^\\s]+[\\w]+[^\"]";
        Pattern pattern = Pattern.compile(urlPattern1);
        Matcher matcher = pattern.matcher(html);

        while (matcher.find()) {
            String uriTemp = matcher.group().replace("href=\"", "");
            if (uriTemp.contains("</a>") || uriTemp.contains("<")) {
            } else {
                try {
                    if (!URI.create(uriTemp).isAbsolute() && (uriTemp.contains(".html"))) {
                        uriTemp = String.valueOf((new URL(new URL(baseUrl), uriTemp)));
                        innerLinks.add(uriTemp);
                        if (!processedLinks.contains(uriTemp)) {
                            processedLinks.add(uriTemp);
                            logger.info(uriTemp);
                            if (haveKeyword(uriTemp, keyword)) {
                                logger.warn("Result found: " + uriTemp);
                            }
                            baseUrlLinkExtract(uriTemp, keyword);
                        }

                    }
                } catch (IllegalArgumentException ex) {
                    logger.error(uriTemp);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return innerLinks;

    }

    public static void process(String uri, String keyword) throws ExecutionException, InterruptedException, MalformedURLException {
        validateUrl(uri);
        baseUrlLinkExtract(uri, keyword);

    }

    private static void validateUrl(String url) throws MalformedURLException {
        String urlPattern = "(www|http:|https:)+[^\\s]+[\\w]";
        Pattern pattern = Pattern.compile(urlPattern);
        Matcher matcher = pattern.matcher(url);
        boolean matchFound = matcher.find();
        if (matchFound) {
            try {
                new URL(url);
            } catch (MalformedURLException ex) {
                throw new IllegalArgumentException("Link inválido");
            }
        } else {
            throw new IllegalArgumentException("Link Não HTTP encontrado.");
        }
    }

    private static String linkProcessor(String Uri) throws ExecutionException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        StringBuilder html = new StringBuilder();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(Uri))
                    .build();
            html.append(client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .get());
        } catch (Exception e) {
            logger.error("Não foi possível conectar, verifique a URL!");
        }
        return html.toString();
    }

    private static boolean haveKeyword(String uri, String keyword) throws ExecutionException, InterruptedException {
        return linkProcessor(uri).contains(keyword);
    }

    public Scrap() throws URISyntaxException {
    }
}
