package com.web.hotel.util;

public class DoubleUtil {
    public static boolean isDouble(String str) {
        try{
            Double.parseDouble(str);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
