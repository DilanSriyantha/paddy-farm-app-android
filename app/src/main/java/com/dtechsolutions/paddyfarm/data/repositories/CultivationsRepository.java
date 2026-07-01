package com.dtechsolutions.paddyfarm.data.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dtechsolutions.paddyfarm.MyApplication;
import com.dtechsolutions.paddyfarm.data.api.ApiService;
import com.dtechsolutions.paddyfarm.data.models.Cultivation;
import com.dtechsolutions.paddyfarm.data.models.CultivationCreateRequest;
import com.dtechsolutions.paddyfarm.data.models.CultivationUpdateRequest;
import com.dtechsolutions.paddyfarm.utils.Resource;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CultivationsRepository {
    private final String TAG = "[CultivationsRepository]";

    private final ApiService apiService;

    public CultivationsRepository() {
        apiService = MyApplication.getApiService();
    }

    public LiveData<Resource<List<Cultivation>>> findAll() {
        MutableLiveData<Resource<List<Cultivation>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        apiService.findAllCultivations().enqueue(new Callback<List<Cultivation>>() {
            @Override
            public void onResponse(Call<List<Cultivation>> call, Response<List<Cultivation>> response) {
                if(!response.isSuccessful()) {
                    result.postValue(Resource.error("Failed to fetch cultivation details."));
                    return;
                }

                result.postValue(Resource.success(response.body()));
            }

            @Override
            public void onFailure(Call<List<Cultivation>> call, Throwable throwable) {
                result.postValue(Resource.error(throwable.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Resource<Cultivation>> findOneById(int id) {
        MutableLiveData<Resource<Cultivation>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        apiService.findCultivationById(id).enqueue(new Callback<Cultivation>() {
            @Override
            public void onResponse(Call<Cultivation> call, Response<Cultivation> response) {
                if(!response.isSuccessful()) {
                    result.postValue(Resource.error("Failed to fetch cultivation details."));
                    return;
                }

                result.postValue(Resource.success(response.body()));
            }

            @Override
            public void onFailure(Call<Cultivation> call, Throwable throwable) {
                result.postValue(Resource.error(throwable.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Resource<Cultivation>> create(CultivationCreateRequest request) {
        MutableLiveData<Resource<Cultivation>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        apiService.createCultivation(request).enqueue(new Callback<Cultivation>() {
            @Override
            public void onResponse(Call<Cultivation> call, Response<Cultivation> response) {
                if(!response.isSuccessful()) {
                    if(response.code() == 406) {
                        result.postValue(Resource.error("An active cultivation session is detected. You cannot create another cultivation session until the current session is completed."));
                        return;
                    }

                    result.postValue(Resource.error("Failed to create a cultivation session\n" + response.message()));
                    return;
                }

                result.postValue(Resource.success(response.body()));
            }

            @Override
            public void onFailure(Call<Cultivation> call, Throwable throwable) {
                result.postValue(Resource.error(throwable.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Resource<Cultivation>> update(int id, CultivationUpdateRequest request) {
        MutableLiveData<Resource<Cultivation>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        apiService.updateCultivation(id, request).enqueue(new Callback<Cultivation>() {
            @Override
            public void onResponse(Call<Cultivation> call, Response<Cultivation> response) {
                if(!response.isSuccessful()) {
                    result.postValue(Resource.error("Failed to fetch cultivation details"));
                    return;
                }

                result.postValue(Resource.success(response.body()));
            }

            @Override
            public void onFailure(Call<Cultivation> call, Throwable throwable) {
                result.postValue(Resource.error(throwable.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Resource<Cultivation>> delete(int id) {
        MutableLiveData<Resource<Cultivation>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        apiService.deleteCultivation(id).enqueue(new Callback<Cultivation>() {
            @Override
            public void onResponse(Call<Cultivation> call, Response<Cultivation> response) {
                if(!response.isSuccessful()) {
                    result.postValue(Resource.error("Failed to fetch cultivation details"));
                    return;
                }

                result.postValue(Resource.success(response.body()));
            }

            @Override
            public void onFailure(Call<Cultivation> call, Throwable throwable) {
                result.postValue(Resource.error(throwable.getMessage()));
            }
        });

        return result;
    }
}
