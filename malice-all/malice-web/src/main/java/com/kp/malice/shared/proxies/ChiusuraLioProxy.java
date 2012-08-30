package com.kp.malice.shared.proxies;

import java.math.BigDecimal;
import java.util.Date;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.kp.malice.entities.business.ChiusuraMensileLio;

@ProxyFor(value = ChiusuraMensileLio.class)
public interface ChiusuraLioProxy extends ValueProxy {

    public Long getId();

    public void setId(Long id);

    String getLabel();

    void setLabel(String s);

    int getTotEstrattiConto();

    void setTotEstrattiConto(int i);

    int getTotTitoli();

    void setTotTitoli(int i);

    BigDecimal getTotPremiEuroCent();

    void setTotPremiEuroCent(BigDecimal i);

    BigDecimal getTotCommissioniEuroCent();

    void setTotCommissioniEuroCent(BigDecimal i);

    Date getDtInvio();

    void setDtInvio(Date i);

    String getStatoChiusuraString();

    void setStatoChiusuraString(String i);

    boolean isNextToBeSent();

    void setNextToBeSent(boolean i);

    boolean isAperta();

    void setAperta(boolean isAperta);

}