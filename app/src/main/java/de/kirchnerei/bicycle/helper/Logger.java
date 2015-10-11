package de.kirchnerei.bicycle.helper;

import android.util.Log;

public class Logger {

    private static final String TAG = "Bicycle";

    public static void debug(String message, Object... args) {
        Log.d(TAG, StringUtil.format(message, args));
    }

    public static void warn(String message, Object... args) {
        Log.w(TAG, StringUtil.format(message, args));
    }
}
