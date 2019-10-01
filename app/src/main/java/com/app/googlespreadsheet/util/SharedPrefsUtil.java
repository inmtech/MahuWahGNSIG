package com.app.googlespreadsheet.util;

import android.content.Context;

import androidx.annotation.NonNull;

public class SharedPrefsUtil {
    private static SharedPrefsUtil instance;
    private static final String PREFS_NAME = "default_preferences";

    public synchronized static SharedPrefsUtil getInstance() {
        if (instance == null) {
            instance = new SharedPrefsUtil();
        }
        return instance;
    }

    private SharedPrefsUtil() {
    }

    public String getSheetName(@NonNull Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString("sheet_name", "");
    }

    public void setSheetName(@NonNull Context context, String value) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit().putString("sheet_name", value).apply();
    }

    public void setLogin(@NonNull Context context, Boolean value) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit().putBoolean("is_login", value).apply();
    }

    public boolean isLogin(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getBoolean("is_login", false);
    }
}