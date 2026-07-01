package com.dtechsolutions.paddyfarm.data.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dtechsolutions.paddyfarm.MyApplication;
import com.dtechsolutions.paddyfarm.data.api.ApiService;
import com.dtechsolutions.paddyfarm.data.models.Fertilizer;
import com.dtechsolutions.paddyfarm.utils.Resource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FertilizersRepository {
    private final ApiService apiService;

    public FertilizersRepository() {
        apiService = MyApplication.getApiService();
    }

    public LiveData<Resource<List<Fertilizer>>> findAll() {
        MutableLiveData<Resource<List<Fertilizer>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        apiService.findAllFertilizers().enqueue(new Callback<List<Fertilizer>>() {
            @Override
            public void onResponse(Call<List<Fertilizer>> call, Response<List<Fertilizer>> response) {
                if(!response.isSuccessful()) {
                    result.postValue(Resource.error("Failed to load fertilizers."));
                    return;
                }

                result.postValue(Resource.success(response.body()));
            }

            @Override
            public void onFailure(Call<List<Fertilizer>> call, Throwable throwable) {
                result.postValue(Resource.error(throwable.getMessage()));
            }
        });

        return result;
    }
}
