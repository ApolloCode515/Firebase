package com.spark.swarajyabiz;

public class orders {

    private String buyerName;
    private String buyerContactNumber;
    private String itemName;
    private Object datetamp;
    private Object timestamp;
    private String quantity;
    private String key; // Unique key associated with the order
    private String status;
    private String shopImage;
    private String shopOwnerContactNumber;
    private String firstImageUrl; // You can add more fields as needed
    private String senderID;
    private String receiverID;

    public orders() {
        // Default constructor required for Firebase
    }



    public orders(String itemName, String buyerName,String buyerContactNumber, String key, Object datetamp,
                  Object timestamp, String quantity, String shopImage, String shopOwnerContactNumber, String firstImageUrl,
                  String senderID, String receiverID, String status) {
        this.key = key;
        this.itemName = itemName;
        this.buyerContactNumber = buyerContactNumber;
        this.buyerName = buyerName;
        this.datetamp = datetamp;
        this.timestamp = timestamp;
        this.quantity =quantity;
        this.shopImage = shopImage;
        this.shopOwnerContactNumber =shopOwnerContactNumber;
        this.firstImageUrl = firstImageUrl;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.status = status;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerContactNumber() {
        return buyerContactNumber;
    }

    public void setBuyerContactNumber(String buyerContactNumber) {
        this.buyerContactNumber = buyerContactNumber;
    }

    public String getItemName() {
        System.out.println("efre" +key);
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getDatetamp() {

        return datetamp;
    }

    public void setDatetamp(Object datetamp) {
        this.datetamp = datetamp;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public String getShopOwnerContactNumber() {
        return shopOwnerContactNumber;
    }

    public void setShopOwnerContactNumber(String shopOwnerContactNumber) {
        this.shopOwnerContactNumber = shopOwnerContactNumber;
    }

    public String getFirstImageUrl() {
        return firstImageUrl;
    }

    public void setFirstImageUrl(String firstImageUrl) {
        this.firstImageUrl = firstImageUrl;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
