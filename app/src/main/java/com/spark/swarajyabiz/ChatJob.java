package com.spark.swarajyabiz;

public class ChatJob {

    String shopName;
    String jobtitle;
    String BusiContactNum;
    String UserContactNum;
    String jobId;

    public ChatJob(){

    }

    public ChatJob(String shopName, String jobtitle, String BusiContactNum, String UserContactNum, String jobId){
        this.shopName = shopName;
        this.jobtitle = jobtitle;
        this.BusiContactNum = BusiContactNum;
        this.UserContactNum = UserContactNum;
        this.jobId = jobId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getBusiContactNum() {
        return BusiContactNum;
    }

    public void setBusiContactNum(String busiContactNum) {
        BusiContactNum = busiContactNum;
    }

    public String getUserContactNum() {
        return UserContactNum;
    }

    public void setUserContactNum(String userContactNum) {
        UserContactNum = userContactNum;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
