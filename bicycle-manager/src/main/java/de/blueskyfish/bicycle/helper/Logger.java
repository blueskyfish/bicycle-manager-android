/*
 * The MIT License (MIT)
 * Copyright (c) 2017 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.blueskyfish.bicycle.helper;

import android.util.Log;

public class Logger {

    private static final String TAG = "Bicycle";

    private static Level level = Level.DEBUG;

    public static void setLogLevel(String s) {
        level = Level.fromString(s);
    }

    public static void debug(String message, Object... args) {
        if (level == Level.DEBUG) {
            Log.d(TAG, StringUtil.format(message, args));
        }
    }

    public static void warn(String message, Object... args) {
        Log.w(TAG, StringUtil.format(message, args));
    }

    public static enum Level {
        DEBUG,
        WARN;

        @Override
        public String toString() {
            return name().toLowerCase();
        }

        public static Level fromString(String s) {
            if (s == null || s.isEmpty()) {
                throw new IllegalArgumentException("Log level is null");
            }
            return valueOf(s.toUpperCase());
        }
    }
}
