package com.kp.malice.shared.proxies;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.kp.malice.entities.persisted.Link;


@ProxyFor(value = Link.class)
public interface LinkProxy extends ValueProxy {

	public String getLabel();

	public void setLabel(String label);

	public String getUrl();

	public void setUrl(String url);

	public String getStringTipo();
	
	public void setStringTipo(String s);
}
