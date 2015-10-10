package de.kirchnerei.bicycle.helper;

import android.util.Log;

public class Logger {

    private static final String TAG = "Bicycle";

    public static void debug(String message, Object... args) {
        if (args != null && args.length > 0) {
            message = String.format(message, args);
        }
        Log.d(TAG, message);
    }
}
