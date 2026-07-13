package com.dtechsolutions.paddyfarm.ui.cultivationhistory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.dtechsolutions.paddyfarm.data.models.Cultivation;
import com.dtechsolutions.paddyfarm.data.repositories.CultivationsRepository;
import com.dtechsolutions.paddyfarm.utils.BaseViewModel;
import com.dtechsolutions.paddyfarm.utils.Resource;

import java.util.List;

public class CultivationHistoryViewModel extends BaseViewModel {
    private final CultivationsRepository cultivationsRepository;

    private final MutableLiveData<Integer> fetchTrigger = new MutableLiveData<>(0);

    private final LiveData<Resource<List<Cultivation>>> result;

    public CultivationHistoryViewModel() {
        cultivationsRepository = new CultivationsRepository();

        result = Transformations.switchMap(fetchTrigger, onChange -> cultivationsRepository.findAll());
    }

    public void fetchCultivationHistory() {
        Integer current = fetchTrigger.getValue();
        fetchTrigger.postValue(current == null ? 1 : current + 1);
    }

    public LiveData<Resource<List<Cultivation>>> getResult() {
        return result;
    }
}
