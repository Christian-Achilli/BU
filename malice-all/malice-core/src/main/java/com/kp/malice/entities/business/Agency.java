package com.kp.malice.entities.business;

import java.util.Date;

import org.apache.log4j.Logger;

public class Agency implements Comparable<Agency>{
    private static Logger log = Logger.getLogger(Agency.class);
    
    private String address;
    private String age;
    private String city;
    private String companyName;
    private String email;
    private String ibanAccount;
    private Long id;
    private String loydsCode;
    private String maliceCode;
    private String rmaCode;
    private Date ruiAcceptanceDate;
    private String ruiNumber;
    private String telephoneNumber;
    private String zipCode;
    
    public Date getClosureDate() {
        return closureDate;
    }

    public void setClosureDate(Date closureDate) {
        this.closureDate = closureDate;
    }

    public Date getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }

    private Date closureDate;
    private Date openingDate;
    
    public Agency(String age) {
        super();
        this.age = age;
    }

    public Agency(){
        super();
    }

    public String getAddress() {
        return address;
    }

    public String getAge() {
        return age;
    }

    public String getCity() {
        return city;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getEmail() {
        return email;
    }

    public String getIbanAccount() {
        return ibanAccount;
    }

    public Long getId() {
        return id;
    }

    public String getLoydsCode() {
        return loydsCode;
    }

    public String getMaliceCode() {
        return maliceCode;
    }

    public String getRmaCode() {
        return rmaCode;
    }

    public Date getRuiAcceptanceDate() {
        return ruiAcceptanceDate;
    }

    public String getRuiNumber() {
        return ruiNumber;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIbanAccount(String ibanAccount) {
        this.ibanAccount = ibanAccount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLoydsCode(String loydsCode) {
        this.loydsCode = loydsCode;
    }

    public void setMaliceCode(String maliceCode) {
        this.maliceCode = maliceCode;
    }

    public void setRmaCode(String rmaCode) {
        this.rmaCode = rmaCode;
    }

    public void setRuiAcceptanceDate(Date ruiAcceptanceDate) {
        this.ruiAcceptanceDate = ruiAcceptanceDate;
    }

    public void setRuiNumber(String ruiNumber) {
        this.ruiNumber = ruiNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public int compareTo(Agency o) {
        return (int) (id - o.getId());
    }

}
