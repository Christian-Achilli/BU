package com.kp.malice.shared.proxies;

import java.util.Date;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.kp.malice.entities.business.Agency;

@ProxyFor(value = Agency.class)
public interface AgencyProxy extends ValueProxy{
    Long getId();
	String getAddress();
	String getAge();
	String getCity();
	String getCompanyName();
    String getEmail();
    String getIbanAccount();
    String getLoydsCode();
    String getMaliceCode();
    String getRmaCode();
    Date getRuiAcceptanceDate();
    String getRuiNumber();
    String getTelephoneNumber();
    String getZipCode();
    Date getClosureDate();
    Date getOpeningDate();
	
	void setAddress(String str);
	void setAge(String str);
	void setCity(String str);
    void setCompanyName(String str);
    void setEmail(String str);
    void setIbanAccount(String str);
    void setLoydsCode(String str);
    void setMaliceCode(String str);
    void setRmaCode(String str);
    void setRuiAcceptanceDate(Date date);
    void setRuiNumber(String str);
    void setTelephoneNumber(String str);
    void setZipCode(String str);
    void setClosureDate(Date date);
    void setOpeningDate(Date date);
}