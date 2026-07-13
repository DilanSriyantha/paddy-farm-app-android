package com.dtechsolutions.paddyfarm.ui.diseasedetection;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dtechsolutions.paddyfarm.R;
import com.dtechsolutions.paddyfarm.data.models.DiseaseDetectionResult;
import com.dtechsolutions.paddyfarm.ui.dashboard.DashboardActivity;
import com.dtechsolutions.paddyfarm.ui.diseasedetectionresults.DiseaseDetectionResultsActivity;
import com.dtechsolutions.paddyfarm.utils.AlertEvent;
import com.dtechsolutions.paddyfarm.utils.BaseActivity;
import com.dtechsolutions.paddyfarm.utils.ImageLoader;
import com.dtechsolutions.paddyfarm.utils.Resource;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.text.SimpleDateFormat;

public class DiseaseDetectionActivity extends BaseActivity<DiseaseDetectionViewModel> {
    private final String TAG = "[DiseaseDetectionActivity]";

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    TextView txtActionBarTitle, txtDiseaseHistory, txtDiseaseScientificNameHistory, txtRiskLevelHistory, txtDateHistory;
    ImageButton btnBack;
    LinearLayout containerHistoryElement, riskLevelBadgeHistory;
    ConstraintLayout btnPickImage, container;
    ImageView imgPreview, imgPreviewHistory;
    ProgressBar pbHistory, pbScanning;
    Button btnScanNow;
    MaterialCardView containerPreview;
    TextView btnViewAll;

