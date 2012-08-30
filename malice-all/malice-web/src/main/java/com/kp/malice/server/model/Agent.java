package com.kp.malice.server.model;

import java.util.Date;

import org.apache.log4j.Logger;


public class Agent {

    private static Logger log = Logger.getLogger(Agent.class);

    private Agency agency;
    private String directNumberBox;
    private String emailBox;
    private Long id;
    private String mobileBox;
    private String name;
    private String password;
    private String role;
    private Date ruiAcceptanceDateBox;
    private String ruiNumberBox;
    private String surname;
    private String username;
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Agent) {
            return getId() == ((Agent) o).getId();
        }
        return false;
    }

    public Agency getAgency() {
        return agency;
    }

    public String getDirectNumberBox() {
        return directNumberBox;
    }

    public String getEmailBox() {
        return emailBox;
    }

    public Long getId() {
        return id;
    }

    public String getMobileBox() {
        return mobileBox;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public Date getRuiAcceptanceDateBox() {
        return ruiAcceptanceDateBox;
    }

    public String getRuiNumberBox() {
        return ruiNumberBox;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public void setDirectNumberBox(String directNumberBox) {
        this.directNumberBox = directNumberBox;
    }

    public void setEmailBox(String emailBox) {
        this.emailBox = emailBox;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMobileBox(String mobileBox) {
        this.mobileBox = mobileBox;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setRuiAcceptanceDateBox(Date ruiAcceptanceDateBox) {
        this.ruiAcceptanceDateBox = ruiAcceptanceDateBox;
    }

    public void setRuiNumberBox(String ruiNumberBox) {
        this.ruiNumberBox = ruiNumberBox;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
}
