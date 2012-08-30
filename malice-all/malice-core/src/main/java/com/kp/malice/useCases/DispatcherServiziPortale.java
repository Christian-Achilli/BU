package com.kp.malice.useCases;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.joda.time.DateTime;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.inject.Inject;
import com.kp.malice.boundaries.services.PortaleServiceBoundary;
import com.kp.malice.entities.business.AgenziaRMA;
import com.kp.malice.entities.business.ChiusuraMensileLio;
import com.kp.malice.entities.business.EstrattoContoLio;
import com.kp.malice.entities.business.GrafiData;
import com.kp.malice.entities.business.IncassoTitoloLloyds.MezzoPagamento;
import com.kp.malice.entities.business.LioReferenceCode;
import com.kp.malice.entities.business.MaliceUserAuthenticated;
import com.kp.malice.entities.business.PuntoVenditaRMAPerLloyds;
import com.kp.malice.entities.business.TitoloLloyds;
import com.kp.malice.entities.business.WelcomeInfo;
import com.kp.malice.entities.miscellaneous.DettaglioIncassoTitoloLloyds;
import com.kp.malice.entities.miscellaneous.ScritturaContabileRma;
import com.kp.malice.entities.persisted.HibernateSessionFactoryUtil;
import com.kp.malice.entities.persisted.Link;
import com.kp.malice.factories.FilieraLloydsFactory;
import com.kp.malice.repositories.ChiusureMensiliLioRepository;
import com.kp.malice.repositories.TitoliRepository;

public class DispatcherServiziPortale implements PortaleServiceBoundary {

    Logger log = Logger.getLogger(DispatcherServiziPortale.class);
    private final FunzioniTitolo funzioniTitolo;
    private final FunzioniChiusura funzioniChiusura;
    private final FunzioniStatistiche funzioniStatistiche;
    private final TitoliRepository titoliRepository;
    private final ChiusureMensiliLioRepository chrRepository;
    private final RecuperaScrittureContabiliRma recuperaScrittureContabili;
    private final FilieraLloydsFactory filieraLloydsFactory;

    @Inject
    public DispatcherServiziPortale(FunzioniTitolo funzioniTitolo, FunzioniChiusura funzioniChiusura,
            TitoliRepository titoliRepository, RecuperaScrittureContabiliRma recuperaScrittureContabili,
            ChiusureMensiliLioRepository chrRepository, FunzioniStatistiche funzioniStatistiche,
            FilieraLloydsFactory filieraLloydsFactory) {
        this.funzioniTitolo = funzioniTitolo;
        this.funzioniChiusura = funzioniChiusura;
        this.titoliRepository = titoliRepository;
        this.recuperaScrittureContabili = recuperaScrittureContabili;
        this.chrRepository = chrRepository;
        this.funzioniStatistiche = funzioniStatistiche;
        this.filieraLloydsFactory = filieraLloydsFactory;
    }

