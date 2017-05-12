/*
 * The MIT License (MIT)
 * Copyright (c) 2017 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.blueskyfish.bicycle.helper;

import de.blueskyfish.bicycle.R;

public enum Unit {

    DISTANCE(R.string.unit_format_distance),

    SPEED(R.string.unit_format_speed);

    private final int formatId;

    Unit(int formatId) {
        this.formatId = formatId;
    }

    public int getFormatId() {
        return formatId;
    }
}
