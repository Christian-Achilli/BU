package com.kp.marsh.ebt.server.webapp.data.domain;

// Generated Feb 1, 2011 12:09:56 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * MarshPeopleCredentials generated by hbm2java
 */
public class MarshPeopleCredentials implements java.io.Serializable {

    private int id;
    private InformationOwners informationOwners;
    private Date created;
    private Integer optlock;
    private String username;
    private String hashPassword;// se vale "manager" vuol dire che � un manager

    public MarshPeopleCredentials() {
    }

    public MarshPeopleCredentials(int id) {
        this.id = id;
    }

    public MarshPeopleCredentials(int id, InformationOwners informationOwners, Date created, Integer optlock,
            String username, String hashPassword) {
        this.id = id;
        this.informationOwners = informationOwners;
        this.created = created;
        this.optlock = optlock;
        this.username = username;
        this.hashPassword = hashPassword;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public InformationOwners getInformationOwners() {
        return this.informationOwners;
    }

    public void setInformationOwners(InformationOwners informationOwners) {
        this.informationOwners = informationOwners;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Integer getOptlock() {
        return this.optlock;
    }

    public void setOptlock(Integer optlock) {
        this.optlock = optlock;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashPassword() {
        return this.hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

}
