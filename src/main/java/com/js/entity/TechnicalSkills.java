package com.js.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TECHNICALSKILLS")
public class TechnicalSkills implements Serializable {

    @javax.persistence.Id
    @Column(name = "IDSKILLS")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idSkills;

    @Column(name = "YEARS", length = 10)
    private String years;

    @Column(name = "TECHNOLOGY", length = 50)
    private String technology;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERNAME")
    @JsonIgnore
    private PersonalDetails personalDetailsWithSkills;

    public long getId() {
        return idSkills;
    }

    public void setId(long idSkills) {
        this.idSkills = idSkills;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public PersonalDetails getPersonalDetailsWithSkills() {
        return personalDetailsWithSkills;
    }

    public void setPersonalDetailsWithSkills(PersonalDetails personalDetailsWithSkills) {
        this.personalDetailsWithSkills = personalDetailsWithSkills;
    }
}
