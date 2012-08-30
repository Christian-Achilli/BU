package com.kp.malice.factories;

import java.text.DecimalFormat;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import jxl.StringFormulaCell;

import org.hibernate.Hibernate;
import org.joda.time.DateTime;

import com.google.inject.Inject;
import com.kp.malice.businessRules.DroolsKnowledgeBaseUtil;
import com.kp.malice.businessRules.FunzioniAbilitate;
import com.kp.malice.businessRules.PvUser;
import com.kp.malice.businessRules.TitoloSnapshot;
import com.kp.malice.entities.business.IncassoTitoloLloyds;
import com.kp.malice.entities.business.IncassoTitoloLloyds.StatoIncasso;
import com.kp.malice.entities.business.TitoloLloyds;
import com.kp.malice.entities.business.TitoloLloyds.StatoTitolo;
import com.kp.malice.entities.persisted.TtlCtb;
import com.kp.malice.entities.xml.ImportDataSet;
import com.kp.malice.entities.xml.InstalmentDetails;

public class TitoloLloydsFactory {

    private final IncassoLloydsFactory incassoLloydsFactory;
    private final CertificateLloydsFactory certificateFactory;
    private final ContraenteLloydsFactory contraenteFactory;

    @Inject
    public TitoloLloydsFactory(IncassoLloydsFactory incassoLloydsFactory, CertificateLloydsFactory certificateFactory,
            ContraenteLloydsFactory contraenteFactory) {
        this.incassoLloydsFactory = incassoLloydsFactory;
        this.certificateFactory = certificateFactory;
        this.contraenteFactory = contraenteFactory;
    }

    public TitoloLloyds getTitoloDaIncassare(ImportDataSet xmlInstance) {
        TitoloLloyds titolo = new TitoloLloyds();
        InstalmentDetails installmentDetails = xmlInstance.getDocument().getInstalments().get(0);
        titolo.setAccessoriEuroCent(installmentDetails.getAccessori().movePointRight(2));
        if (null != installmentDetails.getPremiumCommissionAmount())
            titolo.setCommissionsOnNetEuroCent(installmentDetails.getPremiumCommissionAmount().movePointRight(2));
        if (null != installmentDetails.getAccessoriCommissionAmount())
            titolo.setCommissionsOnAccessoriEuroCent(installmentDetails.getAccessoriCommissionAmount()
                    .movePointRight(2));
        // mancano le percentuali delle commissioni
        titolo.setNetEuroCent(installmentDetails.getPremium().movePointRight(2));
        titolo.setTaxesEuroCent(installmentDetails.getTax().movePointRight(2));
        final XMLGregorianCalendar referenceDate = installmentDetails.getInstalmentDate();
        DateTime refDate = new DateTime(referenceDate.getYear(), referenceDate.getMonth(), referenceDate.getDay(), 0, 0);
        titolo.setInceptionDate(refDate.toDate());
        titolo.setNumeroRata(installmentDetails.getInstalmentNumber());
        titolo.setStatoTitolo(StatoTitolo.DA_INCASSARE);
        titolo.setContraente(contraenteFactory.transcode(xmlInstance.getDocument().getPolicyholderDetails()));
        return titolo;
    }

    public TitoloLloyds makeTitoloLloyds(TtlCtb ttlCtb) throws Exception {
        TitoloLloyds titolo = new TitoloLloyds();
        titolo.setId(ttlCtb.getRecordId());
        titolo.setVersion(ttlCtb.getVersion());
        titolo.setAccessoriEuroCent(ttlCtb.getImpTotAcc());
        titolo.setNetEuroCent(ttlCtb.getImpTotNet());
        titolo.setCommissionsOnAccessoriEuroCent(ttlCtb.getImpPvgIncAcc());
        titolo.setCommissionsOnNetEuroCent(ttlCtb.getImpPvgIncNet());
        titolo.setStatoTitolo(ttlCtb.getStatoTitolo());
        titolo.setDataIncassoMessaInCopertura(ttlCtb.getDatInc());
        titolo.setTaxesEuroCent(ttlCtb.getImpTotIpt());
        titolo.setDataAnnulloTitolo(ttlCtb.getDatAnn());
        titolo.setNotaAnnulloTitolo(ttlCtb.getNotaDiAnnullo());
        titolo.setCodiceCig(ttlCtb.getCodCig());
        titolo.setCodiceSubagente(ttlCtb.getCodSubAge());
        titolo.setInceptionDate(ttlCtb.getDatIniCpr());
        titolo.setExpiryDate(ttlCtb.getDatScaCpr());
        titolo.setCertificatoLloyds(certificateFactory.getCertificatoLloyds(ttlCtb.getMov().getPol()));
        titolo.setNumeroAppendice(ttlCtb.getMov().getVrnPol().getCodAppendice());
        DecimalFormat df = new DecimalFormat("00");
        titolo.setProgressivo(df.format(ttlCtb.getCodPrgMov()));
        titolo.setContraente(contraenteFactory.hidrateContraente(ttlCtb.getMov().getVrnPol()));
        recuperaMovimentiContabili(ttlCtb, titolo);
        elaboraRegolePerDeterminareLeFunzioniDisponibiliSulTitolo(ttlCtb, titolo);
        return titolo;
    }

    private void recuperaMovimentiContabili(TtlCtb ttlCtb, TitoloLloyds titolo) throws Exception {
        if (!ttlCtb.getIncassi().isEmpty()) {
            List<IncassoTitoloLloyds> incassi = incassoLloydsFactory.makeMovimentiContabili(ttlCtb);
            titolo.setIncassiOrderByDataInserimentoDesc(incassi);
        }
    }

    public void elaboraRegolePerDeterminareLeFunzioniDisponibiliSulTitolo(TtlCtb ttlCtb, TitoloLloyds titolo) {
        TitoloSnapshot ts = new TitoloSnapshot();
        ts.setSource(ttlCtb.getMov().getSorgente());
        ts.setStatoTitolo(ttlCtb.getStatoTitolo());
        ts.setStatoMovimento(ttlCtb.getMov().getStatoMovimento());
        ts.setPvUser(PvUser.PV_OWNER);
        if ((StatoTitolo.INCASSATO == ttlCtb.getStatoTitolo() || StatoTitolo.CONSOLIDATO == ttlCtb.getStatoTitolo())
                && ttlCtb.getIncassi().size() > 0)
            ts.setStatoIncasso(ttlCtb.getIncassi().get(0).getStatoIncasso());
        else
            // workaround per evitare NPE
            ts.setStatoIncasso(StatoIncasso.EFFETTIVO);
        FunzioniAbilitate fa = DroolsKnowledgeBaseUtil.getFunzioniDisponibiliPerTitolo(ts);
        titolo.setFunzioniAbilitate(fa);
    }

}
