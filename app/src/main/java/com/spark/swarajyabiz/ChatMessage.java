package com.spark.swarajyabiz;

public class ChatMessage {
    private String sender;
    private String receiver;
    private String message;
    private String timestamp;
    private String datetamp;
    private boolean isRight;
    private String chatkey;

    // Empty constructor (required for Firebase)
    public ChatMessage() {
    }

    public ChatMessage(String sender, String receiver, String message, String timestamp, String datetamp, String chatkey) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.timestamp = timestamp;
        this.datetamp = datetamp;
        this.chatkey = chatkey;
    }

    public String getChatkey() {
        return chatkey;
    }

    public void setChatkey(String chatkey) {
        this.chatkey = chatkey;
    }

    public String getDatetamp() {
        return datetamp;
    }

    public void setDatetamp(String datetamp) {
        this.datetamp = datetamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean getIsRight() {
        return isRight;
    }

    public void setIsRight(boolean isRight) {
        this.isRight = isRight;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
