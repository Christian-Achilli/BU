package com.kp.malice.shared.proxies;

import java.util.Date;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.kp.malice.server.model.Agent;

@ProxyFor(value = Agent.class)
public interface AgentProxy extends ValueProxy {
    Long getId();
	AgencyProxy getAgency();
	String getDirectNumberBox();
    String getEmailBox();
    String getMobileBox();
    String getName();
    String getPassword();
    String getRole();
    Date getRuiAcceptanceDateBox();
    String getRuiNumberBox();
    String getSurname();
    String getUsername();
	
	void setAgency(AgencyProxy str);
	void setName(String str);
	void setPassword(String str);
	void setRole(String str);
	void setSurname(String str);
	void setUsername(String str);
	void setRuiNumberBox(String str);
	void setRuiAcceptanceDateBox(Date date);
	void setMobileBox(String str);
	void setEmailBox(String str);
	void setDirectNumberBox(String str);
}