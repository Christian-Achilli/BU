package com.kp.malice.useCases;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Formatter;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import com.google.common.io.Files;
import com.google.inject.Inject;
import com.kp.malice.entities.business.AgenziaRMA;
import com.kp.malice.entities.business.ChiusuraMensileLio;
import com.kp.malice.entities.business.ChiusuraMensileLio.StatoChiusura;
import com.kp.malice.entities.business.EstrattoContoLio;
import com.kp.malice.entities.business.TitoloLloyds;
import com.kp.malice.entities.persisted.ChrMslLio;
import com.kp.malice.entities.persisted.HibernateSessionFactoryUtil;
import com.kp.malice.factories.FilieraLloydsFactory;
import com.kp.malice.repositories.ChiusureMensiliLioRepository;

public class FunzioniChiusura {

    private static final Logger log = Logger.getLogger(FunzioniChiusura.class);
    private final ChiusureMensiliLioRepository chrRepo;
    private final FilieraLloydsFactory filieraFactory;
    private final CreaEstrattiContoLio creatoreEstrattiContoLio;
    private final ImpacchettaAllegaSpedisciFiles postino;

    @Inject
    public FunzioniChiusura(ChiusureMensiliLioRepository chrRepo, FilieraLloydsFactory filieraFactory,
            CreaEstrattiContoLio creatoreEstrattiContoLio, ImpacchettaAllegaSpedisciFiles postino) {
        this.chrRepo = chrRepo;
        this.filieraFactory = filieraFactory;
        this.creatoreEstrattiContoLio = creatoreEstrattiContoLio;
        this.postino = postino;
    }

    public void creaChiusuraSeNonEsisteNelPeriodoCercatoONelPrimoPeriodoSuccessivoDisponibile(AgenziaRMA age,
            Date periodo) throws Exception {
        try {
            DateTime mdt = new DateTime(periodo);
            ChiusuraMensileLio nuovaNextToGo = chrRepo.getChiusuraMeseX(age, mdt.getYear(), mdt.getMonthOfYear());
            if (null == nuovaNextToGo || !nuovaNextToGo.isAperta()) {
                nuovaNextToGo = chrRepo.getNextToBeSent(age);
                if (null == nuovaNextToGo || nuovaNextToGo.getPeriodo().isBefore(mdt))
                    chrRepo.creaNuovaChiusuraAperta(age, mdt);
            }
        } catch (Exception e) {
            log.error("ERRORE IN CREAZIONE CHIUSURA CHE NON ESISTEVA", e);
            throw e;
        }
    }

    public void creaProssimaChiusuraSeNonEsiste(AgenziaRMA age, ChiusuraMensileLio chiusuraCorrente) throws Exception {
        creaChiusuraSeNonEsisteNelPeriodoCercatoONelPrimoPeriodoSuccessivoDisponibile(age,
                getProssimoPeriodoChiusura(chiusuraCorrente));
    }

    private Date getProssimoPeriodoChiusura(ChiusuraMensileLio chiusuraCorrente) {
        DateTime periodoChiusuraAppenaInviata = new DateTime(chiusuraCorrente.getReferenceYear(),
                chiusuraCorrente.getReferenceCalendarMonth(), 1, 0, 0);
        MutableDateTime mdt = new MutableDateTime(periodoChiusuraAppenaInviata);
        mdt.addMonths(1);
        return mdt.toDate();
    }

    public void aggiungiTitoloAChiusuraAperta(TitoloLloyds titolo) throws Exception {
        log.debug("aggiungiTitoloAChiusuraAperta start");
        MutableDateTime mdt = new MutableDateTime(titolo.getDataIncassoMessaInCopertura());
        final AgenziaRMA agenziaRma = titolo.getCertificatoLloyds().getFilieraLloyds().getAgenziaRma();
        ChiusuraMensileLio chiusura = chrRepo.getChiusuraMeseX(agenziaRma, mdt.getYear(), mdt.getMonthOfYear());
        while (null == chiusura || !chiusura.isAperta()) {
            mdt.addMonths(1);
            chiusura = chrRepo.getChiusuraMeseX(agenziaRma, mdt.getYear(), mdt.getMonthOfYear());
        }
        chrRepo.addToEstrattoConto(titolo, chiusura);
    }

