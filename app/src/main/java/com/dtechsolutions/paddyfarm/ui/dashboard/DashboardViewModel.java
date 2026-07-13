package com.dtechsolutions.paddyfarm.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.dtechsolutions.paddyfarm.data.models.RecommendationSummary;
import com.dtechsolutions.paddyfarm.data.repositories.RecommendationsRepository;
import com.dtechsolutions.paddyfarm.utils.BaseViewModel;
import com.dtechsolutions.paddyfarm.utils.Resource;

public class DashboardViewModel extends BaseViewModel {
    private final RecommendationsRepository recommendationsRepository;
    private final MutableLiveData<Boolean> fetchTrigger = new MutableLiveData<>(false);

    private final LiveData<Resource<RecommendationSummary>> result;

    public DashboardViewModel() {
        recommendationsRepository = new RecommendationsRepository();

        result = Transformations.switchMap(fetchTrigger, shouldFetch -> recommendationsRepository.getRecommendationsForCurrentSession());
    }

    public void fetchSummary() {
        fetchTrigger.postValue(true);
    }

    public LiveData<Resource<RecommendationSummary>> getResult() {
        return result;
    }
}
