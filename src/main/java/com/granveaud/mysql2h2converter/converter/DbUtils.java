package com.granveaud.mysql2h2converter.converter;

public class DbUtils {
    public static String unescapeDbObjectName(String str) {
        if (str.startsWith("\"") || str.startsWith("`") || str.startsWith("'")) {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }
}
