package com.axreng.backend.crawler.service.auxiliar;

import java.util.function.BiPredicate;
public class Conditions {

    public static BiPredicate<Integer, Integer> reachLimit = (limit, counter) ->
            (limit <= counter)||counter == MagicNumbers.DEFAULT_LIMIT.getValue();

    }



