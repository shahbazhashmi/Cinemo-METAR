package cinemo.metar.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Shahbaz Hashmi on 2020-01-29.
 */
public class StringUtils {

    public static String getBetweenString(String st, String pattern1, String pattern2) {

        String regexString = Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2);
        Matcher matcher = Pattern.compile(regexString).matcher(st);

        while (matcher.find()) {
            return matcher.group(1).trim(); // Since (.*?) is capturing group 1
        }

        return "";
    }


    public static String getAnchorTagText(String subjectString) {
        Pattern titleFinder = Pattern.compile("<a[^>]*>(.*?)</a>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Matcher regexMatcher = titleFinder.matcher(subjectString);
        while (regexMatcher.find()) {
             return regexMatcher.group(1).trim();
        }
        return "";
    }

    public static String removeHtmlTags(String data) {
        return data.replaceAll("\\<.*?\\>", "").trim();
    }

}
