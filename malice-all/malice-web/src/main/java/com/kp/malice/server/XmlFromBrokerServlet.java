	package com.kp.malice.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.kp.malice.entities.xml.ImportDataSet;
import com.kp.malice.factories.FilieraLloydsFactory;
import com.kp.malice.repositories.FlussoXmlRepository;
import com.kp.malice.useCases.LavoraXmlRegistrati;
import com.kp.malice.useCases.MaliceSchemaValidator;

public class XmlFromBrokerServlet extends HttpServlet {
    private static final String NOME_PARAMETRO_FORM_HTML = "myXml=";
    private static final long serialVersionUID = 1L;
    private final Logger logger = Logger.getLogger(XmlFromBrokerServlet.class);

    private final FlussoXmlRepository xmlRepository;
    private final LavoraXmlRegistrati lavoratoreXml;

    @Inject
    public XmlFromBrokerServlet(FlussoXmlRepository services, FilieraLloydsFactory lloydsObjFact,
            LavoraXmlRegistrati lavoratoreXml) throws Exception {
        this.xmlRepository = services;
        this.lavoratoreXml = lavoratoreXml;
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        salvaXmlRicevuto(request, response);
        lavoraXmlRicevuto();
    }

    private void lavoraXmlRicevuto() {
        lavoratoreXml.lavora();
    }

    private void salvaXmlRicevuto(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("XML RECEIVED FROM: " + request.getRemoteAddr() + ":" + request.getRemotePort()+"\n XML: "+request.getInputStream().toString());
        String receivedStreamString = "";
        String encoding = "";
        ImportDataSet xmlDataSet = null;
        boolean valid = true;
        String validationErrorMessage = "";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy(request.getInputStream(), baos);
            xmlDataSet = MaliceSchemaValidator.getJavaObjects(new ByteArrayInputStream(baos.toByteArray()));
            receivedStreamString = recuperaStringaXml(new ByteArrayInputStream(baos.toByteArray()));
            encoding = MaliceSchemaValidator.getEncoding(new ByteArrayInputStream(baos.toByteArray()));
            try {
                MaliceSchemaValidator.checkIsValidAgainstSchema(new ByteArrayInputStream(baos.toByteArray()));
            } catch (Exception e) {
                doNegativeResponse(response, e);
                validationErrorMessage = e.getMessage();
                valid = false;
                throw e;
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("ERROR RECEIVING XML FROM BROKER", e);
            doNegativeResponse(response, e);
        } catch (Exception e) {
            doNegativeResponse(response, e);
            logger.error("ERROR RECEIVING XML FROM BROKER", e);
        } finally {
            try {
                xmlRepository.add(xmlDataSet, receivedStreamString, encoding, valid, validationErrorMessage);
                doPositiveResponse(response);
            } catch (Exception e) {
                logger.error("ERRORE IN SALVATAGGIO XML", e);
                doNegativeResponse(response, e);
            }
            response.getWriter().flush();
        }
    }

    private String recuperaStringaXml(InputStream request) throws Exception {
        String receivedStreamString = IOUtils.toString(request);
        if (isRicevutaDaFormHtml(receivedStreamString)) {
            receivedStreamString = elaboraRicezioneDaFormHtml(receivedStreamString);
        }
        logger.info("RECEIVED XML IS:\n" + receivedStreamString);
        return receivedStreamString;
    }

    private String elaboraRicezioneDaFormHtml(String receivedStreamString) {
        receivedStreamString = StringUtils.removeStart(receivedStreamString, NOME_PARAMETRO_FORM_HTML);
        receivedStreamString = unescape(receivedStreamString);
        return receivedStreamString;
    }

    private boolean isRicevutaDaFormHtml(String receivedStreamString) {
        return receivedStreamString.startsWith(NOME_PARAMETRO_FORM_HTML);
    }

    private void doPositiveResponse(HttpServletResponse response) {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.print("<h1>XML SUCCESS</h1>");
        logger.info("XML VALID");
    }

    private void doNegativeResponse(HttpServletResponse response, Exception e) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ee) {
            ee.printStackTrace();
        }
        String messagForClient = "<h1>XML FAIL:</h1><br><p>" + e.getMessage() + "</p>";
        logger.info("XML NOT VALID: "+messagForClient);
        out.print(messagForClient);
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
