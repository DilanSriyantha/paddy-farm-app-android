package com.dtechsolutions.paddyfarm.ui.startcultivation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dtechsolutions.paddyfarm.R;
import com.dtechsolutions.paddyfarm.ui.dashboard.DashboardActivity;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class StartCultivationActivity extends AppCompatActivity {

    TextView txtActionBarTitle;
    ImageButton btnBack;
    TextInputEditText txtCultivationStartDate, txtSizeInAcres;
    MaterialAutoCompleteTextView autoCompleteSeedType, autoCompletePaddyVariety;
    Button btnSaveSession;

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
    }

    private void initialize() {
        this.txtActionBarTitle = findViewById(R.id.txtActionBarTitle);
        this.txtActionBarTitle.setText(R.string.start_cultivation);

        this.btnBack = findViewById(R.id.btnBack);
        this.btnBack.setOnClickListener(this::handleBackClick);

        this.txtSizeInAcres = findViewById(R.id.txtSizeInAcres);

        this.btnSaveSession = findViewById(R.id.btnSaveSession);
        this.btnSaveSession.setOnClickListener(this::handleSaveSessionClick);

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

    }

    private void initializeSeedTypes() {
        List<String> list = Arrays.asList("Element 01", "Element 02", "Element 03");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                com.google.android.material.R.layout.select_dialog_item_material,
                list
        );

        this.autoCompleteSeedType = findViewById(R.id.autoCompleteSeedType);
        autoCompleteSeedType.setAdapter(adapter);
        autoCompleteSeedType.setText(list.get(0), false);
    }

    private void initializePaddyVariations() {
        List <String> list = Arrays.asList("Element 01", "Element 02", "Element 03");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                com.google.android.material.R.layout.select_dialog_item_material,
                list
        );

        this.autoCompletePaddyVariety = findViewById(R.id.autoCompletePaddyVariety);
        autoCompletePaddyVariety.setAdapter(adapter);
        autoCompletePaddyVariety.setText(list.get(0));
    }

    private void initializeCultivationStartDate(){
        this.txtCultivationStartDate = findViewById(R.id.txtCultivationStartDate);

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        this.txtCultivationStartDate.setOnClickListener(v -> {
            if(!datePicker.isAdded()) datePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        });

        datePicker.addOnPositiveButtonClickListener(selection -> {
            TimeZone timeZoneUTC = TimeZone.getTimeZone("UTC");
            SimpleDateFormat offsetFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
            offsetFormat.setTimeZone(timeZoneUTC);

            String formattedDate = offsetFormat.format(new Date(selection));
            this.txtCultivationStartDate.setText(formattedDate);
        });
    }
}