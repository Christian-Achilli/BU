package com.kp.marsh.ebt.server;

import com.google.inject.AbstractModule;
import com.kp.marsh.ebt.server.webapp.PageConfigurator;
import com.kp.marsh.ebt.server.webapp.ProviderReferenceYears;
import com.kp.marsh.ebt.server.webapp.data.domain.ReferenceYears;
import com.kp.marsh.ebt.server.webapp.data.domain.providers.PageConfiguratorProvider;
import com.kp.marsh.ebt.server.webapp.utils.AnnotatedHibernateUtil;

public class DataBaseConfiguration extends AbstractModule {

    @Override
    protected void configure() {
        bind(AnnotatedHibernateUtil.class).asEagerSingleton();
        bind(ReferenceYears[].class).toProvider(ProviderReferenceYears.class);
        bind(PageConfigurator.class).toProvider(PageConfiguratorProvider.class).asEagerSingleton();
    }

}
