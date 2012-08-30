package com.kp.malice.shared.proxies;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.kp.malice.entities.business.LioReferenceCode;

@ProxyFor(value = LioReferenceCode.class)
public interface LioReferenceCodeProxy extends ValueProxy {
	String getCodice();

	String getReferente();

	void setCodice(String value);

	void setReferente(String value);
}