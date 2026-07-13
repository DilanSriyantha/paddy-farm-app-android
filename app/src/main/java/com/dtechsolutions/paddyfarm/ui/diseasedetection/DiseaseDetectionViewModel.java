package com.dtechsolutions.paddyfarm.ui.diseasedetection;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.dtechsolutions.paddyfarm.data.models.DiseaseDetectionResult;
import com.dtechsolutions.paddyfarm.data.repositories.DiseaseDetectionResultsRepository;
import com.dtechsolutions.paddyfarm.utils.BaseViewModel;
import com.dtechsolutions.paddyfarm.utils.Resource;

import java.io.File;
import java.util.List;

public class DiseaseDetectionViewModel extends BaseViewModel {
    private final String TAG = "[DiseaseDetectionViewModel]";

    private final DiseaseDetectionResultsRepository diseaseDetectionResultsRepository;

    private final MutableLiveData<Boolean> listFetchTrigger = new MutableLiveData<>(false);
    private final MutableLiveData<Integer> latestResultFetchTrigger = new MutableLiveData<>(0);
    private final MutableLiveData<File> scanTrigger = new MutableLiveData<>();

    private final LiveData<Resource<List<DiseaseDetectionResult>>> resultsList;
    private final LiveData<Resource<DiseaseDetectionResult>> latestResult;
    private final LiveData<Resource<DiseaseDetectionResult>> scanResult;

    public DiseaseDetectionViewModel() {
        diseaseDetectionResultsRepository = new DiseaseDetectionResultsRepository();

        resultsList = Transformations.switchMap(listFetchTrigger, shouldFetch -> diseaseDetectionResultsRepository.findAllResults());
        latestResult = Transformations.switchMap(latestResultFetchTrigger, shouldFetch -> diseaseDetectionResultsRepository.findLatestResult());
        scanResult = Transformations.switchMap(scanTrigger, diseaseDetectionResultsRepository::predictDisease);
    }

    public void fetchDiseaseDetectionResults() {
        listFetchTrigger.postValue(true);
    }

    public void fetchLatestDiseaseDetectionResult() {
        Integer current = latestResultFetchTrigger.getValue();
        latestResultFetchTrigger.postValue(current == null ? 1 : current + 1);
    }

    public void predictDisease(File imageFile) {
        scanTrigger.postValue(imageFile);
    }

    public LiveData<Resource<List<DiseaseDetectionResult>>> getResultsList() {
        return resultsList;
    }

    public LiveData<Resource<DiseaseDetectionResult>> getLatestResult() {
        return latestResult;
    }

    public LiveData<Resource<DiseaseDetectionResult>> getScanResult() {
        return scanResult;
    }
}
