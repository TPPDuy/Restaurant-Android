package com.ecdc.androidlibs.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;

import com.ecdc.androidlibs.database.entity.User;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

public final class CacheProvider {

    private static String TAG = CacheProvider.class.getSimpleName();
    public static String USER_TYPE = "user_type";
    public static String KEY_TYPE = TAG + "_KEY";

    private final Gson gson;
    private static CacheProvider mInstance;
    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;

    private CacheProvider(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        editor.apply();
        gson = new Gson();
        setDeviceId(context);
    }

    void setDeviceId(Context context) {
        try {
            putToCache(KEY_TYPE, Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getToken() {
        try {
            User user = getFromCache(USER_TYPE, User.class);
            if (user != null)
                return user.getAuthorization();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getDeviceId() {
        return getString(KEY_TYPE);
    }

    public static CacheProvider of(Context context) {
        if (mInstance == null)
            mInstance = new CacheProvider(context);
        return mInstance;
    }

    public static CacheProvider self() {
        return mInstance;
    }

    public void logout() {
        removes(USER_TYPE);
    }
    public boolean getBoolean(String key) {
        boolean value = false;
        if (preferences.contains(key))
            value = preferences.getBoolean(key, false);
        return value;
    }

    public int getInt(String key) {
        if (preferences.contains(key))
            return preferences.getInt(key, -1);
        return -1;
    }

    public String getString(String key) {
        if (preferences != null && preferences.contains(key))
            return preferences.getString(key, "");
        return "";
    }

    public <T> void putToCache(String key, T value) {
        if (preferences != null && editor != null) {
            if (value instanceof Integer)
                editor.putInt(key, (Integer) value);
            else if (value instanceof Boolean)
                editor.putBoolean(key, (Boolean) value);
            else if (value instanceof String)
                editor.putString(key, (String) value);
            else if (value instanceof List)
                editor.putString(key, value.toString());
            editor.apply();
        }
    }

    public boolean isLoggedIn() {
        try {
            User user = getUser();
            if (user != null)
                return !TextUtils.isEmpty(user.getAuthorization());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUser() {
        try {
            return getFromCache(USER_TYPE, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T getFromCache(String key, Type type) {
        try {
            String data = getString(key);
            return gson.fromJson(data, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T getFromCache(String key, Class<T> classOfT) {
        try {
            String data = getString(key);
            return gson.fromJson(data, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removes(String... keys) {
        if (preferences != null && editor != null) {
            for (String key : keys)
                editor.remove(key);
            editor.apply();
        }
    }
}