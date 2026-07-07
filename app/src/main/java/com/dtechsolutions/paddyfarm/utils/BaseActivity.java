package com.dtechsolutions.paddyfarm.utils;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import okhttp3.sse.EventSource;

public abstract class BaseActivity<T extends BaseViewModel> extends AppCompatActivity {
    protected T viewModel;

    protected abstract Class<T> getViewModelClass();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(getViewModelClass());

        observeAlertEvents();
    }

    private void observeAlertEvents() {
        viewModel
                .getAlertEvent()
                .observe(this, this::onAlertEvent);
    }

    private void onAlertEvent(AlertEvent alertEvent) {
        if(alertEvent == null || !alertEvent.getContentIfNotHandled()) return;

        switch (alertEvent.getType()) {
            case ERROR:
                AlertManager.showError(this, alertEvent.getMessage());
                break;

            case SUCCESS:
                AlertManager.showSuccess(
                        this,
                        alertEvent.getTitle(),
                        alertEvent.getMessage(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
        }
    }
}
