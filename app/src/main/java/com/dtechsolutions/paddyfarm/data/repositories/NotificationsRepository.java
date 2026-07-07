package com.dtechsolutions.paddyfarm.data.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dtechsolutions.paddyfarm.MyApplication;
import com.dtechsolutions.paddyfarm.data.api.ApiService;
import com.dtechsolutions.paddyfarm.data.models.BatchPayload;
import com.dtechsolutions.paddyfarm.data.models.Notification;
import com.dtechsolutions.paddyfarm.data.models.NotificationUpdateRequest;
import com.dtechsolutions.paddyfarm.utils.Resource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsRepository {
    private final String TAG = "[NotificationsRepository]";

    private final ApiService apiService;

    public NotificationsRepository() {
        apiService = MyApplication.getApiService();
    }

    public LiveData<Resource<List<Notification>>> findAll() {
        MutableLiveData<Resource<List<Notification>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        apiService.findAllNotifications().enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if(!response.isSuccessful()) {
                    result.postValue(Resource.error("Failed to fetch notifications"));
                    return;
                }

                result.postValue(Resource.success(response.body()));
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable throwable) {
                result.postValue(Resource.error(throwable.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Resource<Notification>> update(int id, NotificationUpdateRequest request) {
        MutableLiveData<Resource<Notification>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        apiService.updateNotification(id, request).enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                if(!response.isSuccessful()) {
                    result.postValue(Resource.error("Failed to update notification."));
                    return;
                }

                result.postValue(Resource.success(response.body()));
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable throwable) {
                result.postValue(Resource.error(throwable.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Resource<BatchPayload>> markAsRead() {
        MutableLiveData<Resource<BatchPayload>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        apiService.markNotificationsAsRead().enqueue(new Callback<BatchPayload>() {
            @Override
            public void onResponse(Call<BatchPayload> call, Response<BatchPayload> response) {
                if(!response.isSuccessful()) {
                    result.postValue(Resource.error("Failed to update notifications"));
                    return;
                }

                result.postValue(Resource.success(response.body()));
            }

            @Override
            public void onFailure(Call<BatchPayload> call, Throwable throwable) {
                result.postValue(Resource.error(throwable.getMessage()));
            }
        });

        return  result;
    };
}
