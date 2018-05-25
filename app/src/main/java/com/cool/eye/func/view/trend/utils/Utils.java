package com.cool.eye.func.view.trend.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by cool on 2018/3/1.
 */
public class Utils {

    private static Date date;

    public static int getHour(long time) {

        if (date == null) {
            date = new Date();
        }
        date.setTime(time);
        return date.getHours();
    }

    public static String formatDate(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return sdf.format(time);
    }

    public static String formatDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return sdf.format(new Date());
    }
}
