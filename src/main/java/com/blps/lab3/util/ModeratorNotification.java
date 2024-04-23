package com.blps.lab3.util;

public class ModeratorNotification {
    private String receiver;
    private String topic;
    private String messageBody;

    public ModeratorNotification(String receiver, String topic, String messageBody) {
        this.receiver = receiver;
        this.topic = topic;
        this.messageBody = messageBody;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
