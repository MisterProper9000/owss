package openway.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLParse {

    public static String findValueInString(String str, String template){
        Pattern pattern = Pattern.compile(template);
        Matcher matcher = pattern.matcher(str);
        matcher.find();
        String searchRes = matcher.group();
        return searchRes;
    }
}
