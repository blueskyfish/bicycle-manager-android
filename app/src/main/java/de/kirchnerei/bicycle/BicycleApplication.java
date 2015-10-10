package de.kirchnerei.bicycle;

import android.app.Application;

import de.kirchnerei.bicycle.helper.Formatter;
import de.kirchnerei.bicycle.setting.SettingManager;

public class BicycleApplication extends Application {

    private SettingRepository mSetting;
    private Formatter mFormatter;

    @Override
    public void onCreate() {
        mSetting = new SettingManager(this);
        mFormatter = new Formatter(this);
    }

    public SettingRepository getSetting() {
        return mSetting;
    }

    public Formatter getFormatter() {
        return mFormatter;
    }
}
