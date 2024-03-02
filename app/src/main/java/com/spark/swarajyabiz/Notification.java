package com.spark.swarajyabiz;

public class Notification {

    String message;
    String contactNumber;
    String shopNumber;
    String order;
    String key;
    String comm;
    String JobKey;

    public Notification(){

    }

    public Notification(String message, String contactNumber, String order, String key, String shopNumber, String comm, String JobKey){
        this.message = message;
        this.contactNumber = contactNumber;
        this.order = order;
        this.key = key;
        this.shopNumber = shopNumber;
        this.comm = comm;
        this.JobKey = JobKey;
    }

    public String getShopNumber() {
        return shopNumber;
    }

    public void setShopNumber(String shopNumber) {
        this.shopNumber = shopNumber;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getComm() {
        return comm;
    }

    public void setComm(String comm) {
        this.comm = comm;
    }

    public String getJobKey() {
        return JobKey;
    }

    public void setJobKey(String jobKey) {
        JobKey = jobKey;
    }
}
