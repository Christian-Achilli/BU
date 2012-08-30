package com.kp.malice.entities.persisted.authentication;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.kp.malice.entities.persisted.RecordIdentifier;

@SuppressWarnings("serial")
@Entity
@Table(name = "UTENTE_MALICE")
public class UserMalice extends RecordIdentifier {

    private String nome;
    private String cognome;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumns({ @JoinColumn(name = "utenteId", referencedColumnName = "ID", insertable = true, updatable = true) })
    private Set<AccountMalice> accounts = new HashSet<AccountMalice>(0);

    public UserMalice() {

    }

    public UserMalice(RecordIdentifier timeStamp) {
        super(timeStamp);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public Set<AccountMalice> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<AccountMalice> accounts) {
        this.accounts = accounts;
    }

}
