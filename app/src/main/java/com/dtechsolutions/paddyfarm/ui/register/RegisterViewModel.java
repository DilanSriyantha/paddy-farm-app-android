package com.dtechsolutions.paddyfarm.ui.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.dtechsolutions.paddyfarm.MyApplication;
import com.dtechsolutions.paddyfarm.data.models.AuthResponse;
import com.dtechsolutions.paddyfarm.data.models.RegisterRequest;
import com.dtechsolutions.paddyfarm.data.repositories.AuthRepository;
import com.dtechsolutions.paddyfarm.enums.PreferredLanguage;
import com.dtechsolutions.paddyfarm.utils.AlertEvent;
import com.dtechsolutions.paddyfarm.utils.BaseViewModel;
import com.dtechsolutions.paddyfarm.utils.Resource;
import com.dtechsolutions.paddyfarm.utils.TokenProvider.SharedPrefsTokenProvider;

public class RegisterViewModel extends BaseViewModel {
    private final AuthRepository authRepository;

    private final MutableLiveData<RegisterRequest> registerTrigger = new MutableLiveData<>();

    private final LiveData<Resource<AuthResponse>> registerResponse;

    public RegisterViewModel() {
        authRepository = new AuthRepository();

        registerResponse = Transformations.switchMap(registerTrigger, authRepository::register);
    }

    public void register(String name, String email, String phone, String password, PreferredLanguage preferredLanguage) {
        RegisterRequest request = new RegisterRequest(name, email, phone, password, preferredLanguage);
        register(request);
    }

    public void register(RegisterRequest request) {
        registerTrigger.postValue(request);
    }

    public LiveData<Resource<AuthResponse>> getRegisterResponse() {
        return registerResponse;
    }
}