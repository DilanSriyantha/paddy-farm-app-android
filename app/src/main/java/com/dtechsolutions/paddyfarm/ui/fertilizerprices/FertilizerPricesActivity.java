package com.dtechsolutions.paddyfarm.ui.fertilizerprices;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dtechsolutions.paddyfarm.R;
import com.dtechsolutions.paddyfarm.adapters.FertilizerPricesAdapter;
import com.dtechsolutions.paddyfarm.data.models.Fertilizer;
import com.dtechsolutions.paddyfarm.ui.dashboard.DashboardActivity;
import com.dtechsolutions.paddyfarm.utils.AlertEvent;
import com.dtechsolutions.paddyfarm.utils.BaseActivity;
import com.dtechsolutions.paddyfarm.utils.Resource;

import java.util.List;

public class FertilizerPricesActivity extends BaseActivity<FertilizerPricesViewModel> {

    TextView txtActionBarTitle, txtUpdatedDatetime, txtSearch;
    ImageButton btnBack, btnRefresh;
    RecyclerView recyclerFertilizerPrices;
    ConstraintLayout container;
    ProgressBar progressBar;

    private FertilizerPricesAdapter fertilizerPricesAdapter;

    @Override
    protected Class<FertilizerPricesViewModel> getViewModelClass() {
        return FertilizerPricesViewModel.class;
    }

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
        observeFertilizers();
    }

    private void initialize() {
        txtActionBarTitle = findViewById(R.id.txtActionBarTitle);
        txtActionBarTitle.setText(R.string.fertilizer_prices);

        txtUpdatedDatetime = findViewById(R.id.txtUpdatedDatetime);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this::handleBackClick);

        btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(this::handleRefreshClick);

        txtSearch = findViewById(R.id.txtSearch);
        txtSearch.addTextChangedListener(handleSearchTextChange());

        container = findViewById(R.id.containerFertilizerPrices);
        progressBar = findViewById(R.id.pbFertilizerPrices);
    }

    private void handleBackClick(View view) {
        finish();
    }

    private void handleRefreshClick(View view) {
        viewModel.fetchFertilizers();
    }

    private TextWatcher handleSearchTextChange() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchByName(s.toString());
            }
        };
    }

    private void searchByName(String query) {
        if(fertilizerPricesAdapter == null) return;

        fertilizerPricesAdapter.getFilter().filter(query);
    }

    private void populateRecycler(List<Fertilizer> list) {
        recyclerFertilizerPrices = findViewById(R.id.recyclerFertilizerPrices);
        recyclerFertilizerPrices.setLayoutManager(new LinearLayoutManager(this));

        fertilizerPricesAdapter = new FertilizerPricesAdapter(this, list);
        recyclerFertilizerPrices.setAdapter(fertilizerPricesAdapter);
    }

    private void startLoading() {
        progressBar.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);
    }

    private void stopLoading() {
        container.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void observeFertilizers() {
        viewModel.fetchFertilizers();
        viewModel.getResult().observe(this, new Observer<Resource<List<Fertilizer>>>() {
            @Override
            public void onChanged(Resource<List<Fertilizer>> result) {
                switch (result.status) {
                    case LOADING:
                        startLoading();
                        break;

                    case SUCCESS:
                        populateRecycler(result.data);
                        stopLoading();
                        break;

                    case ERROR:
                        viewModel.addAlertEvent(new AlertEvent(
                                AlertEvent.Type.ERROR,
                                null,
                                result.message
                        ));
                        stopLoading();
                }
            }
        });
    }
}