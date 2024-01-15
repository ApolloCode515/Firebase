package com.spark.swarajyabiz;

public class BusinessPost {

    String postType;
    String postImg;
    String postID;
    String postUser;
    String bizImg;
    String postAdd;
    String postDesc;
    String postCate;

    String viewCount;
    String clickCount;
    String status;


    public BusinessPost() {
        // Default constructor required for Firebase
    }

    public BusinessPost( String postID, String postType, String postImg, String postUser, String bizImg, String postAdd, String postDesc, String postCate, String viewCount,
                         String clickCount, String status) {
        this.postType = postType;
        this.postImg = postImg;
        this.postID = postID;
        this.postUser = postUser;
        this.bizImg = bizImg;
        this.postAdd = postAdd;
        this.postDesc = postDesc;
        this.postCate = postCate;
        this.viewCount = viewCount;
        this.clickCount = clickCount;
        this.status = status;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getPostImg() {
        return postImg;
    }

    public void setPostImg(String postImg) {
        this.postImg = postImg;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getPostUser() {
        return postUser;
    }

    public void setPostUser(String postUser) {
        this.postUser = postUser;
    }

    public String getPostAdd() {
        return postAdd;
    }

    public void setPostAdd(String postAdd) {
        this.postAdd = postAdd;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }

    public String getPostCate() {
        return postCate;
    }

    public void setPostCate(String postCate) {
        this.postCate = postCate;
    }

    public String getBizImg() {
        return bizImg;
    }

    public void setBizImg(String bizImg) {
        this.bizImg = bizImg;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getClickCount() {
        return clickCount;
    }

    public void setClickCount(String clickCount) {
        this.clickCount = clickCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

