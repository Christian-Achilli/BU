package com.kp.malice.server.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class MaliceGuiceContextListener extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new MaliceServletModule(), new MaliceApplicationModule());
    }
}