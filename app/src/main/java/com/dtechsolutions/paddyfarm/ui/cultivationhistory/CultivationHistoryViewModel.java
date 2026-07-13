package com.dtechsolutions.paddyfarm.ui.cultivationhistory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dtechsolutions.paddyfarm.data.models.Cultivation;
import com.dtechsolutions.paddyfarm.data.repositories.CultivationsRepository;
import com.dtechsolutions.paddyfarm.utils.BaseViewModel;
import com.dtechsolutions.paddyfarm.utils.Resource;

import java.util.List;

public class CultivationHistoryViewModel extends BaseViewModel {
    private final CultivationsRepository cultivationsRepository;
    private final MutableLiveData<Resource<List<Cultivation>>> result;

    public CultivationHistoryViewModel() {
        cultivationsRepository = new CultivationsRepository();
        result = new MutableLiveData<>();
    }

    public void fetchCultivationHistory() {
        cultivationsRepository.findAll().observeForever(result::postValue);
    }

    public LiveData<Resource<List<Cultivation>>> getResult() {
        return result;
    }
}
