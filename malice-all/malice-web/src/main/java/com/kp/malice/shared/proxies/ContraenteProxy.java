package com.kp.malice.shared.proxies;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.kp.malice.entities.business.ContraentePolizzaLloyds;

@ProxyFor(value = ContraentePolizzaLloyds.class)
public interface ContraenteProxy extends ValueProxy {

    void setGenderString(String str);

    String getGenderString();

    void setCompanyName(String companyName);

    String getCompanyName();

    void setFirstName(String firstName);

    String getFirstName();

    void setLastName(String lastName);

    String getLastName();

    void setFiscalCode(String str);

    String getFiscalCode();

    void setAddressLine1(String addressLine1);

    String getAddressLine1();

    void setCity(String str);

    String getCity();

    void setPostCode(String str);

    String getPostCode();

    void setVatNumber(String vatNumber);

    String getVatNumber();

    String getRegion();

    void setRegion(String region);

    String getCountry();

    void setCountry(String country);

}
