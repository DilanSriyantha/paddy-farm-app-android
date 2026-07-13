package com.dtechsolutions.paddyfarm.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.dtechsolutions.paddyfarm.MyApplication;
import com.dtechsolutions.paddyfarm.R;
import com.dtechsolutions.paddyfarm.data.models.User;
import com.dtechsolutions.paddyfarm.enums.PreferredLanguage;
import com.dtechsolutions.paddyfarm.ui.cultivationhistory.CultivationHistoryActivity;
import com.dtechsolutions.paddyfarm.ui.dashboard.DashboardActivity;
import com.dtechsolutions.paddyfarm.ui.editprofile.EditProfileActivity;
import com.dtechsolutions.paddyfarm.ui.login.LoginActivity;
import com.dtechsolutions.paddyfarm.utils.AlertEvent;
import com.dtechsolutions.paddyfarm.utils.AlertManager;
import com.dtechsolutions.paddyfarm.utils.BaseActivity;
import com.dtechsolutions.paddyfarm.utils.Resource;
import com.dtechsolutions.paddyfarm.utils.TokenProvider.SharedPrefsTokenProvider;

import java.util.Objects;

public class ProfileActivity extends BaseActivity<ProfileViewModel> {
    TextView txtActionBarTitle;
    ImageButton btnBack;
    TextView txtUserName, txtPhoneNumber, txtEmail, txtPreferredLanguage;
    Button btnLogout;
    ScrollView container;
    ProgressBar progressbar;
    LinearLayout optionEditProfile, optionCultivationHistory;

    @Override
    protected Class<ProfileViewModel> getViewModelClass() {
        return ProfileViewModel.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        observeProfile();
    }

    private void initialize() {
        txtActionBarTitle = findViewById(R.id.txtActionBarTitle);
        txtActionBarTitle.setText(R.string.profile);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this::handleBackClick);

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this::handleLogoutClick);

        txtUserName = findViewById(R.id.txtUsername);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        txtEmail = findViewById(R.id.txtEmail);
        txtPreferredLanguage = findViewById(R.id.txtPreferredLanguage);

        container = findViewById(R.id.profileContainer);
        progressbar = findViewById(R.id.pbProfile);

        optionEditProfile = findViewById(R.id.optionEditProfile);
        optionEditProfile.setOnClickListener(this::handleEditProfileClick);

        optionCultivationHistory = findViewById(R.id.optionCultivationHistory);
        optionCultivationHistory.setOnClickListener(this::handleCultivationHistoryClick);
    }

    private void handleBackClick(View view) {
        finish();
    }

    private void handleEditProfileClick(View view) {
        Intent i = new Intent(ProfileActivity.this, EditProfileActivity.class);
        startActivity(i);
    }

    private void handleCultivationHistoryClick(View view) {
        Intent i = new Intent(ProfileActivity.this, CultivationHistoryActivity.class);
        startActivity(i);
    }

    private void handleLogoutClick(View view) {
        logout();
    }

    private void observeProfile() {
        viewModel.fetchProfile();
        viewModel.getProfileResult().observe(this, this::onProfileLoaded);
    }

    private void updateProfileCard(User profile) {
        Objects.requireNonNull(txtUserName).setText(profile.getName());
        Objects.requireNonNull(txtPhoneNumber).setText(profile.getPhoneNumber());
        Objects.requireNonNull(txtEmail).setText(profile.getEmail());

        String preferredLanguage = profile.getPreferredLanguage() == PreferredLanguage.ENGLISH ? "English" : "සිංහල (Sinhala)";
        Objects.requireNonNull(txtPreferredLanguage).setText(preferredLanguage);
    }

    private void logout() {
        SharedPrefsTokenProvider tokenProvider = (SharedPrefsTokenProvider) MyApplication.getTokenProvider();
        tokenProvider.clearToken();

        goToLogin();
    }

    private void goToLogin() {
        Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        
        finish();
    }

    private void startLoading() {
        progressbar.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);
        btnLogout.setVisibility(View.GONE);
    }

    private void stopLoading() {
        progressbar.setVisibility(View.GONE);
        container.setVisibility(View.VISIBLE);
        btnLogout.setVisibility(View.VISIBLE);
    }

    private void onProfileLoaded(Resource<User> result) {
        switch (result.status) {
            case LOADING:
                startLoading();
                break;

            case SUCCESS:
                updateProfileCard(result.data);
                stopLoading();
                break;

            case ERROR:
                viewModel.addAlertEvent(new AlertEvent(
                        AlertEvent.Type.ERROR,
                        null,
                        "Failed to load your profile.\n" + result.message
                ));
                break;
        }
    }
}