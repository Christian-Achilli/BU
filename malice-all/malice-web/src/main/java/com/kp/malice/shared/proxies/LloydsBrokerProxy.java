package com.kp.malice.shared.proxies;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.kp.malice.entities.business.LloydsBroker;

@ProxyFor(LloydsBroker.class)
public interface LloydsBrokerProxy extends ValueProxy {

    String getDescription();

    void setDescription(String d);
}
