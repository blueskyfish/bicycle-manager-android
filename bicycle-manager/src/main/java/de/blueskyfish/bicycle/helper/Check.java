/*
 * The MIT License (MIT)
 * Copyright (c) 2017 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.blueskyfish.bicycle.helper;

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
