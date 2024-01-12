package com.spark.swarajyabiz;

import java.io.Serializable;

public class Order implements Serializable {
    private String itemName;
    private String firstImageUrl; // You can add more fields as needed
    private String buyerContactNumber;
    private String buyerName;
    private String quantity;
    private String status;
    private String shopImage;
    private String shopOwnerContactNumber;
    private String orderKey;
    private String senderID;
    private String receiverID;
    private Object timestamp; // Firebase ServerValue.TIMESTAMP will be stored here
    private Object datetamp;
    private String totalAmt;

    public Order() {
        // Default constructor required for Firebase
    }

    public Order(String itemName, String firstImageUrl, String buyerContactNumber, String buyerName, String quantity, String status, Object timestamp, Object datetamp, String orderKey,
                 String shopOwnerContactNumber, String shopImage, String senderID, String receiverID, String totalAmt) {
        this.itemName = itemName;
        this.firstImageUrl = firstImageUrl;
        this.buyerContactNumber = buyerContactNumber;
        this.buyerName = buyerName;
        this.quantity = quantity;
        this.status = status;
        this.timestamp = timestamp;
        this.orderKey = orderKey;
        this.datetamp = datetamp;
        this.shopOwnerContactNumber = shopOwnerContactNumber;
        this.shopImage = shopImage;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.totalAmt = totalAmt;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getItemName() {
        return itemName;
    }

    public String getFirstImageUrl() {
        return firstImageUrl;
    }

    public String getBuyerContactNumber() {
        System.out.println("sdkuv " +buyerContactNumber);
        return buyerContactNumber;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getStatus() {
        return status;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public Object getDatetamp() {
        return datetamp;
    }

    public void setDatetamp(Object datetamp) {
        this.datetamp = datetamp;
    }

    public String getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }

    public String getShopOwnerContactNumber() {
        return shopOwnerContactNumber;
    }

    public void setShopOwnerContactNumber(String shopOwnerContactNumber) {
        this.shopOwnerContactNumber = shopOwnerContactNumber;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }
}
