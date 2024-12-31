package com.web.hotel.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static Date convertToDate(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(s);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isDate(String s){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            sdf.parse(s);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

}
