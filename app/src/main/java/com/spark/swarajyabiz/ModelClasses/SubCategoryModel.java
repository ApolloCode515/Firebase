package com.spark.swarajyabiz.ModelClasses;

public class SubCategoryModel {
    String subName;
    String subImg;
    String keywords;

    public SubCategoryModel() {

    }

    public SubCategoryModel(String subName, String subImg, String keywords) {
        this.subName = subName;
        this.subImg = subImg;
        this.keywords = keywords;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getSubImg() {
        return subImg;
    }

    public void setSubImg(String subImg) {
        this.subImg = subImg;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
