package com.js.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@DynamicInsert
@Table(name = "USERDETAILS")
public class UserDetails {

    @Column(name = "NAME")
    private String name;

    @Column(name = "EMAIL", length = 50, unique = true)
    private String eMail;

    @Column(name = "GENDER", length = 1)
    private String gender;

    @Id
    @Column(name = "USERNAME", length = 50)
    private String userName;

    @Column(name = "PASSWORD", length = 250)
    private String password;

    @Column(name = "CONTACTNUMBER", length = 10)
    private long contactNumber;

    @Column(name = "ROLE", columnDefinition = "varchar(20) default 'USER'")
    private String role;

    @Column(name = "ISVERIFIED", columnDefinition = "varchar(5) default 'N'")
    private String isVerified;

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(long contactNumber) {
        this.contactNumber = contactNumber;
    }
}
