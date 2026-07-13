package com.dtechsolutions.paddyfarm.ui.editprofile;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;

import com.dtechsolutions.paddyfarm.R;
import com.dtechsolutions.paddyfarm.adapters.PreferredLanguagesArrayAdapter;
import com.dtechsolutions.paddyfarm.data.models.LanguageItem;
import com.dtechsolutions.paddyfarm.data.models.User;
import com.dtechsolutions.paddyfarm.data.models.UserUpdateRequest;
import com.dtechsolutions.paddyfarm.enums.PreferredLanguage;
import com.dtechsolutions.paddyfarm.utils.AlertEvent;
import com.dtechsolutions.paddyfarm.utils.BaseActivity;
import com.dtechsolutions.paddyfarm.utils.LanguageManager;
import com.dtechsolutions.paddyfarm.utils.Resource;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditProfileActivity extends BaseActivity<EditProfileViewModel> {
    private final String TAG = "[EditProfileActivity]";

    TextView txtAppbarTitle;
    ImageButton btnBack;
    TextInputEditText txtName, txtPhoneNumber;
    MaterialAutoCompleteTextView autoCompletePreferredLanguage;
    Button btnSave;
    ProgressBar pbSave, pbEditProfile;
    ConstraintLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initialize();
        observeProfile();
    }

    @Override
    protected Class<EditProfileViewModel> getViewModelClass() {
        return EditProfileViewModel.class;
    }

    private void initialize() {
        txtAppbarTitle = findViewById(R.id.txtActionBarTitle);
        txtAppbarTitle.setText(getString(R.string.edit_profile));

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this::handleBackClick);

        txtName = findViewById(R.id.txtName);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this::handleSaveClick);

        pbSave = findViewById(R.id.pbSaveLoading);
        pbEditProfile = findViewById(R.id.pbEditProfile);

        container = findViewById(R.id.containerEditProfile);

        populateLanguageDropdown();
    }

    private void handleBackClick(View view) {
        finish();
    }

    private void handleSaveClick(View view) {
        saveChanges();
    }

    private void populateLanguageDropdown() {
        List<LanguageItem> list = new ArrayList<>();
        list.add(new LanguageItem(PreferredLanguage.ENGLISH, "English"));
        list.add(new LanguageItem(PreferredLanguage.SINHALA, "සිංහල (Sinhala)"));

        PreferredLanguagesArrayAdapter adapter = new PreferredLanguagesArrayAdapter(this, list);

        autoCompletePreferredLanguage = findViewById(R.id.autoCompletePreferredLanguage);
        autoCompletePreferredLanguage.setAdapter(adapter);
        autoCompletePreferredLanguage.setText(list.get(0).getCaption(), false);
    }

    private void saveChanges() {
        if(txtName == null ||
            txtPhoneNumber == null ||
                autoCompletePreferredLanguage == null) {
            viewModel.addAlertEvent(new AlertEvent(
                    AlertEvent.Type.ERROR,
                    null,
                    getString(R.string.something_went_wrong)
            ));
            return;
        }

        String name = txtName.getText().toString();
        String phoneNumber = txtPhoneNumber.getText().toString();
        PreferredLanguage language = ((PreferredLanguagesArrayAdapter)autoCompletePreferredLanguage.getAdapter()).getPreferredLanguage(autoCompletePreferredLanguage.getText().toString());

        if(name.isEmpty() ||
            phoneNumber.isEmpty() ||
                language == null) {
            viewModel.addAlertEvent(new AlertEvent(
                    AlertEvent.Type.ERROR,
                    null,
                    getString(R.string.required_information_missing)
            ));
            return;
        }

        UserUpdateRequest request = new UserUpdateRequest(
                name,
                phoneNumber,
                language
        );
        viewModel.updateUser(request);
        observeResult();
    }

    private void observeResult() {
        viewModel.getResult().observe(this, new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> result) {
                if(result.isHandled()) return;

                switch (result.status) {
                    case LOADING:
                        startSaving();
                        break;

                    case SUCCESS:
                        stopSaving();
                        User profile = result.getContentIfNotHandled();
                        switchAppLanguage(profile);
                        viewModel.addAlertEvent(new AlertEvent(
                                AlertEvent.Type.SUCCESS,
                                getString(R.string.success),
                                getString(R.string.profile_update_success)
                        ));
                        break;

                    case ERROR:
                        stopSaving();
                        viewModel.addAlertEvent(new AlertEvent(
                                AlertEvent.Type.ERROR,
                                null,
                                getString(R.string.something_went_wrong)
                        ));
                        break;
                }
            }
        });
    }

    private void observeProfile() {
        viewModel.fetchProfile();
        viewModel.getProfile().observe(this, new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> result) {
                if(result.isHandled()) return;

                switch (result.status) {
                    case LOADING:
                        startLoading();
                        break;

                    case SUCCESS:
                        stopLoading();
                        updateFields(result.getContentIfNotHandled());
                        break;

                    case ERROR:
                        stopLoading();
                        viewModel.addAlertEvent(new AlertEvent(
                                AlertEvent.Type.ERROR,
                                null,
                                getString(R.string.something_went_wrong)
                        ));
                        break;
                }
            }
        });
    }

    private void startSaving() {
        txtName.setEnabled(false);
        txtPhoneNumber.setEnabled(false);
        autoCompletePreferredLanguage.setEnabled(false);
        btnSave.setEnabled(false);
        pbSave.setVisibility(View.VISIBLE);
    }

    private void stopSaving() {
        txtName.setEnabled(true);
        txtPhoneNumber.setEnabled(true);
        autoCompletePreferredLanguage.setEnabled(true);
        btnSave.setEnabled(true);
        pbSave.setVisibility(View.GONE);
    }

    private void startLoading() {
        pbEditProfile.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);
    }

    private void stopLoading() {
        container.setVisibility(View.VISIBLE);
        pbEditProfile.setVisibility(View.GONE);
    }

    private void updateFields(User profile) {
        txtName.setText(profile.getName());
        txtPhoneNumber.setText(profile.getPhoneNumber());

        PreferredLanguagesArrayAdapter adapter = (PreferredLanguagesArrayAdapter) autoCompletePreferredLanguage.getAdapter();
        autoCompletePreferredLanguage.setText(adapter.getLanguageString(profile.getPreferredLanguage()) ,false);
    }

    private void switchAppLanguage(User profile) {
        LanguageManager.setAppLanguage(profile.getPreferredLanguage());
    }
}