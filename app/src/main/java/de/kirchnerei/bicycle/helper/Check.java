package de.kirchnerei.bicycle.helper;

import java.util.Objects;

public final class Check {

    public static void notNull(Object o, String message, Object... args) {
        if (o == null) {
            if (args != null && args.length > 0) {
                message = String.format(message, args);
            }
            // TODO replace with an own exception type
            throw new IllegalArgumentException(message);
        }
    }
}