    @Override
    protected Class<DiseaseDetectionViewModel> getViewModelClass() {
        return DiseaseDetectionViewModel.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_disease_detection);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initialize();
        registerForActivityResult();
        fetchLatestResult();
    }

    private void initialize() {
        txtActionBarTitle = findViewById(R.id.txtActionBarTitle);
        txtActionBarTitle.setText(R.string.disease_detection);

        txtDiseaseHistory = findViewById(R.id.txtDiseaseHistoryElement);
        txtDiseaseScientificNameHistory = findViewById(R.id.txtDiseaseScientificNameHistoryElement);
        txtRiskLevelHistory = findViewById(R.id.txtRiskLevel);
        txtDateHistory = findViewById(R.id.txtDateHistoryElement);
        riskLevelBadgeHistory = findViewById(R.id.riskLevelBadge);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this::handleBackClick);

        containerHistoryElement = findViewById(R.id.historyElementContainer);

        btnPickImage = findViewById(R.id.btnPickImage);
        btnPickImage.setOnClickListener(this::handlePickImageClick);

        imgPreview = findViewById(R.id.imgPreview);
        imgPreviewHistory = findViewById(R.id.imgHistoryElementPreview);

        container = findViewById(R.id.containerDiseaseDetection);
        pbHistory = findViewById(R.id.pbHistory);

        btnScanNow = findViewById(R.id.btnScanNow);
        btnScanNow.setOnClickListener(this::handleScanNowClick);

        pbScanning = findViewById(R.id.pbScanning);

        containerPreview = findViewById(R.id.containerPreview);

        btnViewAll = findViewById(R.id.btnViewAll);
        btnViewAll.setOnClickListener(this::handleViewAllClick);
    }

    private void handleBackClick(View view) {
        finish();
    }

    private void handleViewAllClick(View view) {
        Intent i = new Intent(DiseaseDetectionActivity.this, DiseaseDetectionResultsActivity.class);
        startActivity(i);
    }

    private void handlePickImageClick(View view) {
        pickImage();
    }

    private void handleScanNowClick(View view) {
        if(imgPreview == null) {
            viewModel.addAlertEvent(new AlertEvent(
                    AlertEvent.Type.ERROR,
                    null,
                    "Something went wrong!"
            ));
            return;
        }

        Drawable drawable = imgPreview.getDrawable();
        if(drawable == null) {
            viewModel.addAlertEvent(new AlertEvent(
                    AlertEvent.Type.ERROR,
                    null,
                    "Invalid image!"
            ));
            return;
        }

        File imageFile = ImageLoader.getInstance().drawableToFile(getBaseContext(), drawable);
        if(imageFile == null) {
            viewModel.addAlertEvent(new AlertEvent(
                    AlertEvent.Type.ERROR,
                    null,
                    "Failed to convert image"
            ));
            return;
        }

        viewModel.predictDisease(imageFile);
        viewModel.getScanResult().observe(this, this::observeScanResult);
    }

    private void registerForActivityResult() {
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if(uri != null) {
                Log.d(TAG, "Selected URI: " + uri);
                setPreview(uri);
            } else {
                Log.d(TAG, "No media selected");
            }
        });
    }

    private void pickImage() {
        if(pickMedia == null) return;
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    private void setPreview(Uri uri) {
        imgPreview.setImageURI(uri);
        containerPreview.setVisibility(View.VISIBLE);
    }

    private void hidePreview() {
        containerPreview.setVisibility(View.GONE);
        imgPreview.setImageURI(null);
    }

    private void fetchLatestResult() {
        viewModel.fetchLatestDiseaseDetectionResult();
        viewModel.getLatestResult().observe(this, this::observeLatestResult);
    }

    private void startHistoryLoading() {
        containerHistoryElement.setVisibility(View.INVISIBLE);
        pbHistory.setVisibility(View.VISIBLE);
    }

    private void stopHistoryLoading(DiseaseDetectionResult result) {
        if(result != null) {
            showHistoryElement(result);
        }else{
            containerHistoryElement.setVisibility(View.GONE);
        }

        pbHistory.setVisibility(View.GONE);
    }

    private void startScanning() {
        pbScanning.setVisibility(View.VISIBLE);
        btnPickImage.setEnabled(false);
        btnScanNow.setEnabled(false);
    }

    private void stopScanning() {
        btnPickImage.setEnabled(true);
        btnScanNow.setEnabled(true);
        pbScanning.setVisibility(View.GONE);
    }

    private void observeLatestResult(Resource<DiseaseDetectionResult> result) {
        switch (result.status) {
            case LOADING:
                startHistoryLoading();
                break;

            case SUCCESS:
                hidePreview();
                stopHistoryLoading(result.data);
                break;

            case ERROR:
                Log.e(TAG, result.message);
                stopHistoryLoading(null);
                break;
        }
    }

    private void observeScanResult(Resource<DiseaseDetectionResult> result) {
        if(result.isHandled()) return;

        switch (result.status) {
            case LOADING:
                startScanning();
                break;

            case SUCCESS:
                DiseaseDetectionResult data = result.getContentIfNotHandled();
                showDiseaseDetectionResult(data);
                stopScanning();
                fetchLatestResult();
                break;

            case ERROR:
                stopScanning();
                break;
        }
    }

    private void showDiseaseDetectionResult(DiseaseDetectionResult result) {
        View view = LayoutInflater.from(this).inflate(R.layout.detection_result_alert_layout, null);

        ImageView imgPreview = view.findViewById(R.id.imgPreviewDetectionResult);
        TextView txtDiseaseName = view.findViewById(R.id.txtDiseaseNameDetectionResult);
        TextView txtDiseaseScientificName = view.findViewById(R.id.txtDiseaseScientificNameDetectionResult);
        TextView txtRiskLevel = view.findViewById(R.id.txtRiskLevelDetectionResult);
        LinearLayout riskLevelBadge = view.findViewById(R.id.riskLevelBadgeDetectionResult);

        ImageLoader.getInstance().loadImage(this, imgPreview, result.getImageUrl());
        txtDiseaseName.setText(result.getDisease());
        txtDiseaseScientificName.setText(result.getDiseaseScientificName());
        updateRiskLevelBadge(txtRiskLevel, riskLevelBadge, result);

        viewModel.addAlertEvent(new AlertEvent(
                AlertEvent.Type.CUSTOM,
                view
        ));
    }

    private void showHistoryElement(DiseaseDetectionResult result) {
        if(result == null) return;

        ImageLoader.getInstance().loadImage(this, imgPreviewHistory, result.getImageUrl());

        txtDiseaseHistory.setText(result.getDisease());
        txtDiseaseScientificNameHistory.setText(result.getDiseaseScientificName());
        updateRiskLevelBadge(txtRiskLevelHistory, riskLevelBadgeHistory, result);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String date = sdf.format(result.getCreatedAt());
        txtDateHistory.setText(date);
        containerHistoryElement.setVisibility(View.VISIBLE);
    }

    private void updateRiskLevelBadge(TextView txtRiskLevel, LinearLayout badge, DiseaseDetectionResult result) {
        switch (result.getRiskLevel()){
            case LOW:
                txtRiskLevel.setText(this.getString(R.string.low_risk));
                txtRiskLevel.setTextColor(this.getColor(R.color.primary));
                badge.setBackgroundResource(R.drawable.badge_2);
                break;

            case MEDIUM:
                txtRiskLevel.setText(this.getString(R.string.medium_risk));
                txtRiskLevel.setTextColor(this.getColor(R.color.warning));
                badge.setBackgroundResource(R.drawable.badge_warning);
                break;

            case HIGH:
                txtRiskLevel.setText(this.getString(R.string.high_risk));
                txtRiskLevel.setTextColor(this.getColor(R.color.error));
                badge.setBackgroundResource(R.drawable.badge_error);
                break;
        }
    }
}