package com.dtechsolutions.paddyfarm.data.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dtechsolutions.paddyfarm.MyApplication;
import com.dtechsolutions.paddyfarm.data.api.ApiService;
import com.dtechsolutions.paddyfarm.data.models.RecommendationSummary;
import com.dtechsolutions.paddyfarm.utils.Resource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendationsRepository {
    private final ApiService apiService;

    public RecommendationsRepository() {
        apiService = MyApplication.getApiService();
    }

    public LiveData<Resource<RecommendationSummary>> getRecommendationsForCurrentSession() {
        MutableLiveData<Resource<RecommendationSummary>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        apiService.getRecommendationsForCurrentSession("en").enqueue(new Callback<RecommendationSummary>() {
            @Override
            public void onResponse(Call<RecommendationSummary> call, Response<RecommendationSummary> response) {
                if(!response.isSuccessful()) {
                    result.postValue(Resource.error("Failed to fetch recommendations for the current cultivation session\n" + response.message()));
                    return;
                }

                result.postValue(Resource.success(response.body()));
            }

            @Override
            public void onFailure(Call<RecommendationSummary> call, Throwable throwable) {
                result.postValue(Resource.error(throwable.getMessage()));
            }
        });

        return result;
    }
}
