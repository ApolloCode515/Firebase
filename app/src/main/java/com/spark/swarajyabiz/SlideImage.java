package com.spark.swarajyabiz;

//public class SlideImage {
//
//    private int image;
//    private int resourceId;
//    private String imageUrl;
//    private String price;
//
//    public SlideImage(int resourceId) {
//        this.resourceId = resourceId;
//    }
//
//    // Getter and setter for imageUrl
//    public String getImageUrl() {
//        return imageUrl;
//    }
//
//    public void setImageUrl(String imageUrl) {
//        this.imageUrl = imageUrl;
//    }
//
//    public String getPrice() {
//        return price;
//    }
//
//    // Getter for resourceId
//    public int getResourceId() {
//        return resourceId;
//    }
//}


public class SlideImage {
    private String imageUrl;
    private String price;

    public SlideImage(String imageUrl, String price) {
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPrice() {
        return price;
    }
}
