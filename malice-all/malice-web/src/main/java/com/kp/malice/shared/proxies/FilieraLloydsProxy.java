package com.kp.malice.shared.proxies;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.kp.malice.entities.business.FilieraLloyds;

@ProxyFor(FilieraLloyds.class)
public interface FilieraLloydsProxy extends ValueProxy {

    AgenziaRmaProxy getAgenziaRma();

    void setAgenziaRma(AgenziaRmaProxy p);

    BindingAuthorityProxy getBindingAuthority();

    void setBindingAuthority(BindingAuthorityProxy p);

    LloydsBrokerProxy getBroker();

    void setBroker(LloydsBrokerProxy p);

    CoverHolderProxy getCoverHolder();

    void setCoverHolder(CoverHolderProxy p);

    String getReferente();

    void setReferente(String s); //lasciata solo per compatibilità col proxy

}
