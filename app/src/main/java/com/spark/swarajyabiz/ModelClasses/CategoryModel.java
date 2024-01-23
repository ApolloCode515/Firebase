package com.spark.swarajyabiz.ModelClasses;

public class CategoryModel {
    String cname;
    String cimg;

    String keywords;

    String details;

    public CategoryModel() {

    }

    public CategoryModel(String cname, String cimg, String keywords, String details) {
        this.cname = cname;
        this.cimg = cimg;
        this.keywords = keywords;
        this.details = details;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCimg() {
        return cimg;
    }

    public void setCimg(String cimg) {
        this.cimg = cimg;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
