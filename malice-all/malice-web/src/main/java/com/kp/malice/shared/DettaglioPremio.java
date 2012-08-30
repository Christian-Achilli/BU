package com.kp.malice.shared;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.kp.malice.shared.proxies.NewTitoloProxy;

public class DettaglioPremio {

    private String agenziaName;

    private BigDecimal percentualePremio;

    private BigDecimal premioNettoEuroCent;

    private BigDecimal tasseEuroCent;

    private BigDecimal premioLordoEuroCent;

    private BigDecimal provvigioniEuroCent;

    private ArrayList<DettaglioPremio> lista;

    public DettaglioPremio() {
    }

    public DettaglioPremio(NewTitoloProxy titoloProxy) {
        lista = new ArrayList<DettaglioPremio>();
        DettaglioPremio dp = new DettaglioPremio();
        dp.agenziaName = titoloProxy.getCertificatoLloyds().getFilieraLloyds().getAgenziaRma().getDescription();
        dp.percentualePremio = titoloProxy.getCertificatoLloyds().getQuotaLloyds();
        dp.premioLordoEuroCent = titoloProxy.getNetEuroCent().add(titoloProxy.getAccessoriEuroCent())
                .add(titoloProxy.getTaxesEuroCent());
        dp.premioNettoEuroCent = titoloProxy.getNetEuroCent();
        dp.provvigioniEuroCent = titoloProxy.getCommissionsOnNetEuroCent().add(
                titoloProxy.getPercentageCommissionsOnAccessori());
        dp.tasseEuroCent = titoloProxy.getTaxesEuroCent();
        lista.add(dp);
    }

    public String getAgenziaName() {
        return agenziaName;
    }

    public void setAgenziaName(String agenziaName) {
        this.agenziaName = agenziaName;
    }

    public BigDecimal getPercentualePremio() {
        return percentualePremio;
    }

    public void setPercentualePremio(BigDecimal percentualePremioEuroCent) {
        this.percentualePremio = percentualePremioEuroCent;
    }

    public ArrayList<DettaglioPremio> getLista() {
        return lista;
    }

    public BigDecimal getPremioNettoEuroCent() {
        return premioNettoEuroCent;
    }

    public void setPremioNettoEuroCent(BigDecimal premioNettoEuroCent) {
        this.premioNettoEuroCent = premioNettoEuroCent;
    }

    public BigDecimal getTasseEuroCent() {
        return tasseEuroCent;
    }

    public void setTasseEuroCent(BigDecimal tasseEuroCent) {
        this.tasseEuroCent = tasseEuroCent;
    }

    public BigDecimal getPremioLordoEuroCent() {
        return premioLordoEuroCent;
    }

    public void setPremioLordoEuroCent(BigDecimal premioLordoEuroCent) {
        this.premioLordoEuroCent = premioLordoEuroCent;
    }

    public BigDecimal getProvvigioniEuroCent() {
        return provvigioniEuroCent;
    }

    public void setProvvigioniEuroCent(BigDecimal provvigioniEuroCent) {
        this.provvigioniEuroCent = provvigioniEuroCent;
    }

    public void setLista(ArrayList<DettaglioPremio> lista) {
        this.lista = lista;
    }

}
