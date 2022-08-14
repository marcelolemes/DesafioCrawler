package com.axreng.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scrap {
    static List letterSoup = new ArrayList();
    static Logger logger = LoggerFactory.getLogger(Scrap.class);

    public static List<String> request(String baseUrl) throws ExecutionException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .build();
        StringBuilder html = new StringBuilder();
        html.append(client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .get());

        String urlPattern1 = "(href=)+[^\\s]+[\\w]+[^\"]";
        Pattern pattern = Pattern.compile(urlPattern1);
        Matcher matcher = pattern.matcher(html);

        while (matcher.find()) {
            String uriTemp = matcher.group().replace("href=\"", "");
            if (uriTemp.contains("</a>") || uriTemp.contains("<")) {
            } else {
                try {
                    if (!URI.create(uriTemp).isAbsolute() && (uriTemp.contains(".html"))) {
                        uriTemp = baseUrl + uriTemp;
                    }
                    if (uriTemp.contains(baseUrl)) {
                        //letterSoup.add(uriTemp);
                        //request(uriTemp);
                        logger.warn(uriTemp);
                    }
                } catch (IllegalArgumentException ex) {
                    logger.error(uriTemp);
                }
            }
        }


        return letterSoup;

    }


    public Scrap() throws URISyntaxException {
    }
}
