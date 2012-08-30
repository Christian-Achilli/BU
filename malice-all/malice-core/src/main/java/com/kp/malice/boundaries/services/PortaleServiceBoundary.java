package com.kp.malice.boundaries.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.kp.malice.entities.business.ChiusuraMensileLio;
import com.kp.malice.entities.business.EstrattoContoLio;
import com.kp.malice.entities.business.GrafiData;
import com.kp.malice.entities.business.LioReferenceCode;
import com.kp.malice.entities.business.TitoloLloyds;
import com.kp.malice.entities.business.WelcomeInfo;
import com.kp.malice.entities.miscellaneous.DettaglioIncassoTitoloLloyds;
import com.kp.malice.entities.miscellaneous.ScritturaContabileRma;

public interface PortaleServiceBoundary {

    TitoloLloyds findTitolo(long id) throws Exception;

    void aggiornaDatiTitolo(TitoloLloyds titolo) throws Exception;

    List<TitoloLloyds> findAllTitoliInPeriodoInizioCopertura(Date startDate, Date endDate) throws Exception;

    void annulloTitolo(TitoloLloyds titolo, Date dataDiAnnullo, String notaDiAnnullo) throws Exception;

    void revocaAnnulloTitolo(TitoloLloyds titolo) throws Exception;

    void incassaTitolo(TitoloLloyds titolo, DettaglioIncassoTitoloLloyds incasso) throws Exception;
    
    void stornaIncassoTitolo(TitoloLloyds titolo) throws Exception;

    void recuperaTitoloSospeso(TitoloLloyds titolo, BigDecimal sommaRecuperataEuroCent, Date datatIncasso,
            String mezzoPagamentoString) throws Exception;

    List<ScritturaContabileRma> findListByDataRegistrazioneIncasso(Date d) throws Exception;

    List<DettaglioIncassoTitoloLloyds> findListIncassiAggregati(ScritturaContabileRma scritturaContabileRma) throws Exception;

    List<ChiusuraMensileLio> getListaChiusure() throws Exception;

    List<EstrattoContoLio> getListaEstrattiConto(ChiusuraMensileLio c) throws Exception;

    List<TitoloLloyds> getListaTitoliInEstrattoConto(EstrattoContoLio e) throws Exception;

    void inviaChiusura(String nota) throws Exception;

    WelcomeInfo getWelcomeInfo() throws Exception;
    
    GrafiData calculateDataGrafi(Date start, Date end, String broker) throws Exception;
    
    List<LioReferenceCode> getLioReferenceCodeProxyList() throws Exception;
}
