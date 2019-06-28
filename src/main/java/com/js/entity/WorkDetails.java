package com.js.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "WORKDETAILS")
public class WorkDetails implements Serializable {

    @Id
    @Column(name = "IDWORK")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idWork;

    @Column(name = "CLIENTNAME", length = 100)
    private String clientName;

    @Column(name = "LOCATION", length = 50)
    private String location;

    @Column(name = "DURATION", length = 25)
    private String duration;

    @Column(name = "ROLE", length = 20)
    private String role;

    @Column(name = "TOOLS", length = 30)
    @ElementCollection
    private List<String> tools;

    @Column(name = "DESCRIPTION", length = 300)
    @ElementCollection
    private List<String> description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERNAME")
    @JsonIgnore
    private PersonalDetails personalDetailsWithWork;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getTools() {
        return tools;
    }

    public void setTools(List<String> tools) {
        this.tools = tools;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public long getId() {
        return idWork;
    }

    public void setId(long idWork) {
        this.idWork = idWork;
    }

    public PersonalDetails getPersonalDetailsWithWork() {
        return personalDetailsWithWork;
    }

    public void setPersonalDetailsWithWork(PersonalDetails personalDetailsWithWork) {
        this.personalDetailsWithWork = personalDetailsWithWork;
    }
}
