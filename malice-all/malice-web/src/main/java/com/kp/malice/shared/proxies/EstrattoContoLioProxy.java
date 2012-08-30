package com.kp.malice.shared.proxies;

import java.math.BigDecimal;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.kp.malice.entities.business.EstrattoContoLio;

@ProxyFor(value = EstrattoContoLio.class)
public interface EstrattoContoLioProxy extends ValueProxy {

    public Long getId();

    public void setId(Long id);

    public String getLabel();

    public void setLabel(String label);

    public int getTotTitoli();

    public void setTotTitoli(int totTitoli);

    public BigDecimal getTotPremiEuroCent();

    public void setTotPremiEuroCent(BigDecimal totPremiEuroCent);

    public BigDecimal getTotCommissioniEuroCent();

    public void setTotCommissioniEuroCent(BigDecimal totCommissioniEuroCent);

    public BigDecimal getTotRimessaEuroCent();

    public void setTotRimessaEuroCent(BigDecimal totRimessaEuroCent);

}
