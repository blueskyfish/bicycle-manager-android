/*
 * The MIT License (MIT)
 * Copyright (c) 2015 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.kirchnerei.bicycle;

public enum FloatingButtonKind {

    SETTING(R.drawable.ic_save_white_18dp),
    OVERVIEW_SETTING(R.drawable.ic_settings_white_18dp),
    OVERVIEW_BATTERY(R.drawable.ic_battery_white_18dp),
    BATTERY(R.drawable.ic_battery_charging_white_18dp),
    BATTERY_ADD(R.drawable.ic_save_white_18dp),
    BATTERY_SAVE(R.drawable.ic_save_white_18dp),
    BATTERY_DETAIL(R.drawable.ic_edit_white_18dp);


    private final int imageId;

    FloatingButtonKind(int imageId) {
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }
}
