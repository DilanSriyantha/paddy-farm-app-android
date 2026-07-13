package com.dtechsolutions.paddyfarm.data.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dtechsolutions.paddyfarm.MyApplication;
import com.dtechsolutions.paddyfarm.data.api.ApiService;
import com.dtechsolutions.paddyfarm.data.models.AuthResponse;
import com.dtechsolutions.paddyfarm.data.models.LoginRequest;
import com.dtechsolutions.paddyfarm.data.models.RegisterRequest;
import com.dtechsolutions.paddyfarm.enums.PreferredLanguage;
import com.dtechsolutions.paddyfarm.utils.Resource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private final ApiService apiService;

    public AuthRepository() {
        apiService = MyApplication.getApiService();
    }

    public LiveData<Resource<AuthResponse>> login(LoginRequest request) {
        MutableLiveData<Resource<AuthResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        apiService.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if(!response.isSuccessful()) {
                    result.postValue(Resource.error("Login failed"));
                    return;
                }

                result.postValue(Resource.success(response.body()));
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable throwable) {
                result.postValue(Resource.error(throwable.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Resource<AuthResponse>> register(RegisterRequest request) {
        MutableLiveData<Resource<AuthResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        apiService.register(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if(!response.isSuccessful()) {
                    result.postValue(Resource.error("Registration failed"));
                    return;
                }

                result.postValue(Resource.success(response.body()));
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable throwable) {
                result.postValue(Resource.error(throwable.getMessage()));
            }
        });

        return result;
    }
}
