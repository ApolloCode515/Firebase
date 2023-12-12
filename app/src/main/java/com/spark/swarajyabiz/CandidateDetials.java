package com.spark.swarajyabiz;

import java.util.ArrayList;
import java.util.List;

public class CandidateDetials {

    String candidateName;
    String candidateContactNumber;
    String candidateEmail;
    String candidateQualification;
    String candidateStream;
    String candidateSkills;
    String candidateAddress;
    String appliedon;

    CandidateDetials(){

    }

    CandidateDetials(String candidateName, String candidateContactNumber, String candidateEmail, String candidateQualification,
                     String candidateStream, String candidateSkills,String candidateAddress, String appliedon){

        this. candidateName = candidateName;
        this.candidateContactNumber = candidateContactNumber;
        this.candidateEmail = candidateEmail;
        this.candidateQualification = candidateQualification;
        this.candidateStream = candidateStream;
        this.candidateSkills = candidateSkills;
        this.appliedon = appliedon;
        this.candidateAddress = candidateAddress;
    }

    public String getCandidateName() {
        return candidateName;

    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
        getKeywords();
    }

    public String getCandidateContactNumber() {
        return candidateContactNumber;
    }

    public void setCandidateContactNumber(String candidateContactNumber) {
        this.candidateContactNumber = candidateContactNumber;
    }

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public void setCandidateEmail(String candidateEmail) {
        this.candidateEmail = candidateEmail;
        getKeywords();
    }

    public String getCandidateQualification() {
        return candidateQualification;
    }

    public void setCandidateQualification(String candidateQualification) {
        this.candidateQualification = candidateQualification;
        getKeywords();
    }

    public String getCandidateStream() {
        return candidateStream;
    }

    public void setCandidateStream(String candidateStream) {
        this.candidateStream = candidateStream;
        getKeywords();
    }

    public String getCandidateSkills() {
        return candidateSkills;
    }

    public void setCandidateSkills(String candidateSkills) {
        this.candidateSkills = candidateSkills;
        getKeywords();
    }

    public String getAppliedon() {
        return appliedon;
    }

    public void setAppliedon(String appliedon) {
        this.appliedon = appliedon;
    }

    public String getCandidateAddress() {
        return candidateAddress;
    }

    public void setCandidateAddress(String candidateAddress) {
        this.candidateAddress = candidateAddress;
        getKeywords();
    }

    public List<String> getKeywords() {
        List<String> keywords = new ArrayList<>();

        // Add non-null keywords associated with the shop to the list
        if (candidateName != null) {
            keywords.add(candidateName.toLowerCase());
        }
        if (candidateEmail != null) {
            keywords.add(candidateEmail.toLowerCase());
        }
        if (candidateQualification != null) {
            keywords.add(candidateQualification.toLowerCase());
        }
        if (candidateStream != null) {
            keywords.add(candidateStream);
        }
        if (candidateSkills != null) {
            keywords.add(candidateSkills);
        }
        if (candidateAddress != null) {
            keywords.add(candidateAddress);
        }

        return keywords;
    }
}
