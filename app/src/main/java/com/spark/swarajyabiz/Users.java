package com.spark.swarajyabiz;

public class Users {
    private String email;
    private String contactNumber;
    private String Name;
    private String password;
    private String district;
    private String taluka;
    private String userID;
    private String installDate;
    private String activeCount;
    private String ExpDate;

    public Users() {
        // Default constructor required for Firebase
    }

    public Users(String name, String email,String contactNumber, String taluka, String district, String password, String userID
                 , String installDate, String activeCount, String ExpDate) {
        this.Name = name;
        this.email = email;
        this.contactNumber = contactNumber;
        this.taluka = taluka;
        this.district = district;
        this.password = password;
        this.userID = userID;
        this.installDate = installDate;
        this.activeCount = activeCount;
        this.ExpDate = ExpDate;
    }

    public String getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(String activeCount) {
        this.activeCount = activeCount;
    }

    public String getInstallDate() {
        return installDate;
    }

    public void setInstallDate(String installDate) {
        this.installDate = installDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTaluka() {
        return taluka;
    }

    public void setTaluka(String taluka) {
        this.taluka = taluka;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getExpDate() {
        return ExpDate;
    }

    public void setExpDate(String expDate) {
        ExpDate = expDate;
    }
}

