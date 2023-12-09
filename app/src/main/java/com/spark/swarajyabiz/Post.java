package com.spark.swarajyabiz;

import java.util.List;

public class Post {

    String shopimage;
    private List<String> shoppostimages;
    String shopname;
    String postkey;
    String postcaption;
    String shopcontactNumber;

    public Post(){

    }

    public Post(String shopname, String shopimage,String shopcontactNumber, List<String> shoppostimages){
        this.shopname = shopname;
        this.shopimage = shopimage;
        this.shopcontactNumber = shopcontactNumber;
        this.shoppostimages = shoppostimages;

    }

    public void setShoppostimages(List<String> shoppostimages) {
        this.shoppostimages = shoppostimages;
    }

    public String getShopcontactNumber() {
        return shopcontactNumber;
    }

    public void setShopcontactNumber(String shopcontactNumber) {
        this.shopcontactNumber = shopcontactNumber;
    }

    public String getPostcaption() {

        return postcaption;
    }

    public List<String> getShoppostimages() {
        return shoppostimages;
    }

    public void setPostcaption(String postcaption) {
        this.postcaption = postcaption;
    }

    public String getShopimage() {
        return shopimage;
    }

    public void setShopimage(String shopimage) {
        this.shopimage = shopimage;
    }

    public String getShopname() {
        System.out.println("efgfb " +shopname);
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getPostkey() {
        return postkey;
    }

    public void setPostkey(String postkey) {
        this.postkey = postkey;
    }
}
