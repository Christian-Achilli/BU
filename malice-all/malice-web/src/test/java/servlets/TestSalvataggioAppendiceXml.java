package servlets;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kp.malice.repositories.AppendiceRepository;
import com.kp.malice.repositories.CertificateRepository;
import com.kp.malice.repositories.DatabaseGatewayLloyds;
import com.kp.malice.repositories.DatabaseRegistroXml;
import com.kp.malice.repositories.FlussoXmlRepository;
import com.kp.malice.server.XmlFromBrokerServlet;

public class TestSalvataggioAppendiceXml {
    private static final int NUM_XML = 1;
    private final Logger logger = Logger.getLogger(TestSalvataggioAppendiceXml.class);

    private final String encoding = "UTF-8";

    @Test
    public void salva_NUM_XML_Su_DB() throws Exception {
        int n = databaseRegistroXml.countRecords();
        for (int i = 0; i < NUM_XML; i++) {
            HttpServletRequest req = mock(HttpServletRequest.class);
            final MockHttpServletResponse resp = new MockHttpServletResponse();
            final InputStream modifiedXml = generateNewPolicyNumberXml();
            when(req.getInputStream()).thenReturn(createServletInputStream(modifiedXml));
            servletUndertest.service(req, resp);
        }
        assertEquals(databaseRegistroXml.countRecords(), (NUM_XML+n));
    }

    private InputStream generateNewPolicyNumberXml() throws Exception {
        FileInputStream fileInStream = new FileInputStream(input_xml);
        return fileInStream;
    }

    private final String INPUT_XML_EXAMPLE_FILE_NAME = "src/test/resources/XML_ERROR_APP.xml";
    private File input_xml;
    private XmlFromBrokerServlet servletUndertest;
    private DatabaseRegistroXml databaseRegistroXml;
    private static long contatoreCertificati = 0;

    class TestModule extends AbstractModule {

        @Override
        protected void configure() {
            bind(XmlFromBrokerServlet.class);
            bind(FlussoXmlRepository.class).to(DatabaseRegistroXml.class);
            bind(CertificateRepository.class).to(DatabaseGatewayLloyds.class);
            bind(AppendiceRepository.class).to(DatabaseGatewayLloyds.class);
        }
    }

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(new TestModule());
        input_xml = new File(INPUT_XML_EXAMPLE_FILE_NAME);
        servletUndertest = injector.getInstance(XmlFromBrokerServlet.class);
        databaseRegistroXml = injector.getInstance(DatabaseRegistroXml.class);
    }

    private ServletInputStream createServletInputStream(final InputStream is) throws Exception {
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return is.read();
            }
        };
    }

}
