package com.kp.malice.useCases;

import org.apache.log4j.Logger;
import org.joda.time.DateTimeZone;
import org.quartz.xml.JobSchedulingDataProcessor.TimeZoneConverter;

import com.google.inject.Inject;
import com.kp.malice.entities.business.AppendiceLloyds;
import com.kp.malice.entities.business.CertificatoLloyds;
import com.kp.malice.entities.business.TitoloLloyds;
import com.kp.malice.entities.xml.ImportDataSet;
import com.kp.malice.factories.CertificateLloydsFactory;
import com.kp.malice.factories.TitoloLloydsFactory;

public class CreaNuovoCertificatoLloydsDaXml {
    private final Logger log = Logger.getLogger(CreaNuovoCertificatoLloydsDaXml.class);
    private final CertificateLloydsFactory lloydsFactory;
    private final TitoloLloydsFactory titoloFactory;

    @Inject
    public CreaNuovoCertificatoLloydsDaXml(CertificateLloydsFactory lloydsFactory, TitoloLloydsFactory titoloFactory) {
        this.lloydsFactory = lloydsFactory;
        this.titoloFactory = titoloFactory;
    }

    // Il time zone viene espresso come argomento fino a quando (se) verrà inserito come campo esplicito dell'xml ricevuto
    public CertificatoLloyds getCertificato(ImportDataSet xmlInstance, DateTimeZone xmlTimeZone) throws Exception {
        try {
            CertificatoLloyds certificato = lloydsFactory.fillCertificatoPerEmissione(xmlInstance);
            TitoloLloyds titolo = titoloFactory.getTitoloDaIncassare(xmlInstance);
            certificato.getTitoli().add(titolo);
            return certificato;
        } catch (Exception e) {
            log.error("ERRORE NELLA CREAZIONE DEL CERTIFICATO DA XML", e);
            throw e;
        }
    }

    public AppendiceLloyds getAppendice(ImportDataSet xmlInstance, DateTimeZone xmlTimeZone) throws Exception {
        try {
            AppendiceLloyds appendiceLloyds = new AppendiceLloyds();
            appendiceLloyds.setCertificatoLloyds(getCertificato(xmlInstance, xmlTimeZone));
            appendiceLloyds.setCodiceAppendice(lloydsFactory.getCertificateICNumber(xmlInstance));
            appendiceLloyds.getCertificatoLloyds().setNumero(lloydsFactory.getICNumber(xmlInstance));
            return appendiceLloyds;
        } catch (Exception e) {
            log.error("ERRORE NELLA CREAZIONE DELL'APPENDICE DA XML", e);
            throw e;
        }
    }
}
