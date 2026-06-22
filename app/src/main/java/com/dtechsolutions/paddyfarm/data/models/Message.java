package com.dtechsolutions.paddyfarm.data.models;

import com.dtechsolutions.paddyfarm.enums.Sender;

import java.util.Date;

public class Message {
    private final int id;
    private final String content;
    private final Date deliveredTime;
    private final Sender sender;

    public Message(int id, String content, Date deliveredTime, Sender sender) {
        this.id = id;
        this.content = content;
        this.deliveredTime = deliveredTime;
        this.sender = sender;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Date getDeliveredTime() {
        return deliveredTime;
    }

    public Sender getSender() {
        return sender;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", deliveredTime=" + deliveredTime +
                ", sender=" + sender +
                '}';
    }
}
