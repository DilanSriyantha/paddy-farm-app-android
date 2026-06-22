package com.dtechsolutions.paddyfarm.ui.chatbot;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dtechsolutions.paddyfarm.data.models.Message;
import com.dtechsolutions.paddyfarm.enums.Sender;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatbotViewModel extends ViewModel {
    private final MutableLiveData<List<Message>> messages;

    public ChatbotViewModel() {
        List<Message> list = new ArrayList<>();
        list.add(new Message(0, "Hi, I am here to help!", new Date(), Sender.CHATBOT));

        this.messages = new MutableLiveData<>(list);
    }

    public LiveData<List<Message>> getMessages() {
        return messages;
    }

    public void sendMessage(String content) {
        Message message = new Message(messages.getValue().size() + 1, content, new Date(), Sender.USER);

        List<Message> list = new ArrayList<>(messages.getValue());
        list.add(message);
        messages.setValue(list);
    }
}
