package com.dtechsolutions.paddyfarm.ui.register;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.dtechsolutions.paddyfarm.R;
import com.dtechsolutions.paddyfarm.enums.PreferredLanguage;
import com.dtechsolutions.paddyfarm.ui.dashboard.DashboardActivity;
import com.dtechsolutions.paddyfarm.ui.login.LoginActivity;
import com.dtechsolutions.paddyfarm.utils.BaseActivity;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RegisterActivity extends BaseActivity<RegisterViewModel> {

    TextView lblLoginRedirect;
    TextInputEditText txtName, txtEmail, txtPhoneNumber, txtPassword, txtConfirmPassword;
    MaterialAutoCompleteTextView autoCompletePreferredLanguage;
    Button btnRegister;
    ProgressBar pbRegisterLoading;

    @Override
    protected Class<RegisterViewModel> getViewModelClass() {
        return RegisterViewModel.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initialize();
    }

    private void initialize() {
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);

        pbRegisterLoading = findViewById(R.id.pbRegisterLoading);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this::handleRegisterClick);

        initializeAutoCompletePreferredLanguage();
        initializeLoginRedirect();

        observeAuthResponse();
        observeIsRegistering();
    }

    private void initializeAutoCompletePreferredLanguage() {
        List<String> list = Arrays.asList("English", "සිංහල (Sinhala)");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                com.google.android.material.R.layout.select_dialog_item_material,
                list
        );

        autoCompletePreferredLanguage = findViewById(R.id.autoCompletePreferredLanguage);
        autoCompletePreferredLanguage.setAdapter(adapter);
        autoCompletePreferredLanguage.setText(list.get(0), false);
    }

    private void initializeLoginRedirect() {
        this.lblLoginRedirect = findViewById(R.id.lblLoginRedirect);

        String fullText = "Already have an account? Log in";
        SpannableString spannableString = new SpannableString(fullText);

        String clickablePart = "Log in";
        int startIndex = fullText.indexOf(clickablePart);
        int endIndex = startIndex + clickablePart.length();

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                redirectToLogin();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#297701"));
                ds.setUnderlineText(false);
                ds.setFakeBoldText(true);
            }
        };

        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        lblLoginRedirect.setText(spannableString);
        lblLoginRedirect.setMovementMethod(LinkMovementMethod.getInstance());

        lblLoginRedirect.setHighlightColor(Color.TRANSPARENT);
    }

    private void redirectToLogin() {
        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(i);

        finish();
    }

    private void handleRegisterClick(View view) {
        register();
    }

    private void register() {
        String name = Objects.requireNonNull(txtName.getText()).toString();
        String email = Objects.requireNonNull(txtEmail.getText()).toString();
        String phone = Objects.requireNonNull(txtPhoneNumber.getText()).toString();
        String password1 = Objects.requireNonNull(txtPassword.getText()).toString();
        String password2 = Objects.requireNonNull(txtConfirmPassword.getText()).toString();
        PreferredLanguage preferredLanguage = autoCompletePreferredLanguage.getText().toString().equals("English") ? PreferredLanguage.ENGLISH : PreferredLanguage.SINHALA;

        if(name.isEmpty() ||
        email.isEmpty() ||
        phone.isEmpty() ||
        password1.isEmpty() ||
        password2.isEmpty()) {
            return;
        }

        if(!password1.equals(password2)) {
            Toast.makeText(this, "Passwords are not matching!", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.register(
                name,
                email,
                phone,
                password1,
                preferredLanguage
        );
    }

    private void processSuccessfulRegistration() {
        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
        cleanInputs();
        goToDashboard();
    }

    private void cleanInputs() {
        txtName.setText("");
        txtEmail.setText("");
        txtPhoneNumber.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
    }

    private void goToDashboard() {
        Intent i = new Intent(RegisterActivity.this, DashboardActivity.class);
        startActivity(i);

        finish();
    }

    private void observeAuthResponse() {
        viewModel.getRegisterResult()
                .observe(this, result -> {
                    switch (result.status) {
                        case LOADING:
                            break;

                        case SUCCESS:
                            processSuccessfulRegistration();
                            break;

                        case ERROR:
                            Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show();
                            break;
                    }
                });
    }

    private void observeIsRegistering() {
        viewModel.getIsRegistering()
                .observe(this, this::setLoading);
    }

    private void setLoading(boolean flag) {
        if (flag) {
            txtName.setEnabled(false);
            txtEmail.setEnabled(false);
            txtPhoneNumber.setEnabled(false);
            txtPassword.setEnabled(false);
            txtConfirmPassword.setEnabled(false);
            btnRegister.setEnabled(false);
            btnRegister.setText("");
            pbRegisterLoading.setVisibility(View.VISIBLE);
        }else{
            txtName.setEnabled(true);
            txtEmail.setEnabled(true);
            txtPhoneNumber.setEnabled(true);
            txtPassword.setEnabled(true);
            txtConfirmPassword.setEnabled(true);
            btnRegister.setEnabled(true);
            btnRegister.setText(R.string.register);
            pbRegisterLoading.setVisibility(View.GONE);
        }
    }
}