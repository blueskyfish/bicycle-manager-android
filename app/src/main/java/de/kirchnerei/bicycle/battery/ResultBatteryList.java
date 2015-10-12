package de.kirchnerei.bicycle.battery;

import java.util.List;

import de.kirchnerei.bicycle.battery.BatteryItem;
import de.kirchnerei.bicycle.http.BaseResult;

public class ResultBatteryList extends BaseResult {

    private List<BatteryItem> batteryList;

    public List<BatteryItem> getBatteryList() {
        return batteryList;
    }

    public void setBatteryList(List<BatteryItem> batteryList) {
        this.batteryList = batteryList;
    }
}