package com.spark.admin;

public class User {
    private String name;
    private String email;
    private String contactNumber;
    private String taluka;

    public User() {
        // Default constructor required for Firebase
    }

    public User(String name, String email,String contactNumber) {
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
        this.taluka = taluka;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
// Add other getter and setter methods for additional fields

    public String getTaluka() {
        return taluka;
    }

    public void setTaluka(String taluka) {
        this.taluka = taluka;
    }

    // Override toString() method if necessary
}

