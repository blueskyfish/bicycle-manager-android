/*
 * The MIT License (MIT)
 * Copyright (c) 2015 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.kirchnerei.bicycle.battery;

import de.kirchnerei.bicycle.http.BaseResult;

public class ResultStorage extends BaseResult {

    private int id;

    public ResultStorage() {
    }

    public ResultStorage(int id) {
        setStatus("error");
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
