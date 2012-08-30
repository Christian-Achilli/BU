package com.kp.malice.entities.persisted.authentication;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;

import com.kp.malice.entities.persisted.RecordIdentifier;

@SuppressWarnings("serial")
@Entity
@Table(name = "ACCOUNT_UTENTE")
public class AccountMalice extends RecordIdentifier {

    @NotNull
    private String username;
    @NotNull
    private String password;
    private String email;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumns({ @JoinColumn(name = "utenteId", referencedColumnName = "ID", insertable = true, updatable = true) })
    @ForeignKey(name = "FK_ACC_UTZ")
    private UserMalice utente;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<AccountPunVndRuolo> listaAccPunVndRuolo = new HashSet<AccountPunVndRuolo>(0);

    public Set<AccountPunVndRuolo> getListaAccPunVndRuolo() {
        return listaAccPunVndRuolo;
    }

    public void setListaAccPunVndRuolo(Set<AccountPunVndRuolo> listaAccPunVndRuolo) {
        this.listaAccPunVndRuolo = listaAccPunVndRuolo;
    }

    private boolean abilitato;

    public AccountMalice() {
    }

    public AccountMalice(RecordIdentifier timeStamp) {
        super(timeStamp);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAbilitato() {
        return abilitato;
    }

    public void setAbilitato(boolean abilitato) {
        this.abilitato = abilitato;
    }

    public UserMalice getUtente() {
        return utente;
    }

    public void setUtente(UserMalice utente) {
        this.utente = utente;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
