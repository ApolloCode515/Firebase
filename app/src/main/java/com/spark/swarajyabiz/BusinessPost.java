package com.spark.swarajyabiz;

public class BusinessPost {
    private String imageUrl;
    private String text;
    private String shopName;
    private String shopImage;
    private String postkey;

    public BusinessPost() {
        // Default constructor required for Firebase
    }

    public BusinessPost(String imageUrl, String text, String shopName, String shopImage, String postkey) {
        this.imageUrl = imageUrl;
        this.text = text;
        this.shopName = shopName;
        this.shopImage = shopImage;
        this.postkey = postkey;
    }

    public String getPostkey() {
        return postkey;
    }

    public void setPostkey(String postkey) {
        this.postkey = postkey;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getText() {
        return text;
    }
}

