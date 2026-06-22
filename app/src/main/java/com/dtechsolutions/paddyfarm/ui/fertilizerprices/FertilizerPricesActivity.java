package com.dtechsolutions.paddyfarm.ui.fertilizerprices;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dtechsolutions.paddyfarm.R;
import com.dtechsolutions.paddyfarm.adapters.FertilizerPricesAdapter;
import com.dtechsolutions.paddyfarm.data.models.FertilizerPrice;
import com.dtechsolutions.paddyfarm.ui.dashboard.DashboardActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class FertilizerPricesActivity extends AppCompatActivity {

    TextView txtActionBarTitle, txtUpdatedDatetime;
    TextInputEditText txtSearch;
    ImageButton btnBack;
    RecyclerView recyclerFertilizerPrices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fertilizer_prices);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initialize();
    }

    private void initialize() {
        txtActionBarTitle = findViewById(R.id.txtActionBarTitle);
        txtActionBarTitle.setText(R.string.fertilizer_prices);

        txtUpdatedDatetime = findViewById(R.id.txtUpdatedDatetime);
        txtSearch = findViewById(R.id.txtSearch);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this::handleBackClick);

        populateRecycler();
    }

    private void handleBackClick(View view) {
        Intent i = new Intent(FertilizerPricesActivity.this, DashboardActivity.class);
        startActivity(i);

        finish();
    }

    private void populateRecycler() {
        recyclerFertilizerPrices = findViewById(R.id.recyclerFertilizerPrices);
        recyclerFertilizerPrices.setLayoutManager(new LinearLayoutManager(this));

        List<FertilizerPrice> list = new ArrayList<>();
        list.add(new FertilizerPrice("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS4Z-PV-aGs6LwWx9m5MQiH35XH_QIE6wxpyZolFCyFjsfkJrujAFjkEWkd&s=10", "Fertilizer 01", 1500));
        list.add(new FertilizerPrice("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS4Z-PV-aGs6LwWx9m5MQiH35XH_QIE6wxpyZolFCyFjsfkJrujAFjkEWkd&s=10", "Fertilizer 02", 1200));
        list.add(new FertilizerPrice("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS4Z-PV-aGs6LwWx9m5MQiH35XH_QIE6wxpyZolFCyFjsfkJrujAFjkEWkd&s=10", "Fertilizer 03", 1300));

        FertilizerPricesAdapter adapter = new FertilizerPricesAdapter(this, list);
        recyclerFertilizerPrices.setAdapter(adapter);
    }
}