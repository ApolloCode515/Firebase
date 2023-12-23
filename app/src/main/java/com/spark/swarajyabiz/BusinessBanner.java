package com.spark.swarajyabiz;

import java.util.List;
public class BusinessBanner {
    private String imageUrl;
    private String businessName;
    private boolean isFav;

    public BusinessBanner(String imageUrl, String businessName) {
        this.imageUrl = imageUrl;
        this.businessName = businessName;
        this.isFav = false; // Default value
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getBusinessName() {
        return businessName;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }
}
