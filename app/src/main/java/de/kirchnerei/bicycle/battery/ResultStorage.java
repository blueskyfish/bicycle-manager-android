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
