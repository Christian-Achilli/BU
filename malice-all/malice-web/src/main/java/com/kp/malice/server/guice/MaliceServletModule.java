package com.kp.malice.server.guice;

import com.google.inject.servlet.ServletModule;
import com.kp.malice.server.AuthServiceImpl;
import com.kp.malice.server.DownloadServlet;
import com.kp.malice.server.XmlFromBrokerServlet;
import com.kp.malice.server.inject.RequestFactoryInjectingModule;

class MaliceServletModule extends ServletModule {

    @Override
    protected void configureServlets() {

        install(new ServletModule() {

            @Override
            protected void configureServlets() {
                serve("/malice/xml/receiver").with(XmlFromBrokerServlet.class);
            }
        });
        install(new ServletModule() {

            @Override
            protected void configureServlets() {
                serve("/malice/download").with(DownloadServlet.class);
            }
        });
        install(new ServletModule() {

            @Override
            protected void configureServlets() {
                serve("/malice/auth").with(AuthServiceImpl.class);
            }
        });
        install(new RequestFactoryInjectingModule("/gwtRequest"));
    }
    
}
