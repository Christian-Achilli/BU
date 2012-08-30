package com.kp.malice.factories;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.google.inject.Inject;
import com.kp.malice.entities.business.CertificatoLloyds;
import com.kp.malice.entities.business.CompagniaPortafoglio;
import com.kp.malice.entities.business.DettaglioCoassicurazione;
import com.kp.malice.entities.business.FilieraLloyds;
import com.kp.malice.entities.business.PuntoVenditaRMAPerLloyds;
import com.kp.malice.entities.business.RischioAssicurato;
import com.kp.malice.entities.persisted.Mov;
import com.kp.malice.entities.persisted.Pol;
import com.kp.malice.entities.persisted.RipDttTtl;
import com.kp.malice.entities.persisted.VrnPol;
import com.kp.malice.entities.persisted.VrnRipRch;
import com.kp.malice.entities.xml.CertificateDateTimeDetails;
import com.kp.malice.entities.xml.ImportDataSet;
import com.kp.malice.entities.xml.RiskPremiumDetails;
import com.kp.malice.entities.xml.TotalPremiumDetails;

public class CertificateLloydsFactory {

    private final Logger log = Logger.getLogger(CertificateLloydsFactory.class);

    private final FilieraLloydsFactory filieraLloydsFactory;

    @Inject
    public CertificateLloydsFactory(FilieraLloydsFactory filieraLloydsFactory) {
        this.filieraLloydsFactory = filieraLloydsFactory;
    }

    public int getDocumentType(ImportDataSet xmlInstance) {
        return xmlInstance.getDocument().getDocumentReferences().getICType().getDocumentType();
    }

    public String getCertificateICNumber(ImportDataSet xmlInstance) {
        return xmlInstance.getDocument().getDocumentReferences().getCertificateICNumber();
    }

    public String getICNumber(ImportDataSet xmlInstance) {
        return xmlInstance.getDocument().getDocumentReferences().getICNumber();
    }

    public CertificatoLloyds fillCertificatoPerEmissione(ImportDataSet xmlInstance) throws Exception {
        String numPolizza = getNumeroPolizza(xmlInstance);
        PuntoVenditaRMAPerLloyds puntoVenditaRMAPerLloyds = filieraLloydsFactory.makePuntoVenditaRma(xmlInstance);
        FilieraLloyds filiera = filieraLloydsFactory.getFiliera(xmlInstance);
        log.debug("puntoVenditaRMAPerLloyds.getId(): " + puntoVenditaRMAPerLloyds.getId());
        log.debug("numPolizza: " + numPolizza);
        log.debug("coverHolder.getId(): " + filiera.getCoverHolder().getId());
        log.debug("bindingAuthority: " + filiera.getBindingAuthority().getDescription());
        CertificatoLloyds certificato = instanziaCertificato(puntoVenditaRMAPerLloyds, filiera, numPolizza);
        certificato.setRischi(getRischiAssicurati(xmlInstance));
        setNumberOfInstallments(xmlInstance, certificato);
        setNumberOfInsurers(xmlInstance, certificato);
        setNumberOfRisks(xmlInstance, certificato);
        setPremioPolizza(xmlInstance, certificato);
        setDateTimeDetails(xmlInstance, certificato);
        final XMLGregorianCalendar referenceDate = xmlInstance.getDocument().getLastUpdatedDate();
        DateTime refDate = new DateTime(referenceDate.getYear(), referenceDate.getMonth(), referenceDate.getDay(), 0, 0);
        certificato.setDataEmissioneDocumento(refDate.toDate());
        return certificato;
    }

    private static List<RischioAssicurato> getRischiAssicurati(ImportDataSet xmlInstance) {
        List<RischioAssicurato> listaRischi = new ArrayList<RischioAssicurato>();
        List<RiskPremiumDetails> riskDetails = xmlInstance.getDocument().getRiskPremiumDetails();
        for (RiskPremiumDetails risk : riskDetails) {
            RischioAssicurato rischio = new RischioAssicurato();
            rischio.setCodice(String.valueOf(risk.getRiskReference()));
            rischio.setImportoPremioAnnuoAccessori(risk.getAccessori());
            rischio.setImportoPremioAnnuoImposte(risk.getTax());
            rischio.setImportoPremioAnnuoNetto(risk.getPremium());
            rischio.setPercentualeProvvigioniIncassoSugliAccessori(risk.getAccessoriCommissionPerc());
            rischio.setPercentualeProvvigioniIncassoSulNetto(risk.getPremiumCommissionPerc());
            listaRischi.add(rischio);
        }
        return listaRischi;
    }

    private static void setPremioPolizza(ImportDataSet xmlInstance, CertificatoLloyds certificato) {
        TotalPremiumDetails premiumDetail = xmlInstance.getDocument().getTotalPremiumDetails();
        certificato.setCurrency(premiumDetail.getCurrency());
        certificato.setPremioNettoPolizzaEuroCent(premiumDetail.getPremium().movePointRight(2));
        certificato.setAccessoriEuroCent(premiumDetail.getAccessori().movePointRight(2));
        certificato.setCommissioniAccessoriEuroCent(premiumDetail.getPremiumCommissionAmount().movePointRight(2));
        certificato.setTotaleImposteEuroCent(premiumDetail.getTax().movePointRight(2));
    }

