package de.kirchnerei.bicycle.helper;

public class StringUtil {

    public static String whenNull(String value, String def) {
        if (value == null) {
            return def;
        }
        return value;
    }
}
