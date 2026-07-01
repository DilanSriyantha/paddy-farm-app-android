package com.dtechsolutions.paddyfarm.ui.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dtechsolutions.paddyfarm.MyApplication;
import com.dtechsolutions.paddyfarm.data.models.AuthResponse;
import com.dtechsolutions.paddyfarm.data.repositories.AuthRepository;
import com.dtechsolutions.paddyfarm.enums.PreferredLanguage;
import com.dtechsolutions.paddyfarm.utils.AlertEvent;
import com.dtechsolutions.paddyfarm.utils.BaseViewModel;
import com.dtechsolutions.paddyfarm.utils.Resource;
import com.dtechsolutions.paddyfarm.utils.TokenProvider.SharedPrefsTokenProvider;

public class RegisterViewModel extends BaseViewModel {
    private final AuthRepository repository;
    private final MutableLiveData<Resource<AuthResponse>> result;

    public RegisterViewModel() {
        repository = new AuthRepository();
        result = new MutableLiveData<>();
    }

    public void register(
            String name,
            String email,
            String phoneNumber,
            String password,
            PreferredLanguage preferredLanguage
    ) {
        repository
                .register(name, email, password, phoneNumber, preferredLanguage)
                .observeForever(this::updateRegisterResult);
    }

    public LiveData<Resource<AuthResponse>> getRegisterResult() {
        return result;
    }

    private void updateRegisterResult(Resource<AuthResponse> authResponse) {
        if(authResponse.status == Resource.Status.SUCCESS) {
            SharedPrefsTokenProvider tokenProvider = (SharedPrefsTokenProvider) MyApplication.getTokenProvider();
            tokenProvider.saveToken(authResponse.data.getAccessToken());
        }

        result.postValue(authResponse);
    }
}