    private static void setNumberOfRisks(ImportDataSet xmlInstance, CertificatoLloyds certificato) {
        certificato.setNumberOfRisks(xmlInstance.getDocument().getRiskPremiumDetails().size());
    }

    private static void setNumberOfInsurers(ImportDataSet xmlInstance, CertificatoLloyds certificato) {
        certificato.setNumberOfInsurers(xmlInstance.getDocument().getCertificateSecurityDetails().getFollowerDetails()
                .size() + 1);
    }

    private static void setNumberOfInstallments(ImportDataSet xmlInstance, CertificatoLloyds certificato) {
        TotalPremiumDetails premiumDetail = xmlInstance.getDocument().getTotalPremiumDetails();
        certificato.setNumberOfInstallments(premiumDetail.getNumberOfInstalments());
    }

    private static void setDateTimeDetails(ImportDataSet xmlInstance, CertificatoLloyds certificato) {
        CertificateDateTimeDetails details = xmlInstance.getDocument().getCertificateDateTimeDetails();
        final XMLGregorianCalendar inceptionDate = details.getInceptionDate();
        DateTime d1 = new DateTime(inceptionDate.getYear(), inceptionDate.getMonth(), inceptionDate.getDay(), 0, 0);
        certificato.setInceptionDate(d1.toDate());
        certificato.setInceptionH24Time(details.getInceptionTime());
        final XMLGregorianCalendar expiryDate = details.getExpiryDate();
        DateTime d2 = new DateTime(expiryDate.getYear(), expiryDate.getMonth(), expiryDate.getDay(), 0, 0);
        certificato.setExpiryDate(d2.toDate());
        certificato.setExpiryH24Time(details.getExpiryTime());
        certificato.setTacitRenewal(details.isTacitRenewal());
    }

    private String getNumeroPolizza(ImportDataSet xmlInstance) {
        return xmlInstance.getDocument().getDocumentReferences().getCertificateICNumber();
    }

    public CertificatoLloyds getCertificatoLloyds(Pol pol) throws Exception {
        FilieraLloyds fil = filieraLloydsFactory.getFiliera(pol);
        PuntoVenditaRMAPerLloyds puntoVendita = filieraLloydsFactory.getPuntoVendita(pol.getPunVnd());
        CertificatoLloyds c = instanziaCertificato(puntoVendita, fil, pol.getCodPol());
        aggiungiRischio(c, pol);
        aggiungiCompagnieAssuntrici(c, pol);
        aggiungiFromMovimento(c, pol);
        aggiungiFromVersionePolizza(c, pol);
        return c;
    }

    private String getCodiceRamoDotGaranzia(Pol pol) {
        Mov mov = pol.getMovs().iterator().next();
        RipDttTtl ripartoDettaglioTitolo = mov.getTtlCtbs().iterator().next().getRipartoTitoloCoass().iterator().next();
        return "" + ripartoDettaglioTitolo.getCodSez() + "." + ripartoDettaglioTitolo.getCodUnt();
    }

    private void aggiungiFromVersionePolizza(CertificatoLloyds c, Pol pol) {
        VrnPol vrnPol = pol.getVrnPols().iterator().next();
        c.setAccessoriEuroCent(vrnPol.getImpTotAccAnn().movePointRight(2));
        c.setInceptionDate(vrnPol.getDatEfftPol());
        c.setExpiryDate(vrnPol.getDatScaOrgnPol());
        c.setNumberOfInstallments(vrnPol.getCodFrz());
        c.setTacitRenewal(vrnPol.isIdcTaciRin());
    }

    private void aggiungiFromMovimento(CertificatoLloyds c, Pol pol) {
        Mov mov = pol.getMovs().iterator().next();
        c.setDataEmissioneDocumento(mov.getDatEmiDoc());
        c.setDataProposta(mov.getDatEfftMov());
    }

    private void aggiungiRischio(CertificatoLloyds c, Pol pol) {
        Mov mov = pol.getMovs().iterator().next();
        int numero_rischi = getNumeroRischiAssicurati(mov);
        c.setRischio(getCodiceRamoDotGaranzia(pol));
        c.setNumberOfRisks(numero_rischi);
    }

    private void aggiungiCompagnieAssuntrici(CertificatoLloyds c, Pol pol) {
        c.setNumberOfInsurers(pol.getVrnRipRchs().size());
        for (VrnRipRch vrnRipRsc : pol.getVrnRipRchs()) {
            DettaglioCoassicurazione dc = new DettaglioCoassicurazione();
            CompagniaPortafoglio compAssuntrice = new CompagniaPortafoglio();
            compAssuntrice.setCodice("" + vrnRipRsc.getCodCompPtfCoas());
            dc.setCompagniaAssuntrice(compAssuntrice);
            dc.setPercentualeAssunzione(vrnRipRsc.getPrctQtaRis());
            c.getCoassicuratori().add(dc);
        }
    }

    private int getNumeroRischiAssicurati(Mov mov) {
        return mov.getTtlCtbs().iterator().next().getRipartoTitoloCoass().size();
    }

    private CertificatoLloyds instanziaCertificato(PuntoVenditaRMAPerLloyds puntoVendita, FilieraLloyds filieraLloyds,
            String numeroPolizza) {
        CertificatoLloyds polizza = new CertificatoLloyds();
        polizza.setPuntoVendita(puntoVendita);
        polizza.setNumero(numeroPolizza);
        polizza.setFilieraLloyds(filieraLloyds);
        return polizza;
    }

}
