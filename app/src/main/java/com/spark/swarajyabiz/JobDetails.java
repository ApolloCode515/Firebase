package com.spark.swarajyabiz;

public class JobDetails {

    String jobtitle;
    String companyname;
    String workplacetype;
    String joblocation;
    String jobtype;
    String description;
    String currentdate;
    String jobkey;
    String contactNumber;
    String JobID;


    public JobDetails(String jobtitle, String companyname, String workplacetype, String joblocation, String jobtype,
                      String description,String currentdate, String jobkey, String contactNumber, String JobID){
        this.jobtitle = jobtitle;
        this.companyname = companyname;
        this.workplacetype = workplacetype;
        this.joblocation = joblocation;
        this.jobtype = jobtype;
        this.description = description;
        this.jobkey = jobkey;
        this.currentdate = currentdate;
        this.contactNumber = contactNumber;
        this.JobID = JobID;
    }

    public JobDetails() {

    }

    public String getJobtitle() {
        return jobtitle;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getWorkplacetype() {
        return workplacetype;
    }

    public void setWorkplacetype(String workplacetype) {
        this.workplacetype = workplacetype;
    }

    public String getJoblocation() {
        return joblocation;
    }

    public void setJoblocation(String joblocation) {
        this.joblocation = joblocation;
    }

    public String getJobtype() {
        return jobtype;
    }

    public void setJobtype(String jobtype) {
        this.jobtype = jobtype;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobkey() {
        return jobkey;
    }

    public void setJobkey(String jobkey) {
        this.jobkey = jobkey;
    }

    public String getCurrentdate() {
        return currentdate;
    }

    public void setCurrentdate(String currentdate) {
        this.currentdate = currentdate;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getJobID() {
        return JobID;
    }

    public void setJobID(String jobID) {
        JobID = jobID;
    }
}
