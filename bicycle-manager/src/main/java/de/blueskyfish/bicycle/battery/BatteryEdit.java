/*
 * The MIT License (MIT)
 * Copyright (c) 2017 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.blueskyfish.bicycle.battery;

import java.util.Date;

/**
 * POJO for a battery edit
 */
public class BatteryEdit {

    private int id;

    private Date date;

    private int averageSpeed;

    private int mileage;

    private int leftover;

    public BatteryEdit() {
    }

    public BatteryEdit(int id, Date date, int averageSpeed, int mileage, int leftover) {
        this.id = id;
        this.date = date;
        this.averageSpeed = averageSpeed;
        this.mileage = mileage;
        this.leftover = leftover;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(int averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public int getLeftover() {
        return leftover;
    }

    public void setLeftover(int leftover) {
        this.leftover = leftover;
    }
}
