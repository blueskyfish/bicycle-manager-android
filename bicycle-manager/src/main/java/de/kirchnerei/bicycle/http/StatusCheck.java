/*
 * The MIT License (MIT)
 * Copyright (c) 2015 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.kirchnerei.bicycle.http;

public final class StatusCheck {

    public static final String STATUS_OKAY = "okay";

    public static boolean isOkay(String status) {
        return STATUS_OKAY.equals(status);
    }

    public static boolean isOkay(BaseResult result) {
        return isOkay(result.getStatus());
    }
}
