package com.dtechsolutions.paddyfarm.ui.notifications;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.dtechsolutions.paddyfarm.MyApplication;
import com.dtechsolutions.paddyfarm.data.api.ApiClient;
import com.dtechsolutions.paddyfarm.data.models.BatchPayload;
import com.dtechsolutions.paddyfarm.data.models.Notification;
import com.dtechsolutions.paddyfarm.data.repositories.NotificationsRepository;
import com.dtechsolutions.paddyfarm.utils.BaseViewModel;
import com.dtechsolutions.paddyfarm.utils.Resource;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

public class NotificationsViewModel extends BaseViewModel {
    private final String TAG = "[NotificationsViewModel - SSE]";

    private final MutableLiveData<Resource<List<Notification>>> notifications;
    private final NotificationsRepository notificationsRepository;
    private EventSource notificationEventSource;

    public NotificationsViewModel() {
        notifications = new MutableLiveData<>();
        notificationsRepository = new NotificationsRepository();
    }

    public void stopNotificationStream() {
        if(notificationEventSource == null) return;
        notificationEventSource.cancel();
        notificationEventSource = null;
    }

    public void fetchNotifications() {
        notificationsRepository.findAll().observeForever(new Observer<Resource<List<Notification>>>() {
            @Override
            public void onChanged(Resource<List<Notification>> result) {
                switch (result.status){
                    case LOADING:
                        notifications.postValue(Resource.loading());
                        break;

                    case SUCCESS:
                        notifications.postValue(Resource.success(result.data));
                        break;

                    case ERROR:
                        notifications.postValue(Resource.error(result.message));
                        break;
                }
            }
        });
    }

    public LiveData<Resource<List<Notification>>> getNotifications() {
        return notifications;
    }

    public void markAllAsRead() {
        notificationsRepository.markAsRead().observeForever(new Observer<Resource<BatchPayload>>() {
            @Override
            public void onChanged(Resource<BatchPayload> result) {
                switch (result.status) {
                    case SUCCESS:
                        Log.i(TAG, "Notifications have been marked as read.");
                        break;

                    case ERROR:
                        Log.e(TAG, "Error occurred while updating notifications. " + result.message);
                        break;
                }
            }
        });
    }

    public void startNotificationStream() {
        fetchNotifications();

        String path = "api/v1/notifications/stream";

        EventSourceListener sseListener = new EventSourceListener() {
            private final Gson gson = new Gson();

            @Override
            public void onOpen(@NonNull EventSource eventSource, @NonNull Response response) {
                Log.d(TAG, "Connected to notification pipeline.");
            }

            @Override
            public void onEvent(@NonNull EventSource eventSource, @Nullable String id, @Nullable String type, @NonNull String data) {
                try{
                    Log.i(TAG, "Payload received: " + data);

                    Notification newNotification = gson.fromJson(data, Notification.class);

                    List<Notification> newNotifications = Objects.requireNonNull(notifications.getValue()).data;
                    newNotifications.add(newNotification);

                    notifications.postValue(Resource.success(newNotifications));
                }catch (JsonSyntaxException e) {
                    Log.e(TAG, "Failed to parse incoming notification JSON structure");
                }
            }

            @Override
            public void onFailure(@NonNull EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
                Log.e(TAG, "Error", t);
            }
        };

        notificationEventSource = ApiClient.createSseConnection(path, MyApplication.getTokenProvider(), sseListener);
    }
}
