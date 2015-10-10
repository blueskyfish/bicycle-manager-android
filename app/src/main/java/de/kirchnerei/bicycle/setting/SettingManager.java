package de.kirchnerei.bicycle.setting;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import de.kirchnerei.bicycle.SettingRepository;
import de.kirchnerei.bicycle.helper.Check;
import de.kirchnerei.bicycle.helper.Logger;
import de.kirchnerei.bicycle.helper.StringUtil;

public class SettingManager implements SettingRepository {

    public static final String PREF_FILENAME = "de.kirchnerei.bicycle.settings";
    public static final String PREF_USER_EMAIL = "de.kirchnerei.bicycle.userEmail";
    public static final String PREF_PASSWORD = "de.kirchnerei.bicycle.password";
    public static final String PREF_TOKEN = "de.kirchnerei.bicycle.token";

    private String mUserEmail = null;
    private String mPassword = null;
    private String mToken = null;

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

        HashCode hashCode = hashFunction
            .newHasher()
            .putString(userEmail, Charsets.UTF_8)
            .putString(password, Charsets.UTF_8)
            .hash();

        Logger.debug("save login [%s - %s]", userEmail, password);
        String token = hashCode.toString();
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
}
