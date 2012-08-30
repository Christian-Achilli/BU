package com.kp.marsh.ebt.server;

import com.google.inject.servlet.ServletModule;
import com.kp.marsh.ebt.server.importer.ProcessoreServiceImpl;
import com.kp.marsh.ebt.server.webapp.AuthenticationServiceImpl;
import com.kp.marsh.ebt.server.webapp.CalculateIndicatorInfoServiceImpl;
import com.kp.marsh.ebt.server.webapp.DataServiceImpl;

public class ServletsInjection extends ServletModule {

    @Override
    protected void configureServlets() {
        serve("/expandedbusinesstool/data").with(DataServiceImpl.class);
        serve("/expandedbusinesstool/login").with(AuthenticationServiceImpl.class);
        serve("/expandedbusinesstool/calculate").with(CalculateIndicatorInfoServiceImpl.class);
        serve("/ebt_importerwebinterface/processore").with(ProcessoreServiceImpl.class);
    }

}
