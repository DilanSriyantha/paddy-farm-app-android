package com.dtechsolutions.paddyfarm.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.dtechsolutions.paddyfarm.R;
import com.dtechsolutions.paddyfarm.data.models.User;
import com.dtechsolutions.paddyfarm.ui.diseasedetection.DiseaseDetectionActivity;
import com.dtechsolutions.paddyfarm.ui.fertilizerprices.FertilizerPricesActivity;
import com.dtechsolutions.paddyfarm.ui.profile.ProfileActivity;
import com.dtechsolutions.paddyfarm.ui.profile.ProfileViewModel;
import com.dtechsolutions.paddyfarm.ui.recommendations.RecommendationsActivity;
import com.dtechsolutions.paddyfarm.ui.startcultivation.StartCultivationActivity;
import com.dtechsolutions.paddyfarm.ui.chatbot.ChatbotActivity;
import com.dtechsolutions.paddyfarm.utils.Resource;
import com.google.android.material.card.MaterialCardView;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class DashboardActivity extends AppCompatActivity {
    MaterialCardView btnStartCultivation, btnRecommendations, btnAiChatbot,
    btnDiseaseDetection, btnFertilizerPrices, btnProfile;
    TextView txtGreeting, txtUsername;

    private ProfileViewModel profileViewModel;

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
    }

    private void initialize() {
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

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        observeProfile();
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

    private void observeProfile() {
        profileViewModel.fetchProfile();
        profileViewModel
                .getProfile()
                .observe(this, this::onProfileLoaded);
    }

    private void onProfileLoaded(Resource<User> profileResult) {
        switch (profileResult.status) {
            case SUCCESS:
                Objects.requireNonNull(txtGreeting).setText(getGreeting());
                Objects.requireNonNull(txtUsername).setText(profileResult.data.getName());
                break;

            case ERROR:
                break;
        }
    }

    private String getGreeting() {
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if(hour >= 4 && hour < 12) {
            return "Good Morning,";
        }else if(hour >= 12 && hour < 17) {
            return "Good Afternoon,";
        }else if(hour >= 17 && hour < 22) {
            return "Good Evening,";
        }else{
            return "Good Night,";
        }
    }
}