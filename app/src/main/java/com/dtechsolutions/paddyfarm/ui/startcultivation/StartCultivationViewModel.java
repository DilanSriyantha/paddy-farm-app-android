package com.dtechsolutions.paddyfarm.ui.startcultivation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.dtechsolutions.paddyfarm.data.models.Cultivation;
import com.dtechsolutions.paddyfarm.data.models.CultivationCreateRequest;
import com.dtechsolutions.paddyfarm.data.repositories.CultivationsRepository;
import com.dtechsolutions.paddyfarm.utils.BaseViewModel;
import com.dtechsolutions.paddyfarm.utils.Resource;

public class StartCultivationViewModel extends BaseViewModel {
    private final String TAG = "[StartCultivationViewModel]";

    private final CultivationsRepository cultivationsRepository;

    private final MutableLiveData<CultivationCreateRequest> createTrigger = new MutableLiveData<>();

    private final LiveData<Resource<Cultivation>> createResult;

    public StartCultivationViewModel() {
        cultivationsRepository = new CultivationsRepository();

        createResult = Transformations.switchMap(createTrigger, cultivationsRepository::create);
    }

    public void create(CultivationCreateRequest request) {
        createTrigger.postValue(request);
    }

    public LiveData<Resource<Cultivation>> getCreateResult() {
        return createResult;
    }
}
