package com.spark.swarajyabiz.ModelClasses;

public class BoxCpnModel {
    String front,back;

    public BoxCpnModel() {
    }

    public BoxCpnModel(String front, String back) {
        this.front = front;
        this.back = back;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }
}
