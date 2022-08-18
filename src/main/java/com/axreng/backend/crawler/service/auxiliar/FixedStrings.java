package com.axreng.backend.crawler.service.auxiliar;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FixedStrings {
    public final static List<String> StringRemoveList =
            Arrays.asList("href=\"", "\\", ">Prev", ">Next", "</a>", "<td",
                    "<td>", "</td>", "<tr>", "</tr>", "<th", "<hr", "<hr>", "</table>", "\"");
    public final static String spaceRegex = "[\\s|\\u00A0]+";
    final static String urlPattern = "(href=)+[^\\s]+[\\w]+[^\"]";
    final static String httpPattern = "(www|http:|https:)+[^\\s]+[\\w]";


    public static Matcher getInnerLinkMatcher(String url) {
        return Pattern.compile(urlPattern).matcher(url);
    }
    public static Matcher getHttpUrlMatcher(String url) {
        return Pattern.compile(httpPattern).matcher(url);
    }


}
