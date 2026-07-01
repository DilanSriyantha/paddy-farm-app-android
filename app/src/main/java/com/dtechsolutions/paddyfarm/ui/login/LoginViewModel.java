package com.dtechsolutions.paddyfarm.ui.login;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dtechsolutions.paddyfarm.MyApplication;
import com.dtechsolutions.paddyfarm.data.models.AuthResponse;
import com.dtechsolutions.paddyfarm.data.repositories.AuthRepository;
import com.dtechsolutions.paddyfarm.utils.AlertEvent;
import com.dtechsolutions.paddyfarm.utils.BaseViewModel;
import com.dtechsolutions.paddyfarm.utils.Resource;
import com.dtechsolutions.paddyfarm.utils.TokenProvider.SharedPrefsTokenProvider;

public class LoginViewModel extends BaseViewModel {
    private final AuthRepository repository;
    private final MutableLiveData<Resource<AuthResponse>> result;

    public LoginViewModel() {
        repository = new AuthRepository();
        result = new MutableLiveData<>();
    }

    public LiveData<Resource<AuthResponse>> getLoginResult() {
        return result;
    }

    public void login(String email, String password) {
        repository
                .login(email, password)
                .observeForever(this::updateLoginResult);
    }

    private void updateLoginResult(Resource<AuthResponse> authResponse) {
        if(authResponse.status == Resource.Status.SUCCESS) {
            SharedPrefsTokenProvider tokenProvider = (SharedPrefsTokenProvider) MyApplication.getTokenProvider();
            tokenProvider.saveToken(authResponse.data.getAccessToken());
        }

        result.setValue(authResponse);
    }
}
