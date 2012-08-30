package com.kp.malice.repositories;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

import com.google.inject.Inject;
import com.kp.malice.entities.business.MaliceUserAuthenticated;
import com.kp.malice.entities.persisted.HibernateSessionFactoryUtil;
import com.kp.malice.entities.persisted.UntaOperAun;
import com.kp.malice.entities.persisted.authentication.AccountMalice;
import com.kp.malice.factories.FilieraLloydsFactory;

public class UserDbRepository implements UserRepository {

    private static Logger log = Logger.getLogger(UserDbRepository.class);
    private FilieraLloydsFactory filieraLloydsFactory;

    @Inject
    public UserDbRepository(FilieraLloydsFactory filieraLloydsFactory) {
        this.filieraLloydsFactory = filieraLloydsFactory;
    }

    @Override
    public MaliceUserAuthenticated findUtente(String username, String plainPassword) throws AuthenticationException {
        AccountMalice account = controllaUsernameEPasswordSuDB(username, plainPassword);
        MaliceUserAuthenticated maliceUserAuthenticated = creaUtenteMalice(account);
        switch (maliceUserAuthenticated.getRuolo()) {
        case ROLE_AGENTE:
            aggiungiAgenzia(account, maliceUserAuthenticated);
            break;
        case ROLE_SUPERAGENTE:
        case ROLE_LIO:
        case ROLE_MONITOR:
            break;
        }
        return maliceUserAuthenticated;
    }

    private void aggiungiAgenzia(AccountMalice account, MaliceUserAuthenticated maliceUserAuthenticated) {
        try {
            UntaOperAun uoa = account.getListaAccPunVndRuolo().iterator().next().getPuntoVendita().getUntaOperAun();
            maliceUserAuthenticated.setAgenzia(filieraLloydsFactory.getAgenziaRma(uoa));
        } catch (Exception e) {
            log.error(e);
            throw new SessionAuthenticationException("ERRORE NELLA CREAZIONE DELL'UTENTE");
        }
    }

    private MaliceUserAuthenticated creaUtenteMalice(AccountMalice account) {
        MaliceUserAuthenticated maliceUserAuthenticated = new MaliceUserAuthenticated(account);
        maliceUserAuthenticated.setAuthenticated(true);
        return maliceUserAuthenticated;
    }

    private AccountMalice controllaUsernameEPasswordSuDB(String username, String plainPassword) {
        Criteria criteria = HibernateSessionFactoryUtil.getSession().createCriteria(AccountMalice.class);
        criteria.add(Restrictions.eq("username", username));
        AccountMalice account = (AccountMalice) criteria.uniqueResult();
        if (account == null)
            throw new UsernameNotFoundException("User " + username + " not found");
        if (!BCrypt.checkpw(plainPassword, account.getPassword())) {
            log.error("Password errata per utente con username " + username);
            throw new BadCredentialsException("Invalid password for " + username);
        }
        return account;
    }
}
