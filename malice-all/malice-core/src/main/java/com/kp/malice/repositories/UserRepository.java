package com.kp.malice.repositories;

import org.springframework.security.core.AuthenticationException;

import com.kp.malice.entities.business.MaliceUserAuthenticated;

public interface UserRepository {

    MaliceUserAuthenticated findUtente(String username, String plainPassword) throws AuthenticationException;

}