package com.kp.malice.shared.proxies;

import java.math.BigDecimal;
import java.util.Date;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.kp.malice.entities.business.CertificatoLloyds;

@ProxyFor(value = CertificatoLloyds.class)
public interface CertificatoLloydsProxy extends ValueProxy {

    String getNumero();

    void setNumero(String numero);

    void setDataEmissioneDocumento(Date date);

    Date getDataEmissioneDocumento();

    void setPremioNettoPolizzaEuroCent(BigDecimal premioNettoPolizza);

    BigDecimal getPremioNettoPolizzaEuroCent();

    void setNumberOfInstallments(int numberOfInstallments); //frazionamento

    int getNumberOfInstallments();

    void setAccessoriEuroCent(BigDecimal accessori);

    BigDecimal getAccessoriEuroCent();

    void setTotaleImposteEuroCent(BigDecimal totaleImposte);

    BigDecimal getTotaleImposteEuroCent();

    void setInceptionDate(Date inceptionDate); //propopsta

    Date getInceptionDate();

    void setRischio(String codice);

    String getRischio();

    void setDataProposta(Date date);

    Date getDataProposta();

    void setQuotaLloyds(BigDecimal decimal);

    BigDecimal getQuotaLloyds();

    void setFilieraLloyds(FilieraLloydsProxy filieraLloyds);

    FilieraLloydsProxy getFilieraLloyds();

    void setFilieraLloydsOpenMarket(FilieraLloydsOpenMarketProxy openMarket);

    FilieraLloydsOpenMarketProxy getFilieraLloydsOpenMarket();

    void setCommissioniSulNettoEuroCent(BigDecimal commissioni);

    BigDecimal getCommissioniSulNettoEuroCent();

    void setCommissioniAccessoriEuroCent(BigDecimal commissioniAccessori);

    BigDecimal getCommissioniAccessoriEuroCent();

}
