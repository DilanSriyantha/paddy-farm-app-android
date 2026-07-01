package com.dtechsolutions.paddyfarm.ui.fertilizerprices;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dtechsolutions.paddyfarm.data.models.Fertilizer;
import com.dtechsolutions.paddyfarm.data.repositories.FertilizersRepository;
import com.dtechsolutions.paddyfarm.utils.BaseViewModel;
import com.dtechsolutions.paddyfarm.utils.Resource;

import java.util.List;

public class FertilizerPricesViewModel extends BaseViewModel {
    private final FertilizersRepository fertilizersRepository;
    private final MutableLiveData<Resource<List<Fertilizer>>> result;

    public FertilizerPricesViewModel() {
        fertilizersRepository = new FertilizersRepository();
        result = new MutableLiveData<>();
    }

    public void fetchFertilizers() {
        fertilizersRepository.findAll().observeForever(this::onResultChange);
    }

    public LiveData<Resource<List<Fertilizer>>> getResult() {
        return result;
    }

    private void onResultChange(Resource<List<Fertilizer>> result) {
        this.result.postValue(result);
    }
}
