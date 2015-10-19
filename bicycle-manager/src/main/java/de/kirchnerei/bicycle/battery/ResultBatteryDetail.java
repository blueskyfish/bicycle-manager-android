/*
 * The MIT License (MIT)
 * Copyright (c) 2015 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.kirchnerei.bicycle.battery;

import de.kirchnerei.bicycle.http.BaseResult;

public class ResultBatteryDetail extends BaseResult {

    private BatteryItem battery;

    public BatteryItem getBattery() {
        return battery;
    }

    public void setBattery(BatteryItem battery) {
        this.battery = battery;
    }
}
