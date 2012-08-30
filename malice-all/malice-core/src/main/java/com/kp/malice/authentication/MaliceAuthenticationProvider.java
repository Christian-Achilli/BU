package com.kp.malice.authentication;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.kp.malice.entities.business.MaliceUserAuthenticated;
import com.kp.malice.entities.persisted.HibernateSessionFactoryUtil;
import com.kp.malice.repositories.UserDbRepository;
import com.kp.malice.repositories.UserRepository;

public class MaliceAuthenticationProvider implements AuthenticationProvider {

    private static final Logger log = Logger.getLogger(MaliceAuthenticationProvider.class);

    public MaliceAuthenticationProvider() {
        // instantiated by spring as defined in applicationcontext.xml
    }

    @Override
    @Inject
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MaliceUserAuthenticated utente = null;
        try {
            UserRepository utenteDao = Guice.createInjector(new AbstractModule() {
                @Override
                protected void configure() {
                    bind(UserRepository.class).to(UserDbRepository.class);
                }
            }).getInstance(UserRepository.class);
            String username = (String) authentication.getPrincipal();
            String password = (String) authentication.getCredentials();
            utente = utenteDao.findUtente(username, password);
            utente.setAuthentication(authentication);
            SecurityContextHolder.getContext().setAuthentication(utente);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            log.error("ERRORE IN AUTENTICAZIONE", e);
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
        return utente;
    }

    @Override
    public boolean supports(Class<? extends Object> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
