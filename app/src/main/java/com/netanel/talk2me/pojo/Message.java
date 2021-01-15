package com.netanel.talk2me.pojo;

import java.util.Calendar;

public class Message {
    private String input;
    private String fromUser;
    private String toUser;
    private int messageType;
    private String timeStamp;

    public Message() {
    }

    public Message(String message, String fromUser, String toUser) {
        this.input = message;
        this.fromUser = fromUser;
        this.toUser = toUser;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public int getMessageType() {

        return messageType;
    }

    public int setMessageType(int messageType) {
        this.messageType = messageType;
        return messageType;
    }

    public String getTimeStamp() {

        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + input + '\'' +
                ", fromUser='" + fromUser + '\'' +
                ", toUser='" + toUser + '\'' +
                ", messageType=" + messageType +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
