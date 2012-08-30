package com.kp.malice.entities.business;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

import com.kp.malice.entities.persisted.authentication.AccountMalice;
import com.kp.malice.entities.persisted.authentication.RuoloApplicativo.TipoRuolo;
import com.kp.malice.entities.persisted.authentication.UserMalice;

@SuppressWarnings("serial")
public class MaliceUserAuthenticated implements Authentication {

    private boolean authenticated;
    private Authentication authentication;
    private Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    private AgenziaRMA agenzia;
    private AccountMalice account;
    private String nomeUtente = "";
    private TipoRuolo ruolo;

    public MaliceUserAuthenticated(AccountMalice account) {
        ruolo = account.getListaAccPunVndRuolo().iterator().next().getRuolo().getTipoRuolo();
        authorities.add(new GrantedAuthorityImpl(ruolo.name()));
        this.account = account;
        UserMalice user = account.getUtente();
        nomeUtente = user.getNome() + " " + user.getCognome();
    }

    public TipoRuolo getRuolo() {
        return ruolo;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return authentication.getCredentials();
    }

    @Override
    public Object getDetails() {
        return authentication.getDetails();
    }

    @Override
    public Object getPrincipal() {
        return authentication.getPrincipal();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        this.authenticated = authenticated;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String toString() { // questa stringa ? visualizzata nel dettaglio sessione del gestore delle sessioni del contenitore (tomcat) 
        StringBuilder sb = new StringBuilder();
        sb.append("Username: " + (String) authentication.getPrincipal());
        //        sb.append(" - Role: " + role.name());
        return sb.toString();
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    public String getNomeUtente() {
        return nomeUtente;
    }

    public AgenziaRMA getAgenzia() {
        return agenzia;
    }

    public void setAgenzia(AgenziaRMA agenzia) {
        this.agenzia = agenzia;
    }

    public AccountMalice getAccount() {
        return account;
    }

}
