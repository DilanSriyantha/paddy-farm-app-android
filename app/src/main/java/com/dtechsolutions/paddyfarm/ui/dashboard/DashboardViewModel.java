package com.dtechsolutions.paddyfarm.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dtechsolutions.paddyfarm.data.models.RecommendationSummary;
import com.dtechsolutions.paddyfarm.data.repositories.RecommendationsRepository;
import com.dtechsolutions.paddyfarm.utils.BaseViewModel;
import com.dtechsolutions.paddyfarm.utils.Resource;

public class DashboardViewModel extends BaseViewModel {
    private final RecommendationsRepository recommendationsRepository;
    private final MutableLiveData<Resource<RecommendationSummary>> result;

    public DashboardViewModel() {
        recommendationsRepository = new RecommendationsRepository();
        result = new MutableLiveData<>();
    }

    public LiveData<Resource<RecommendationSummary>> getResult() {
        return result;
    }

    public void fetchSummary() {
        recommendationsRepository.getRecommendationsForCurrentSession().observeForever(this::onRecommendationResultsChange);
    }

    private void onRecommendationResultsChange(Resource<RecommendationSummary> result) {
        this.result.postValue(result);
    }
}
