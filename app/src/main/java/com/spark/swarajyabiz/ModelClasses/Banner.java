package com.spark.swarajyabiz.ModelClasses;
public class Banner {

    private String Img;
    private String Redirect;

    public Banner() {
        // Default constructor required for Firebase
    }

    public Banner(String img, String redirect) {
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
