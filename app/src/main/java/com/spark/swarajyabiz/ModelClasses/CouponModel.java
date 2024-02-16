package com.spark.swarajyabiz.ModelClasses;

public class CouponModel {
    String cpnId,cpnAmt,cpnFront,cpnBack;

    public CouponModel() {
    }

    public CouponModel(String cpnId, String cpnAmt, String cpnFront, String cpnBack) {
        this.cpnId = cpnId;
        this.cpnAmt = cpnAmt;
        this.cpnFront = cpnFront;
        this.cpnBack = cpnBack;
    }

    public String getCpnId() {
        return cpnId;
    }

    public void setCpnId(String cpnId) {
        this.cpnId = cpnId;
    }

    public String getCpnAmt() {
        return cpnAmt;
    }

    public void setCpnAmt(String cpnAmt) {
        this.cpnAmt = cpnAmt;
    }

    public String getCpnFront() {
        return cpnFront;
    }

    public void setCpnFront(String cpnFront) {
        this.cpnFront = cpnFront;
    }

    public String getCpnBack() {
        return cpnBack;
    }

    public void setCpnBack(String cpnBack) {
        this.cpnBack = cpnBack;
    }
}
