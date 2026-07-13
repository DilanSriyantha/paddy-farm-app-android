package com.dtechsolutions.paddyfarm.ui.diseasedetection;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dtechsolutions.paddyfarm.data.models.DiseaseDetectionResult;
import com.dtechsolutions.paddyfarm.data.repositories.DiseaseDetectionResultsRepository;
import com.dtechsolutions.paddyfarm.utils.BaseViewModel;
import com.dtechsolutions.paddyfarm.utils.Resource;

import java.io.File;
import java.util.List;

public class DiseaseDetectionViewModel extends BaseViewModel {
    private final String TAG = "[DiseaseDetectionViewModel]";

    private final DiseaseDetectionResultsRepository diseaseDetectionResultsRepository;
    private final MutableLiveData<Resource<List<DiseaseDetectionResult>>> results;
    private final MutableLiveData<Resource<DiseaseDetectionResult>> latestResult;
    private final MutableLiveData<Resource<DiseaseDetectionResult>> scanResult;

    public DiseaseDetectionViewModel() {
        diseaseDetectionResultsRepository = new DiseaseDetectionResultsRepository();
        results = new MutableLiveData<>();
        latestResult = new MutableLiveData<>();
        scanResult = new MutableLiveData<>();
    }

    public void fetchDiseaseDetectionResults() {
        diseaseDetectionResultsRepository.findAllResults().observeForever(results::postValue);
    }

    public void fetchLatestDiseaseDetectionResult() {
        diseaseDetectionResultsRepository.findLatestResult().observeForever(latestResult::postValue);
    }

    public void predictDisease(File imageFile) {
        cleanScanResult();
        diseaseDetectionResultsRepository.predictDisease(imageFile).observeForever(scanResult::postValue);
    }

    public LiveData<Resource<List<DiseaseDetectionResult>>> getResults() {
        return results;
    }

    public LiveData<Resource<DiseaseDetectionResult>> getLatestResult() {
        return latestResult;
    }

    public LiveData<Resource<DiseaseDetectionResult>> getScanResult() {
        return scanResult;
    }

    public void cleanScanResult() {
        scanResult.postValue(Resource.loading());
    }
}
