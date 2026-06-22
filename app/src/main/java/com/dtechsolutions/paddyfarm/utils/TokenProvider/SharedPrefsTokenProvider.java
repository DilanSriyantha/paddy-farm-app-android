package com.dtechsolutions.paddyfarm.utils.TokenProvider;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsTokenProvider implements TokenProvider{
    private final SharedPreferences preferences;

    public SharedPrefsTokenProvider(Context context) {
        preferences = context.getSharedPreferences(
                "app_prefs",
                Context.MODE_PRIVATE
        );
    }

    @Override
    public String getToken() {
        return preferences.getString("jwt_token", "");
    }

    public void saveToken(String token) {
        preferences.edit()
                .putString("jwt_token", token)
                .apply();
    }

    public void clearToken() {
        preferences.edit()
                .remove("jwt_token")
                .apply();
    }
}
