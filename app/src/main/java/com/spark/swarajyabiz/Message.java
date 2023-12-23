package com.spark.swarajyabiz;

public class Message {
    private String message;
    private String sender;
    private String receiver;
    private String timestamp;
    private String datetamp;

   public Message(){

   }

   public Message(String message, String sender, String receiver, String timestamp, String datetamp){
       this.message = message;
       this.sender = sender;
       this.receiver = receiver;
       this.timestamp = timestamp;
       this.datetamp = datetamp;
   }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDatetamp() {
        return datetamp;
    }

    public void setDatetamp(String datetamp) {
        this.datetamp = datetamp;
    }
}
