/*
 * The MIT License (MIT)
 * Copyright (c) 2017 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.blueskyfish.bicycle.setting;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import de.blueskyfish.bicycle.R;
import de.blueskyfish.bicycle.SettingRepository;
import de.blueskyfish.bicycle.helper.Check;
import de.blueskyfish.bicycle.helper.Logger;
import de.blueskyfish.bicycle.helper.StringUtil;

public class SettingManager implements SettingRepository {

    public static final String PREF_FILENAME = "de.kirchnerei.bicycle.settings";
    public static final String PREF_USER_EMAIL = "de.kirchnerei.bicycle.userEmail";
    public static final String PREF_PASSWORD = "de.kirchnerei.bicycle.password";
    public static final String PREF_TOKEN = "de.kirchnerei.bicycle.token";
    public static final String PREF_BASE_URL = "de.kirchnerei.bicycle.baseUrl";
    public static final String PREF_CURRENT_VIEW = "de.kirchnerei.bicycle.currentView";

    private String mUserEmail = null;
    private String mPassword = null;
    private String mToken = null;

    private String mBaseUrl = null;

    private HashFunction hashFunction = Hashing.sha1();

    private final Context mContext;

    public SettingManager(Context context) {
        this.mContext = context;
    }

    @Override
    public String getToken() {
        if (mToken == null) {
            SharedPreferences prefs = mContext.getSharedPreferences(
                PREF_FILENAME, Context.MODE_PRIVATE);
            mToken = prefs.getString(PREF_TOKEN, null);
        }
        return StringUtil.whenNull(mToken, "");
    }

    @Override
    public boolean hasToken() {
        String p = getPassword();
        return p != null;
    }

    @Override
    public void verifyToken(String token) {
        String originToken = buildToken(getUserEmail(), getPassword());
        if (!originToken.equals(token)) {
            Logger.warn("token is not equal to the origin '%s'", originToken);
        }
    }

    @Override
    public String getBaseUrl() {
        if (mBaseUrl == null) {
            mBaseUrl = mContext.getString(R.string.setting_base_url);
            Logger.debug("base url '%s'", mBaseUrl);
        }
        return mBaseUrl;
    }

    @Override
    public String getUserEmail() {
        if (mUserEmail == null) {
            SharedPreferences prefs = mContext.getSharedPreferences(
                PREF_FILENAME, Context.MODE_PRIVATE);
            mUserEmail = prefs.getString(PREF_USER_EMAIL, null);
        }
        return StringUtil.whenNull(mUserEmail, "");
    }

    @Override
    public String getPassword() {
        if (mPassword == null) {
            SharedPreferences prefs = mContext.getSharedPreferences(
                PREF_FILENAME, Context.MODE_PRIVATE);
            mPassword = prefs.getString(PREF_PASSWORD, null);
        }
        return StringUtil.whenNull(mPassword, "");
    }

    @Override
    public void change(String userEmail, String password) {
        Check.notNull(userEmail,
            "Could not change the settings, because the parameter 'userEmail' is null!");
        Check.notNull(password,
            "Could not change the settings, because the parameter 'password' is null!");

        String token = buildToken(userEmail, password);
        Logger.debug("token [%s]", token);

        SharedPreferences prefs = mContext.getSharedPreferences(
            PREF_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(PREF_USER_EMAIL, userEmail);
        editor.putString(PREF_PASSWORD, password);
        editor.putString(PREF_TOKEN, token);
        editor.apply();

        mUserEmail = userEmail;
        mPassword = password;
        mToken = token;
    }

    @Override
    public void storeCurrentView(int tagId) {
        // save only a view without Bundle arguments!!
        switch (tagId) {
            case R.string.fragment_battery_detail:
            case R.string.fragment_battery_edit:
                tagId = R.string.fragment_battery_list;
                break;
        }
        SharedPreferences prefs = mContext.getSharedPreferences(
            PREF_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREF_CURRENT_VIEW, tagId);
        editor.apply();
    }

    @Override
    public int getCurrentView() {
        SharedPreferences prefs = mContext.getSharedPreferences(
            PREF_FILENAME, Context.MODE_PRIVATE);
        return prefs.getInt(PREF_CURRENT_VIEW, R.string.fragment_overview);
    }

    private String buildToken(String userEmail, String password) {
        HashCode hashCode = hashFunction
            .newHasher()
            .putString(userEmail, Charsets.UTF_8)
            .putString(password, Charsets.UTF_8)
            .hash();
        return hashCode.toString();
    }
}
