package com.spark.swarajyabiz;

import java.util.ArrayList;
import java.util.List;

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
    String experience;
    String salary;
    String skills;
    String jobopenings;
    String JobID;



    public JobDetails(String jobtitle, String companyname, String workplacetype, String joblocation, String jobtype,
                      String description,String currentdate, String jobkey, String contactNumber,
                      String experience, String salary, String skills, String jobopenings, String JobID){
        this.jobtitle = jobtitle;
        this.companyname = companyname;
        this.workplacetype = workplacetype;
        this.joblocation = joblocation;
        this.jobtype = jobtype;
        this.description = description;
        this.jobkey = jobkey;
        this.currentdate = currentdate;
        this.contactNumber = contactNumber;
        this.experience =experience;
        this.salary = salary;
        this.skills = skills;
        this.jobopenings = jobopenings;
        this.JobID = JobID;
    }

    public JobDetails() {

    }

    public String getJobtitle() {
        return jobtitle;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
        getKeywords();
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
        getKeywords();
    }

    public String getWorkplacetype() {
        return workplacetype;
    }

    public void setWorkplacetype(String workplacetype) {
        this.workplacetype = workplacetype;
        getKeywords();
    }

    public String getJoblocation() {
        return joblocation;
    }

    public void setJoblocation(String joblocation) {
        this.joblocation = joblocation;
        getKeywords();
    }

    public String getJobtype() {
        return jobtype;
    }

    public void setJobtype(String jobtype) {
        this.jobtype = jobtype;
        getKeywords();getKeywords();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        getKeywords();
    }

    public String getJobopenings() {
        return jobopenings;
    }

    public void setJobopenings(String jobopenings) {
        this.jobopenings = jobopenings;
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

    public String getExperience() {
        return experience;

    }

    public void setExperience(String experience) {
        this.experience = experience;
        getKeywords();
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
        getKeywords();
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
        getKeywords();
    }

    public List<String> getKeywords() {
        List<String> keywords = new ArrayList<>();

        // Add non-null keywords associated with the shop to the list
        if (jobtitle != null) {
            keywords.add(jobtitle.toLowerCase());
        }
        if (companyname != null) {
            keywords.add(companyname.toLowerCase());
        }
        if (workplacetype != null) {
            keywords.add(workplacetype.toLowerCase());
        }
        if (joblocation != null) {
            keywords.add(joblocation);
        }
        if (jobtype != null) {
            keywords.add(jobtype);
        }
        if (description != null) {
            keywords.add(description);
        }
        if (experience != null) {
            keywords.add(experience);
        }
        if (salary != null) {
            keywords.add(salary);
        }
        if (skills != null) {
            keywords.add(skills);
        }

        return keywords;
    }

}
