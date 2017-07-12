package com.jhp.banseok.bap.tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preference {
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    public Preference(Context mContext) {
        mPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        mEditor = mPref.edit();
    }

    public Preference(Context mContext, String prefName) {
        mPref = mContext.getSharedPreferences(prefName, 0);
        mEditor = mPref.edit();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mPref.getBoolean(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return mPref.getInt(key, defValue);
    }

    public String getString(String key, String defValue) {
        return mPref.getString(key, defValue);
    }

    public void putBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value).commit();
    }

    public void putInt(String key, int value) {
        mEditor.putInt(key, value).commit();
    }

    public void putString(String key, String value) {
        mEditor.putString(key, value).commit();
    }

    public void clear() {
        mEditor.clear().commit();
    }

    public void remove(String key) {
        mEditor.remove(key).commit();
    }
}
