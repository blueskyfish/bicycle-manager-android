/*
 * The MIT License (MIT)
 * Copyright (c) 2017 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.blueskyfish.bicycle.battery;

import de.blueskyfish.bicycle.http.BaseResult;

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
