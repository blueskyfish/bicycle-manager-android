/*
 * The MIT License (MIT)
 * Copyright (c) 2017 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.blueskyfish.bicycle.battery;

import java.util.List;

import de.blueskyfish.bicycle.http.BaseResult;

public class ResultBatteryList extends BaseResult {

    private List<BatteryItem> batteryList;

    public List<BatteryItem> getBatteryList() {
        return batteryList;
    }

    public void setBatteryList(List<BatteryItem> batteryList) {
        this.batteryList = batteryList;
    }
}
