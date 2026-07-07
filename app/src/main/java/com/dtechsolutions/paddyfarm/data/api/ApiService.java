package com.dtechsolutions.paddyfarm.data.api;

import com.dtechsolutions.paddyfarm.data.models.AuthResponse;
import com.dtechsolutions.paddyfarm.data.models.BatchPayload;
import com.dtechsolutions.paddyfarm.data.models.Cultivation;
import com.dtechsolutions.paddyfarm.data.models.CultivationCreateRequest;
import com.dtechsolutions.paddyfarm.data.models.CultivationUpdateRequest;
import com.dtechsolutions.paddyfarm.data.models.Fertilizer;
import com.dtechsolutions.paddyfarm.data.models.LoginRequest;
import com.dtechsolutions.paddyfarm.data.models.Notification;
import com.dtechsolutions.paddyfarm.data.models.NotificationUpdateRequest;
import com.dtechsolutions.paddyfarm.data.models.RecommendationSummary;
import com.dtechsolutions.paddyfarm.data.models.RegisterRequest;
import com.dtechsolutions.paddyfarm.data.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
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

    // cultivation routes
    @GET("api/v1/cultivations/findAll")
    Call<List<Cultivation>> findAllCultivations();

    @GET("api/v1/cultivations/findOneById")
    Call<Cultivation> findCultivationById(@Query("id") int id);

    @POST("api/v1/cultivations/create")
    Call<Cultivation> createCultivation(@Body CultivationCreateRequest request);

    @PUT("api/v1/cultivations/update")
    Call<Cultivation> updateCultivation(
            @Query("id") int id,
            @Body CultivationUpdateRequest request
    );

    @DELETE("api/v1/cultivations/delete")
    Call<Cultivation> deleteCultivation(@Query("id") int id);

    // recommendation routes
    @GET("api/v1/recommendations/getRecommendationsForCurrentSession")
    Call<RecommendationSummary> getRecommendationsForCurrentSession(@Query("language") String language);

    // fertilizers routes
    @GET("api/v1/fertilizers/findAll")
    Call<List<Fertilizer>> findAllFertilizers();

    // notifications routes
    @GET("api/v1/notifications/findAll")
    Call<List<Notification>> findAllNotifications();

    @PUT("api/v1/notifications/update")
    Call<Notification> updateNotification(
            @Query("id") int id,
            @Body() NotificationUpdateRequest request
    );

    @PUT("api/v1/notifications/markAsRead")
    Call<BatchPayload> markNotificationsAsRead();
}
