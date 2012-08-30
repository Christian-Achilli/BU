package com.kp.malice.businessRules;

import com.kp.malice.entities.business.IncassoTitoloLloyds.StatoIncasso;
import com.kp.malice.entities.business.TitoloLloyds.StatoTitolo;
import com.kp.malice.entities.persisted.Mov.Source;
import com.kp.malice.entities.persisted.Mov.StatoMovimento;

public class TitoloSnapshot {
    private StatoMovimento statoMovimento;
    private StatoTitolo statoTitolo;
    private StatoIncasso statoIncasso;
    private PvUser pvUser;
    private Source source;

    public StatoMovimento getStatoMovimento() {
        return statoMovimento;
    }

    public void setStatoMovimento(StatoMovimento statoMovimento) {
        this.statoMovimento = statoMovimento;
    }

    public StatoTitolo getStatoTitolo() {
        return statoTitolo;
    }

    public void setStatoTitolo(StatoTitolo statoTitolo) {
        this.statoTitolo = statoTitolo;
    }

    public StatoIncasso getStatoIncasso() {
        return statoIncasso;
    }

    public void setStatoIncasso(StatoIncasso statoIncasso) {
        this.statoIncasso = statoIncasso;
    }

    public PvUser getPvUser() {
        return pvUser;
    }

    public void setPvUser(PvUser pvUser) {
        this.pvUser = pvUser;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

}
