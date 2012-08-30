package com.kp.marsh.ebt.server;

import com.google.inject.AbstractModule;
import com.kp.marsh.ebt.client.webapp.AuthenticationService;
import com.kp.marsh.ebt.server.webapp.AuthenticationServiceImpl;
import com.kp.marsh.ebt.server.webapp.IMarshAuthService;
import com.kp.marsh.ebt.server.webapp.MarshAuthClientForTesting;

public class AuthenticationConfiguration extends AbstractModule {

    @Override
    protected void configure() {
        bind(IMarshAuthService.class).to(MarshAuthClientForTesting.class);
        bind(AuthenticationService.class).to(AuthenticationServiceImpl.class);
    }

}
