package com.kp.malice.entities.persisted.authentication;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kp.malice.entities.persisted.PunVnd;
import com.kp.malice.entities.persisted.RecordIdentifier;

@SuppressWarnings("serial")
@Entity
@Table(name = "ACC_PUN_VND_ROL")
public class AccountPunVndRuolo extends RecordIdentifier {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumns({ @JoinColumn(name = "accountId", referencedColumnName = "ID", insertable = true, updatable = true) })
    private AccountMalice account;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumns({ @JoinColumn(name = "rolId", referencedColumnName = "ID", insertable = true, updatable = true) })
    private RuoloApplicativo ruolo;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private PunVnd puntoVendita;

    public AccountPunVndRuolo() {
    }

    public AccountPunVndRuolo(RecordIdentifier ri) {
        super(ri);
    }

    public AccountMalice getAccount() {
        return account;
    }

    public void setAccount(AccountMalice account) {
        this.account = account;
    }

    public RuoloApplicativo getRuolo() {
        return ruolo;
    }

    public void setRuolo(RuoloApplicativo ruolo) {
        this.ruolo = ruolo;
    }

    public PunVnd getPuntoVendita() {
        return puntoVendita;
    }

    public void setPuntoVendita(PunVnd puntoVendita) {
        this.puntoVendita = puntoVendita;
    }

}
