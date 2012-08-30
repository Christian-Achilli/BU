package com.kp.malice.repositories;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kp.malice.entities.business.AgenziaRMA;
import com.kp.malice.entities.business.ChiusuraMensileLio;
import com.kp.malice.entities.business.TitoloLloyds;
import com.kp.malice.entities.business.WelcomeInfo;

public interface TitoliRepository {

    public TitoloLloyds findTitolo(Long id) throws Exception;

    ArrayList<TitoloLloyds> findAllTitoliInPeriodoInizioCopertura(List<Long> idList, Date startDate, Date endDate)
            throws Exception;

    public void salvaTitoloIncassato(TitoloLloyds titolo) throws Exception;
    
    public void salvaTitoloStornato(TitoloLloyds titoloIncassato) throws Exception;

    public void salvaTitoloRecuperato(TitoloLloyds titoloIncassato) throws Exception;

    public void update(TitoloLloyds titolo) throws Exception;

    public List<TitoloLloyds> getTitoliDellaChiusura(ChiusuraMensileLio chrDaInviare) throws Exception;

    public WelcomeInfo getInfoTitoliDaIncassare(AgenziaRMA age) throws Exception;

    public WelcomeInfo getInfoTitoliSospesi(AgenziaRMA age) throws Exception;

    public BigDecimal getPremiIncassatiEuroCentAnnoCorrente(AgenziaRMA age) throws Exception;

    public BigDecimal getProvvigioniIncassateEuroCentAnnoCorrente(AgenziaRMA age) throws Exception;

}