package de.kirchnerei.bicycle;

import android.app.Application;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.kirchnerei.bicycle.helper.Formatter;
import de.kirchnerei.bicycle.http.HttpManager;
import de.kirchnerei.bicycle.setting.SettingManager;

public class BicycleApplication extends Application {

    private SettingRepository mSetting;
    private Formatter mFormatter;
    private HttpManager mHttpManager;
    private ObjectMapper mMapper;

    @Override
    public void onCreate() {
        super.onCreate();
        mSetting = new SettingManager(this);
        mFormatter = new Formatter(this);
        mHttpManager = new HttpManager(mSetting, this);
        mMapper = new ObjectMapper();
    }

    public SettingRepository getSetting() {
        return mSetting;
    }

    public Formatter getFormatter() {
        return mFormatter;
    }

    public HttpManager getHttpManager() {
        return mHttpManager;
    }

    public ObjectMapper getMapper() {
        return mMapper;
    }
}
