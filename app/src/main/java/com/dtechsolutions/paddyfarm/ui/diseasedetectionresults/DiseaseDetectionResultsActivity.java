package com.dtechsolutions.paddyfarm.ui.diseasedetectionresults;

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
import com.dtechsolutions.paddyfarm.adapters.DiseaseDetectionResultsAdapter;
import com.dtechsolutions.paddyfarm.data.models.DiseaseDetectionResult;
import com.dtechsolutions.paddyfarm.ui.diseasedetection.DiseaseDetectionViewModel;
import com.dtechsolutions.paddyfarm.utils.AlertEvent;
import com.dtechsolutions.paddyfarm.utils.BaseActivity;
import com.dtechsolutions.paddyfarm.utils.Resource;

import java.util.List;

public class DiseaseDetectionResultsActivity extends BaseActivity<DiseaseDetectionViewModel> {
    TextView txtAppbarTitle;
    ImageButton btnBack;
    RecyclerView recyclerDiseaseDetectionResults;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_disease_detection_results);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initialize();
        observeDiseaseDetectionResults();
    }

    @Override
    protected Class<DiseaseDetectionViewModel> getViewModelClass() {
        return DiseaseDetectionViewModel.class;
    }

    private void initialize() {
        txtAppbarTitle = findViewById(R.id.txtActionBarTitle);
        txtAppbarTitle.setText(getString(R.string.detection_history));

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this::handleBackPress);

        progressBar = findViewById(R.id.pbDiseaseDetectionResults);
        recyclerDiseaseDetectionResults = findViewById(R.id.recyclerDiseaseDetectionResults);
    }

    private void handleBackPress(View view) {
        finish();
    }

    private void startLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerDiseaseDetectionResults.setVisibility(View.GONE);
    }

    private void stopLoading() {
        progressBar.setVisibility(View.GONE);
        recyclerDiseaseDetectionResults.setVisibility(View.VISIBLE);
    }

    private void populateRecycler(List<DiseaseDetectionResult> list) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerDiseaseDetectionResults.setLayoutManager(layoutManager);

        DiseaseDetectionResultsAdapter adapter = new DiseaseDetectionResultsAdapter(this, list);
        recyclerDiseaseDetectionResults.setAdapter(adapter);
    }

    private void observeDiseaseDetectionResults() {
        viewModel.fetchDiseaseDetectionResults();
        viewModel.getResultsList().observe(this, new Observer<Resource<List<DiseaseDetectionResult>>>() {
            @Override
            public void onChanged(Resource<List<DiseaseDetectionResult>> result) {
                switch (result.status) {
                    case LOADING:
                        startLoading();
                        break;

                    case SUCCESS:
                        populateRecycler(result.data);
                        stopLoading();
                        break;

                    case ERROR:
                        stopLoading();
                        viewModel.addAlertEvent(new AlertEvent(
                                AlertEvent.Type.ERROR,
                                null,
                                result.message
                        ));
                        break;
                }
            }
        });
    }
}