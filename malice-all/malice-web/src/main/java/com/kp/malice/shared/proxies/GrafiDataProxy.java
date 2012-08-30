package com.kp.malice.shared.proxies;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.kp.malice.entities.business.GrafiData;

@ProxyFor(value = GrafiData.class)
public interface GrafiDataProxy extends ValueProxy {
	String getMontanti();
	String getPremiProvvigioni();
}