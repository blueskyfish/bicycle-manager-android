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
