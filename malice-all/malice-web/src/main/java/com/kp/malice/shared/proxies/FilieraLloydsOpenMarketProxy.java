package com.kp.malice.shared.proxies;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.kp.malice.entities.business.FilieraLloydsOpenMarket;

@ProxyFor(value = FilieraLloydsOpenMarket.class)
public interface FilieraLloydsOpenMarketProxy extends ValueProxy {
    String getCoverNote();

    void setCoverNote(String s);

    String getBinder();

    void setBinder(String s);

    String getCodiceLB();

    void setCodiceLB(String s);

    String getCodiceDoc();

    void setCodiceDoc(String s);
}
