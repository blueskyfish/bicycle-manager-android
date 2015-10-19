/*
 * The MIT License (MIT)
 * Copyright (c) 2015 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.kirchnerei.bicycle.helper;

public final class NumberUtils {

    public static int toNumber(String s) {
        if (s == null || s.isEmpty()) {
            Logger.warn("null or empty is not a double!");
            return 0;
        }
        s = s.replaceAll(",", ".");
        try {
            Double value = Double.valueOf(s) * 10;
            return value.intValue();
        } catch (NumberFormatException e) {
            Logger.warn("%s is not a double!", s);
            return 0;
        }
    }


    private NumberUtils() {}
}
