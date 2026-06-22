package com.dtechsolutions.paddyfarm.ui.chatbot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dtechsolutions.paddyfarm.R;
import com.dtechsolutions.paddyfarm.adapters.MessageAdapter;
import com.dtechsolutions.paddyfarm.ui.dashboard.DashboardActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class ChatbotActivity extends AppCompatActivity {

    TextView txtActionBarTitle;
    TextInputEditText txtMessage;
    ImageButton btnBack, btnSend;
    RecyclerView recyclerMessages;

    private ChatbotViewModel chatbotViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chatbot);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        chatbotViewModel = new ViewModelProvider(this).get(ChatbotViewModel.class);

        initialize();
    }

    private void initialize() {
        this.txtActionBarTitle = findViewById(R.id.txtActionBarTitle);
        txtActionBarTitle.setText(R.string.ai_chatbot);

        this.txtMessage = findViewById(R.id.txtMessage);

        this.btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this::handleBackClick);

        this.btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this::handleSendClick);

        populateRecycler();
    }

    private void populateRecycler() {
        recyclerMessages = findViewById(R.id.recyclerMessageList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        recyclerMessages.setLayoutManager(layoutManager);
        MessageAdapter adapter = new MessageAdapter();
        chatbotViewModel.getMessages().observe(this, adapter::setMessages);
        recyclerMessages.setAdapter(adapter);
    }

    private void handleBackClick(View view) {
        Intent i = new Intent(ChatbotActivity.this, DashboardActivity.class);
        startActivity(i);

        finish();
    }

    private void handleSendClick(View view) {
        Log.d("ChatbotActivity", "clicked");
        if(Objects.requireNonNull(txtMessage.getText()).toString().isEmpty()) return;

        chatbotViewModel.sendMessage(txtMessage.getText().toString());
    }
}