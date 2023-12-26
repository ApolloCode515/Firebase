package com.spark.swarajyabiz;

public class Count {

    private int promotionCount;
    private int requestcount;
    private int ordercount;
    private int notificationcount;

    public Count() {
        // Default constructor required for Firebase
    }

    public Count(int promotionCount, int requestcount, int ordercount, int notificationcount) {

        this.promotionCount = promotionCount;
        this.ordercount =ordercount;
        this.requestcount = requestcount;
        this.notificationcount = notificationcount;

    }

    public int getPromotionCount(){
        return promotionCount;
    }

    public void setPromotionCount(int promotionCount) {
        this.promotionCount = promotionCount;
    }

    public int getRequestcount() {
        return requestcount;
    }

    public void setRequestcount(int requestcount) {
        this.requestcount = requestcount;
    }

    public int getOrdercount() {
        return ordercount;
    }

    public void setOrdercount(int ordercount) {
        this.ordercount = ordercount;
    }

    public int getNotificationcount() {
        return notificationcount;
    }

    public void setNotificationcount(int notificationcount) {
        this.notificationcount = notificationcount;
    }
}
