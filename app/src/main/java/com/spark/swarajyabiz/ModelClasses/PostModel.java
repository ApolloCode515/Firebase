package com.spark.swarajyabiz.ModelClasses;

public class PostModel {
    String postId;
    String postUser;
    String userAdd;
    String postDesc;
    String postImg;
    String postType;
    String postKeys;
    String userImg;

    String postCate;

    String postcontactKey;

    String postStatus;

    String postvisibilityCount;
    String postclickCount;
    private boolean isVisibilityCountUpdated;


    public PostModel() {

    }

    public PostModel(String postId, String postUser, String userAdd, String postDesc, String postImg, String postType, String postKeys, String userImg, String postCate, String postcontactKey, String postStatus
    , String postvisibilityCount, String postclickCount) {
        this.postId = postId;
        this.postUser = postUser;
        this.userAdd = userAdd;
        this.postDesc = postDesc;
        this.postImg = postImg;
        this.postType = postType;
        this.postKeys = postKeys;
        this.userImg = userImg;
        this.postCate = postCate;
        this.postcontactKey = postcontactKey;
        this.postStatus = postStatus;
        this.postvisibilityCount = postvisibilityCount;
        this.postclickCount = postclickCount;
    }


    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostUser() {
        return postUser;
    }

    public void setPostUser(String postUser) {
        this.postUser = postUser;
    }

    public String getUserAdd() {
        return userAdd;
    }

    public void setUserAdd(String userAdd) {
        this.userAdd = userAdd;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }

    public String getPostImg() {
        return postImg;
    }

    public void setPostImg(String postImg) {
        this.postImg = postImg;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getPostKeys() {
        return postKeys;
    }

    public void setPostKeys(String postKeys) {
        this.postKeys = postKeys;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getPostCate() {
        return postCate;
    }

    public void setPostCate(String postCate) {
        this.postCate = postCate;
    }

    public String getPostcontactKey() {
        return postcontactKey;
    }

    public void setPostcontactKey(String postcontactKey) {
        this.postcontactKey = postcontactKey;
    }

    public String getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(String postStatus) {
        this.postStatus = postStatus;
    }

    public String getPostvisibilityCount() {
        return postvisibilityCount;
    }

    public void setPostvisibilityCount(String postvisibilityCount) {
        this.postvisibilityCount = postvisibilityCount;
    }

    public String getPostclickCount() {
        return postclickCount;
    }

    public void setPostclickCount(String postclickCount) {
        this.postclickCount = postclickCount;
    }

    public boolean isVisibilityCountUpdated() {
        return isVisibilityCountUpdated;
    }

    public void setVisibilityCountUpdated(boolean visibilityCountUpdated) {
        isVisibilityCountUpdated = visibilityCountUpdated;
    }

    public void resetVisibilityCountUpdated() {
        isVisibilityCountUpdated = false;
    }
}