    public void rimuoviTitoloDaEstrattoConto(TitoloLloyds titolo) throws Exception {
        chrRepo.removeFromEstrattoContoIfAttached(titolo);
    }

    public void inviaChiusura(ChiusuraMensileLio chr, AgenziaRMA age) throws Exception {
        Formatter formatter = new Formatter();
        DecimalFormat df = new DecimalFormat("00");
        String commonNameFileAllegati = formatter.format("EC_%s-%s-%s-%s", chr.getReferenceYear(),
                df.format(chr.getReferenceCalendarMonth()), age.getOmcCode(), age.getDescription()).toString();
        ChrMslLio chrPersisted = HibernateSessionFactoryUtil.getPersistedInstance(ChrMslLio.class, chr.getId());
        chrPersisted.aggiornaTimestamp();
        chrPersisted.setStatoChiusura(StatoChiusura.INVIATA);
        chrPersisted.setInviataA(filieraFactory.getLio().getEmail());
        chrPersisted.setDtInvio(new Date());
        chrPersisted.setNota(chr.getNota());
        HibernateSessionFactoryUtil.getSession().saveOrUpdate(chrPersisted);
        File tempDir = generaFileExcelDaInviareNellaTempDir(commonNameFileAllegati, chr);
        String emalisTo = filieraFactory.getLio().getEmail();
        String[] to = emalisTo.trim().split(","); //potrei avere piu' di un indirizzo mail separati da una virgola
        log.debug("to 0 : " + to[0]);
        String emailsCc = age.getEmail();
        String[] cc = emailsCc.trim().split(","); //potrei avere piu' di un indirizzo mail separati da una virgola
        try {
            postino.comprimiEInviaInAllegato(commonNameFileAllegati, tempDir, to, cc, "Estratti conto del mese di "
                    + chr.getReferenceYear() + "-" + new DecimalFormat("00").format(chr.getReferenceCalendarMonth())
                    + " per l'OMC " + age.getOmcCode() + " (" + age.getDescription() + ")",
                    "Buongiorno,\nIn allegato trova gli Estratti Conto in oggetto.\nCordiali saluti.\n");
        } catch (Exception e) {
            throw e;
        } finally {
            eliminaLaTempDir(tempDir);
        }
    }

    private File generaFileExcelDaInviareNellaTempDir(String commonNameFileAllegati, ChiusuraMensileLio chr)
            throws Exception, IOException {
        File tempDir = Files.createTempDir();
        log.debug("Creata directory temporanea: " + tempDir.getPath());
        for (EstrattoContoLio ec : chrRepo.getListaEstrattiConto(chr)) {
            String nomeFile = commonNameFileAllegati + "-" + ec.getLabel() + "_";
            log.debug("Creo file con nome: " + nomeFile + " e estensione xls");
            File estrattoContoExcel = File.createTempFile(nomeFile, ".xls", tempDir);
            log.debug("Creato file excel da popolare: " + estrattoContoExcel.getPath());
            creatoreEstrattiContoLio.generaExcel(chr, ec, estrattoContoExcel,
                    chrRepo.getListaTitoliPerEstrattoConto(ec));
        }
        return tempDir;
    }

    private void eliminaLaTempDir(File tempDir) throws IOException {
        Files.deleteDirectoryContents(new File(tempDir.getCanonicalPath())); //necessita di un path senza link per poter eseguire con successo la procedura
        if (tempDir.delete()) { //se la directory non è vuota l'operazione non può essere eseguita
            log.info("Directory temporanea " + tempDir.getPath() + " eliminata con successo.");
        } else {
            log.error("Errore eliminazione directory temporanea " + tempDir.getPath());
        }
    }
}
