package com.ty.wetalk.utils;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Utils {
    public static String dateToString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }
}
