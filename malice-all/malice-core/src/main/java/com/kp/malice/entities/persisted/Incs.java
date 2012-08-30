package com.kp.malice.entities.persisted;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.kp.malice.entities.business.IncassoTitoloLloyds.MezzoPagamento;
import com.kp.malice.entities.business.IncassoTitoloLloyds.StatoIncasso;
import com.kp.malice.entities.business.IncassoTitoloLloyds.TipoOperazione;

@SuppressWarnings("serial")
@Entity
@Table(name = "INCASSI")
public class Incs extends RecordIdentifier implements Serializable {

    @NotNull
    private BigDecimal importoIncasso = BigDecimal.ZERO;
    @NotNull
    @Enumerated(EnumType.STRING)
    private StatoIncasso statoIncasso;
    @NotNull
    @Enumerated(EnumType.STRING)
    private MezzoPagamento codMzzPagm;
    @NotNull
    private Date dataIncasso;
    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoOperazione tipoOperazione;

    public Incs() {

    }

    public Incs(RecordIdentifier rid) {
        super(rid);
    }

    public BigDecimal getImportoIncasso() {
        return importoIncasso;
    }

    public void setImportoIncasso(BigDecimal importoIncasso) {
        this.importoIncasso = importoIncasso;
    }

    public StatoIncasso getStatoIncasso() {
        return statoIncasso;
    }

    public void setStatoIncasso(StatoIncasso statoIncasso) {
        this.statoIncasso = statoIncasso;
    }

    public Date getDataIncasso() {
        return dataIncasso;
    }

    public void setDataIncasso(Date dataIncasso) {
        this.dataIncasso = dataIncasso;
    }

    public MezzoPagamento getCodMzzPagm() {
        return codMzzPagm;
    }

    public void setCodMzzPagm(MezzoPagamento codMzzPagm) {
        this.codMzzPagm = codMzzPagm;
    }

    public TipoOperazione getTipoOperazione() {
        return tipoOperazione;
    }

    public void setTipoOperazione(TipoOperazione tipoOperazione) {
        this.tipoOperazione = tipoOperazione;
    }

}
