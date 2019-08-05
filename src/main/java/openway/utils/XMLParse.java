package openway.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLParse {

    public static String findValueInString(String str, String template){
        Pattern pattern = Pattern.compile(template);
        Matcher matcher = pattern.matcher(str);
        String searchRes = "";
        try{
            matcher.find();
            searchRes = matcher.group();
        }
        catch (Exception e){
            return e.toString();

        }
        return searchRes;
    }
}
