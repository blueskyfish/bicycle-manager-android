package de.kirchnerei.bicycle.helper;

public class StringUtil {

    public static String whenNull(String value, String def) {
        if (value == null) {
            return def;
        }
        return value;
    }

    public static String format(String message, Object... args) {
        if (args != null && args.length > 0) {
            return String.format(message, args);
        }
        return message;
    }
}
