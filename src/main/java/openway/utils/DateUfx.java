package openway.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUfx {

    public static String getDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(new Date());
        return dateString;
    }

    public static String getTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = format.format(new Date());
        return dateString;
    }


}
