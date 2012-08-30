package com.kp.marsh.ebt.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class MyGuiceContextListener extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ServletsInjection(), new DataBaseConfiguration(), new ServicesInjection(),
                new ImporterConfiguration(), new AuthenticationConfiguration());
    }

}