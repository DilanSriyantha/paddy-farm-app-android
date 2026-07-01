package com.dtechsolutions.paddyfarm.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.dtechsolutions.paddyfarm.R;
import com.dtechsolutions.paddyfarm.ui.register.RegisterActivity;
import com.dtechsolutions.paddyfarm.ui.dashboard.DashboardActivity;
import com.dtechsolutions.paddyfarm.utils.AlertEvent;
import com.dtechsolutions.paddyfarm.utils.AlertManager;
import com.dtechsolutions.paddyfarm.utils.BaseActivity;
import com.dtechsolutions.paddyfarm.utils.PreferenceManager;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class LoginActivity extends BaseActivity<LoginViewModel> {

    TextInputEditText txtEmail, txtPassword;
    Button btnLogin, btnRegister;
    ProgressBar pbLoginLoading;

    @Override
    protected Class<LoginViewModel> getViewModelClass() {
        return LoginViewModel.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initialize();
    }

    private void initialize() {
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this::handleLoginClick);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this::handleRegisterClick);

        pbLoginLoading = findViewById(R.id.pbLoginLoading);

        observeAuthResponse();
    }

    private void handleLoginClick(View view) {
        login();
    }

    private void handleRegisterClick(View view) {
        Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(i);
    }

    private void login() {
        String email = Objects.requireNonNull(txtEmail.getText()).toString();
        String password = Objects.requireNonNull(txtPassword.getText()).toString();

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter your email and password to proceed.", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.login(email, password);
    }

    private void goToDashboard(){
        Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(i);

        finish();
    }

    private void startLoading() {
        txtEmail.setEnabled(false);
        txtPassword.setEnabled(false);
        btnLogin.setEnabled(false);
        btnLogin.setText("");
        btnRegister.setEnabled(false);
        pbLoginLoading.setVisibility(View.VISIBLE);
    }

    private void stopLoading() {
        txtEmail.setEnabled(true);
        txtPassword.setEnabled(true);
        btnLogin.setEnabled(true);
        btnLogin.setText(R.string.login);
        btnRegister.setEnabled(true);
        pbLoginLoading.setVisibility(View.GONE);
    }

    private void observeAuthResponse() {
        viewModel.getLoginResult()
                .observe(this, result -> {
                    switch (result.status) {
                        case LOADING:
                            startLoading();
                            break;

                        case SUCCESS:
                            goToDashboard();
                            stopLoading();
                            break;

                        case ERROR:
                            viewModel.addAlertEvent(new AlertEvent(
                                    AlertEvent.Type.ERROR,
                                    null,
                                    "Failed to log into your account.\n" + result.message
                            ));
                            stopLoading();
                            break;
                    }
                });
    }
}