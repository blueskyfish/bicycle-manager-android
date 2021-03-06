/*
 * The MIT License (MIT)
 * Copyright (c) 2017 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.blueskyfish.bicycle;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.blueskyfish.bicycle.helper.Formatter;
import de.blueskyfish.bicycle.helper.Logger;
import de.blueskyfish.bicycle.http.HttpManager;
import de.blueskyfish.bicycle.setting.SettingManager;
import de.blueskyfish.httpclient.Definition;

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
        mMapper.setDateFormat(Definition.DEFAULT_DATE_FORMAT);
        Logger.setLogLevel(getString(R.string.logger_level));
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

    /**
     * Checks whether the network is online and a access to the internet is possible.
     *
     * @return true, when the access to internet is possible, otherwise it is false.
     */
    public boolean isNetworkOnline() {
        ConnectivityManager cm =
            (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
