package com.move_up.dto;

public class MessageSocketDTO {
    private String token;
    private String receiver;
    private Object content;
    private MessageType type;

    public enum MessageType {
        NOTIFICATION,
        TIME_COUNT,
        EXCEPTION,
        FB_INACTIVE,
        FB_ACTIVE,
        ON_PAGE,
        INCREASE_MINUTE,
        INCREASE_COIN_GIFT_BOX
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
