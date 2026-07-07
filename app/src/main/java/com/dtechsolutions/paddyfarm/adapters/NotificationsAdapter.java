package com.dtechsolutions.paddyfarm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.dtechsolutions.paddyfarm.R;
import com.dtechsolutions.paddyfarm.data.models.Notification;
import com.dtechsolutions.paddyfarm.data.models.NotificationStatus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = "[NotificationsAdapter]";

    private List<Notification> notifications;

    public NotificationsAdapter(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_layout, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Notification notification = notifications.get(position);

        NotificationViewHolder viewHolder = (NotificationViewHolder) holder;
        if(isRead(notification)) {
            viewHolder.notificationContainer.setBackgroundResource(R.drawable.bg_default);
            viewHolder.imgNotificationUnreadIndicator.setVisibility(View.GONE);
        }else {
            viewHolder.notificationContainer.setBackgroundResource(R.drawable.bg_primary_surface);
            viewHolder.imgNotificationUnreadIndicator.setVisibility(View.VISIBLE);
        }

        viewHolder.txtTitle.setText(notification.getTitle());
        viewHolder.txtContent.setText(notification.getContent());
        viewHolder.txtDateTime.setText(formatDateTime(notification.getCreatedAt()));
    }

    @Override
    public int getItemCount() {
        return notifications != null ? notifications.size() : 0;
    }

    private boolean isRead(Notification notification) {
        return notification.getStatus() == NotificationStatus.READ;
    }

    private String formatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm a");
        return sdf.format(date);
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtContent, txtDateTime;
        ImageView imgNotificationUnreadIndicator;
        ConstraintLayout notificationContainer;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtNotificationTitle);
            txtContent = itemView.findViewById(R.id.txtNotificationContent);
            txtDateTime = itemView.findViewById(R.id.txtNotificationDatetime);
            imgNotificationUnreadIndicator = itemView.findViewById(R.id.imgNotificationUnreadIndicator);
            notificationContainer = itemView.findViewById(R.id.containerNotification);
        }
    }
}
