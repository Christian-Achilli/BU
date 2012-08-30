package com.kp.malice.entities.persisted;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.kp.malice.entities.business.ContraentePolizzaLloyds.Gender;

@SuppressWarnings("serial")
@Entity
@Table(name = "PSN")
public class Psn extends RecordIdentifier {

    @Enumerated(EnumType.STRING)
    private Gender gender;
    private boolean foreigner;
    private String fiscalCode;
    private String vatNumber;
    private String companyName;
    private String firstName;
    private String lastName;
    private String jobTitle;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String postCode;
    private String region;
    private String Country;

    public Psn(RecordIdentifier timestamp) {
        super(timestamp);
    }

    public Psn() {

    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public boolean isForeigner() {
        return foreigner;
    }

    public void setForeigner(boolean foreigner) {
        this.foreigner = foreigner;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    /**
        CREATE TABLE AD2K.PSN
        (
          COD_UNTA_OPER_AUN  NUMBER(5)                  NOT NULL,
          COD_PSN            NUMBER(10)                 NOT NULL,
          COD_PRTA_IVA       VARCHAR2(11 BYTE),
          IDC_PCY_PRNL       VARCHAR2(1 BYTE),
          IDC_PCY_CMR        VARCHAR2(1 BYTE),
          IDC_PCY_INDU       VARCHAR2(1 BYTE),
          DAT_NASC_CLI       DATE,
          DAT_ACQZ_PSN       DATE,
          IDC_CLI            VARCHAR2(1 BYTE)           NOT NULL,
          COD_CMU            VARCHAR2(4 BYTE),
          COD_FORM_GIUR      NUMBER(5),
          COD_TIP_ENTE_PBB   VARCHAR2(2 BYTE),
          DEN_RNE_SOC        VARCHAR2(80 BYTE),
          DEN_SGL_RNE_SOC    VARCHAR2(10 BYTE),
          IDC_ESEN_FISC      VARCHAR2(1 BYTE),
          COD_STA_CIV        VARCHAR2(1 BYTE),
          DEN_NOME           VARCHAR2(40 BYTE),
          DEN_COGN           VARCHAR2(40 BYTE),
          COD_FISC           VARCHAR2(16 BYTE),
          IDC_SEX            VARCHAR2(1 BYTE),
          IDC_CPCT_GIUR      VARCHAR2(1 BYTE),
          IDC_PRTR_HCAP      VARCHAR2(1 BYTE),
          COD_UTE_INS        VARCHAR2(8 BYTE)           NOT NULL,
          TMST_INS_RIG       DATE                       NOT NULL,
          COD_UTE_AGR        VARCHAR2(8 BYTE)           NOT NULL,
          TMST_AGR_RIG       DATE                       NOT NULL,
          DEN_CMU_NASC_SNR   VARCHAR2(40 BYTE),
          COD_RAM_SGRU       NUMBER(5),
          COD_SGRU_ATT_UIC   NUMBER(5),
          COD_RAM_ATT_UIC    NUMBER(5),
          DAT_FINE_VDT_PSN   DATE,
          DAT_DCS_PSN        DATE
        );
        */

}
