package com.axreng.backend.crawler.service.auxiliar;

import java.net.URI;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static com.axreng.backend.crawler.service.auxiliar.AuxiliarMethods.linkProcessor;

public class Conditions {

    public static BiPredicate<Integer, Integer> reachLimit = (limit, counter) ->
            (limit <= counter) || counter == MagicNumbers.DEFAULT_LIMIT.getValue();


    public static boolean notContainsUrl(Set<String> stringSet, String url) {
        return !stringSet.contains(url);
    }

    public static boolean haveKeyword(String uri, String keyword) throws ExecutionException, InterruptedException {
        return linkProcessor(uri).toLowerCase().contains(keyword.toLowerCase());
    }

    public static boolean validateInnerLink(String innerUri) throws ExecutionException, InterruptedException {
        return !URI.create(innerUri).isAbsolute() && (innerUri.contains(".html"));
    }
    public static boolean byPassHtmlTags(String innerUri) throws ExecutionException, InterruptedException {
        return (innerUri.contains("</a>") || innerUri.contains("<")) && innerUri.contains("mail");
    }

}



