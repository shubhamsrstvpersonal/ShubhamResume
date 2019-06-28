package com.js.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "EDUCATION")
public class Education implements Serializable {

    @javax.persistence.Id
    @Column(name = "IDEDUCATION")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idEducation;

    @Column(name = "COURSE", length = 50)
    private String course;

    @Column(name = "COLLEGE", length = 100)
    private String college;

    @Column(name = "SCORE", length = 10)
    private String score;

    @Column(name = "YEAROFPASSING", length = 5)
    private String yearOfPassing;

    @Column(name = "LOCATION", length = 20)
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERNAME")
    @JsonIgnore
    private PersonalDetails personalDetailsWithEducation;

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getYearOfPassing() {
        return yearOfPassing;
    }

    public void setYearOfPassing(String yearOfPassing) {
        this.yearOfPassing = yearOfPassing;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getId() {
        return idEducation;
    }

    public void setId(long idEducation) {
        this.idEducation = idEducation;
    }

    public PersonalDetails getPersonalDetailsWithEducation() {
        return personalDetailsWithEducation;
    }

    public void setPersonalDetailsWithEducation(PersonalDetails personalDetailsWithEducation) {
        this.personalDetailsWithEducation = personalDetailsWithEducation;
    }
}
