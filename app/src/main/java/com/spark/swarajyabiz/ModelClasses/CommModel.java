package com.spark.swarajyabiz.ModelClasses;

public class CommModel {
    String commId;
    String commName;
    String commDesc;
    String commAdmin;
    String commImg;
    String mbrCount;

    public CommModel() {
    }

    public CommModel(String commId, String commName, String commDesc, String commAdmin, String commImg, String mbrCount) {
        this.commId = commId;
        this.commName = commName;
        this.commDesc = commDesc;
        this.commAdmin = commAdmin;
        this.commImg = commImg;
        this.mbrCount = mbrCount;
    }

    public String getCommId() {
        return commId;
    }

    public void setCommId(String commId) {
        this.commId = commId;
    }

    public String getCommName() {
        return commName;
    }

    public void setCommName(String commName) {
        this.commName = commName;
    }

    public String getCommDesc() {
        return commDesc;
    }

    public void setCommDesc(String commDesc) {
        this.commDesc = commDesc;
    }

    public String getCommAdmin() {
        return commAdmin;
    }

    public void setCommAdmin(String commAdmin) {
        this.commAdmin = commAdmin;
    }

    public String getCommImg() {
        return commImg;
    }

    public void setCommImg(String commImg) {
        this.commImg = commImg;
    }

    public String getMbrCount() {
        return mbrCount;
    }

    public void setMbrCount(String mbrCount) {
        this.mbrCount = mbrCount;
    }
}