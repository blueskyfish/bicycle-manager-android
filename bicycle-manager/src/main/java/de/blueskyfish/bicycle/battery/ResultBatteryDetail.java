/*
 * The MIT License (MIT)
 * Copyright (c) 2017 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.blueskyfish.bicycle.battery;

import de.blueskyfish.bicycle.http.BaseResult;

public class ResultBatteryDetail extends BaseResult {

    private BatteryItem battery;

    public BatteryItem getBattery() {
        return battery;
    }

    public void setBattery(BatteryItem battery) {
        this.battery = battery;
    }
}
