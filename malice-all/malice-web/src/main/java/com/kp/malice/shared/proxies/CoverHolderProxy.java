package com.kp.malice.shared.proxies;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.kp.malice.entities.business.LloydsCoverHolder;

@ProxyFor(LloydsCoverHolder.class)
public interface CoverHolderProxy extends ValueProxy {

    String getDescription();

    void setDescription(String d);
}
