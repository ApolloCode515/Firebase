package com.spark.swarajyabiz;

import java.util.List;

public class PostBannerClass {

    String bannerImageUrls;

    public PostBannerClass() {
        // Default constructor required for Firebase
    }

    public PostBannerClass(String bannerImageUrls) {
        this.bannerImageUrls = bannerImageUrls;
    }

    public String getBannerImageUrls() {
        return bannerImageUrls;
    }

    public void setBannerImageUrls(String bannerImageUrls) {
        this.bannerImageUrls = bannerImageUrls;
    }
}
