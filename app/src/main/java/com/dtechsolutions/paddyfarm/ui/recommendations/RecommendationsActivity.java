package com.dtechsolutions.paddyfarm.ui.recommendations;

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

import com.dtechsolutions.paddyfarm.R;
import com.dtechsolutions.paddyfarm.ui.dashboard.DashboardActivity;

public class RecommendationsActivity extends AppCompatActivity {
    TextView txtActionBarTitle;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recommendations);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initialize();
    }

    private void initialize() {
        this.txtActionBarTitle = findViewById(R.id.txtActionBarTitle);
        txtActionBarTitle.setText(R.string.recommendations);

        this.btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this::handleBackClick);
    }

    private void handleBackClick(View view) {
        Intent i = new Intent(RecommendationsActivity.this, DashboardActivity.class);
        startActivity(i);

        finish();
    }
}