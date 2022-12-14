package com.axreng.backend.crawler.service;

import com.axreng.backend.crawler.service.auxiliar.AuxiliarMethods;
import com.axreng.backend.crawler.service.auxiliar.Conditions;
import com.axreng.backend.crawler.service.auxiliar.FixedStrings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;

import static com.axreng.backend.crawler.service.auxiliar.Conditions.reachLimit;

public class CrawlerServiceImpl extends Thread implements CrawlerService {
    public CrawlerServiceImpl() {
    }

    final static Set<String> processedLinks = Collections.synchronizedSet(new HashSet<>());
    static Logger logger = LoggerFactory.getLogger(CrawlerService.class);

    private void urlAnalysis(int limit,
                             AtomicInteger counter,
                             String baseUrl,
                             String keyword)
            throws ExecutionException, InterruptedException {
        String html = AuxiliarMethods.linkProcessor(baseUrl);
        Matcher matcher = FixedStrings.getInnerLinkMatcher(html);
        while (matcher.find() &&
                reachLimit.test(counter.get(), limit)) {
            String innerUri = AuxiliarMethods.validateStringLink(matcher.group());
            if (Conditions.byPassHtmlTags(innerUri)) {
            } else {
                try {
                    if (Conditions.validateInnerLink(innerUri)) {
                        innerUri = AuxiliarMethods.relativeToAbsoluteUrl(baseUrl, innerUri);
                        if (Conditions.notContainsUrl(processedLinks, innerUri)) {
                            processedLinks.add(innerUri);
                            if (Conditions.haveKeyword(innerUri, keyword)) {
                                logger.warn("Result found: " + innerUri);
                                counter.incrementAndGet();
                            }
                            urlAnalysis(limit, counter, innerUri, keyword);
                        }
                    }
                } catch (IllegalArgumentException ex) {
                    logger.warn("Um erro ocorreu na URL: " + innerUri + " " + ex.getMessage());

                }
            }

        }

    }

    @Override
    public void run(int limit, AtomicInteger counter, String uri, String keyword) {
        if (AuxiliarMethods.validUrl(uri)) {
            try {
                this.urlAnalysis(limit, counter, uri, keyword);
            } catch (ExecutionException executionException) {
                logger.error("Erro de execu????o: " + executionException.getMessage());
            } catch (InterruptedException interruptedException) {
                logger.error("Erro de interrup????o: " + interruptedException.getMessage());
            } catch (Exception exception) {
                logger.error("Exce????o ainda n??o tratada: ");
                exception.printStackTrace();
            }
        }
    }


}
