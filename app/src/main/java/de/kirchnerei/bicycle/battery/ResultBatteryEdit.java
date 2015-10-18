package de.kirchnerei.bicycle.battery;

import de.kirchnerei.bicycle.http.BaseResult;

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
