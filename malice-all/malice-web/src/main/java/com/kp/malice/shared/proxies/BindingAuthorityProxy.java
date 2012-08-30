package com.kp.malice.shared.proxies;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.kp.malice.entities.business.BindingAuthority;

@ProxyFor(BindingAuthority.class)
public interface BindingAuthorityProxy extends ValueProxy {

    String getDescription();

    void setDescription(String d);
}
