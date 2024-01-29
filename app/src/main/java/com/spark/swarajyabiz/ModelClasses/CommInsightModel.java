package com.spark.swarajyabiz.ModelClasses;

public class CommInsightModel {
    String commId;
    String commName;
    String commMbrCnt;
    String commViews;
    String commEarns;
    String commMonit;
    String commStatus;
    String commProf;

    public CommInsightModel() {
    }

    public CommInsightModel(String commId, String commName, String commMbrCnt, String commViews, String commEarns, String commMonit, String commStatus, String commProf) {
        this.commId = commId;
        this.commName = commName;
        this.commMbrCnt = commMbrCnt;
        this.commViews = commViews;
        this.commEarns = commEarns;
        this.commMonit = commMonit;
        this.commStatus = commStatus;
        this.commProf = commProf;
    }

    public String getCommId() {
        return commId;
    }

    public void setCommId(String commId) {
        this.commId = commId;
    }

    public String getCommName() {
        return commName;
    }

    public void setCommName(String commName) {
        this.commName = commName;
    }

    public String getCommMbrCnt() {
        return commMbrCnt;
    }

    public void setCommMbrCnt(String commMbrCnt) {
        this.commMbrCnt = commMbrCnt;
    }

    public String getCommViews() {
        return commViews;
    }

    public void setCommViews(String commViews) {
        this.commViews = commViews;
    }

    public String getCommEarns() {
        return commEarns;
    }

    public void setCommEarns(String commEarns) {
        this.commEarns = commEarns;
    }

    public String getCommMonit() {
        return commMonit;
    }

    public void setCommMonit(String commMonit) {
        this.commMonit = commMonit;
    }

    public String getCommStatus() {
        return commStatus;
    }

    public void setCommStatus(String commStatus) {
        this.commStatus = commStatus;
    }

    public String getCommProf() {
        return commProf;
    }

    public void setCommProf(String commProf) {
        this.commProf = commProf;
    }
}
