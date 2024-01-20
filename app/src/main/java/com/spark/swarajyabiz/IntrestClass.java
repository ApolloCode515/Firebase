package com.spark.swarajyabiz;

public class IntrestClass {

    String shopImage;

    String shopContactNumber;

    public  IntrestClass(){

    }
    public IntrestClass(String shopImage, String shopContactNumber) {
        this.shopImage = shopImage;
        this.shopContactNumber = shopContactNumber;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public String getShopContactNumber() {
        return shopContactNumber;
    }

    public void setShopContactNumber(String shopContactNumber) {
        this.shopContactNumber = shopContactNumber;
    }
}
