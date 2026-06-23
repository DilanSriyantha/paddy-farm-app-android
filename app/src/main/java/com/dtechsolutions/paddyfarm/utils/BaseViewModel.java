package com.dtechsolutions.paddyfarm.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BaseViewModel extends ViewModel {
    protected final MutableLiveData<AlertEvent> alertEvent = new MutableLiveData<>();

    public LiveData<AlertEvent> getAlertEvent() {
        return alertEvent;
    }
}
