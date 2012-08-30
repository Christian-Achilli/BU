package com.kp.malice.useCases;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.google.inject.Inject;
import com.kp.malice.entities.business.ChiusuraMensileLio;
import com.kp.malice.entities.business.IncassoTitoloLloyds.MezzoPagamento;
import com.kp.malice.entities.business.TitoloLloyds;
import com.kp.malice.entities.miscellaneous.DettaglioIncassoTitoloLloyds;
import com.kp.malice.repositories.IncassiRepository;
import com.kp.malice.repositories.TitoliRepository;

public class FunzioniTitolo {

    private TitoliRepository titoliRepository;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FunzioniTitolo.class); 

    @Inject
    public FunzioniTitolo(TitoliRepository repositoryTitoli) {
        this.titoliRepository = repositoryTitoli;
    }

    public void incassa(TitoloLloyds titolo, DettaglioIncassoTitoloLloyds incasso) throws Exception {
        try {
            titolo.incassa(incasso);
            titoliRepository.salvaTitoloIncassato(titolo);
        } catch (Exception e) {
            throw e;
        }
    }

    public void storna(TitoloLloyds titoloIncassato) throws Exception {
        try {
            titoloIncassato.storna();
            titoliRepository.salvaTitoloStornato(titoloIncassato);
        } catch (Exception e) {
            throw e;
        }
    }
    
//    public void eliminaFisicamenteIncasso(TitoloLloyds titoloIncassato) throws Exception{
//        try {
//            long idIncasso = titoloIncassato.getUltimoIncassoCheHaMessoIlTitoloInStatoIncassato().getID();
//            log.debug("incasso da eliminare id: "+idIncasso);
//            titoliRepository.eliminaIncassoDaTitolo(titoloIncassato, idIncasso);
////            titoloIncassato.rimuoviIncasso(titoloIncassato.getUltimoIncassoCheHaMessoIlTitoloInStatoIncassato());
//        } catch (Exception e) {
//            throw e;
//        }
//    }

    public void recupera(TitoloLloyds titoloIncassato, BigDecimal sommaRecuperataEuroCent, Date dataIncasso,
            MezzoPagamento mezzoPagamento) {
        try {
            titoloIncassato.recupera(sommaRecuperataEuroCent, dataIncasso, mezzoPagamento);
            titoliRepository.salvaTitoloRecuperato(titoloIncassato);
        } catch (Exception e) {
            throw new RuntimeException("ERRORE IN RECUPERO TITOLO", e);
        }
    }

    public void annulla(TitoloLloyds titolo, Date dataDiAnnullo, String notaDiAnnullo) {
        try {
            titolo.annulla(dataDiAnnullo, notaDiAnnullo);
            titoliRepository.update(titolo);
        } catch (Exception e) {
            throw new RuntimeException("ERRORE IN ANNULLO TITOLO", e);
        }
    }

    public void revocaAnnullo(TitoloLloyds titolo) throws Exception {
        try {
            titolo.revocaAnnullo();
            titoliRepository.update(titolo);
        } catch (Exception e) {
            throw e;
        }
    }

    public void consolidaTitoli(ChiusuraMensileLio chrDaInviare) throws Exception {
        List<TitoloLloyds> listaTitoli = titoliRepository.getTitoliDellaChiusura(chrDaInviare);
        for (TitoloLloyds ttlCtb : listaTitoli) {
            ttlCtb.consolida();
            titoliRepository.update(ttlCtb);
        }
    }
}
