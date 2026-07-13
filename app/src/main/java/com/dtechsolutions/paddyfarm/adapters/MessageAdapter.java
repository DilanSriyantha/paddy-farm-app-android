package com.dtechsolutions.paddyfarm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dtechsolutions.paddyfarm.R;
import com.dtechsolutions.paddyfarm.enums.Sender;
import com.dtechsolutions.paddyfarm.data.models.Message;
import com.dtechsolutions.paddyfarm.utils.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_CHATBOT = 0;
    private static final int VIEW_TYPE_USER = 1;

    private List<Message> list;
    private final DateTimeFormatter datetimeFormatter;

    public MessageAdapter(List<Message> list) {
        this.list = list;
        this.datetimeFormatter = DateTimeFormatter.getInstance();
    }

    public MessageAdapter() {
        this.list = new ArrayList<>();
        this.datetimeFormatter = DateTimeFormatter.getInstance();
    }

    public void setMessages(List<Message> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == VIEW_TYPE_USER){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_element_user, parent, false);
            return new UserMessageViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_element_chatbot, parent, false);
            return new ChatbotMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = list.get(position);

        if(holder instanceof UserMessageViewHolder) {
            UserMessageViewHolder viewHolder = (UserMessageViewHolder)holder;
            viewHolder.txtMessageContent.setText(message.getContent());
            viewHolder.txtDeliveredTime.setText(datetimeFormatter.format(message.getDeliveredTime()));
        }else if(holder instanceof ChatbotMessageViewHolder) {
            ChatbotMessageViewHolder viewHolder = (ChatbotMessageViewHolder) holder;
            viewHolder.txtMessageContent.setText(message.getContent());
            viewHolder.txtDeliveredTime.setText(datetimeFormatter.format(message.getDeliveredTime()));
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = list.get(position);
        if(message.getSender() == Sender.USER) return VIEW_TYPE_USER;
        else return VIEW_TYPE_CHATBOT;
    }

    public static class UserMessageViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessageContent, txtDeliveredTime;

        public UserMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.txtMessageContent = itemView.findViewById(R.id.txtMessageContent);
            this.txtDeliveredTime = itemView.findViewById(R.id.txtDeliveredTime);
        }
    }

    public static class ChatbotMessageViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessageContent, txtDeliveredTime;

        public ChatbotMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.txtMessageContent = itemView.findViewById(R.id.txtMessageContent);
            this.txtDeliveredTime = itemView.findViewById(R.id.txtDeliveredTime);
        }
    }
}
