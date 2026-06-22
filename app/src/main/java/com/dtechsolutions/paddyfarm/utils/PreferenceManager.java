package com.dtechsolutions.paddyfarm.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private static final String PREF_NAME = "app_prefs";
    private static final String KEY_TOKEN = "jwt_token";

    private final SharedPreferences sharedPreferences;


    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        sharedPreferences.edit()
                .putString(KEY_TOKEN, token)
                .apply();
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, "");
    }

    public void clearToken() {
        sharedPreferences.edit()
                .remove(KEY_TOKEN)
                .apply();
    }
}
