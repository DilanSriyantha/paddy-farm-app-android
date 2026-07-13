package com.dtechsolutions.paddyfarm.ui.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.dtechsolutions.paddyfarm.R;
import com.dtechsolutions.paddyfarm.data.models.User;
import com.dtechsolutions.paddyfarm.ui.dashboard.DashboardActivity;
import com.dtechsolutions.paddyfarm.ui.getstarted.GetStartedActivity;
import com.dtechsolutions.paddyfarm.ui.login.LoginActivity;
import com.dtechsolutions.paddyfarm.ui.profile.ProfileViewModel;
import com.dtechsolutions.paddyfarm.utils.Resource;

public class SplashActivity extends AppCompatActivity {

    private ProfileViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initialize();
    }

    private void initialize() {
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        observeProfile();
    }

    private void observeProfile() {
        viewModel.fetchProfile();
        viewModel
                .getProfile()
                .observe(this, this::onProfileLoaded);
    }

    private void onProfileLoaded(Resource<User> profile) {
        switch (profile.status) {
            case SUCCESS:
                goToDashboard();
                break;

            case ERROR:
                Toast.makeText(this, "Session expired! Please log in again", Toast.LENGTH_SHORT).show();

                if(isFirstTime()) {
                    goToStart();
                    return;
                }
                goToLogin();
                break;
        }
    }

    private void goToDashboard() {
        Intent i = new Intent(SplashActivity.this, DashboardActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

        finish();
    }

    private void goToLogin() {
        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

        finish();
    }

    private void goToStart() {
        Intent i = new Intent(SplashActivity.this, GetStartedActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

        finish();
    }

    private boolean isFirstTime() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("app_prefs", MODE_PRIVATE);

        boolean firstTime = preferences.getBoolean("is_first_time", true);
        if(firstTime) preferences.edit()
                .putBoolean("is_first_time", false)
                .apply();

        return firstTime;
    }
}