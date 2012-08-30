package com.kp.malice.shared.proxies;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.kp.malice.entities.business.AgenziaRMA;

@ProxyFor(AgenziaRMA.class)
public interface AgenziaRmaProxy extends ValueProxy {

    String getDescription();

    void setDescription(String d);

}
