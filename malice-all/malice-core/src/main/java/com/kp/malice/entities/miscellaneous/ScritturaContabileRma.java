package com.kp.malice.entities.miscellaneous;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.kp.malice.entities.business.IncassoTitoloLloyds;
import com.kp.malice.entities.business.IncassoTitoloLloyds.TipoOperazione;

public class ScritturaContabileRma {

    private Date tmstInsRig;
    private BigInteger counter = BigInteger.ZERO;
    private String statoIncasso = "";
    private String tipoOperazione = "";
    private String codMzzPagm = "";
    private BigDecimal importiEuroCent = BigDecimal.ZERO;
    private BigDecimal premiEuroCent = BigDecimal.ZERO;
    private BigDecimal provvigioniEuroCent = BigDecimal.ZERO;

    public ScritturaContabileRma() {

    }

    public BigDecimal getAbbuoniEuroCent() {
        return IncassoTitoloLloyds.calcolaAbbuonoEuroCent(TipoOperazione.fromString(tipoOperazione),
                getImportiEuroCent(), getPremiEuroCent());
    }

    public void setAbbuoniEuroCent(BigDecimal bd) {
        // dummy for RF
    }

    public BigInteger getCounter() {
        return counter;
    }

    public void setCounter(BigInteger counter) {
        this.counter = counter;
    }

    public String getStatoIncasso() {
        return statoIncasso;
    }

    public void setStatoIncasso(String statoIncasso) {
        this.statoIncasso = statoIncasso;
    }

    public String getTipoOperazione() {
        return tipoOperazione;
    }

    public void setTipoOperazione(String tipoOperazione) {
        this.tipoOperazione = tipoOperazione;
    }

    public String getCodMzzPagm() {
        return codMzzPagm;
    }

    public void setCodMzzPagm(String codMzzPagm) {
        this.codMzzPagm = codMzzPagm;
    }

    public BigDecimal getImportiEuroCent() {
        return importiEuroCent;
    }

    public void setImportiEuroCent(BigDecimal importiEuroCent) {
        this.importiEuroCent = importiEuroCent;
    }

    public BigDecimal getPremiEuroCent() {
        return premiEuroCent;
    }

    public void setPremiEuroCent(BigDecimal premiEuroCent) {
        this.premiEuroCent = premiEuroCent;
    }

    public BigDecimal getProvvigioniEuroCent() {
        return provvigioniEuroCent;
    }

    public void setProvvigioniEuroCent(BigDecimal provvigioniEuroCent) {
        this.provvigioniEuroCent = provvigioniEuroCent;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codMzzPagm == null) ? 0 : codMzzPagm.hashCode());
        result = prime * result + ((statoIncasso == null) ? 0 : statoIncasso.hashCode());
        result = prime * result + ((tipoOperazione == null) ? 0 : tipoOperazione.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ScritturaContabileRma other = (ScritturaContabileRma) obj;
        if (codMzzPagm == null) {
            if (other.codMzzPagm != null)
                return false;
        } else if (!codMzzPagm.equals(other.codMzzPagm))
            return false;
        if (statoIncasso == null) {
            if (other.statoIncasso != null)
                return false;
        } else if (!statoIncasso.equals(other.statoIncasso))
            return false;
        if (tipoOperazione == null) {
            if (other.tipoOperazione != null)
                return false;
        } else if (!tipoOperazione.equals(other.tipoOperazione))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ScritturaContabileRma [dataRegistrazione=" + tmstInsRig + ", counter=" + counter + ", statoIncasso="
                + statoIncasso + ", tipoOperazione=" + tipoOperazione + ", codMzzPagm=" + codMzzPagm
                + ", importiEuroCent=" + importiEuroCent + ", premiEuroCent=" + premiEuroCent
                + ", provvigioniEuroCent=" + provvigioniEuroCent + "]";
    }

    public Date getTmstInsRig() {
        return tmstInsRig;
    }

    public void setTmstInsRig(Date tmstInsRig) {
        this.tmstInsRig = tmstInsRig;
    }

}
