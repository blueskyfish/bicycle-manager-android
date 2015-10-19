/*
 * The MIT License (MIT)
 * Copyright (c) 2015 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.kirchnerei.bicycle.helper;

public class StringUtil {

    public static String whenNull(String value, String def) {
        if (value == null) {
            return def;
        }
        return value;
    }

    public static int length(String s) {
        if (s == null || s.isEmpty()) {
            return -1;
        }
        return s.length();
    }

    public static String format(String message, Object... args) {
        if (args != null && args.length > 0) {
            return String.format(message, args);
        }
        return message;
    }
}
