package com.dtechsolutions.paddyfarm.data.api;

import com.dtechsolutions.paddyfarm.data.models.AuthResponse;
import com.dtechsolutions.paddyfarm.data.models.LoginRequest;
import com.dtechsolutions.paddyfarm.data.models.RegisterRequest;
import com.dtechsolutions.paddyfarm.data.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    // auth routes
    @POST("api/v1/auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @POST("api/v1/auth/register")
    Call<AuthResponse> register(@Body RegisterRequest request);

    // user routes
    @GET("api/v1/users/profile")
    Call<User> getProfile();
}
