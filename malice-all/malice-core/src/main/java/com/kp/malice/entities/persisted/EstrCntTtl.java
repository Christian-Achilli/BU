package com.kp.malice.entities.persisted;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NaturalId;

@SuppressWarnings("serial")
@Entity
@Table(name = "ESTR_CNT_TTL")
public class EstrCntTtl extends RecordIdentifier {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TTL_ID", referencedColumnName = "ID")
    @NotNull
    @NaturalId
    private TtlCtb titolo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ESTR_ID", referencedColumnName = "ID")
    @NotNull
    @NaturalId
    private EstrContLio estrattoConto;

    public EstrCntTtl() {

    }

    public EstrCntTtl(RecordIdentifier ri, TtlCtb titolo, EstrContLio estrattoConto) {
        super(ri);
        setTitolo(titolo);
        setEstrattoConto(estrattoConto);
    }

    public TtlCtb getTitolo() {
        return titolo;
    }

    public void setTitolo(TtlCtb titolo) {
        this.titolo = titolo;
    }

    public EstrContLio getEstrattoConto() {
        return estrattoConto;
    }

    public void setEstrattoConto(EstrContLio estrattoConto) {
        this.estrattoConto = estrattoConto;
    }

}
