/*
 * The MIT License (MIT)
 * Copyright (c) 2015 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.kirchnerei.bicycle.battery;

import java.util.Date;

/**
 * POJO for a battery item
 */
public class BatteryItem extends BatteryEdit {

    private int distance;

    public BatteryItem() {
    }

    public BatteryItem(int id, Date date, int averageSpeed, int mileage, int leftover, int distance) {
        super(id, date, averageSpeed, mileage, leftover);
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
