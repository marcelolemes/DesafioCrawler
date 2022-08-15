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

    public static Set<String> baseUrlLinkExtract(String baseUrl) throws ExecutionException, InterruptedException {
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
                            if (linkProcessor(uriTemp).contains("four")) {
                                logger.warn(uriTemp);
                                logger.warn("FOUR FIND");
                            }
                            baseUrlLinkExtract(uriTemp);
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

//    public static void processInnerLinks(String baseUrl) throws ExecutionException, InterruptedException {
//        String html = linkProcessor(baseUrl);
//
//        String urlPattern1 = "(href=)+[^\\s]+[\\w]+[^\"]";
//        String urlPattern2 = "(four)";
//        Pattern pattern = Pattern.compile(urlPattern1);
//        Pattern pattern2 = Pattern.compile(urlPattern2);
//        Matcher matcher = pattern.matcher(html);
//        Matcher matcher2 = pattern2.matcher(html);
//        boolean keyword = matcher2.matches();
//        logger.warn("four: "+keyword);
//        while (matcher.find()) {
//            String uriTemp = matcher.group().replace("href=\"", "");
//            if (uriTemp.contains("</a>") || uriTemp.contains("<")) {
//            } else {
//                try {
//                    if (!URI.create(uriTemp).isAbsolute() && (uriTemp.contains(".html"))) {
//                        uriTemp = String.valueOf((new URL(new URL(baseUrl), uriTemp)));
//                        logger.warn("RELATIVO *");
//                        logger.warn(uriTemp);
//                        if (!processedLinks.contains(uriTemp)) {
//                            processedLinks.add(uriTemp);
//                            processInnerLinks(uriTemp);
//                        }
//
//                    }
//
//                } catch (IllegalArgumentException ex) {
//                    logger.error(uriTemp);
//                } catch (MalformedURLException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }
//
//
//    }

    public static void process(String host) throws ExecutionException, InterruptedException {
        baseUrlLinkExtract(host);

    }

    private static String linkProcessor(String Uri) throws ExecutionException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Uri))
                .build();
        StringBuilder html = new StringBuilder();
        html.append(client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .get());
        return html.toString();
    }

    public Scrap() throws URISyntaxException {
    }
}
