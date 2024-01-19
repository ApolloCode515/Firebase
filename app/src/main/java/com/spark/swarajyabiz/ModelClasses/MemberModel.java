package com.spark.swarajyabiz.ModelClasses;

public class MemberModel {
    String id;
    String mbName;
    String mbImage;
    String mbMob;
    String mbAdd;
    String mbShop;
    String mbLink;

    public MemberModel() {
    }

    public MemberModel(String id, String mbName, String mbImage, String mbMob, String mbAdd, String mbShop, String mbLink) {
        this.id = id;
        this.mbName = mbName;
        this.mbImage = mbImage;
        this.mbMob = mbMob;
        this.mbAdd = mbAdd;
        this.mbShop = mbShop;
        this.mbLink = mbLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMbName() {
        return mbName;
    }

    public void setMbName(String mbName) {
        this.mbName = mbName;
    }

    public String getMbImage() {
        return mbImage;
    }

    public void setMbImage(String mbImage) {
        this.mbImage = mbImage;
    }

    public String getMbMob() {
        return mbMob;
    }

    public void setMbMob(String mbMob) {
        this.mbMob = mbMob;
    }

    public String getMbAdd() {
        return mbAdd;
    }

    public void setMbAdd(String mbAdd) {
        this.mbAdd = mbAdd;
    }

    public String getMbShop() {
        return mbShop;
    }

    public void setMbShop(String mbShop) {
        this.mbShop = mbShop;
    }

    public String getMbLink() {
        return mbLink;
    }

    public void setMbLink(String mbLink) {
        this.mbLink = mbLink;
    }
}
