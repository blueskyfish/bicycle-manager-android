/*
 * The MIT License (MIT)
 * Copyright (c) 2017 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.blueskyfish.bicycle.battery;

import de.blueskyfish.bicycle.http.BaseResult;

/**
 * Result of the request <code>GET: /battery/:id</code>. The property <b>battery</b> contains
 * the battery item for editing.
 */
public class ResultBatteryEdit extends BaseResult {

    private BatteryEdit battery;

    public BatteryEdit getBattery() {
        return battery;
    }

    public void setBattery(BatteryEdit battery) {
        this.battery = battery;
    }
}
