package com.kp.malice.useCases;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTimeZone;

import com.google.inject.Inject;
import com.kp.malice.entities.business.AppendiceLloyds;
import com.kp.malice.entities.business.CertificatoLloyds;
import com.kp.malice.entities.persisted.HibernateSessionFactoryUtil;
import com.kp.malice.entities.persisted.RegisteredDocument;
import com.kp.malice.entities.xml.ImportDataSet;
import com.kp.malice.repositories.AppendiceRepository;
import com.kp.malice.repositories.CertificateRepository;
import com.kp.malice.repositories.FlussoXmlRepository;

public class LavoraXmlRegistrati {
    public final Logger log = Logger.getLogger(LavoraXmlRegistrati.class);

    private final FlussoXmlRepository xmlRepository;
    private final CreaNuovoCertificatoLloydsDaXml creaCertificatoDaXml;
    private final CertificateRepository certificateReposotory;
    private final AppendiceRepository appendiceRepository;

    @Inject
    public LavoraXmlRegistrati(FlussoXmlRepository xmlRepository, CreaNuovoCertificatoLloydsDaXml creaCertificatoDaXml,
            CertificateRepository salvaNuovoCertificatoLloydsDaXml, AppendiceRepository appendiceRepository) {
        this.xmlRepository = xmlRepository;
        this.creaCertificatoDaXml = creaCertificatoDaXml;
        this.certificateReposotory = salvaNuovoCertificatoLloydsDaXml;
        this.appendiceRepository = appendiceRepository;
    }

    public void lavora() {
        lavoraGliXml(recuperaXmlDaLavorare());
    }

    private void lavoraGliXml(List<RegisteredDocument> registeredDocuments) {
        if (null != registeredDocuments)
            for (RegisteredDocument documentoXml : registeredDocuments) {
                try {
                    salvaDocumentoXmlInCertificatoLloyds(documentoXml);
                } catch (Exception e) {
                    registraEccezioneSuLavorazioneDocumentoXml(documentoXml, e);
                } finally {
                    registraDocumentoInStatoLavorato(documentoXml);
                }
            }
    }

    private Long salvaDocumentoXmlInCertificatoLloyds(RegisteredDocument documentoXml) throws Exception {
        try {
            ImportDataSet importDataSet = convertXmlToJava(documentoXml.getXmlStr(), documentoXml.getEncoding());
            Long movimentoId = null;
            DateTimeZone xmlTimeZone = DateTimeZone.forID("CET");
            if (documentoXml.isAddendum()) {
                if (importDataSet.getDocument().getDocumentReferences().getICType().getDocumentType() == 1) {
                    AppendiceLloyds appendiceLloyds = creaCertificatoDaXml.getAppendice(importDataSet, xmlTimeZone);
                    movimentoId = appendiceRepository.addNewAppendiceDaXml(appendiceLloyds);
                } else
                    log.error("ICType doesn't match value in numCertificato and numAppendice");
            } else {
                CertificatoLloyds certificatoLloyds = creaCertificatoDaXml.getCertificato(importDataSet, xmlTimeZone);
                movimentoId = certificateReposotory.addNewCertificateMonoRischioELavorabileDaXml(certificatoLloyds);
            }
            documentoXml.setMovimentoId(movimentoId);
            documentoXml.setConvertedOk(true);
            return movimentoId;
        } catch (Exception e) {
            HibernateSessionFactoryUtil.rollbackTransaction();
            log.error("ERRORE NEL SALVATATGGIO DEL CERTIFICATO LLOYDS DA XML", e);
            throw e;
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
    }

    private void registraDocumentoInStatoLavorato(RegisteredDocument documentoXml) {
        documentoXml.setWorked(true);
        HibernateSessionFactoryUtil.beginTransaction();
        HibernateSessionFactoryUtil.getSession().saveOrUpdate(documentoXml);
        HibernateSessionFactoryUtil.commitTransaction();
        HibernateSessionFactoryUtil.closeSession();
    }

    private void registraEccezioneSuLavorazioneDocumentoXml(RegisteredDocument documentoXml, Exception e) {
        log.error("ERRORE DURANTE IL SALVATAGGIO DA XML DEL CERTIFICATO  " + documentoXml.getNumCertificato() + "/"
                + documentoXml.getNumAppendice() + ": " + e.getMessage() + " "
                + (e.getCause() != null ? e.getCause().getMessage() : ""));
        documentoXml.setConvertedOk(false);
        documentoXml.setConversionException(e.getMessage() + " "
                + (e.getCause() != null ? e.getCause().getMessage() : ""));
    }

    private ImportDataSet convertXmlToJava(String xmlStr, String encoding) throws Exception {
        ByteArrayInputStream b = null;
        if ("UTF-8".equals(encoding)) {
            b = new ByteArrayInputStream(xmlStr.getBytes());
        } else { // UTF-16
            b = new ByteArrayInputStream(xmlStr.getBytes(), 6, xmlStr.getBytes().length);
        }
        ImportDataSet xmlDataSet = MaliceSchemaValidator.getJavaObjects(b);
        return xmlDataSet;
    }

    private List<RegisteredDocument> recuperaXmlDaLavorare() {
        List<RegisteredDocument> registeredDocuments = null;
        try {
            registeredDocuments = xmlRepository.findAllNotWorked();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return registeredDocuments;
    }
}
