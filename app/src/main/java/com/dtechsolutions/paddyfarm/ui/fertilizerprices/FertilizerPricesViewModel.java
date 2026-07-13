package com.dtechsolutions.paddyfarm.ui.fertilizerprices;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.dtechsolutions.paddyfarm.data.models.Fertilizer;
import com.dtechsolutions.paddyfarm.data.repositories.FertilizersRepository;
import com.dtechsolutions.paddyfarm.utils.BaseViewModel;
import com.dtechsolutions.paddyfarm.utils.Resource;

import java.util.List;

public class FertilizerPricesViewModel extends BaseViewModel {
    private final FertilizersRepository fertilizersRepository;

    private final MutableLiveData<Integer> fetchTrigger = new MutableLiveData<>(0);

    private final LiveData<Resource<List<Fertilizer>>> result;

    public FertilizerPricesViewModel() {
        fertilizersRepository = new FertilizersRepository();
        result = Transformations.switchMap(fetchTrigger, onChange -> fertilizersRepository.findAll());
    }

    public void fetchFertilizers() {
        Integer current = fetchTrigger.getValue();
        fetchTrigger.postValue(current == null ? 1 : current + 1);
    }

    public LiveData<Resource<List<Fertilizer>>> getResult() {
        return result;
    }
}
