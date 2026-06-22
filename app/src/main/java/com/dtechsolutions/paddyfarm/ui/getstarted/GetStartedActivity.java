package com.dtechsolutions.paddyfarm.ui.getstarted;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dtechsolutions.paddyfarm.R;
import com.dtechsolutions.paddyfarm.ui.login.LoginActivity;

public class GetStartedActivity extends AppCompatActivity {

    Button btnGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_get_started);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initialize();
    }

    private void initialize() {
        this.btnGetStarted = findViewById(R.id.btnGetStarted);

        this.btnGetStarted.setOnClickListener(this::handleGetStartedClick);
    }

    private void handleGetStartedClick(View view) {
        Intent i = new Intent(GetStartedActivity.this, LoginActivity.class);
        startActivity(i);
    }
}