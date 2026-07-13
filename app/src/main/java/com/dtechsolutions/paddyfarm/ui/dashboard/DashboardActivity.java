package com.dtechsolutions.paddyfarm.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.dtechsolutions.paddyfarm.R;
import com.dtechsolutions.paddyfarm.data.models.Notification;
import com.dtechsolutions.paddyfarm.data.models.NotificationStatus;
import com.dtechsolutions.paddyfarm.data.models.RecommendationSummary;
import com.dtechsolutions.paddyfarm.data.models.User;
import com.dtechsolutions.paddyfarm.ui.diseasedetection.DiseaseDetectionActivity;
import com.dtechsolutions.paddyfarm.ui.fertilizerprices.FertilizerPricesActivity;
import com.dtechsolutions.paddyfarm.ui.notifications.NotificationsActivity;
import com.dtechsolutions.paddyfarm.ui.notifications.NotificationsViewModel;
import com.dtechsolutions.paddyfarm.ui.profile.ProfileActivity;
import com.dtechsolutions.paddyfarm.ui.profile.ProfileViewModel;
import com.dtechsolutions.paddyfarm.ui.recommendations.RecommendationsActivity;
import com.dtechsolutions.paddyfarm.ui.startcultivation.StartCultivationActivity;
import com.dtechsolutions.paddyfarm.ui.chatbot.ChatbotActivity;
import com.dtechsolutions.paddyfarm.utils.AlertEvent;
import com.dtechsolutions.paddyfarm.utils.BaseActivity;
import com.dtechsolutions.paddyfarm.utils.Resource;
import com.google.android.material.card.MaterialCardView;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class DashboardActivity extends BaseActivity<DashboardViewModel> {
    private final String TAG = "[DashboardActivity]";

    MaterialCardView btnStartCultivation, btnRecommendations, btnAiChatbot,
    btnDiseaseDetection, btnFertilizerPrices, btnProfile;
    TextView txtGreeting, txtUsername, txtStage, txtDaysGone, txtNotificationCount, txtTipsCardTitle;
    ImageView imgPlant;
    LinearLayout summaryContainer, startNewCultivationSessionContainer;
    ProgressBar pbSummary;
    ImageButton btnNotifications;

    NotificationsViewModel notificationsViewModel;

    @Override
    protected Class<DashboardViewModel> getViewModelClass() {
        return DashboardViewModel.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initialize();
        observeProfile();
        observeSummary();
        observeNotifications();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.fetchSummary();
    }

    @Override
    protected void onStart() {
        super.onStart();
        notificationsViewModel.startNotificationStream();
    }

    @Override
    protected void onStop() {
        super.onStop();
        notificationsViewModel.stopNotificationStream();
    }

    private void initialize() {
        btnNotifications = findViewById(R.id.btnNotifications);
        btnNotifications.setOnClickListener(this::handleNotificationsClick);

        txtNotificationCount = findViewById(R.id.txtNotificationCount);

        btnStartCultivation = findViewById(R.id.btnStartCultivation);
        btnStartCultivation.setOnClickListener(this::handleStartCultivationClick);

        btnRecommendations = findViewById(R.id.btnRecommendations);
        btnRecommendations.setOnClickListener(this::handleRecommendationsClick);

        btnAiChatbot = findViewById(R.id.btnAiChatbot);
        btnAiChatbot.setOnClickListener(this::handleAiChatbotClick);

        btnDiseaseDetection = findViewById(R.id.btnDiseaseDetection);
        btnDiseaseDetection.setOnClickListener(this::handleDiseaseDetectionClick);

        btnFertilizerPrices = findViewById(R.id.btnFertilizerPrices);
        btnFertilizerPrices.setOnClickListener(this::handleFertilizerPricesClick);

        btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(this::handleProfileClick);

        txtGreeting = findViewById(R.id.txtGreeting);
        txtUsername = findViewById(R.id.txtUsername);

        txtStage = findViewById(R.id.txtStage);
        txtDaysGone = findViewById(R.id.txtDaysGone);

        summaryContainer = findViewById(R.id.summaryContainer);
        imgPlant = findViewById(R.id.imgPlant);
        pbSummary = findViewById(R.id.pbSummaryCard);

        txtTipsCardTitle = findViewById(R.id.txtTipsCardTitle);
        txtTipsCardTitle.setSelected(true);

        startNewCultivationSessionContainer = findViewById(R.id.startNewCultivationSessionContainer);
    }

    private void handleNotificationsClick(View view) {
        Intent i = new Intent(DashboardActivity.this, NotificationsActivity.class);
        startActivity(i);
    }

    private void handleStartCultivationClick(View view) {
        Intent i = new Intent(DashboardActivity.this, StartCultivationActivity.class);
        startActivity(i);
    }

    private void handleRecommendationsClick(View view) {
        Intent i = new Intent(DashboardActivity.this, RecommendationsActivity.class);
        startActivity(i);
    }

    private void handleAiChatbotClick(View view) {
        Intent i = new Intent(DashboardActivity.this, ChatbotActivity.class);
        startActivity(i);
    }

    private void handleDiseaseDetectionClick(View view) {
        Intent i = new Intent(DashboardActivity.this, DiseaseDetectionActivity.class);
        startActivity(i);
    }

    private void handleFertilizerPricesClick(View view) {
        Intent i = new Intent(DashboardActivity.this, FertilizerPricesActivity.class);
        startActivity(i);
    }

    private void handleProfileClick(View view) {
        Intent i = new Intent(DashboardActivity.this, ProfileActivity.class);
        startActivity(i);
    }

    private String getGreeting() {
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if(hour >= 4 && hour < 12) {
            return getString(R.string.good_morning);
        }else if(hour >= 12 && hour < 17) {
            return getString(R.string.good_afternoon);
        }else if(hour >= 17 && hour < 22) {
            return getString(R.string.good_evening);
        }else{
            return getString(R.string.good_night);
        }
    }

    private void updateSummaryCard(RecommendationSummary summary) {
        txtStage.setText(summary.getStage().getTitle());
        txtStage.setSelected(true);

        String daysGone = getString(R.string.day) + " " + (summary.getDaysGone());
        txtDaysGone.setText(daysGone);
    }

    private void startSummaryLoading() {
        pbSummary.setVisibility(View.VISIBLE);
        imgPlant.setVisibility(View.INVISIBLE);
        summaryContainer.setVisibility(View.GONE);
        startNewCultivationSessionContainer.setVisibility(View.GONE);
    }

    private void stopSummaryLoading() {
        pbSummary.setVisibility(View.GONE);
        imgPlant.setVisibility(View.VISIBLE);
        summaryContainer.setVisibility(View.VISIBLE);
    }

    private void updateNotificationsBadge(List<Notification> notifications) {
        if(notifications == null) return;

        int count = (int) notifications.stream().filter(notification -> {
            return notification.getStatus().equals(NotificationStatus.NOT_READ);
        }).count();

        if(count == 0) {
            txtNotificationCount.setVisibility(View.GONE);
            return;
        }

        txtNotificationCount.setText(String.valueOf(count));
        txtNotificationCount.setVisibility(View.VISIBLE);
    }

    private void observeProfile() {
        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.fetchProfile();
        profileViewModel.getProfileResult().observe(this, new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> result) {
                if(result.status != Resource.Status.SUCCESS) return;

                Objects.requireNonNull(txtGreeting).setText(getGreeting());
                Objects.requireNonNull(txtUsername).setText(result.data.getName());
            }
        });
    }

    private void showStartNewCultivationSessionBanner() {
        startNewCultivationSessionContainer.setVisibility(View.VISIBLE);

        if(summaryContainer.getVisibility() == View.VISIBLE){
            summaryContainer.setVisibility(View.INVISIBLE);
            imgPlant.setVisibility(View.INVISIBLE);
        }
    }

    private void observeSummary() {
        viewModel.getResult().observe(this, new Observer<Resource<RecommendationSummary>>() {
            @Override
            public void onChanged(Resource<RecommendationSummary> result) {
                switch (result.status) {
                    case LOADING:
                        startSummaryLoading();
                        break;

                    case SUCCESS:
                        updateSummaryCard(result.data);
                        stopSummaryLoading();
                        break;

                    case ERROR:
                        stopSummaryLoading();
                        showStartNewCultivationSessionBanner();
                        break;
                }
            }
        });
    }

    private void observeNotifications() {
        notificationsViewModel = new NotificationsViewModel();
        notificationsViewModel.fetchNotifications();
        notificationsViewModel.getNotifications().observe(this, new Observer<Resource<List<Notification>>>() {
            @Override
            public void onChanged(Resource<List<Notification>> result) {
                if(result.status != Resource.Status.SUCCESS) return;

                updateNotificationsBadge(result.data);
            }
        });
    }
}