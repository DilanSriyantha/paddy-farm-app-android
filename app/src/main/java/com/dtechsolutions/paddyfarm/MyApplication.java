package com.dtechsolutions.paddyfarm;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.dtechsolutions.paddyfarm.data.api.ApiClient;
import com.dtechsolutions.paddyfarm.data.api.ApiService;
import com.dtechsolutions.paddyfarm.utils.TokenProvider.SharedPrefsTokenProvider;
import com.dtechsolutions.paddyfarm.utils.TokenProvider.TokenProvider;

public class MyApplication extends Application {
    private static TokenProvider tokenProvider;
    private static ApiService apiService;

    @Override
    public void onCreate() {
        super.onCreate();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        tokenProvider = new SharedPrefsTokenProvider(this);
        apiService = ApiClient.getInstance(tokenProvider);
    }

    public static ApiService getApiService() {
        return apiService;
    }

    public static TokenProvider getTokenProvider() {
        return tokenProvider;
    }
}
