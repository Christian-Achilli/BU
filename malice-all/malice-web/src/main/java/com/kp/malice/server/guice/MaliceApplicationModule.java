package com.kp.malice.server.guice;

import com.google.inject.AbstractModule;
import com.kp.malice.boundaries.services.PortaleServiceBoundary;
import com.kp.malice.factories.CertificateLloydsFactory;
import com.kp.malice.factories.ChiusuraMensileLioFactory;
import com.kp.malice.factories.ContraenteLloydsFactory;
import com.kp.malice.factories.FilieraLloydsFactory;
import com.kp.malice.factories.IncassoLloydsFactory;
import com.kp.malice.factories.TitoloLloydsFactory;
import com.kp.malice.repositories.AppendiceRepository;
import com.kp.malice.repositories.CertificateRepository;
import com.kp.malice.repositories.ChiusureMensiliLioRepository;
import com.kp.malice.repositories.DatabaseGatewayLloyds;
import com.kp.malice.repositories.DatabaseRegistroXml;
import com.kp.malice.repositories.FlussoXmlRepository;
import com.kp.malice.repositories.IncassiRepository;
import com.kp.malice.repositories.TitoliRepository;
import com.kp.malice.server.AuthServiceImpl;
import com.kp.malice.server.DownloadServlet;
import com.kp.malice.server.XmlFromBrokerServlet;
import com.kp.malice.useCases.DispatcherServiziPortale;

public class MaliceApplicationModule extends AbstractModule {

    public MaliceApplicationModule() {

    }

    @Override
    protected void configure() {

        //                bind(ChiusureDao.class).to(ChiusureDaoImpl.class).asEagerSingleton();

        //        bind(GrafiDataDao.class).to(GrafiDataDaoImpl.class).asEagerSingleton();

        //        bind(AgentDao.class).to(AgentDaoImpl.class).asEagerSingleton();

        //        bind(AgencyDao.class).to(AgencyDaoImpl.class).asEagerSingleton();

        // nuova conf

        bind(PortaleServiceBoundary.class).to(DispatcherServiziPortale.class);
        bind(TitoliRepository.class).to(DatabaseGatewayLloyds.class);
        bind(IncassiRepository.class).to(DatabaseGatewayLloyds.class);
        bind(CertificateRepository.class).to(DatabaseGatewayLloyds.class);
        bind(AppendiceRepository.class).to(DatabaseGatewayLloyds.class);
        bind(FlussoXmlRepository.class).to(DatabaseRegistroXml.class);
        bind(ChiusureMensiliLioRepository.class).asEagerSingleton();
        bind(DatabaseRegistroXml.class).asEagerSingleton();
        bind(DatabaseGatewayLloyds.class).asEagerSingleton();
        bind(CertificateLloydsFactory.class).asEagerSingleton();
        bind(IncassoLloydsFactory.class).asEagerSingleton();
        bind(TitoloLloydsFactory.class).asEagerSingleton();
        bind(FilieraLloydsFactory.class).asEagerSingleton();
        bind(ContraenteLloydsFactory.class).asEagerSingleton();
        bind(AuthServiceImpl.class).asEagerSingleton();
        bind(DownloadServlet.class).asEagerSingleton();
        bind(XmlFromBrokerServlet.class).asEagerSingleton();
        bind(ChiusuraMensileLioFactory.class).asEagerSingleton();
    }

}
