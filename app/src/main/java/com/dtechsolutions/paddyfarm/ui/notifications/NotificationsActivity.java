package com.dtechsolutions.paddyfarm.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dtechsolutions.paddyfarm.R;
import com.dtechsolutions.paddyfarm.adapters.NotificationsAdapter;
import com.dtechsolutions.paddyfarm.data.models.Notification;
import com.dtechsolutions.paddyfarm.ui.dashboard.DashboardActivity;
import com.dtechsolutions.paddyfarm.utils.AlertEvent;
import com.dtechsolutions.paddyfarm.utils.BaseActivity;
import com.dtechsolutions.paddyfarm.utils.Resource;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends BaseActivity<NotificationsViewModel> {
    private ConstraintLayout containerNotifications;
    private ProgressBar progressBar;

    private NotificationsAdapter notificationsAdapter;

    @Override
    protected Class<NotificationsViewModel> getViewModelClass() {
        return NotificationsViewModel.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notifications);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initialize();
    }

    private void initialize() {
        containerNotifications = findViewById(R.id.containerNotifications);
        progressBar = findViewById(R.id.pbNotifications);

        TextView txtActionBarTitle = findViewById(R.id.txtActionBarTitle);
        txtActionBarTitle.setText(R.string.notifications);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this::handleBackClick);

        populateNotificationsRecycler();
        listenToNotifications();
        markNotificationsAsRead();
    }

    private void handleBackClick(View view) {
        Intent i = new Intent(NotificationsActivity.this, DashboardActivity.class);
        startActivity(i);

        finish();
    }

    private void populateNotificationsRecycler() {
        RecyclerView recyclerNotifications = findViewById(R.id.recyclerNotifications);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerNotifications.setLayoutManager(linearLayoutManager);

        notificationsAdapter = new NotificationsAdapter(new ArrayList<>());
        recyclerNotifications.setAdapter(notificationsAdapter);
    }

    private void listenToNotifications() {
        viewModel.fetchNotifications();
        viewModel.getNotifications().observe(this, this::observeNotifications);
    }

    private void markNotificationsAsRead() {
        viewModel.markAllAsRead();
    }

    private void startLoading() {
        progressBar.setVisibility(View.VISIBLE);
        containerNotifications.setVisibility(View.GONE);
    }

    private void stopLoading() {
        containerNotifications.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void observeNotifications(Resource<List<Notification>> result) {
        switch (result.status) {
            case LOADING:
                startLoading();
                break;

            case SUCCESS:
                notificationsAdapter.setNotifications(result.data);
                stopLoading();
                break;

            case ERROR:
                viewModel.addAlertEvent(new AlertEvent(
                        AlertEvent.Type.ERROR,
                        null,
                        "Failed to fetch notifications."
                ));
                stopLoading();
                break;
        }
    }
}