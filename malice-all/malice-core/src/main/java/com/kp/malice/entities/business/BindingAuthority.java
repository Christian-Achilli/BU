package com.kp.malice.entities.business;

import java.util.Date;

public class BindingAuthority {

    private String description;
    private String registrationNumber;
    private Date registrationDate;
    private String brokerRef;

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getBrokerRef() {
        return brokerRef;
    }

    public void setBrokerRef(String brokerRef) {
        this.brokerRef = brokerRef;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}