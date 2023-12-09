package com.spark.admin;

public class Request {
    private String userId;
    private String userName;
    private String contactNumber;
    public Request() {
        // Default constructor required for Firebase
    }

    public Request(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}

