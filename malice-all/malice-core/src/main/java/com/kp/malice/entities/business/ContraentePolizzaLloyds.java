package com.kp.malice.entities.business;

public class ContraentePolizzaLloyds {

    private Long id;
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

    
    public ContraentePolizzaLloyds() {
    	
    }
    
    public boolean isPersonaFisica() {
        return !(gender == Gender.C);
    }

    public String getIdentificativo() {
        if (isPersonaFisica()) {
            return getFirstName() + " " + getLastName();
        } else {
            return getCompanyName();
        }
    }

    public String getIndirizzo() {
        return getAddressLine1() + " " + getAddressLine2();
    }

    public enum Gender {
        M, F, C;

        public static Gender fromString(String s) {
            for (Gender g : values()) {
                if (g.name().equals(s)) {
                    return g;
                }
            }
            return null;
        }

    }

    public Gender getGender() {
        return gender;
    }

    public String getGenderString() {
        return gender.name();
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setGenderString(String s) {
        this.gender = Gender.fromString(s);
    }

    public void setGender(String tipoContraente) {
        gender = Gender.fromString(tipoContraente);
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isForeigner() {
        return foreigner;
    }

    public void setForeigner(boolean foreigner) {
        this.foreigner = foreigner;
    }
}
