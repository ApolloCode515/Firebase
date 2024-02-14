package com.spark.swarajyabiz.ModelClasses;

public class RefModel {
    String mobno;
    String refby;
    String status;
    String cplan;
    String date;
    String refamt;

    public RefModel() {

    }

    public RefModel(String mobno, String refby, String status, String cplan, String date, String refamt) {
        this.mobno = mobno;
        this.refby = refby;
        this.status = status;
        this.cplan = cplan;
        this.date = date;
        this.refamt = refamt;
    }

    public String getMobno() {
        return mobno;
    }

    public void setMobno(String mobno) {
        this.mobno = mobno;
    }

    public String getRefby() {
        return refby;
    }

    public void setRefby(String refby) {
        this.refby = refby;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCplan() {
        return cplan;
    }

    public void setCplan(String cplan) {
        this.cplan = cplan;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRefamt() {
        return refamt;
    }

    public void setRefamt(String refamt) {
        this.refamt = refamt;
    }
}
