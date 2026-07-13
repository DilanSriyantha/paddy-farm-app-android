package com.dtechsolutions.paddyfarm.data.repositories;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dtechsolutions.paddyfarm.MyApplication;
import com.dtechsolutions.paddyfarm.data.api.ApiService;
import com.dtechsolutions.paddyfarm.data.models.DiseaseDetectionResult;
import com.dtechsolutions.paddyfarm.utils.Resource;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class DiseaseDetectionResultsRepository {
    private final String TAG = "[DiseaseDetectionResultsRepository]";

    private final ApiService apiService;

    public DiseaseDetectionResultsRepository() {
        apiService = MyApplication.getApiService();
    }

    public LiveData<Resource<DiseaseDetectionResult>> findLatestResult() {
        MutableLiveData<Resource<DiseaseDetectionResult>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        apiService.findLatestDiseaseDetectionResult().enqueue(new Callback<DiseaseDetectionResult>() {
            @Override
            public void onResponse(Call<DiseaseDetectionResult> call, Response<DiseaseDetectionResult> response) {
                if(!response.isSuccessful()) {
                    result.postValue(Resource.error("Failed to fetch disease detection results"));
                    return;
                }

                result.postValue(Resource.success(response.body()));
            }

            @Override
            public void onFailure(Call<DiseaseDetectionResult> call, Throwable throwable) {
                result.postValue(Resource.error(throwable.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Resource<List<DiseaseDetectionResult>>> findAllResults() {
        MutableLiveData<Resource<List<DiseaseDetectionResult>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        apiService.findAllDiseaseDetectionResults().enqueue(new Callback<List<DiseaseDetectionResult>>() {
            @Override
            public void onResponse(Call<List<DiseaseDetectionResult>> call, Response<List<DiseaseDetectionResult>> response) {
                if(!response.isSuccessful()) {
                    result.postValue(Resource.error("Failed to fetch disease detection results"));
                    return;
                }

                result.postValue(Resource.success(response.body()));
            }

            @Override
            public void onFailure(Call<List<DiseaseDetectionResult>> call, Throwable throwable) {
                result.postValue(Resource.error(throwable.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Resource<DiseaseDetectionResult>> predictDisease(File imageFile) {
        MutableLiveData<Resource<DiseaseDetectionResult>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);

        MultipartBody.Part body = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);

        apiService.predictDisease(body).enqueue(new Callback<DiseaseDetectionResult>() {
            @Override
            public void onResponse(Call<DiseaseDetectionResult> call, Response<DiseaseDetectionResult> response) {
                if(!response.isSuccessful()) {
                    result.postValue(Resource.error("Failed to fetch disease detection results"));
                    return;
                }

                result.postValue(Resource.success(response.body()));
            }

            @Override
            public void onFailure(Call<DiseaseDetectionResult> call, Throwable throwable) {
                result.postValue(Resource.error(throwable.getMessage()));
            }
        });

        return result;
    }
}
