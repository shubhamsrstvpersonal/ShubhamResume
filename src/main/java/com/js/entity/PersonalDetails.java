package com.js.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "PERSONALDETAILS")
public class PersonalDetails implements Serializable {

    @Id
    @Column(name = "USERNAME", length = 50)
    private String username;

    @Column(name = "NAME", length = 20)
    private String name;

    @Column(name = "CONTACT", length = 15)
    private String contact;

    @Column(name = "EMAIL", length = 25)
    private String email;

    @Column(name = "HOMETOWN", length = 100)
    private String homeTown;

    @Column(name = "DESIGNATION", length = 30)
    private String designation;

    @Column(name = "CAREERSUMMARY", length = 250)
    private String careerSummary;

    @Column(name = "SOFTSKILLS", length = 250)
    //@ElementCollection
    private List<String> softSkills;

    @OneToMany(mappedBy = "personalDetailsWithSkills", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TechnicalSkills> technicalSkills;

    @Column(name = "CURRENTROLES")
    //@ElementCollection
    private List<String> currentRoles;

    @OneToMany(mappedBy = "personalDetailsWithWork", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WorkDetails> workDetails;

    @OneToMany(mappedBy = "personalDetailsWithEducation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Education> educations;

    @Column(name = "SPECIALMESSAGE", length = 2000)
    private String specialMessage;

    @Column(name = "RESUMEFILE", columnDefinition = "MEDIUMBLOB")
    private byte[] resumeFile;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCareerSummary() {
        return careerSummary;
    }

    public void setCareerSummary(String careerSummary) {
        this.careerSummary = careerSummary;
    }

    public List<String> getSoftSkills() {
        return softSkills;
    }

    public void setSoftSkills(List<String> softSkills) {
        this.softSkills = softSkills;
    }

    public List<TechnicalSkills> getTechnicalSkills() {
        return technicalSkills;
    }

    public void setTechnicalSkills(List<TechnicalSkills> technicalSkills) {
        this.technicalSkills = technicalSkills;
    }

    public List<String> getCurrentRoles() {
        return currentRoles;
    }

    public void setCurrentRoles(List<String> currentRoles) {
        this.currentRoles = currentRoles;
    }

    public List<WorkDetails> getWorkDetails() {
        return workDetails;
    }

    public void setWorkDetails(List<WorkDetails> workDetails) {
        this.workDetails = workDetails;
    }

    public List<Education> getEducations() {
        return educations;
    }

    public void setEducations(List<Education> educations) {
        this.educations = educations;
    }

    public String getSpecialMessage() {
        return specialMessage;
    }

    public void setSpecialMessage(String specialMessage) {
        this.specialMessage = specialMessage;
    }

    public byte[] getResumeFile() {
        return resumeFile;
    }

    public void setResumeFile(byte[] resumeFile) {
        this.resumeFile = resumeFile;
    }
}
