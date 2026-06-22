package com.dtechsolutions.paddyfarm.ui.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dtechsolutions.paddyfarm.MyApplication;
import com.dtechsolutions.paddyfarm.data.models.AuthResponse;
import com.dtechsolutions.paddyfarm.data.repositories.AuthRepository;
import com.dtechsolutions.paddyfarm.enums.PreferredLanguage;
import com.dtechsolutions.paddyfarm.utils.Resource;
import com.dtechsolutions.paddyfarm.utils.TokenProvider.SharedPrefsTokenProvider;

public class RegisterViewModel extends ViewModel {
    private final AuthRepository repository;
    private final MutableLiveData<Resource<AuthResponse>> registerResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isRegistering = new MutableLiveData<>(false);

    public RegisterViewModel() {
        repository = new AuthRepository();
    }

    public void register(
            String name,
            String email,
            String password,
            PreferredLanguage preferredLanguage
    ) {
        repository
                .register(name, email, password, preferredLanguage)
                .observeForever(this::updateRegisterResult);
    }

    public LiveData<Resource<AuthResponse>> getRegisterResult() {
        return registerResult;
    }

    public LiveData<Boolean> getIsRegistering() {
        return isRegistering;
    }

    private void updateRegisterResult(Resource<AuthResponse> authResponse) {
        if(authResponse.status == Resource.Status.LOADING) isRegistering.setValue(true);

        if(authResponse.status == Resource.Status.SUCCESS) {
            SharedPrefsTokenProvider tokenProvider = (SharedPrefsTokenProvider) MyApplication.getTokenProvider();
            tokenProvider.saveToken(authResponse.data.getAccessToken());

            isRegistering.setValue(false);
        }

        if(authResponse.status == Resource.Status.ERROR) isRegistering.setValue(false);

        registerResult.postValue(authResponse);
    }
}
