package com.spark.swarajyabiz.ModelClasses;
public class BannerX {

    private String Img;
    private String Redirect;

    public BannerX() {
        // Default constructor required for Firebase
    }

    public BannerX(String img, String redirect) {
        Img = img;
        Redirect = redirect;
    }

    public String getImg() {
        return Img;
    }

    public String getRedirect() {
        return Redirect;
    }
}
