package com.kp.malice.repositories;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.MutableDateTime;

import com.google.inject.Inject;
import com.kp.malice.MalicePropertyContainer;
import com.kp.malice.entities.business.AgenziaRMA;
import com.kp.malice.entities.business.AppendiceLloyds;
import com.kp.malice.entities.business.CertificatoLloyds;
import com.kp.malice.entities.business.ChiusuraMensileLio;
import com.kp.malice.entities.business.ContraentePolizzaLloyds;
import com.kp.malice.entities.business.IncassoTitoloLloyds;
import com.kp.malice.entities.business.IncassoTitoloLloyds.StatoIncasso;
import com.kp.malice.entities.business.IncassoTitoloLloyds.TipoOperazione;
import com.kp.malice.entities.business.PuntoVenditaRMAPerLloyds;
import com.kp.malice.entities.business.RischioAssicurato;
import com.kp.malice.entities.business.TitoloLloyds;
import com.kp.malice.entities.business.TitoloLloyds.StatoTitolo;
import com.kp.malice.entities.business.WelcomeInfo;
import com.kp.malice.entities.persisted.CompPtf;
import com.kp.malice.entities.persisted.DelegatoIncassoTitolo;
import com.kp.malice.entities.persisted.GarPrst;
import com.kp.malice.entities.persisted.HibernateSessionFactoryUtil;
import com.kp.malice.entities.persisted.Incs;
import com.kp.malice.entities.persisted.Mov;
import com.kp.malice.entities.persisted.Mov.Source;
import com.kp.malice.entities.persisted.Mov.StatoMovimento;
import com.kp.malice.entities.persisted.Mov.TipoMovimento;
import com.kp.malice.entities.persisted.OggAsct;
import com.kp.malice.entities.persisted.Pol;
import com.kp.malice.entities.persisted.Pol.StatoPolizza;
import com.kp.malice.entities.persisted.Pol.TipoCoass;
import com.kp.malice.entities.persisted.Psn;
import com.kp.malice.entities.persisted.PunVnd;
import com.kp.malice.entities.persisted.RapCntn;
import com.kp.malice.entities.persisted.RapCntn.TipoContraenza;
import com.kp.malice.entities.persisted.RecordIdentifier;
import com.kp.malice.entities.persisted.RipDttTtl;
import com.kp.malice.entities.persisted.SezPrst;
import com.kp.malice.entities.persisted.TipoDocumentoAssicurativo;
import com.kp.malice.entities.persisted.TtlCtb;
import com.kp.malice.entities.persisted.VrnPol;
import com.kp.malice.entities.persisted.VrnRipRch;
import com.kp.malice.factories.IncassoLloydsFactory;
import com.kp.malice.factories.TitoloLloydsFactory;

