package de.kirchnerei.bicycle;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.kirchnerei.bicycle.helper.Formatter;
import de.kirchnerei.bicycle.http.DiagnoseManager;
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
