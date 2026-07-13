package com.dtechsolutions.paddyfarm.ui.recommendations;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;

import com.dtechsolutions.paddyfarm.R;
import com.dtechsolutions.paddyfarm.data.models.Recommendation;
import com.dtechsolutions.paddyfarm.data.models.RecommendationSummary;
import com.dtechsolutions.paddyfarm.ui.dashboard.DashboardActivity;
import com.dtechsolutions.paddyfarm.utils.AlertEvent;
import com.dtechsolutions.paddyfarm.utils.BaseActivity;
import com.dtechsolutions.paddyfarm.utils.Resource;

public class RecommendationsActivity extends BaseActivity<RecommendationsViewModel> {
    TextView txtActionBarTitle, txtWateringRecommendation, txtFertilizerRecommendation,
            txtDiseaseAndPestCheckRecommendation, txtMaintenanceTips, txtNextStagePrediction,
            txtCurrentStage, txtDaysGone;
    ImageButton btnBack;
    ScrollView container;
    ProgressBar progressBar;

    @Override
    protected Class<RecommendationsViewModel> getViewModelClass() {
        return RecommendationsViewModel.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recommendations);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initialize();
        observeRecommendations();
    }

    private void initialize() {
        txtActionBarTitle = findViewById(R.id.txtActionBarTitle);
        txtActionBarTitle.setText(R.string.recommendations);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this::handleBackClick);

        txtWateringRecommendation = findViewById(R.id.txtWateringRecommendation);
        txtFertilizerRecommendation = findViewById(R.id.txtFertilizerRecommendation);
        txtDiseaseAndPestCheckRecommendation = findViewById(R.id.txtDiseaseAndPestCheckRecommendation);
        txtMaintenanceTips = findViewById(R.id.txtMaintenanceTip);
        txtNextStagePrediction = findViewById(R.id.txtNextStagePrediction);
        txtCurrentStage = findViewById(R.id.txtCurrentStage);
        txtDaysGone = findViewById(R.id.txtDaysGone);

        container = findViewById(R.id.recommendationsContainer);

        progressBar = findViewById(R.id.pbRecommendations);
    }

    private void handleBackClick(View view) {
        finish();
    }

    private void startLoading() {
        progressBar.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);
    }

    private void stopLoading() {
        progressBar.setVisibility(View.GONE);
        container.setVisibility(View.VISIBLE);
    }

    private void populateRecommendations(RecommendationSummary recommendationSummary) {
        txtWateringRecommendation.setText(recommendationSummary.getRecommendation().getWateringRecommendation());
        txtFertilizerRecommendation.setText(recommendationSummary.getRecommendation().getFertilizerRecommendation());
        txtDiseaseAndPestCheckRecommendation.setText(recommendationSummary.getRecommendation().getDiseaseAndPestCheck());
        txtMaintenanceTips.setText(recommendationSummary.getRecommendation().getMaintenanceTips());
        txtNextStagePrediction.setText(recommendationSummary.getRecommendation().getNextStagePrediction());

        txtCurrentStage.setText(recommendationSummary.getStage().getTitle());
        txtCurrentStage.setSelected(true);

        String daysGone = getString(R.string.day) + " " + recommendationSummary.getDaysGone();
        txtDaysGone.setText(daysGone);
    }

    private void observeRecommendations() {
        viewModel.fetchRecommendations();
        viewModel.getResult().observe(this, new Observer<Resource<RecommendationSummary>>() {
            @Override
            public void onChanged(Resource<RecommendationSummary> result) {
                switch (result.status) {
                    case LOADING:
                        startLoading();
                        break;

                    case ERROR:
                        viewModel.addAlertEvent(new AlertEvent(
                                AlertEvent.Type.ERROR,
                                null,
                                result.message
                        ));
//                        stopLoading();
                        break;

                    case SUCCESS:
                        populateRecommendations(result.data);
                        stopLoading();
                }
            }
        });
    }
}