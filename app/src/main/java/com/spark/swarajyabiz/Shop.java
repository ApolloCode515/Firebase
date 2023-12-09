package com.spark.swarajyabiz;

import androidx.room.ColumnInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shop implements Serializable {
    private String name;
    private String shopName;
    private String service;
    private String url;
    private List<String> imageUrls;
    private String contactNumber;
    private String phoneNumber;
    private String email;
    private String address;
    private String district;
    private String taluka;
    private String shopcategory;
    private HashMap<String, String> time;
    @ColumnInfo(name = "random_order")
    private int randomOrder;
    private Request request;
    private int promotionCount;
    private int requestcount;
    private int ordercount;
    private List<Count> countList;
    private boolean deleted;
    private boolean Profileverified;
    private boolean premium;
    private List<ItemList> itemList;


    public Shop() {
        // Default constructor required for Firebase
    }



    public Shop(String name, String shopName,String contactNumber, String address, String url, String service,
                String district, String taluka, int promotionCount,  List<ItemList> itemList
                , int ordercount, int requestcount, String shopcategory) {

        this.name = name;
        this.shopName = shopName;
        this.service = service;
        this.url = url;
        this.contactNumber = contactNumber;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.district = district;
        this.taluka = taluka;
        this.promotionCount = promotionCount;
        this.itemList = itemList;
        this.ordercount = ordercount;
        this.requestcount = requestcount;
        this.shopcategory = shopcategory;

    }

    public String getShopcategory() {
        return shopcategory;
    }

    public void setShopcategory(String shopcategory) {
        this.shopcategory = shopcategory;
    }

    public int getRequestcount() {
        return requestcount;
    }

    public void setRequestcount(int requestcount) {
        this.requestcount = requestcount;
    }

    public int getOrdercount() {
        return ordercount;
    }

    public void setOrdercount(int ordercount) {
        this.ordercount = ordercount;
    }

    public List<ItemList> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemList> itemList) {
        this.itemList = itemList;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public boolean isProfileverified() {
        return Profileverified;
    }

    public void setProfileverified(boolean profileverified) {
        Profileverified = profileverified;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        getKeywords();
    }

    public String getShopName() {
        return shopName;

    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
        getKeywords();
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
        getKeywords();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        getKeywords();
    }

    public HashMap<String, String> getTime() {
        return time;
    }

    public void setTime(HashMap<String, String> time) {
        this.time = time;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDistrict() {
        return district;

    }

    public void setDistrict(String district) {
        this.district = district;
        getKeywords();
    }


    public String getTaluka() {
        return taluka;
    }

    public void setTaluka(String taluka) {
        this.taluka = taluka;
        getKeywords();
    }

    public List<String> getKeywords() {
        List<String> keywords = new ArrayList<>();

        // Add non-null keywords associated with the shop to the list
        if (name != null) {
            keywords.add(name.toLowerCase());
        }
        if (shopName != null) {
            keywords.add(shopName.toLowerCase());
        }
        if (address != null) {
            keywords.add(address.toLowerCase());
        }
        if (district != null) {
            keywords.add(district);
        }
        if (taluka != null) {
            keywords.add(taluka);
        }

        return keywords;
    }
    public int getRandomOrder() {
        return randomOrder;
    }

    public void setRandomOrder(int randomOrder) {
        this.randomOrder = randomOrder;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("shopName", shopName);
        result.put("address", address);
        result.put("phoneNumber", phoneNumber);
        result.put("email", email);
        result.put("service", service);
        result.put("district", district);
        result.put("taluka", taluka);
        // ... map other fields

        return result;
    }

    public int getpromotionCount() {
        return promotionCount;
    }

    public void setpromotionCount(int promotionCount) {
        this.promotionCount = promotionCount;
    }
}