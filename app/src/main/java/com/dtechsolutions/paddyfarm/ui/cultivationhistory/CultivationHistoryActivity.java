package com.dtechsolutions.paddyfarm.ui.cultivationhistory;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dtechsolutions.paddyfarm.R;
import com.dtechsolutions.paddyfarm.adapters.CultivationHistoryAdapter;
import com.dtechsolutions.paddyfarm.data.models.Cultivation;
import com.dtechsolutions.paddyfarm.utils.AlertEvent;
import com.dtechsolutions.paddyfarm.utils.BaseActivity;
import com.dtechsolutions.paddyfarm.utils.Resource;

import java.util.List;

public class CultivationHistoryActivity extends BaseActivity<CultivationHistoryViewModel> {
    TextView txtActionBarTitle;
    ImageButton btnBack;
    RecyclerView recyclerCultivationHistory;
    ProgressBar pbCultivationHistory;

    @Override
    protected Class<CultivationHistoryViewModel> getViewModelClass() {
        return CultivationHistoryViewModel.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cultivation_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initialize();
        observeCultivationHistory();
    }

    private void initialize() {
        txtActionBarTitle = findViewById(R.id.txtActionBarTitle);
        txtActionBarTitle.setText(getString(R.string.cultivation_history));

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this::handleBackPress);

        recyclerCultivationHistory = findViewById(R.id.recyclerCultivationHistory);
        pbCultivationHistory = findViewById(R.id.pbCultivationHistory);
    }

    private void handleBackPress(View view) {
        finish();
    }

    private void populateCultivationHistoryRecycler(List<Cultivation> data) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        CultivationHistoryAdapter adapter = new CultivationHistoryAdapter(this, data);
        recyclerCultivationHistory.setLayoutManager(layoutManager);
        recyclerCultivationHistory.setAdapter(adapter);
    }

    private void observeCultivationHistory() {
        viewModel.fetchCultivationHistory();
        viewModel.getResult().observe(this, new Observer<Resource<List<Cultivation>>>() {

            @Override
            public void onChanged(Resource<List<Cultivation>> result) {
                if(result.isHandled()) return;

                switch (result.status) {
                    case LOADING:
                        startLoading();
                        break;

                    case SUCCESS:
                        populateCultivationHistoryRecycler(result.getContentIfNotHandled());
                        stopLoading();
                        break;

                    case ERROR:
                        viewModel.addAlertEvent(new AlertEvent(
                                AlertEvent.Type.ERROR,
                                null,
                                result.message
                        ));
                        stopLoading();
                        break;
                }
            }
        });
    }

    private void startLoading() {
        pbCultivationHistory.setVisibility(View.VISIBLE);
        recyclerCultivationHistory.setVisibility(View.GONE);
    }

    private void stopLoading() {
        recyclerCultivationHistory.setVisibility(View.VISIBLE);
        pbCultivationHistory.setVisibility(View.GONE);
    }
}