public class DatabaseGatewayLloyds implements AppendiceRepository, CertificateRepository, TitoliRepository,
        IncassiRepository {

    private static final String RISK_CODE_SEPARATOR = "\\.";
    private final DateTime endOfTheWorld = new DateTime(9999, 12, 31, 0, 0, 0, 0);
    private final Logger log = Logger.getLogger(DatabaseGatewayLloyds.class);

    private final TitoloLloydsFactory titoloLloydsFactory;
    private final IncassoLloydsFactory incassoLloydsFactory;

    @Inject
    public DatabaseGatewayLloyds(TitoloLloydsFactory titoloLloydsFactory, IncassoLloydsFactory incassoLloydsFactory) {
        this.titoloLloydsFactory = titoloLloydsFactory;
        this.incassoLloydsFactory = incassoLloydsFactory;
    }

    @Override
    public long addNewAppendiceDaXml(AppendiceLloyds appendicePolizza) throws Exception {
        try {
            RecordIdentifier rid = RecordIdentifier.getInitRecord();
            PunVnd puntoVenditaDB = HibernateSessionFactoryUtil.getPersistedInstance(PunVnd.class, appendicePolizza
                    .getCertificatoLloyds().getPuntoVendita().getId());
            CompPtf compPtf = getLloydsCorpFromDB();
            log.debug("numero polizza: " + appendicePolizza.getCertificatoLloyds().getNumero());
            Pol pol = findPolizzaByNumero(appendicePolizza.getCertificatoLloyds().getNumero());
            Mov lastMov = pol.getMovs().iterator().next();
            int progressivoMovimento = (int) (lastMov.getCodPrgMov() + 1L);
            VrnRipRch vrnRsc = new VrnRipRch(rid, pol, progressivoMovimento, compPtf.getCodCompPtf());
            fillVersioneRischioNoCoass(vrnRsc, appendicePolizza.getCertificatoLloyds());
            vrnRsc.setCodPrgMov(progressivoMovimento);
            VrnPol vrnPol = new VrnPol(rid, pol, progressivoMovimento);
            vrnPol.setCodAppendice(appendicePolizza.getCodiceAppendice());
            fillVersionePolizzaEmissioneMonorata(vrnPol, appendicePolizza.getCertificatoLloyds());
            fillContraente(vrnPol, appendicePolizza.getCertificatoLloyds().getTitoli().get(0).getContraente());
            vrnPol.setPunVnd(puntoVenditaDB);
            vrnPol.setCodPrgMov(progressivoMovimento);
            OggAsct ogg = new OggAsct(rid, vrnPol, 1);
            fillOggAsct(ogg, appendicePolizza.getCertificatoLloyds());
            SezPrst sez = new SezPrst(rid, ogg, 1, 1,
                    decodificaCodiceRischioLloyds(appendicePolizza.getCertificatoLloyds())[0]);
            GarPrst gar = new GarPrst(rid, sez, 1,
                    decodificaCodiceRischioLloyds(appendicePolizza.getCertificatoLloyds())[1]);
            fillGaranzia(gar, appendicePolizza.getCertificatoLloyds());
            Mov mov = new Mov(rid, pol, vrnPol, vrnRsc, progressivoMovimento);
            mov.setSorgente(Source.XML);
            mov.setStatoMovimento(StatoMovimento.LAVORABILE);
            fillMovimentoAppendice(mov, appendicePolizza.getCertificatoLloyds(), progressivoMovimento);
            TtlCtb ttl = new TtlCtb(rid, puntoVenditaDB, mov, Calendar.getInstance().get(Calendar.YEAR),
                    progressivoMovimento);
            fillTitoloMonorataMonorischio(ttl, appendicePolizza.getCertificatoLloyds());
            RipDttTtl dttTtlCoas = new RipDttTtl(rid, ttl, gar, vrnRsc);
            dttTtlCoas.setCodVrnPol(progressivoMovimento);
            dttTtlCoas.setCodVrnPro(1);
            dttTtlCoas.setCodOggAsct(1);
            fillRipartoTitoloCoasMonoRischioNoCoas(dttTtlCoas, ttl);
            HibernateSessionFactoryUtil.beginTransaction();
            persisti(vrnRsc);
            persisti(vrnPol);
            persisti(ogg);
            persisti(sez);
            persisti(gar);
            persisti(mov);
            persisti(ttl);
            persisti(dttTtlCoas);
            HibernateSessionFactoryUtil.commitTransaction();
            return mov.getRecordId();
        } catch (Exception e) {
            log.error("ERRORE NEL SALVATAGGIO DELL'APPENDICE " + appendicePolizza.getCertificatoLloyds().getNumero()
                    + " XML SUL DB", e);
            HibernateSessionFactoryUtil.rollbackTransaction();
            throw e;
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
    }

    private Pol findPolizzaByNumero(String numero) throws Exception {
        Criteria criteria = HibernateSessionFactoryUtil.getSession().createCriteria(Pol.class);
        criteria.add(Restrictions.eq("codPol", numero));
        Pol pol = (Pol) criteria.uniqueResult();
        if (pol != null)
            log.debug("trovate polizza con numero: " + numero);
        else {
            String error = "NON trovata polizza con numero " + numero;
            log.error(error);
            throw new Exception(error);
        }
        return pol;
    }

    @Override
    public long addNewCertificateMonoRischioELavorabileDaXml(CertificatoLloyds polizza) throws Exception {
        try {
            RecordIdentifier rid = RecordIdentifier.getInitRecord();
            PunVnd puntoVenditaDB = HibernateSessionFactoryUtil.getPersistedInstance(PunVnd.class, polizza
                    .getPuntoVendita().getId());
            CompPtf compPtf = getLloydsCorpFromDB();
            Pol pol = new Pol(rid, polizza.getNumero(), compPtf, puntoVenditaDB);
            fillPolizza100x100Lloyds(pol, polizza);
            persisti(pol);
            VrnRipRch vrnRsc = new VrnRipRch(rid, pol, 1, compPtf.getCodCompPtf());
            fillVersioneRischioNoCoass(vrnRsc, polizza);
            VrnPol vrnPol = new VrnPol(rid, pol, 1);
            fillVersionePolizzaEmissioneMonorata(vrnPol, polizza);
            fillContraente(vrnPol, polizza.getTitoli().get(0).getContraente());
            vrnPol.setPunVnd(puntoVenditaDB);
            OggAsct ogg = new OggAsct(rid, vrnPol, 1);
            fillOggAsct(ogg, polizza);
            SezPrst sez = new SezPrst(rid, ogg, 1, 1, decodificaCodiceRischioLloyds(polizza)[0]);
            GarPrst gar = new GarPrst(rid, sez, 1, decodificaCodiceRischioLloyds(polizza)[1]);
            fillGaranzia(gar, polizza);
            Mov mov = new Mov(rid, pol, vrnPol, vrnRsc, 1L);
            mov.setSorgente(Source.XML);
            mov.setStatoMovimento(StatoMovimento.LAVORABILE);
            fillMovimentoEmissioneNuovo(mov, polizza);
            TtlCtb ttl = new TtlCtb(rid, puntoVenditaDB, mov, Calendar.getInstance().get(Calendar.YEAR), 1L);
            fillTitoloMonorataMonorischio(ttl, polizza);
            RipDttTtl dttTtlCoas = new RipDttTtl(rid, ttl, gar, vrnRsc);
            fillRipartoTitoloCoasMonoRischioNoCoas(dttTtlCoas, ttl);
            dttTtlCoas.setCodVrnPol(1);
            dttTtlCoas.setCodVrnPro(1);
            dttTtlCoas.setCodOggAsct(1);
            HibernateSessionFactoryUtil.beginTransaction();
            persisti(vrnRsc);
            persisti(vrnPol);
            persisti(ogg);
            persisti(sez);
            persisti(gar);
            persisti(mov);
            persisti(ttl);
            persisti(dttTtlCoas);
            HibernateSessionFactoryUtil.commitTransaction();
            return mov.getRecordId();
        } catch (Exception e) {
            log.error("ERRORE NEL SALVATAGGIO DEL CERTIFICATO " + polizza.getNumero() + " XML SUL DB", e);
            HibernateSessionFactoryUtil.rollbackTransaction();
            throw e;
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
    }

    private void fillContraente(VrnPol vrnPol, ContraentePolizzaLloyds contraente) throws Exception {
        Psn psn = new Psn(vrnPol);
        psn.setAddressLine1(contraente.getAddressLine1());
        psn.setAddressLine2(contraente.getAddressLine2());
        psn.setCity(contraente.getCity());
        psn.setCountry(contraente.getCountry());
        psn.setGender(contraente.getGender());
        psn.setFiscalCode(contraente.getFiscalCode());
        psn.setVatNumber(contraente.getVatNumber());
        psn.setCompanyName(contraente.getCompanyName());
        psn.setFirstName(contraente.getFirstName());
        psn.setLastName(contraente.getLastName());
        psn.setJobTitle(contraente.getJobTitle());
        psn.setPostCode(contraente.getPostCode());
        psn.setRegion(contraente.getRegion());
        RapCntn rapCntn = new RapCntn(vrnPol);
        rapCntn.setPersona(psn);
        rapCntn.setVrnPol(vrnPol);
        rapCntn.setTipoCntn(TipoContraenza.CONTRAENTE);
        persisti(psn);
        vrnPol.getRapportiContraenza().add(rapCntn);
    }

    private CompPtf getLloydsCorpFromDB() {
        CompPtf lloyds = HibernateSessionFactoryUtil.getPersistedInstance(CompPtf.class,
                MalicePropertyContainer.getLloydsRecordId());
        return lloyds;
    }

    @Override
    public void removeCertificate(CertificatoLloyds polizza) throws Exception {
        // TODO Auto-generated method stub
    }

    private void fillOggAsct(OggAsct ogg, CertificatoLloyds polizza) {
        ogg.setDscOgg("");
        ogg.setCodBne(1L);
        ogg.setDatInclOgg(polizza.getInceptionDate());
        ogg.setDatEsclOgg(endOfTheWorld.toDate());
        ogg.setNumSin(0);
        ogg.setImpTotLqt(BigDecimal.ZERO);
    }

    private void fillRipartoTitoloCoasMonoRischioNoCoas(RipDttTtl dttTtlCoas, TtlCtb titolo) {
        dttTtlCoas.setImpNet(titolo.getImpTotNet());
        dttTtlCoas.setImpAcc(titolo.getImpTotAcc());
        dttTtlCoas.setImpIpt(titolo.getImpTotIpt());
        dttTtlCoas.setImpPvgIncNet(titolo.getImpPvgIncNet());
        dttTtlCoas.setImpPvgIncAcc(titolo.getImpPvgIncAcc());
    }

    private void fillPolizza100x100Lloyds(Pol pol, CertificatoLloyds polizza) {
        pol.setCodTipCoas(TipoCoass.COMP_PTF);
        pol.setCodiceCoverHolder(polizza.getFilieraLloyds().getCoverHolder().getCodiceLloyds());
    }

    private String[] decodificaCodiceRischioLloyds(CertificatoLloyds polizza) {
        return polizza.getRischi().get(0).getCodice().split(RISK_CODE_SEPARATOR);
    }

    private void persisti(Object local) throws Exception {
        HibernateSessionFactoryUtil.getSession().save(local);
    }

    private void fillVersionePolizzaEmissioneMonorata(VrnPol vrnPol, CertificatoLloyds polizza) {
        vrnPol.setCodPrgMov(1L);
        vrnPol.setDatEfftPol(polizza.getInceptionDate());
        vrnPol.setDatScaOrgnPol(polizza.getExpiryDate());
        vrnPol.setBrokerRef(polizza.getFilieraLloyds().getBindingAuthority().getBrokerRef());
        vrnPol.setRegistrationDate(polizza.getFilieraLloyds().getBindingAuthority().getRegistrationDate());
        vrnPol.setRegistrationNumber(polizza.getFilieraLloyds().getBindingAuthority().getRegistrationNumber());
        vrnPol.setDatScaUltRin(null);
        vrnPol.setDatEfftUltRin(null);
        if (!polizza.getTitoli().isEmpty())
            vrnPol.setDatPrssQtzt(polizza.getTitoli().get(0).getDataProssimaQuietanza());
        vrnPol.setCodStaPol(StatoPolizza.IN_VIGORE);
        vrnPol.setCodFrz(1);
        vrnPol.setIdcTaciRin(polizza.isTacitRenewal());
        vrnPol.setImpTotAccAnn(polizza.getAccessoriEuroCent());
        vrnPol.setImpTotIpt(polizza.getTotaleImposteEuroCent());
        vrnPol.setImpTotNetAnn(polizza.getPremioNettoPolizzaEuroCent());
    }

    private void fillGaranzia(GarPrst gar, CertificatoLloyds polizza) {
        RischioAssicurato rischio = polizza.getRischi().get(0);
        String codiceRischioLloyds = rischio.getCodice(); //002
        gar.setCodEstnGar(codiceRischioLloyds);// codice rischio lloyd's -->> ci andrà il codice IASSICUR
        gar.setImpPreAnnAcc(rischio.getImportoPremioAnnuoAccessori());
        gar.setImpPreAnnNet(rischio.getImportoPremioAnnuoNetto());
        gar.setPrctAlqIpt(/*recuperare da tabella rischi*/BigDecimal.ZERO);
        gar.setImpPreAnnIpt(rischio.getImportoPremioAnnuoImposte());
        gar.setPrctPvgIncNet(rischio.getPercentualeProvvigioniIncassoSulNetto());
        gar.setPrctPvgIncAcc(rischio.getPercentualeProvvigioniIncassoSugliAccessori());
    }

    private void fillMovimentoEmissioneNuovo(Mov mov, CertificatoLloyds polizza) {
        mov.setCodTipMov(TipoMovimento.EMISSIONE_NUOVO);
        mov.setCodDocAssv(TipoDocumentoAssicurativo.PO);
        mov.setCodVrnPol(1);
        mov.setCodVrnRipRsc(1);
        mov.setDatEfftMov(polizza.getInceptionDate());
        mov.setDatEmiDoc(polizza.getDataEmissioneDocumento());
        mov.setTxtMov("");
    }

    private void fillMovimentoQuietanza(Mov mov, CertificatoLloyds polizza, int progressivoMovimento) {
        mov.setCodTipMov(TipoMovimento.QUIETANZA);
        mov.setCodDocAssv(TipoDocumentoAssicurativo.QT);
        mov.setCodVrnPol(progressivoMovimento);
        mov.setCodVrnRipRsc(progressivoMovimento);
        mov.setDatEfftMov(polizza.getInceptionDate());
        mov.setDatEmiDoc(polizza.getDataEmissioneDocumento());
        mov.setTxtMov("");
    }

    private void fillMovimentoAppendice(Mov mov, CertificatoLloyds polizza, int progressivoMovimento) {
        mov.setCodTipMov(TipoMovimento.APPENDICE_GENERICA);
        mov.setCodDocAssv(TipoDocumentoAssicurativo.AP);
        mov.setCodVrnPol(progressivoMovimento);
        mov.setCodVrnRipRsc(progressivoMovimento);
        mov.setDatEfftMov(polizza.getInceptionDate());
        mov.setDatEmiDoc(polizza.getDataEmissioneDocumento());
        mov.setTxtMov("");
    }

    private void fillTitoloMonorataMonorischio(TtlCtb ttl, CertificatoLloyds polizza) {
        TitoloLloyds titolo = polizza.getTitoli().get(0);
        ttl.setStatoTitolo(StatoTitolo.DA_INCASSARE);
        ttl.setDatIniCpr(polizza.getInceptionDate());
        ttl.setDatScaCpr(polizza.getExpiryDate());
        ttl.setImpTotNet(titolo.getNetEuroCent());
        ttl.setImpTotAcc(titolo.getAccessoriEuroCent());
        ttl.setImpTotIpt(titolo.getTaxesEuroCent());
        ttl.setImpPvgIncNet(titolo.getCommissionsOnNetEuroCent());
        ttl.setImpPvgIncAcc(titolo.getCommissionsOnAccessoriEuroCent());
        ttl.setPrctPvgIncNet(polizza.getRischi().get(0).getPercentualeProvvigioniIncassoSulNetto());
        ttl.setPrctPvgIncAcc(polizza.getRischi().get(0).getPercentualeProvvigioniIncassoSugliAccessori());
    }

    private void fillVersioneRischioNoCoass(VrnRipRch vrnRip, CertificatoLloyds polizza) {
        vrnRip.setCodPrgMov(1L);
        vrnRip.setPrctQtaRis(new BigDecimal(100L));
        vrnRip.setDatIniVdtCoas(polizza.getInceptionDate());
        vrnRip.setDatFineVdtCoas(endOfTheWorld.toDate());
    }

    @Override
    public TitoloLloyds findTitolo(Long id) {
        TtlCtb ttlCtb = HibernateSessionFactoryUtil.getPersistedInstance(TtlCtb.class, id);
        try {
            TitoloLloyds titolo = hidrateTitolo(ttlCtb);
            return titolo;
        } catch (Exception e) {
            e.printStackTrace();
            final String errMsg = ": ERRORE NELLA IDRATAZIONE DEL TITOLO CON ID: ";
            logMaliceSourceOfException(ttlCtb, e, errMsg);
        }
        return null;
    }

    private TitoloLloyds hidrateTitolo(TtlCtb ttlCtb) throws Exception {
        TitoloLloyds ttl = titoloLloydsFactory.makeTitoloLloyds(ttlCtb);
        return ttl;
    }

    private void logMaliceSourceOfException(RecordIdentifier rid, Exception e, final String errMsg) {
        for (StackTraceElement st : e.getStackTrace()) {
            if (st.getClassName().startsWith("com.kp.malice")) {
                log.error(st.getMethodName() + errMsg + rid.getRecordId(), e);
                break;
            }
        }
    }

    //    public findPolizzaByID(long id)    
    @Override
    public ArrayList<TitoloLloyds> findAllTitoliInPeriodoInizioCopertura(List<Long> idList, Date startDate, Date endDate)
            throws Exception {
        Query q = HibernateSessionFactoryUtil.getSession().getNamedQuery("ttlctb.inPeriodoCopertura");
        q.setParameterList("pvId", idList);
        q.setParameter("startPeriod", startDate);
        q.setParameter("endPeriod", endDate);
        @SuppressWarnings("unchecked")
        List<TtlCtb> titoliPunVnd = q.list();
        ArrayList<TitoloLloyds> titoli = new ArrayList<TitoloLloyds>();
        for (TtlCtb ttlCtb : titoliPunVnd) {
            try {
                titoli.add(hidrateTitolo(ttlCtb));
            } catch (Exception e) {
                e.printStackTrace();
                final String errMsg = ": ERRORE NELLA IDRATAZIONE DEL TITOLO CON ID: ";
                logMaliceSourceOfException(ttlCtb, e, errMsg);
            }
        }
        return titoli;
    }

    @Override
    public void salvaTitoloIncassato(TitoloLloyds titolo) throws Exception {
        try {
            RecordIdentifier timestamp = RecordIdentifier.getInitRecord();
            update(titolo);
            IncassoTitoloLloyds incasso = titolo.getUltimoIncassoCheHaMessoIlTitoloInStatoIncassato();
            linkTitoloToIncasso(titolo, saveIncasso(timestamp, incasso));
        } catch (Exception e) {
            log.error("ERRORE IN SAVATAGGIO TITOLO INCASSATO", e);
            throw e;
        }
    }

    private void eliminaIncassoDaTitolo(TitoloLloyds titolo, long idIncasso) throws Exception {
        dissociaTitoloToIncasso(titolo, idIncasso);
        eliminaIncasso(idIncasso);
    }

    private void dissociaTitoloToIncasso(TitoloLloyds titolo, long idIncasso) {
        Query q = HibernateSessionFactoryUtil.getSession().createQuery(
                "from DelegatoIncassoTitolo where incasso.recordId = " + idIncasso);
        DelegatoIncassoTitolo delegatoIncassoTitolo = (DelegatoIncassoTitolo) q.uniqueResult();
        HibernateSessionFactoryUtil.getSession().delete(delegatoIncassoTitolo);
        TtlCtb ttlCtb = HibernateSessionFactoryUtil.getPersistedInstance(TtlCtb.class, titolo.getId());
        ttlCtb.getDelegatiIncasso().remove(delegatoIncassoTitolo);
        //		Set<DelegatoIncassoTitolo> delegatiIncassoTitoloSet = ttlCtb.getDelegatiIncasso();
        //		for (DelegatoIncassoTitolo delegatoIncassoTitolo : delegatiIncassoTitoloSet) {
        //			if(delegatoIncassoTitolo.getIncasso().getRecordId() == idIncasso){
        //				//                DelegatoIncassoTitolo delegatoIncassoTitoloDaEliminare = HibernateSessionFactoryUtil.getPersistedInstance(DelegatoIncassoTitolo.class, delegatoIncassoTitolo.getRecordId());
        //				HibernateSessionFactoryUtil.getSession().delete(delegatoIncassoTitolo);
        //				//                delegatiIncassoTitoloSet.remove(delegatoIncassoTitolo);
        //				break;
        //			}
        //		}
        HibernateSessionFactoryUtil.getSession().saveOrUpdate(ttlCtb);
    }

    private void eliminaIncasso(long idIncasso) {
        Incs incassoDaEliminare = HibernateSessionFactoryUtil.getPersistedInstance(Incs.class, idIncasso);
        HibernateSessionFactoryUtil.getSession().delete(incassoDaEliminare);
    }

    private void linkTitoloToIncasso(TitoloLloyds titolo, Incs incs) {
        TtlCtb ttlCtb = HibernateSessionFactoryUtil.getPersistedInstance(TtlCtb.class, titolo.getId());
        DelegatoIncassoTitolo delegatoIncasso = new DelegatoIncassoTitolo(ttlCtb, incs, ttlCtb.getPunVnd());
        HibernateSessionFactoryUtil.getSession().save(delegatoIncasso);
        HibernateSessionFactoryUtil.getSession().saveOrUpdate(ttlCtb);
    }

    private Incs saveIncasso(RecordIdentifier timestamp, IncassoTitoloLloyds incasso) {
        Incs incs = new Incs(timestamp);
        incs.setCodMzzPagm(incasso.getMezzoPagamento());
        incs.setDataIncasso(incasso.getDataIncasso());
        incs.setImportoIncasso(incasso.getImportoIncassoEuroCent());
        incs.setStatoIncasso(StatoIncasso.fromString(incasso.getStatoIncasso()));
        incs.setTipoOperazione(incasso.getTipoOperazione());
        HibernateSessionFactoryUtil.getSession().save(incs);
        incasso.setID(incs.getRecordId());
        return incs;
    }

    @Override
    public List<IncassoTitoloLloyds> findMovimentiContabiliByTitoloId(long id) throws Exception {
        TtlCtb titolo = HibernateSessionFactoryUtil.getPersistedInstance(TtlCtb.class, id);
        ArrayList<IncassoTitoloLloyds> incassiLloyds = incassoLloydsFactory.makeMovimentiContabili(titolo);
        return incassiLloyds;
    }

    @Override
    public void salvaTitoloStornato(TitoloLloyds titoloStornato) throws Exception {
        try {
            RecordIdentifier timestamp = RecordIdentifier.getInitRecord();
            update(titoloStornato);
            IncassoTitoloLloyds incassoDaStornare = titoloStornato.getIncassiOrderByDataInserimentoDesc().get(1);
            DateTime dateTimeAtStartOfToday = new DateTime(new DateMidnight());
            DateTime dateTimeAtEndOfToday = new DateTime((new DateMidnight()).plusDays(1));
            Interval intervalOdierno = new Interval(dateTimeAtStartOfToday, dateTimeAtEndOfToday);
            DateTime dataInserimentoIncasso = new DateTime(incassoDaStornare.getDataInserimento());
            if (intervalOdierno.contains(dataInserimentoIncasso)) {
                eliminaIncassoDaTitolo(titoloStornato, incassoDaStornare.getID());
            } else {
                IncassoTitoloLloyds stornoIncasso = titoloStornato.getIncassiOrderByDataInserimentoDesc().get(0);
                linkTitoloToIncasso(titoloStornato, saveIncasso(timestamp, stornoIncasso));
            }
        } catch (Exception e) {
            log.error("ERRORE IN SAVATAGGIO TITOLO STORNATO", e);
            throw e;
        }
    }

    @Override
    public void salvaTitoloRecuperato(TitoloLloyds titoloRecuperato) throws Exception {
        try {
            RecordIdentifier timestamp = RecordIdentifier.getInitRecord();
            update(titoloRecuperato);
            IncassoTitoloLloyds stornoIncassoSospeso = titoloRecuperato.getIncassiOrderByDataInserimentoDesc().get(1);
            linkTitoloToIncasso(titoloRecuperato, saveIncasso(timestamp, stornoIncassoSospeso));
            IncassoTitoloLloyds incassoEffettivo = titoloRecuperato
                    .getUltimoIncassoCheHaMessoIlTitoloInStatoIncassato();
            linkTitoloToIncasso(titoloRecuperato, saveIncasso(timestamp, incassoEffettivo));
        } catch (Exception e) {
            log.error("ERRORE IN SAVATAGGIO TITOLO RECUPERATO", e);
            throw e;
        }
    }

    @Override
    public void update(TitoloLloyds titolo) throws Exception {
        TtlCtb ttlCtb = HibernateSessionFactoryUtil.getPersistedInstance(TtlCtb.class, titolo.getId());
        ttlCtb.aggiornaTimestamp();
        ttlCtb.setCodCig(titolo.getCodiceCig());
        ttlCtb.setCodSubAge(titolo.getCodiceSubagente());
        ttlCtb.setStatoTitolo(titolo.getStatoTitolo());
        ttlCtb.setDatAnn(titolo.getDataAnnulloTitolo());
        ttlCtb.setNotaDiAnnullo(titolo.getNotaAnnulloTitolo());
        ttlCtb.setDatInc(titolo.getDataIncassoMessaInCopertura());
        HibernateSessionFactoryUtil.getSession().saveOrUpdate(ttlCtb);
    }

    @Override
    public List<TitoloLloyds> getTitoliDellaChiusura(ChiusuraMensileLio chrDaInviare) throws Exception {
        Query q = HibernateSessionFactoryUtil.getSession().getNamedQuery("ttlctb.trovaTitoliPerChiusura");
        q.setParameter("chrId", chrDaInviare.getId());
        @SuppressWarnings("unchecked")
        List<TtlCtb> listaTitoli = q.list();
        List<TitoloLloyds> result = new ArrayList<TitoloLloyds>();
        for (TtlCtb ttl : listaTitoli) {
            result.add(titoloLloydsFactory.makeTitoloLloyds(ttl));
        }
        return result;
    }

    @Override
    public WelcomeInfo getInfoTitoliDaIncassare(AgenziaRMA age) throws Exception {
        Query q = HibernateSessionFactoryUtil
                .getSession()
                .createSQLQuery(
                        "select count(*) numTtlDaIncassare, COALESCE(SUM(t.impTotAcc+t.impTotIpt+t.impTotNet),0) totPremiDaIncassareEuroCent"
                                + " from TTL_CTB t where  t.statoTitolo = '" + StatoTitolo.DA_INCASSARE.name()
                                + "' and t.punVnd_ID in (:listaPuntiVendita)")
                .setResultTransformer(Transformers.aliasToBean(WelcomeInfo.class));
        q.setParameterList("listaPuntiVendita", getListaIdPuntiVendita(age));
        WelcomeInfo queryResult = (WelcomeInfo) q.uniqueResult();
        return queryResult;
    }

    private List<Long> getListaIdPuntiVendita(AgenziaRMA age) {
        List<Long> idList = new ArrayList<Long>();
        for (PuntoVenditaRMAPerLloyds puntoVendita : age.getPuntiVendita()) {
            idList.add(puntoVendita.getId());
        }
        return idList;
    }

    @Override
    public WelcomeInfo getInfoTitoliSospesi(AgenziaRMA age) throws Exception {
        Query q = HibernateSessionFactoryUtil
                .getSession()
                .createSQLQuery(
                        "select count(*) numIncassiSospesi, COALESCE(sum(INCASSI.importoIncasso),0) totPremiInSospesoEuroCent "
                                + "from TTL_CTB, DLG_INC, INCASSI " + "where DLG_INC.TTL_ID = TTL_CTB.ID "
                                + "and DLG_INC.INC_ID = INCASSI.ID " + "and INCASSI.ID = (select max(I.ID) "
                                + "from INCASSI I, DLG_INC DI " + "where DI.INC_ID = I.ID "
                                + "and DI.TTL_ID = TTL_CTB.ID) " + "and TTL_CTB.statoTitolo in ('"
                                + StatoTitolo.INCASSATO + "', '" + StatoTitolo.CONSOLIDATO + "')"
                                + "and INCASSI.tipoOperazione = '" + TipoOperazione.INCASSO + "' "
                                + "and INCASSI.statoIncasso = '" + StatoIncasso.SOSPESO + "'"
                                + " and TTL_CTB.punVnd_ID in (:listaPuntiVendita)")
                .setResultTransformer(Transformers.aliasToBean(WelcomeInfo.class));
        q.setParameterList("listaPuntiVendita", getListaIdPuntiVendita(age));
        WelcomeInfo queryResult = (WelcomeInfo) q.uniqueResult();
        return queryResult;
    }

    @Override
    public BigDecimal getPremiIncassatiEuroCentAnnoCorrente(AgenziaRMA age) throws Exception {
        List<PunVnd> puntiVendita = new ArrayList<PunVnd>();
        for (PuntoVenditaRMAPerLloyds puntoVendita : age.getPuntiVendita()) {
            puntiVendita.add(HibernateSessionFactoryUtil.getPersistedInstance(PunVnd.class, puntoVendita.getId()));
        }
        Criteria c = HibernateSessionFactoryUtil.getSession().createCriteria(TtlCtb.class);
        c.add(Restrictions.in("punVnd", puntiVendita));
        int annoCorrente = Calendar.getInstance().get(Calendar.YEAR);
        DateTime thisYear = new DateTime(annoCorrente, 1, 1, 0, 0, 0, 0);
        MutableDateTime nextYear = new MutableDateTime(annoCorrente, 1, 1, 0, 0, 0, 0);
        nextYear.addYears(1);
        c.add(Restrictions.between("datInc", thisYear.toDate(), nextYear.toDate()));
        c.add(Restrictions.in("statoTitolo", new StatoTitolo[] { StatoTitolo.INCASSATO, StatoTitolo.CONSOLIDATO }));
        c.setProjection(Projections.sqlProjection("sum(impTotNet + impTotIpt + impTotAcc) as amntPremi",
                new String[] { "amntPremi" }, new Type[] { org.hibernate.type.StandardBasicTypes.BIG_DECIMAL }));
        return changeNullToZero(c);
    }

    @Override
    public BigDecimal getProvvigioniIncassateEuroCentAnnoCorrente(AgenziaRMA age) throws Exception {
        List<PunVnd> puntiVendita = new ArrayList<PunVnd>();
        for (PuntoVenditaRMAPerLloyds puntoVendita : age.getPuntiVendita()) {
            puntiVendita.add(HibernateSessionFactoryUtil.getPersistedInstance(PunVnd.class, puntoVendita.getId()));
        }
        Criteria c = HibernateSessionFactoryUtil.getSession().createCriteria(TtlCtb.class);
        c.add(Restrictions.in("punVnd", puntiVendita));
        int annoCorrente = Calendar.getInstance().get(Calendar.YEAR);
        DateTime thisYear = new DateTime(annoCorrente, 1, 1, 0, 0, 0, 0);
        MutableDateTime nextYear = new MutableDateTime(annoCorrente, 1, 1, 0, 0, 0, 0);
        nextYear.addYears(1);
        c.add(Restrictions.between("datInc", thisYear.toDate(), nextYear.toDate()));
        c.add(Restrictions.in("statoTitolo", new StatoTitolo[] { StatoTitolo.INCASSATO, StatoTitolo.CONSOLIDATO }));
        c.setProjection(Projections.sqlProjection("sum(impPvgIncAcc + impPvgIncNet) as amntProvvigioni",
                new String[] { "amntProvvigioni" }, new Type[] { org.hibernate.type.StandardBasicTypes.BIG_DECIMAL }));
        return changeNullToZero(c);
    }

    private BigDecimal changeNullToZero(Criteria c) {
        BigDecimal result = BigDecimal.ZERO;
        BigDecimal b = (BigDecimal) c.uniqueResult();
        if (b != null)
            result = b;
        return result;
    }

}
