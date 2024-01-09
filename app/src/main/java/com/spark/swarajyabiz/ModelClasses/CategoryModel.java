package com.spark.swarajyabiz.ModelClasses;

public class CategoryModel {
    String cname;
    String cimg;

    public CategoryModel() {

    }

    public CategoryModel(String cname, String cimg) {
        this.cname = cname;
        this.cimg = cimg;
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
}
