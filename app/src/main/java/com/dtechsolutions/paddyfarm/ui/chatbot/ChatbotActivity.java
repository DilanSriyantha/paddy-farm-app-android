package com.dtechsolutions.paddyfarm.ui.chatbot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dtechsolutions.paddyfarm.R;
import com.dtechsolutions.paddyfarm.adapters.MessageAdapter;
import com.dtechsolutions.paddyfarm.data.models.ChatResponse;
import com.dtechsolutions.paddyfarm.data.models.Message;
import com.dtechsolutions.paddyfarm.enums.Sender;
import com.dtechsolutions.paddyfarm.ui.dashboard.DashboardActivity;
import com.dtechsolutions.paddyfarm.utils.AlertEvent;
import com.dtechsolutions.paddyfarm.utils.BaseActivity;
import com.dtechsolutions.paddyfarm.utils.Resource;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ChatbotActivity extends BaseActivity<ChatbotViewModel> {
    private final String TAG = "[ChatbotActivity]";

    TextView txtActionBarTitle;
    TextInputEditText txtMessage;
    ImageButton btnBack, btnSend;
    RecyclerView recyclerMessages;
    ProgressBar pbChatbot;

    MessageAdapter messageAdapter;

    @Override
    protected Class<ChatbotViewModel> getViewModelClass() {
        return ChatbotViewModel.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chatbot);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets imeBars = insets.getInsets(WindowInsetsCompat.Type.ime());

            int bottomPadding = (imeBars.bottom > 0) ? imeBars.bottom : systemBars.bottom;

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding);

            return insets;
        });

        initialize();
    }

    private void initialize() {
        this.txtActionBarTitle = findViewById(R.id.txtActionBarTitle);
        txtActionBarTitle.setText(R.string.ai_chatbot);

        txtMessage = findViewById(R.id.txtMessage);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this::handleBackClick);

        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this::handleSendClick);

        pbChatbot = findViewById(R.id.pbChatbot);

        populateRecycler();
        observeChatHistory();
        observeReply();
    }

    private void populateRecycler() {
        recyclerMessages = findViewById(R.id.recyclerMessageList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        recyclerMessages.setLayoutManager(layoutManager);
        messageAdapter = new MessageAdapter();
        recyclerMessages.setAdapter(messageAdapter);
    }

    private void handleBackClick(View view) {
        finish();
    }

    private void handleSendClick(View view) {
        String message = txtMessage.getText().toString();
        if(message.isEmpty()) return;


        viewModel.sendMessage(message);
        txtMessage.setText("");

        messageAdapter.addNewMessage(message, Sender.USER);
    }

    private void startLoading() {
        pbChatbot.setVisibility(View.VISIBLE);
        recyclerMessages.setVisibility(View.GONE);
    }

    private void stopLoading() {
        recyclerMessages.setVisibility(View.VISIBLE);
        pbChatbot.setVisibility(View.GONE);
    }

    private void startThinking() {
        messageAdapter.addNewMessage(getString(R.string.thinking), Sender.CHATBOT);
        recyclerMessages.scrollToPosition(messageAdapter.getMessages().size() - 1);
    }

    private void stopThinking() {
        messageAdapter.pop();
    }

    private void updateChat(List<Message> updatedMessages) {
        messageAdapter.setMessages(updatedMessages);
    }

    private void updateChat(ChatResponse chatResponse) {
        int id = (int)(Math.random() * 1000) + 1;
        String content = chatResponse.getReply();
        Message newMessage = new Message(id, content, new Date(), Sender.CHATBOT);

        List<Message> updatedMessages = messageAdapter.getMessages();
        updatedMessages.add(newMessage);
        messageAdapter.setMessages(updatedMessages);
    }

    private void observeChatHistory() {
        viewModel.fetchChatHistory();
        viewModel.getChatHistory().observe(this, new Observer<Resource<List<Message>>>() {
            @Override
            public void onChanged(Resource<List<Message>> result) {
                switch (result.status) {
                    case LOADING:
                        startLoading();
                        break;

                    case SUCCESS:
                        updateChat(result.getContentIfNotHandled());
                        stopLoading();
                        break;

                    case ERROR:
                        viewModel.addAlertEvent(new AlertEvent(
                                AlertEvent.Type.ERROR,
                                null,
                                result.message
                        ));
                        stopLoading();
                        break;
                }
            }
        });
    }

    private void observeReply() {
        viewModel.getReply().observe(this, new Observer<Resource<ChatResponse>>() {
            @Override
            public void onChanged(Resource<ChatResponse> reply) {
                switch (reply.status) {
                    case LOADING:
                        startThinking();
                        break;

                    case SUCCESS:
                        stopThinking();
                        updateChat(reply.getContentIfNotHandled());
                        break;

                    case ERROR:
                        viewModel.addAlertEvent(new AlertEvent(
                                AlertEvent.Type.ERROR,
                                null,
                                reply.message
                        ));
                        stopThinking();
                        break;
                }
            }
        });
    }
}