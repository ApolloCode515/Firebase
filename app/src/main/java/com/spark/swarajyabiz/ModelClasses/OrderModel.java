package com.spark.swarajyabiz.ModelClasses;

import java.util.List;

public class OrderModel {
    String prodId;
    String prodName;
    String proDesc;
    String proImg;
    String rating;
    String proTag;
    String crossRate;
    String showRate;
    String offer;
    String proSeq;
    String dealerCode;
    String proprice;
    String prosell;
    String shopContactNum;
    private List<String> imagesUrls;


    public OrderModel() {

    }

    public OrderModel(String prodId, String prodName, String proDesc, String proImg, String rating, String proTag,
                      String crossRate, String showRate, String offer, String proSeq, String dealerCode,
                      String proprice, String prosell, String shopContactNum,List<String> imagesUrls) {
        this.prodId = prodId;
        this.prodName = prodName;
        this.proDesc = proDesc;
        this.proImg = proImg;
        this.rating = rating;
        this.proTag = proTag;
        this.crossRate = crossRate;
        this.showRate = showRate;
        this.offer = offer;
        this.proSeq = proSeq;
        this.dealerCode = dealerCode;
        this.proprice = proprice;
        this.prosell = prosell;
        this.shopContactNum = shopContactNum;
        this.imagesUrls = imagesUrls;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProDesc() {
        return proDesc;
    }

    public void setProDesc(String proDesc) {
        this.proDesc = proDesc;
    }

    public String getProImg() {
        return proImg;
    }

    public void setProImg(String proImg) {
        this.proImg = proImg;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getProTag() {
        return proTag;
    }

    public void setProTag(String proTag) {
        this.proTag = proTag;
    }

    public String getCrossRate() {
        return crossRate;
    }

    public void setCrossRate(String crossRate) {
        this.crossRate = crossRate;
    }

    public String getShowRate() {
        return showRate;
    }

    public void setShowRate(String showRate) {
        this.showRate = showRate;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getProSeq() {
        return proSeq;
    }

    public void setProSeq(String proSeq) {
        this.proSeq = proSeq;
    }

    public String getDealerCode() {
        return dealerCode;
    }

    public void setDealerCode(String dealerCode) {
        this.dealerCode = dealerCode;
    }

    public String getProprice() {
        return proprice;
    }

    public void setProprice(String proprice) {
        this.proprice = proprice;
    }

    public String getProsell() {
        return prosell;
    }

    public void setProsell(String prosell) {
        this.prosell = prosell;
    }

    public String getShopContactNum() {
        return shopContactNum;
    }

    public void setShopContactNum(String shopContactNum) {
        this.shopContactNum = shopContactNum;
    }

    public List<String> getImagesUrls() {
        return imagesUrls;
    }

    public void setImagesUrls(List<String> imagesUrls) {
        this.imagesUrls = imagesUrls;
    }
}
