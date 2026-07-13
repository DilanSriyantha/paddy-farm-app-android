package com.dtechsolutions.paddyfarm.ui.chatbot;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
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
    private final MutableLiveData<List<Message>> messages;
    private final MutableLiveData<Boolean> isChatHistoryLoading = new MutableLiveData<>(false);

    public ChatbotViewModel() {
        List<Message> list = new ArrayList<>();
        list.add(new Message(0, "Hi, I am here to help!", new Date(), Sender.CHATBOT));

        messages = new MutableLiveData<>(list);
        chatbotRepository = new ChatbotRepository(MyApplication.getApiService());

        fetchChatHistory();
    }

    public LiveData<Boolean> isChatHistoryLoading() {
        return isChatHistoryLoading;
    }

    public LiveData<List<Message>> getMessages() {
        return messages;
    }

    public void sendMessage(String content, String textToDisplayWhileLoading) {
        Message message = new Message(messages.getValue().size() + 1, content, new Date(), Sender.USER);

        List<Message> list = new ArrayList<>(messages.getValue());
        list.add(message);
        messages.setValue(list);

        final LiveData<Resource<ChatResponse>> replySource = chatbotRepository.sendMessage(content);
        replySource.observeForever(new Observer<Resource<ChatResponse>>() {
            @Override
            public void onChanged(Resource<ChatResponse> result) {
                List<Message> updatedList = new ArrayList<>(messages.getValue());
                switch (result.status) {
                    case LOADING:
                        updatedList.add(new Message(updatedList.size() + 1, textToDisplayWhileLoading, new Date(), Sender.CHATBOT));
                        messages.postValue(updatedList);
                        break;

                    case SUCCESS:
                        updatedList.remove(updatedList.size() - 1);
                        ChatResponse response = result.getContentIfNotHandled();
                        updatedList.add(new Message(updatedList.size() + 1, response.getReply(), new Date(), Sender.CHATBOT));
                        messages.postValue(updatedList);
                        replySource.removeObserver(this);
                        break;

                    case ERROR:
                        updatedList.remove(updatedList.size() - 1);
                        messages.postValue(updatedList);
                        alertEvent.postValue(new AlertEvent(
                                AlertEvent.Type.ERROR,
                                null,
                                result.message
                        ));
                        replySource.removeObserver(this);
                        break;
                }
            }
        });
    }

    private void fetchChatHistory() {
        final LiveData<Resource<List<Message>>> historySource = chatbotRepository.getChatHistory();
        historySource.observeForever(new Observer<Resource<List<Message>>>() {
            @Override
            public void onChanged(Resource<List<Message>> result) {
                if(result == null) return;

                switch (result.status) {
                    case LOADING:
                        isChatHistoryLoading.postValue(true);
                        break;

                    case SUCCESS:
                        isChatHistoryLoading.postValue(false);

                        List<Message> list = result.getContentIfNotHandled();
                        if(list == null || list.isEmpty()) return;
                        messages.postValue(list);
                        historySource.removeObserver(this);
                        break;

                    case ERROR:
                        isChatHistoryLoading.postValue(false);
                        alertEvent.postValue(new AlertEvent(
                                AlertEvent.Type.ERROR,
                                null,
                                result.message
                        ));
                        historySource.removeObserver(this);
                        break;
                }
            }
        });
    }
}
