package com.dtechsolutions.paddyfarm.ui.startcultivation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;

import com.dtechsolutions.paddyfarm.R;
import com.dtechsolutions.paddyfarm.adapters.PaddyVarietyArrayAdapter;
import com.dtechsolutions.paddyfarm.adapters.SeedTypeArrayAdapter;
import com.dtechsolutions.paddyfarm.data.models.Cultivation;
import com.dtechsolutions.paddyfarm.data.models.CultivationCreateRequest;
import com.dtechsolutions.paddyfarm.data.models.SeedType;
import com.dtechsolutions.paddyfarm.ui.dashboard.DashboardActivity;
import com.dtechsolutions.paddyfarm.utils.AlertEvent;
import com.dtechsolutions.paddyfarm.utils.BaseActivity;
import com.dtechsolutions.paddyfarm.utils.DateTimeFormatter;
import com.dtechsolutions.paddyfarm.utils.Resource;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class StartCultivationActivity extends BaseActivity<StartCultivationViewModel> {

    TextView txtActionBarTitle;
    ImageButton btnBack;
    TextInputEditText txtCultivationStartDate, txtSizeInAcres;
    MaterialAutoCompleteTextView autoCompleteSeedType, autoCompletePaddyVariety;
    Button btnSaveSession;
    ProgressBar progressBar;

    @Override
    protected Class<StartCultivationViewModel> getViewModelClass() {
        return StartCultivationViewModel.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start_cultivation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initialize();
        observeSessionCreateState();
    }

    private void initialize() {
        txtActionBarTitle = findViewById(R.id.txtActionBarTitle);
        txtActionBarTitle.setText(R.string.start_cultivation);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this::handleBackClick);

        txtSizeInAcres = findViewById(R.id.txtSizeInAcres);

        btnSaveSession = findViewById(R.id.btnSaveSession);
        btnSaveSession.setOnClickListener(this::handleSaveSessionClick);

        progressBar = findViewById(R.id.pbSaveSessionLoading);

        initializeCultivationStartDate();
        initializeSeedTypes();
        initializePaddyVariations();
    }

    private void handleBackClick(View view) {
        Intent i = new Intent(StartCultivationActivity.this, DashboardActivity.class);
        startActivity(i);

        finish();
    }

    private void handleSaveSessionClick(View view) {
        CultivationCreateRequest request = constructRequest();
        if(request == null) return;
        saveSession(request);
    }

    private void initializeSeedTypes() {
        List<SeedType> list = new ArrayList<>();
        list.add(new SeedType(0, getString(R.string.certified_seed_title), getString(R.string.certified_seed_description)));
        list.add(new SeedType(1, getString(R.string.registered_seed_title), getString(R.string.registered_seed_description)));
        list.add(new SeedType(2, getString(R.string.farmer_saved_seed_title), getString(R.string.farmer_saved_seed_description)));
        list.add(new SeedType(3, getString(R.string.traditional_seed_title), getString(R.string.traditional_seed_description)));
        list.add(new SeedType(4, getString(R.string.hybrid_seed_title), getString(R.string.hybrid_seed_description)));

        SeedTypeArrayAdapter adapter = new SeedTypeArrayAdapter(this, list);

        autoCompleteSeedType = findViewById(R.id.autoCompleteSeedType);
        autoCompleteSeedType.setAdapter(adapter);
        autoCompleteSeedType.setText(list.get(0).getTitle(), false);
    }

    private void initializePaddyVariations() {
        List <String> list = Arrays.asList(
                getString(R.string.bg_250),
                getString(R.string.bg_300),
                getString(R.string.bg_304),
                getString(R.string.bg_352),
                getString(R.string.bg_357),
                getString(R.string.bg_358),
                getString(R.string.bg_360),
                getString(R.string.bg_366),
                getString(R.string.bg_379_2),
                getString(R.string.at_362),
                getString(R.string.at_354),
                getString(R.string.bg_359),
                getString(R.string.bg_369),
                getString(R.string.bg_374),
                getString(R.string.suwandel),
                getString(R.string.pachchaperumal),
                getString(R.string.kalu_heenati),
                getString(R.string.madathawalu),
                getString(R.string.kuruluthuda),
                getString(R.string.maa_wee),
                getString(R.string.rathu_heenati)
        );

        PaddyVarietyArrayAdapter adapter = new PaddyVarietyArrayAdapter(this, list);

        this.autoCompletePaddyVariety = findViewById(R.id.autoCompletePaddyVariety);
        autoCompletePaddyVariety.setAdapter(adapter);
        autoCompletePaddyVariety.setText(list.get(0), false);
    }

    private void initializeCultivationStartDate(){
        txtCultivationStartDate = findViewById(R.id.txtCultivationStartDate);
        txtCultivationStartDate.setText(getFormattedStartDate(new Date().getTime()));

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        txtCultivationStartDate.setOnClickListener(v -> {
            if(!datePicker.isAdded()) datePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        });

        datePicker.addOnPositiveButtonClickListener(selection -> {
            String formattedDate = getFormattedStartDate(selection);
            txtCultivationStartDate.setText(formattedDate);
        });
    }

    private String getFormattedStartDate(Long selection) {
        String pattern = "yyyy/MM/dd";
        TimeZone timeZoneUTC = TimeZone.getTimeZone("UTC");
        SimpleDateFormat offsetFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        offsetFormat.setTimeZone(timeZoneUTC);

        return offsetFormat.format(new Date(selection));
    }

    private void clear() {
        txtCultivationStartDate.setText(getFormattedStartDate(new Date().getTime()));

        SeedType seedType = (SeedType) autoCompleteSeedType.getAdapter().getItem(0);
        autoCompleteSeedType.setText(seedType.getTitle());

        txtSizeInAcres.setText("");

        String paddyVariety = (String) autoCompletePaddyVariety.getAdapter().getItem(0);
        autoCompletePaddyVariety.setText(paddyVariety);
    }

    private void startLoading() {
        progressBar.setVisibility(View.VISIBLE);

        btnSaveSession.setEnabled(false);
        txtCultivationStartDate.setEnabled(false);
        autoCompleteSeedType.setEnabled(false);
        txtSizeInAcres.setEnabled(false);
        autoCompletePaddyVariety.setEnabled(false);
    }

    private void stopLoading() {
        progressBar.setVisibility(View.GONE);

        btnSaveSession.setEnabled(true);
        txtCultivationStartDate.setEnabled(true);
        autoCompleteSeedType.setEnabled(true);
        txtSizeInAcres.setEnabled(true);
        autoCompletePaddyVariety.setEnabled(true);
    }

    private CultivationCreateRequest constructRequest() {
        try{
            String startDateStr = Objects.requireNonNull(txtCultivationStartDate.getText()).toString();
            String seedType = autoCompleteSeedType.getText().toString();
            String sizeInAcresStr = Objects.requireNonNull(txtSizeInAcres.getText()).toString();
            String paddyVariety = autoCompletePaddyVariety.getText().toString();

            if(startDateStr.isEmpty()) throw new Exception("Start date is required!");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Date startDate = sdf.parse(startDateStr);

            if(sizeInAcresStr.isEmpty()) throw new Exception("Size in acres is required!");
            float sizeInAcres = Float.parseFloat(sizeInAcresStr);

            return new CultivationCreateRequest(
                    startDate,
                    seedType,
                    sizeInAcres,
                    paddyVariety
            );
        }catch (Exception e) {
            viewModel.addAlertEvent(new AlertEvent(
                    AlertEvent.Type.ERROR,
                    null,
                    e.getMessage()
            ));

            return null;
        }
    }

    private void saveSession(CultivationCreateRequest request) {
        viewModel.create(request);
    }

    private void observeSessionCreateState() {
        viewModel.getCreateResult().observe(this, new Observer<Resource<Cultivation>>() {
            @Override
            public void onChanged(Resource<Cultivation> result) {
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
                        stopLoading();
                        break;

                    case SUCCESS:
                        viewModel.addAlertEvent(new AlertEvent(
                                AlertEvent.Type.SUCCESS,
                                "Success",
                                "Cultivation session created successfully."
                        ));
                        stopLoading();
                        clear();
                        break;
                }
            }
        });
    }
}