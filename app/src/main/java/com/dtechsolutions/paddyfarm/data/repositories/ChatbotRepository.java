package com.dtechsolutions.paddyfarm.data.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dtechsolutions.paddyfarm.data.api.ApiService;
import com.dtechsolutions.paddyfarm.data.models.ChatDto;
import com.dtechsolutions.paddyfarm.data.models.ChatResponse;
import com.dtechsolutions.paddyfarm.data.models.Message;
import com.dtechsolutions.paddyfarm.utils.LanguageManager;
import com.dtechsolutions.paddyfarm.utils.Resource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatbotRepository {
    private final ApiService apiService;

    public ChatbotRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<Resource<List<Message>>> getChatHistory() {
        MutableLiveData<Resource<List<Message>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        apiService.getChatHistory().enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if(!response.isSuccessful()) {
                    result.postValue(Resource.error("Failed to fetch chat history from the server."));
                    return;
                }

                result.postValue(Resource.success(response.body()));
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable throwable) {
                result.postValue(Resource.error(throwable.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Resource<ChatResponse>> sendMessage(String message) {
        MutableLiveData<Resource<ChatResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        String language = LanguageManager.getCurrentLanguage();

        apiService.chat(language, new ChatDto(message)).enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                if(!response.isSuccessful()) {
                    result.postValue(Resource.error("Failed to get a reply from the chatbot"));
                    return;
                }

                result.postValue(Resource.success(response.body()));
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable throwable) {
                result.postValue(Resource.error(throwable.getMessage()));
            }
        });

        return result;
    }
}
