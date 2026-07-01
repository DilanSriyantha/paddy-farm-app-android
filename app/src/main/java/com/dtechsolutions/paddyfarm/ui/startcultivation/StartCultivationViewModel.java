package com.dtechsolutions.paddyfarm.ui.startcultivation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dtechsolutions.paddyfarm.data.models.Cultivation;
import com.dtechsolutions.paddyfarm.data.models.CultivationCreateRequest;
import com.dtechsolutions.paddyfarm.data.repositories.CultivationsRepository;
import com.dtechsolutions.paddyfarm.utils.AlertEvent;
import com.dtechsolutions.paddyfarm.utils.BaseViewModel;
import com.dtechsolutions.paddyfarm.utils.Resource;

public class StartCultivationViewModel extends BaseViewModel {
    private final CultivationsRepository cultivationsRepository;
    private final MutableLiveData<Resource<Cultivation>> result;

    public StartCultivationViewModel() {
        cultivationsRepository = new CultivationsRepository();
        result = new MutableLiveData<>();
    }

    public LiveData<Resource<Cultivation>> getCreateResult() {
        return result;
    }

    public void create(CultivationCreateRequest request) {
        cultivationsRepository
                .create(request)
                .observeForever(this::onCreateStateChange);
    }

    private void onCreateStateChange(Resource<Cultivation> result) {
        this.result.postValue(result);
    }
}
