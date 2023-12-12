package com.spark.swarajyabiz;

public class CandidateDetials {

    String candidateName;
    String candidateContactNumber;
    String candidateEmail;
    String candidateQualification;
    String candidateStream;
    String candidateSkills;

    private CandidateDetials(){

    }

    private CandidateDetials(String candidateName, String candidateContactNumber, String candidateEmail, String candidateQualification,
                              String candidateStream, String candidateSkills){

        this. candidateName = candidateName;
        this.candidateContactNumber = candidateContactNumber;
        this.candidateEmail = candidateEmail;
        this.candidateQualification = candidateQualification;
        this.candidateStream = candidateStream;
        this.candidateSkills = candidateSkills;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
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
    }

    public String getCandidateQualification() {
        return candidateQualification;
    }

    public void setCandidateQualification(String candidateQualification) {
        this.candidateQualification = candidateQualification;
    }

    public String getCandidateStream() {
        return candidateStream;
    }

    public void setCandidateStream(String candidateStream) {
        this.candidateStream = candidateStream;
    }

    public String getCandidateSkills() {
        return candidateSkills;
    }

    public void setCandidateSkills(String candidateSkills) {
        this.candidateSkills = candidateSkills;
    }
}
