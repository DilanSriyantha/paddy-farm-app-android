package com.dtechsolutions.paddyfarm.ui.chatbot;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.dtechsolutions.paddyfarm.MyApplication;
import com.dtechsolutions.paddyfarm.data.models.ChatResponse;
import com.dtechsolutions.paddyfarm.data.models.Message;
import com.dtechsolutions.paddyfarm.data.repositories.ChatbotRepository;
import com.dtechsolutions.paddyfarm.enums.Sender;
import com.dtechsolutions.paddyfarm.utils.AlertEvent;
import com.dtechsolutions.paddyfarm.utils.BaseViewModel;
import com.dtechsolutions.paddyfarm.utils.Resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatbotViewModel extends BaseViewModel {
    private final ChatbotRepository chatbotRepository;

    private final MutableLiveData<Integer> fetchChatHistoryTrigger = new MutableLiveData<>(0);
    private final MutableLiveData<String> sendMessageTrigger = new MutableLiveData<>();

    private final LiveData<Resource<List<Message>>> chatHistory;
    private final LiveData<Resource<ChatResponse>> reply;

    public ChatbotViewModel() {
        chatbotRepository = new ChatbotRepository();

        chatHistory = Transformations.switchMap(fetchChatHistoryTrigger, onChange -> chatbotRepository.getChatHistory());
        reply = Transformations.switchMap(sendMessageTrigger, chatbotRepository::sendMessage);
    }

    public void fetchChatHistory() {
        Integer current = fetchChatHistoryTrigger.getValue();
        fetchChatHistoryTrigger.postValue(current == null ? 1 : current + 1);
    }

    public LiveData<Resource<List<Message>>> getChatHistory() {
        return chatHistory;
    }

    public void sendMessage(String content) {
        sendMessageTrigger.postValue(content);
    }

    public LiveData<Resource<ChatResponse>> getReply() {
        return reply;
    }
}
