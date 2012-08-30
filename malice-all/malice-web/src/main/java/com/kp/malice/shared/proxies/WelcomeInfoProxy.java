package com.kp.malice.shared.proxies;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.kp.malice.entities.business.WelcomeInfo;

@ProxyFor(value = WelcomeInfo.class)
public interface WelcomeInfoProxy extends ValueProxy {

    public Date getMeseAperto();

    public void setMeseAperto(Date meseAperto);

    public BigInteger getNumTtlDaIncassare();

    public void setNumTtlDaIncassare(BigInteger numTtlDaIncassare);

    public BigDecimal getTotPremiDaIncassareEuroCent();

    public void setTotPremiDaIncassareEuroCent(BigDecimal totPremiDaIncassareEuroCent);

    public BigInteger getNumIncassiSospesi();

    public void setNumIncassiSospesi(BigInteger numIncassiSospesi);

    public BigDecimal getTotPremiInSospesoEuroCent();

    public void setTotPremiInSospesoEuroCent(BigDecimal totPremiInSospesoEuroCent);

    public BigDecimal getTotPremiIncassatiAnnoEuroCent();

    public void setTotPremiIncassatiAnnoEuroCent(BigDecimal totPremiIncassatiAnno);

    public BigDecimal getTotProvvigioniIncassateAnnoEuroCent();

    public void setTotProvvigioniIncassateAnnoEuroCent(BigDecimal totProvvigioniIncassateAnno);

    public List<LinkProxy> getLinks();

    public void setLinks(List<LinkProxy> links);

}
