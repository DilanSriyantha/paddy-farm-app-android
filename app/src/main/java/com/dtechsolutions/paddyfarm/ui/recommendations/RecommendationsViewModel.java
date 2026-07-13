package com.dtechsolutions.paddyfarm.ui.recommendations;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.dtechsolutions.paddyfarm.data.models.Recommendation;
import com.dtechsolutions.paddyfarm.data.models.RecommendationSummary;
import com.dtechsolutions.paddyfarm.data.repositories.RecommendationsRepository;
import com.dtechsolutions.paddyfarm.utils.BaseViewModel;
import com.dtechsolutions.paddyfarm.utils.Resource;

public class RecommendationsViewModel extends BaseViewModel {
    private final RecommendationsRepository recommendationsRepository;

    private final MutableLiveData<Integer> fetchTrigger = new MutableLiveData<>(0);

    private final LiveData<Resource<RecommendationSummary>> result;

    public RecommendationsViewModel() {
        recommendationsRepository = new RecommendationsRepository();

        result = Transformations.switchMap(fetchTrigger, onChange -> recommendationsRepository.getRecommendationsForCurrentSession());
    }

    public void fetchRecommendations() {
        Integer current = fetchTrigger.getValue();
        fetchTrigger.postValue(current == null ? 1 : current + 1);
    }

    public LiveData<Resource<RecommendationSummary>> getResult() {
        return result;
    }
}
