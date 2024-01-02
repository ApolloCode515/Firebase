package com.spark.swarajyabiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemList {
    private String name;
    private String price;
    private String description;
    private String sellPrice;
    private String image; // List of image URLs
    private String firstImageUrl; // Add a field for the first image URL
    private List<String> imagesUrls;
    private String itemkey;
    private String shopName;
    private String shopimage;
    private String shopcontactNumber;
    private String district;
    private String taluka;
    private String address;


    public ItemList() {
        // Empty constructor needed for Firebase
    }

    public ItemList(String shopName,String shopimage,String shopcontactNumber, String name, String price, String sellPrice, String description, String firstImageUrl, String itemkey,
                    List<String> imagesUrls, String district, String taluka, String address) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
        this.firstImageUrl = firstImageUrl;
        this.itemkey = itemkey;
        this.imagesUrls = imagesUrls;
        this.shopName = shopName;
        this.shopimage = shopimage;
        this.shopcontactNumber = shopcontactNumber;
       this.district = district;
       this.taluka = taluka;
       this.address = address;
       this.sellPrice = sellPrice;
    }

    public String getTaluka() {
        return taluka;
    }

    public void setTaluka(String taluka) {
        this.taluka = taluka;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getShopcontactNumber() {
        return shopcontactNumber;
    }

    public void setShopcontactNumber(String shopcontactNumber) {
        this.shopcontactNumber = shopcontactNumber;
    }

    public String getShopimage() {
        return shopimage;
    }

    public void setShopimage(String shopimage) {
        this.shopimage = shopimage;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        getKeywords();
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        getKeywords();
    }

    public String getFirstImageUrl() {
        return firstImageUrl;
    }

    public List<String> getImagesUrls() {
        return imagesUrls;
    }

    public void setImagesUrls(List<String> imagesUrls) {
        this.imagesUrls = imagesUrls;
    }

    public String getItemkey() {
        return itemkey;
    }

    public void setItemkey(String itemkey) {
        this.itemkey = itemkey;
    }


    public List<String> getKeywords() {
        List<String> keywords = new ArrayList<>();

        // Add non-null keywords associated with the shop to the list
        if (name != null) {
            keywords.add(name.toLowerCase());
        }
        if (description != null) {
            keywords.add(description.toLowerCase());
        }

        return keywords;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("itemname", name);
        result.put("description", description);

        // ... map other fields

        return result;
    }


}
