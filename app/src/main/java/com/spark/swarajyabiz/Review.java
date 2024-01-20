package com.spark.swarajyabiz;

public class Review {

    String rating;
    String review;
    String date;
    String userNo;

    String userName;

    public Review() {

    }

    public Review(String rating, String review, String date, String userNo, String userName) {
        this.rating = rating;
        this.review = review;
        this.date = date;
        this.userNo = userNo;
        this.userName = userName;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
