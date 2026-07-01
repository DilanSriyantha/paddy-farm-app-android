package com.dtechsolutions.paddyfarm.ui.recommendations;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dtechsolutions.paddyfarm.data.models.Recommendation;
import com.dtechsolutions.paddyfarm.data.models.RecommendationSummary;
import com.dtechsolutions.paddyfarm.data.repositories.RecommendationsRepository;
import com.dtechsolutions.paddyfarm.utils.BaseViewModel;
import com.dtechsolutions.paddyfarm.utils.Resource;

public class RecommendationsViewModel extends BaseViewModel {
    private final MutableLiveData<Resource<RecommendationSummary>> result;
    private final RecommendationsRepository recommendationsRepository;

    public RecommendationsViewModel() {
        result = new MutableLiveData<>();
        recommendationsRepository = new RecommendationsRepository();
    }

    public void fetchRecommendations() {
        recommendationsRepository.getRecommendationsForCurrentSession().observeForever(this::onRecommendationResultChange);
    }

    public LiveData<Resource<RecommendationSummary>> getResult() {
        return result;
    }

    private void onRecommendationResultChange(Resource<RecommendationSummary> result) {
        this.result.postValue(result);
    }
}
