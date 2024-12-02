package com.web.hotel.util;

public class LongUtil {
    public static boolean isLong(String str) {
        try{
            Long.parseLong(str);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
