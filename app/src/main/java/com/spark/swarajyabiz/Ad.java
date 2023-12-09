package com.spark.swarajyabiz;

public class Ad {
    private String imageUrl;

    public Ad() {
        // Default constructor required for Firebase
    }

    public Ad(String imageUrl) {
        this.imageUrl = imageUrl;
        System.out.println("fsdfs " +imageUrl);

    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
