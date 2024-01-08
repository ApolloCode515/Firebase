package com.spark.swarajyabiz;

public class EmployeeDetails {

    String candidateName;
    String candidateEmail;
    String candidateContactNumber;
    String candidateAddress;
    String candidateQualification;
    String candidateSkills;
    String candidateStream;

    public EmployeeDetails() {

    }

    public EmployeeDetails(String candidateName, String candidateEmail, String candidateContactNumber, String candidateAddress, String candidateQualification, String candidateSkills, String candidateStream) {
        this.candidateName = candidateName;
        this.candidateEmail = candidateEmail;
        this.candidateContactNumber = candidateContactNumber;
        this.candidateAddress = candidateAddress;
        this.candidateQualification = candidateQualification;
        this.candidateSkills = candidateSkills;
        this.candidateStream = candidateStream;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public void setCandidateEmail(String candidateEmail) {
        this.candidateEmail = candidateEmail;
    }

    public String getCandidateContactNumber() {
        return candidateContactNumber;
    }

    public void setCandidateContactNumber(String candidateContactNumber) {
        this.candidateContactNumber = candidateContactNumber;
    }

    public String getCandidateAddress() {
        return candidateAddress;
    }

    public void setCandidateAddress(String candidateAddress) {
        this.candidateAddress = candidateAddress;
    }

    public String getCandidateQualification() {
        return candidateQualification;
    }

    public void setCandidateQualification(String candidateQualification) {
        this.candidateQualification = candidateQualification;
    }

    public String getCandidateSkills() {
        return candidateSkills;
    }

    public void setCandidateSkills(String candidateSkills) {
        this.candidateSkills = candidateSkills;
    }

    public String getCandidateStream() {
        return candidateStream;
    }

    public void setCandidateStream(String candidateStream) {
        this.candidateStream = candidateStream;
    }
}
