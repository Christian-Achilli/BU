package com.kp.malice.shared.proxies;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.kp.malice.boundaries.locators.TitoloLocator;
import com.kp.malice.entities.business.TitoloLloyds;

@ProxyFor(value = TitoloLloyds.class, locator = TitoloLocator.class)
public interface NewTitoloProxy extends EntityProxy {

    Long getId();

    Long getVersion();

    int getNumeroRata();

    void setNumeroRata(int numeroRata);

    Date getInceptionDate();

    void setInceptionDate(Date date);

    Date getExpiryDate();

    void setExpiryDate(Date Date);

    String getCodiceSubagente();

    void setCodiceSubagente(String str);

    String getCodiceCig();

    void setCodiceCig(String str);

    BigDecimal getAccessoriEuroCent();

    void setAccessoriEuroCent(BigDecimal bd);

    BigDecimal getCommissionsOnAccessoriEuroCent();

    void setCommissionsOnAccessoriEuroCent(BigDecimal bd);

    BigDecimal getCommissionsOnNetEuroCent();

    void setCommissionsOnNetEuroCent(BigDecimal bd);

    BigDecimal getNetEuroCent();

    void setNetEuroCent(BigDecimal bd);

    BigDecimal getTaxesEuroCent();

    void setTaxesEuroCent(BigDecimal bd);

    BigDecimal getPercentageCommissionsOnAccessori();

    void setPercentageCommissionsOnAccessori(BigDecimal bd);

    BigDecimal getPercentageCommissionsOnNet();

    void setPercentageCommissionsOnNet(BigDecimal bd);

    String getStringStatoTitolo();

    void setStringStatoTitolo(String statoTitoloString);

    CertificatoLloydsProxy getCertificatoLloyds();

    void setCertificatoLloyds(CertificatoLloydsProxy certificato);

    IncassoTitoloProxy getUltimoIncassoCheHaMessoIlTitoloInStatoIncassato();

    void setUltimoIncassoCheHaMessoIlTitoloInStatoIncassato(IncassoTitoloProxy incasso); // serve solo per la compatibilità del proxy: se c'è get ci vuole anche set

    List<IncassoTitoloProxy> getIncassiOrderByDataInserimentoDesc();

    void setIncassiOrderByDataInserimentoDesc(List<IncassoTitoloProxy> lista);// serve solo per la compatibilità del proxy: se c'è get ci vuole anche set

    FunzioniAbilitateProxy getFunzioniAbilitate();

    void setFunzioniAbilitate(FunzioniAbilitateProxy fa); // serve solo per la compatibilità del proxy: se c'è get ci vuole anche set

    Date getDataRegistrazione();
    
    String getProgressivo();
    
    void setProgressivo(String progressivo);
    
    String getNumeroAppendice();
    
    void setNumeroAppendice(String numeroAppendice);
    
    ContraenteProxy getContraente();
    
    void setContraente(ContraenteProxy contraenteProxy);

}