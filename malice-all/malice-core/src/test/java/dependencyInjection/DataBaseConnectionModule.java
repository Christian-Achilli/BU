package dependencyInjection;

import com.google.inject.AbstractModule;
import com.kp.malice.factories.FilieraLloydsFactory;
import com.kp.malice.repositories.CertificateRepository;
import com.kp.malice.repositories.DatabaseGatewayLloyds;
import com.kp.malice.useCases.CreaNuovoCertificatoLloydsDaXml;

public class DataBaseConnectionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CertificateRepository.class).to(DatabaseGatewayLloyds.class);
        bind(CreaNuovoCertificatoLloydsDaXml.class);
    }
}
