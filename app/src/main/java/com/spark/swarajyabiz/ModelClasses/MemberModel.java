package com.spark.swarajyabiz.ModelClasses;

public class MemberModel {
    String id;
    String mbName;
    String mbDistrict;
    String shopImage;
    String ShopName;
    String shopCate;
    String shopAdd;
    String mbLink;

    public MemberModel() {
    }

    public MemberModel(String id, String mbName, String mbDistrict, String shopImage, String shopName, String shopCate, String shopAdd, String mbLink) {
        this.id = id;
        this.mbName = mbName;
        this.mbDistrict = mbDistrict;
        this.shopImage = shopImage;
        this.ShopName = shopName;
        this.shopCate = shopCate;
        this.shopAdd = shopAdd;
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

    public String getMbDistrict() {
        return mbDistrict;
    }

    public void setMbDistrict(String mbDistrict) {
        this.mbDistrict = mbDistrict;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        this.ShopName = shopName;
    }

    public String getShopCate() {
        return shopCate;
    }

    public void setShopCate(String shopCate) {
        this.shopCate = shopCate;
    }

    public String getShopAdd() {
        return shopAdd;
    }

    public void setShopAdd(String shopAdd) {
        this.shopAdd = shopAdd;
    }

    public String getMbLink() {
        return mbLink;
    }

    public void setMbLink(String mbLink) {
        this.mbLink = mbLink;
    }
}