    @Override
    public TitoloLloyds findTitolo(long id) throws Exception {
        log.debug(".findTitolo: id=" + id);
        try {
            return titoliRepository.findTitolo(id);
        } catch (Exception e) {
            log.error("ERRORE IN FIND TITOLO", e);
            throw e;
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
    }

    @Override
    public void aggiornaDatiTitolo(TitoloLloyds titolo) throws Exception {
        log.debug(".persist: codice subagente del titolo=" + titolo.getCodiceSubagente());
        try {
            HibernateSessionFactoryUtil.beginTransaction();
            titoliRepository.update(titolo);
            HibernateSessionFactoryUtil.commitTransaction();
            log.info("AGGIORNATO TITOLO id: " + titolo.getId());
        } catch (Exception e) {
            HibernateSessionFactoryUtil.rollbackTransaction();
            log.error("ERRORE IN PERSIST TITOLO", e);
            throw e;
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
    }

    @Override
    public List<TitoloLloyds> findAllTitoliInPeriodoInizioCopertura(Date startDate, Date endDate) throws Exception {
        try {
            log.debug(".findAllTitoliInPeriodoInizioCopertura: da=" + startDate + " a=" + endDate);
            List<TitoloLloyds> titoli = new ArrayList<TitoloLloyds>();
            SecurityContext ctx = SecurityContextHolder.getContext();
            List<PuntoVenditaRMAPerLloyds> puntiVendita = ((MaliceUserAuthenticated) ctx.getAuthentication())
                    .getAgenzia().getPuntiVendita();
            log.debug("trovati " + puntiVendita.size() + " PUNTI VENDITA");
            List<Long> idList = new ArrayList<Long>(2);
            for (PuntoVenditaRMAPerLloyds pv : puntiVendita) {
                idList.add(pv.getId());
            }
            titoli = titoliRepository.findAllTitoliInPeriodoInizioCopertura(idList, startDate, endDate);
            log.info("Ttrovati " + titoli.size() + "     TITOLI per " + idList.size() + " PUNTI VENDITA");
            return titoli;
        } catch (Exception e) {
            log.error("ERRORE IN findAllTitoliInPeriodoInizioCopertura", e);
            throw e;
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
    }

    @Override
    public void annulloTitolo(TitoloLloyds titolo, Date dataDiAnnullo, String notaDiAnnullo) throws Exception {
        log.debug(".annulloTitolo: id=" + titolo.getId());
        try {
            HibernateSessionFactoryUtil.beginTransaction();
            funzioniTitolo.annulla(titolo, dataDiAnnullo, notaDiAnnullo);
            HibernateSessionFactoryUtil.commitTransaction();
            log.info("ANNULLATO titolo id: " + titolo.getId());
        } catch (Exception e) {
            HibernateSessionFactoryUtil.rollbackTransaction();
            log.error("ERRORE IN ANNULLO TITOLO", e);
            throw e;
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
    }

    @Override
    public void revocaAnnulloTitolo(TitoloLloyds titolo) throws Exception {
        log.debug(".revocaAnnulloTitolo: id=" + titolo.getId());
        try {
            HibernateSessionFactoryUtil.beginTransaction();
            funzioniTitolo.revocaAnnullo(titolo);
            HibernateSessionFactoryUtil.commitTransaction();
            log.info("REVOCATO ANNULLO del titolo id: " + titolo.getId());
        } catch (Exception e) {
            HibernateSessionFactoryUtil.rollbackTransaction();
            log.error("ERRORE IN REVOCA ANNULLO TITOLO", e);
            throw e;
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
    }

    @Override
    public void incassaTitolo(TitoloLloyds titolo, DettaglioIncassoTitoloLloyds incasso) throws Exception {
        log.debug(".incassaTitolo: id=" + titolo.getId());
        try {
            HibernateSessionFactoryUtil.beginTransaction();
            funzioniTitolo.incassa(titolo, incasso);
            funzioniChiusura.creaChiusuraSeNonEsisteNelPeriodoCercatoONelPrimoPeriodoSuccessivoDisponibile(
                    getAgenziaUtenteLoggato(), titolo.getDataIncassoMessaInCopertura());
            funzioniChiusura.aggiungiTitoloAChiusuraAperta(titolo);
            HibernateSessionFactoryUtil.commitTransaction();
            log.info("INCASSA TITOLO titolo id: " + titolo.getId());
        } catch (Exception e) {
            HibernateSessionFactoryUtil.rollbackTransaction();
            log.error("ERRORE IN INCASSO TITOLO", e);
            throw e;
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
    }

    @Override
    public void stornaIncassoTitolo(TitoloLloyds titolo) throws Exception {
        log.debug(".stornaIncassoTitolo: id=" + titolo.getId());
        try {
            HibernateSessionFactoryUtil.beginTransaction();
            funzioniTitolo.storna(titolo);
            funzioniChiusura.rimuoviTitoloDaEstrattoConto(titolo);
            HibernateSessionFactoryUtil.commitTransaction();
            log.info("STORNATO INCASSO titolo id: " + titolo.getId());
        } catch (Exception e) {
            HibernateSessionFactoryUtil.rollbackTransaction();
            log.error("ERRORE IN STORNO TITOLO", e);
            throw e;
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
    }

    @Override
    public void recuperaTitoloSospeso(TitoloLloyds titolo, BigDecimal sommaRecuperataEuroCent, Date dataIncasso,
            String mezzoPagamentoString) throws Exception {
        log.debug(".recuperaTitoloSospeso: id=" + titolo.getId());
        try {
            HibernateSessionFactoryUtil.beginTransaction();
            funzioniTitolo.recupera(titolo, sommaRecuperataEuroCent, dataIncasso,
                    MezzoPagamento.fromString(mezzoPagamentoString));
            HibernateSessionFactoryUtil.commitTransaction();
            log.info("RECUPERATO TITOLO SOSPESO id: " + titolo.getId());
        } catch (Exception e) {
            HibernateSessionFactoryUtil.rollbackTransaction();
            log.error("ERRORE IN RECUPERO TITOLO SOSPESO", e);
            throw e;
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
    }

    @Override
    public List<ScritturaContabileRma> findListByDataRegistrazioneIncasso(Date d) throws Exception {
        try {
            List<ScritturaContabileRma> result = recuperaScrittureContabili.getListaScrittureByDataRegistrazione(d);
            log.info("trovate " + result.size() + " SCRITTURE CONTABILI per data: " + d);
            return result;
        } catch (Exception e) {
            log.error("ERRORE IN RECUPERO SCRITTURE CONTABILI PER DATA DI REGISTRAZIONE", e);
            throw e;
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
    }

    @Override
    public List<DettaglioIncassoTitoloLloyds> findListIncassiAggregati(ScritturaContabileRma scritturaContabileRma)
            throws Exception {
        try {
            log.debug(".findListIncassiAggregati: tipo operazione=" + scritturaContabileRma.getTipoOperazione());
            List<DettaglioIncassoTitoloLloyds> dettaglioIncassiList = recuperaScrittureContabili
                    .getListaDettaglioIncassiByScritturaContabile(scritturaContabileRma);
            log.info("trovati " + dettaglioIncassiList.size() + " INCASSI AGGREGATI per scritturaContabileRma: "
                    + scritturaContabileRma.getTipoOperazione() + "/" + scritturaContabileRma.getStatoIncasso() + "/"
                    + scritturaContabileRma.getCodMzzPagm());
            return dettaglioIncassiList;
        } catch (Exception e) {
            log.error("ERRORE IN FINDLISTINCASSIAGGREGATI", e);
            throw e;
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
    }

    @Override
    public List<ChiusuraMensileLio> getListaChiusure() throws Exception {
        log.debug(".getListaChiusure");
        try {
            AgenziaRMA age = getAgenziaUtenteLoggato();
            List<ChiusuraMensileLio> chiusuraMensileLios = chrRepository.getListaChiusure(age);
            log.info("trovate " + chiusuraMensileLios.size() + " CHIUSURE");
            return chiusuraMensileLios;
        } catch (Exception e) {
            log.error("ERRORE IN RECUPERO CHIUSURE", e);
            throw e;
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
    }

    @Override
    public List<EstrattoContoLio> getListaEstrattiConto(ChiusuraMensileLio c) throws Exception {
        log.debug(".getListaEstrattiConto: id chiusura mensile=" + c.getId());
        try {
            List<EstrattoContoLio> estrattoContoLios = chrRepository.getListaEstrattiConto(c);
            log.info("trovati " + estrattoContoLios.size() + " ESTRATTI CONTO per chiusuraMensileLio id: " + c.getId());
            return estrattoContoLios;
        } catch (Exception e) {
            log.error("ERRORE IN RECUPERO LISTA ESTRATTI CONTO", e);
            throw e;
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
    }

    @Override
    public List<TitoloLloyds> getListaTitoliInEstrattoConto(EstrattoContoLio ec) throws Exception {
        try {
            List<TitoloLloyds> titoloLloyds = chrRepository.getListaTitoliPerEstrattoConto(ec);
            log.info("trovati " + titoloLloyds.size() + " TITOLI IN ESTRATTO CONTO per EstrattoContoLio id: "
                    + ec.getId());
            return titoloLloyds;
        } catch (Exception e) {
            log.error("ERRORE IN RECUPERO LISTA TITOLI PER ESTRATTO CONTO", e);
            throw e;
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
    }

    @Override
    public void inviaChiusura(String nota) throws Exception {
        try {
            HibernateSessionFactoryUtil.beginTransaction();
            AgenziaRMA age = getAgenziaUtenteLoggato();
            ChiusuraMensileLio chrDaInviare = chrRepository.getNextToBeSent(age);
            chrDaInviare.setNota(nota);
            funzioniTitolo.consolidaTitoli(chrDaInviare);
            funzioniChiusura.inviaChiusura(chrDaInviare, age);
            funzioniChiusura.creaProssimaChiusuraSeNonEsiste(age, chrDaInviare);
            HibernateSessionFactoryUtil.commitTransaction();
            log.info("inviata CHIUSURA id: " + chrDaInviare.getId());
        } catch (Exception e) {
            HibernateSessionFactoryUtil.rollbackTransaction();
            log.error("ERRORE IN INVIO CHIUSURA", e);
            throw e;
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
    }

    private AgenziaRMA getAgenziaUtenteLoggato() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        AgenziaRMA age = ((MaliceUserAuthenticated) ctx.getAuthentication()).getAgenzia();
        return age;
    }

    @Override
    public WelcomeInfo getWelcomeInfo() throws Exception {
        AgenziaRMA age = getAgenziaUtenteLoggato();
        WelcomeInfo result = new WelcomeInfo();
        WelcomeInfo temp = new WelcomeInfo();
        ChiusuraMensileLio chr = chrRepository.getNextToBeSent(age);
        result.setMeseAperto(new DateTime(chr.getReferenceYear(), chr.getReferenceCalendarMonth(), 1, 0, 0, 0).toDate());
        temp = titoliRepository.getInfoTitoliDaIncassare(age);
        result.setNumTtlDaIncassare(temp.getNumTtlDaIncassare());
        result.setTotPremiDaIncassareEuroCent(temp.getTotPremiDaIncassareEuroCent());
        temp = titoliRepository.getInfoTitoliSospesi(age);
        result.setNumIncassiSospesi(temp.getNumIncassiSospesi());
        result.setTotPremiInSospesoEuroCent(temp.getTotPremiInSospesoEuroCent());
        result.setTotPremiIncassatiAnnoEuroCent(titoliRepository.getPremiIncassatiEuroCentAnnoCorrente(age));
        result.setTotProvvigioniIncassateAnnoEuroCent(titoliRepository.getProvvigioniIncassateEuroCentAnnoCorrente(age));
        result.setLinks(getLinks());
        return result;
    }

    private List<Link> getLinks() throws Exception {
        List<Link> links = new ArrayList<Link>();
        try {
            HibernateSessionFactoryUtil.beginTransaction();
            Criteria criteria = HibernateSessionFactoryUtil.getSession().createCriteria(Link.class);
            links = criteria.list();
            HibernateSessionFactoryUtil.commitTransaction();
            log.debug("trovati " + links.size() + " link");
        } catch (Exception e) {
            HibernateSessionFactoryUtil.rollbackTransaction();
            log.error("ERRORE IN RECUPERO LINK", e);
            throw e;
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
        return links;
    }

    @Override
    public GrafiData calculateDataGrafi(Date start, Date end, String broker) throws Exception {
        log.debug("DiapstcehrServiziPortale.calculateDataGrafi: start date " + start);
        AgenziaRMA age = getAgenziaUtenteLoggato();
        try {
            return funzioniStatistiche.calculateDataGrafi(age, start, end, broker);
        } catch (Exception e) {
            HibernateSessionFactoryUtil.rollbackTransaction();
            log.error("ERRORE IN CALCOLA DATI PER GRAFI", e);
            throw e;
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
    }

    @Override
    public List<LioReferenceCode> getLioReferenceCodeProxyList() throws Exception {
        log.debug("DiapstcehrServiziPortale.getLioReferenceCodeProxyList");
        return filieraLloydsFactory.getListLioReferenceCode();
    }
}
