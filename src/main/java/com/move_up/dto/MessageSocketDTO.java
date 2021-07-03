package com.move_up.dto;

public class MessageSocketDTO {
    private String token;
    private String receiver;
    private Object content;
    private MessageType type;

    public enum MessageType {
        NOTIFICATION, TIME_COUNT, EXCEPTION
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}
