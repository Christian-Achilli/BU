package jaxb;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kp.malice.repositories.CertificateRepository;
import com.kp.malice.repositories.DatabaseGatewayLloyds;
import com.kp.malice.repositories.DatabaseRegistroXml;
import com.kp.malice.repositories.FlussoXmlRepository;
import com.kp.malice.useCases.CreaNuovoCertificatoLloydsDaXml;
import com.kp.malice.useCases.LavoraXmlRegistrati;

public class TestLavorazioneXml {
    private final Logger logger = Logger.getLogger(TestLavorazioneXml.class);

    @Test
    public void controlla_valori_xml_e_valori_salvati_sul_DB() throws Exception {
        lavoraRegisteredDocuments.lavora();
    }

    private LavoraXmlRegistrati lavoraRegisteredDocuments;

    class TestModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(CreaNuovoCertificatoLloydsDaXml.class);
            bind(LavoraXmlRegistrati.class);
            bind(FlussoXmlRepository.class).to(DatabaseRegistroXml.class);
            bind(CertificateRepository.class).to(DatabaseGatewayLloyds.class);
        }
    }

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(new TestModule());
        lavoraRegisteredDocuments = injector.getInstance(LavoraXmlRegistrati.class);
    }

}
