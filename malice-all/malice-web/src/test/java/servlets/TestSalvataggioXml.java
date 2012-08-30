package servlets;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kp.malice.entities.xml.ImportDataSet;
import com.kp.malice.repositories.AppendiceRepository;
import com.kp.malice.repositories.CertificateRepository;
import com.kp.malice.repositories.DatabaseGatewayLloyds;
import com.kp.malice.repositories.DatabaseRegistroXml;
import com.kp.malice.repositories.FlussoXmlRepository;
import com.kp.malice.server.XmlFromBrokerServlet;
import com.kp.malice.useCases.MaliceSchemaValidator;

public class TestSalvataggioXml {
    private static final int NUM_XML = 5;
    private final Logger logger = Logger.getLogger(TestSalvataggioXml.class);

    private final String encoding = "UTF-8";

    @Test
    public void salva_NUM_XML_Su_DB() throws Exception {
        int n_beforeTest = databaseRegistroXml.countRecords();
        for (int i = 0; i < NUM_XML; i++) {
            HttpServletRequest req = mock(HttpServletRequest.class);
            final MockHttpServletResponse resp = new MockHttpServletResponse();
            final InputStream modifiedXml = generateNewPolicyNumberXml();
            when(req.getInputStream()).thenReturn(createServletInputStream(modifiedXml));
            servletUndertest.service(req, resp);
        }
        int n_afterTest = databaseRegistroXml.countRecords();
        assertEquals(n_afterTest, n_beforeTest + NUM_XML);
    }

    private InputStream generateNewPolicyNumberXml() throws Exception {
        FileInputStream fileInStream = new FileInputStream(input_xml);
        ImportDataSet javaObjs = MaliceSchemaValidator.getJavaObjects(fileInStream);
        NumberFormat nf = new DecimalFormat("00");
        javaObjs.getDocument()
                .getDocumentReferences()
                .setCertificateICNumber(
                        javaObjs.getDocument().getDocumentReferences().getCertificateICNumber()
                                + nf.format(Math.random() * 100) + "_" + nf.format(contatoreCertificati++));
        String modifiedXmlString = "";
        JAXBContext jc;
        try {
            jc = JAXBContext.newInstance(ImportDataSet.class);
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            StringWriter sw = new StringWriter();
            marshaller.marshal(javaObjs, sw);
            modifiedXmlString = sw.toString();
            logger.debug("modifiedXmlString: " + modifiedXmlString);
            System.out.println(unescape(modifiedXmlString));
            InputStream modifiedXml = IOUtils.toInputStream(unescape(modifiedXmlString), encoding);
            return modifiedXml;
        } catch (JAXBException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private final String INPUT_XML_EXAMPLE_FILE_NAME = "src/test/resources/XML_ERROR.xml";
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

    private String unescape(String s) {
        StringBuffer sbuf = new StringBuffer();
        int l = s.length();
        int ch = -1;
        int b, sumb = 0;
        for (int i = 0, more = -1; i < l; i++) {
            /* Get next byte b from URL segment s */
            switch (ch = s.charAt(i)) {
            case '%':
                ch = s.charAt(++i);
                int hb = (Character.isDigit((char) ch) ? ch - '0' : 10 + Character.toLowerCase((char) ch) - 'a') & 0xF;
                ch = s.charAt(++i);
                int lb = (Character.isDigit((char) ch) ? ch - '0' : 10 + Character.toLowerCase((char) ch) - 'a') & 0xF;
                b = (hb << 4) | lb;
                break;
            case '+':
                b = ' ';
                break;
            default:
                b = ch;
            }
            /* Decode byte b as UTF-8, sumb collects incomplete chars */
            if ((b & 0xc0) == 0x80) { // 10xxxxxx (continuation byte)
                sumb = (sumb << 6) | (b & 0x3f); // Add 6 bits to sumb
                if (--more == 0)
                    sbuf.append((char) sumb); // Add char to sbuf
            } else if ((b & 0x80) == 0x00) { // 0xxxxxxx (yields 7 bits)
                sbuf.append((char) b); // Store in sbuf
            } else if ((b & 0xe0) == 0xc0) { // 110xxxxx (yields 5 bits)
                sumb = b & 0x1f;
                more = 1; // Expect 1 more byte
            } else if ((b & 0xf0) == 0xe0) { // 1110xxxx (yields 4 bits)
                sumb = b & 0x0f;
                more = 2; // Expect 2 more bytes
            } else if ((b & 0xf8) == 0xf0) { // 11110xxx (yields 3 bits)
                sumb = b & 0x07;
                more = 3; // Expect 3 more bytes
            } else if ((b & 0xfc) == 0xf8) { // 111110xx (yields 2 bits)
                sumb = b & 0x03;
                more = 4; // Expect 4 more bytes
            } else /*if ((b & 0xfe) == 0xfc)*/{ // 1111110x (yields 1 bit)
                sumb = b & 0x01;
                more = 5; // Expect 5 more bytes
            }
            /* We don't test if the UTF-8 encoding is well-formed */
        }
        return sbuf.toString();
    }

}
