package com.dtechsolutions.paddyfarm.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dtechsolutions.paddyfarm.MyApplication;
import com.dtechsolutions.paddyfarm.data.models.AuthResponse;
import com.dtechsolutions.paddyfarm.data.repositories.AuthRepository;
import com.dtechsolutions.paddyfarm.utils.Resource;
import com.dtechsolutions.paddyfarm.utils.TokenProvider.SharedPrefsTokenProvider;

public class LoginViewModel extends ViewModel {
    private final AuthRepository repository;
    private final MutableLiveData<Resource<AuthResponse>> loginResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoggingIn = new MutableLiveData<>(false);

    public LoginViewModel() {
        repository = new AuthRepository();
    }

    public LiveData<Resource<AuthResponse>> getLoginResult() {
        return loginResult;
    }

    public LiveData<Boolean> getIsLoggingIn() {
        return isLoggingIn;
    }

    public void login(String email, String password) {
        repository
                .login(email, password)
                .observeForever(this::updateLoginResult);
    }

    private void updateLoginResult(Resource<AuthResponse> authResponse) {
        if(authResponse.status == Resource.Status.LOADING) isLoggingIn.setValue(true);

        if(authResponse.status == Resource.Status.SUCCESS){
            SharedPrefsTokenProvider tokenProvider = (SharedPrefsTokenProvider) MyApplication.getTokenProvider();
            tokenProvider.saveToken(authResponse.data.getAccessToken());

            isLoggingIn.setValue(false);
        }

        if(authResponse.status == Resource.Status.ERROR) isLoggingIn.setValue(false);

        loginResult.setValue(authResponse);
    }
}
