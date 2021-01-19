package com.netanel.talk2me.pojo;

import java.util.Calendar;

public class Message {
    private String input;
    private String fromUser;
    private String toUser;
    private String timeStamp;

    public Message() {
    }

    public Message(String input, String fromUser, String toUser, String timeStamp) {
        this.input = input;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.timeStamp = timeStamp;
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
                ", timeStamp=" + timeStamp +
                '}';
    }
}
