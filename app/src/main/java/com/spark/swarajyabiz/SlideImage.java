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
    private String description;
    private String packageName;
    private String plan;

    public SlideImage(String imageUrl, String price, String description, String packageName, String plan) {
        this.imageUrl = imageUrl;
        this.price = price;
        this.description = description;
        this.packageName = packageName;
        this.plan = plan;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription(){return description;}

    public String getPackageName(){return packageName;}

    public String getPlan(){return plan;}
}
