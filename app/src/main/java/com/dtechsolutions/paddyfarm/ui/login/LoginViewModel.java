package com.dtechsolutions.paddyfarm.ui.login;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.dtechsolutions.paddyfarm.MyApplication;
import com.dtechsolutions.paddyfarm.data.models.AuthResponse;
import com.dtechsolutions.paddyfarm.data.models.LoginRequest;
import com.dtechsolutions.paddyfarm.data.repositories.AuthRepository;
import com.dtechsolutions.paddyfarm.utils.AlertEvent;
import com.dtechsolutions.paddyfarm.utils.BaseViewModel;
import com.dtechsolutions.paddyfarm.utils.Resource;
import com.dtechsolutions.paddyfarm.utils.TokenProvider.SharedPrefsTokenProvider;

public class LoginViewModel extends BaseViewModel {
    private final AuthRepository authRepository;

    private final MutableLiveData<LoginRequest> loginTrigger = new MutableLiveData<>();

    private final LiveData<Resource<AuthResponse>> loginResponse;

    public LoginViewModel() {
        authRepository = new AuthRepository();

        loginResponse = Transformations.switchMap(loginTrigger, authRepository::login);
    }

    public void login(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);
        login(request);
    }

    public void login(LoginRequest request) {
        loginTrigger.postValue(request);
    }

    public LiveData<Resource<AuthResponse>> getLoginResponse() {
        return loginResponse;
    }
}
