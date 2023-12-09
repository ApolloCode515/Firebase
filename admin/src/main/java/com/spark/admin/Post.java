package com.spark.admin;

public class Post {

    String shopimage;
    String shoppostimage;
    String shopname;
    String postkey;
    String postcaption;

    public Post(){

    }

    public Post(String shopname, String shopimage, String shoppostimage, String postkey, String caption){
        this.shopname = shopname;
        this.shopimage = shopimage;
        this.shoppostimage = shoppostimage;
        this.postkey = postkey;
        this.postcaption = caption;
    }

    public String getPostcaption() {
        System.out.println("efgfb " +postcaption);
        return postcaption;
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

    public String getShoppostimage() {
        return shoppostimage;
    }

    public void setShoppostimage(String shoppostimage) {
        this.shoppostimage = shoppostimage;
    }

    public String getShopname() {
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
