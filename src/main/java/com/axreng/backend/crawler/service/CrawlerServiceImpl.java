package com.axreng.backend.crawler.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.axreng.backend.crawler.service.auxiliar.Conditions.reachLimit;

public class CrawlerServiceImpl extends Thread implements CrawlerService {
    final static Set<String> processedLinks = Collections.synchronizedSet(new HashSet<>());
    final Set<String> result = Collections.synchronizedSet(new HashSet<>());

    static Logger logger = LoggerFactory.getLogger(CrawlerService.class);

    public Set<String> baseUrlLinkExtract(AtomicInteger counter,
                                          String baseUrl,
                                          String keyword)
            throws ExecutionException, InterruptedException {


        final int limit = System.getenv("MAX_RESULTS") != null ?
                Integer.parseInt(
                        System.getenv("MAX_RESULTS")) : -1;
        Set<String> innerLinks = new HashSet<>();
        String html = linkProcessor(baseUrl);

        String urlPattern1 = "(href=)+[^\\s]+[\\w]+[^\"]";
        Pattern pattern = Pattern.compile(urlPattern1);
        Matcher matcher = pattern.matcher(html);
        while (matcher.find() &&
                reachLimit.test(counter.get(), limit)) {

            String uriTemp = matcher.group().replace("href=\"", "");
            if (uriTemp.contains("</a>") || uriTemp.contains("<")) {
            } else {
                try {
                    if (!URI.create(uriTemp).isAbsolute() && (uriTemp.contains(".html"))) {
                        uriTemp = relativeToAbsoluteUrl(baseUrl, uriTemp);
                        innerLinks.add(uriTemp);
                        if (!processedLinks.contains(uriTemp)) {
                            processedLinks.add(uriTemp);
                            if (haveKeyword(uriTemp, keyword)) {
                                logger.warn("Result found: " + uriTemp);
                                counter.incrementAndGet();
                                result.add(uriTemp);
                            }
                            baseUrlLinkExtract(counter, uriTemp, keyword);
                        }

                    }
                } catch (IllegalArgumentException ex) {

                }
            }

        }

        return result;

    }

    @Override
    public void run(AtomicInteger counter, String uri, String keyword) {
        if (validUrl(uri)) {
            try {
                this.baseUrlLinkExtract(counter, uri, keyword);
            } catch (ExecutionException executionException) {
                logger.error("Erro de execução: " + executionException.getMessage());
            } catch (InterruptedException interruptedException) {
                logger.error("Erro de interrupção: " + interruptedException.getMessage());
            } catch (Exception exception) {
                logger.error("Exceção ainda não tratada: ");
                exception.printStackTrace();
            }
        }
    }

    private static boolean validUrl(String url) {
        String urlPattern = "(www|http:|https:)+[^\\s]+[\\w]";
        Pattern pattern = Pattern.compile(urlPattern);
        Matcher matcher = pattern.matcher(url);
        boolean matchFound = matcher.find();
        if (matchFound && url.endsWith("/")) {
            try {
                new URL(url);
                return true;
            } catch (MalformedURLException ex) {
                throw new IllegalArgumentException("Link inválido");
            }
        } else {
            throw new IllegalArgumentException("Link Não HTTP encontrado.");
        }
    }

    private static String relativeToAbsoluteUrl(String baseUrl, String uri) {
        try {
            return String.valueOf((new URL(new URL(baseUrl), uri)));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Link inválido");
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
        return linkProcessor(uri).toLowerCase().contains(keyword.toLowerCase());
    }

    public CrawlerServiceImpl() throws URISyntaxException {
    }
}
