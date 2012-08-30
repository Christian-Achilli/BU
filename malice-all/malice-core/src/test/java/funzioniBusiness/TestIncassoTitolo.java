package funzioniBusiness;

import java.math.BigDecimal;
import java.util.Date;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.kp.malice.businessRules.FunzioniAbilitate;
import com.kp.malice.entities.business.IncassoTitoloLloyds;
import com.kp.malice.entities.business.TitoloLloyds;
import com.kp.malice.entities.business.IncassoTitoloLloyds.MezzoPagamento;
import com.kp.malice.entities.business.IncassoTitoloLloyds.StatoIncasso;
import com.kp.malice.entities.business.TitoloLloyds.StatoTitolo;
import com.kp.malice.entities.miscellaneous.DettaglioIncassoTitoloLloyds;

public class TestIncassoTitolo {

    private TitoloLloyds titolo;
    private DettaglioIncassoTitoloLloyds incasso;

    @Test
    public void incassa_se_editabile_e_DA_INCASSARE() throws Exception {
        titolo.setStatoTitolo(StatoTitolo.DA_INCASSARE);
        titolo.incassa(incasso);
        Assert.assertEquals(StatoTitolo.INCASSATO, titolo.getStatoTitolo());
        Assert.assertNotNull(titolo.getUltimoIncassoCheHaMessoIlTitoloInStatoIncassato());
        Assert.assertNotNull(titolo.getUltimoIncassoCheHaMessoIlTitoloInStatoIncassato());
        Assert.assertNotNull(titolo.getDataIncassoMessaInCopertura());
        Assert.assertEquals(incasso.getImportoIncassoEuroCent(), titolo
                .getUltimoIncassoCheHaMessoIlTitoloInStatoIncassato().getImportoIncassoEuroCent());
    }

    @Test
    public void incassa_2_volte() throws Exception {
        titolo.setStatoTitolo(StatoTitolo.DA_INCASSARE);
        titolo.incassa(incasso);
        titolo.setStatoTitolo(StatoTitolo.DA_INCASSARE);
        incasso.setImportoIncassoEuroCent(BigDecimal.valueOf(Math.random() * 10000));
        titolo.incassa(incasso);
        Assert.assertEquals(StatoTitolo.INCASSATO, titolo.getStatoTitolo());
        Assert.assertNotNull(titolo.getUltimoIncassoCheHaMessoIlTitoloInStatoIncassato());
        Assert.assertNotNull(titolo.getDataIncassoMessaInCopertura());
        Assert.assertEquals(incasso.getImportoIncassoEuroCent(), titolo
                .getUltimoIncassoCheHaMessoIlTitoloInStatoIncassato().getImportoIncassoEuroCent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void non_incassare_se_manca_data_incasso() throws Exception {
        titolo.setStatoTitolo(StatoTitolo.DA_INCASSARE);
        incasso.setDataIncasso(null);
        titolo.incassa(incasso);
    }

    @Test
    public void test_non_esiste_incasso() throws Exception {
        titolo.setStatoTitolo(StatoTitolo.DA_INCASSARE);
        incasso.setDataIncasso(null);
        try {
            titolo.incassa(incasso);
        } catch (Exception e) {
            // do nothing
        }
        Assert.assertNull(titolo.getUltimoIncassoCheHaMessoIlTitoloInStatoIncassato());
    }

    @Test(expected = IllegalArgumentException.class)
    public void non_incassare_se_manca_importo_incasso() throws Exception {
        titolo.setStatoTitolo(StatoTitolo.DA_INCASSARE);
        incasso.setImportoIncassoEuroCent(null);
        titolo.incassa(incasso);
    }

    @Test(expected = IllegalArgumentException.class)
    public void non_incassare_se_manca_mezzo_pagamento() throws Exception {
        titolo.setStatoTitolo(StatoTitolo.DA_INCASSARE);
        incasso.setMezzoPagamento(null);
        titolo.incassa(incasso);
    }

    @Test(expected = IllegalArgumentException.class)
    public void non_incassare_se_manca_stato_incasso() throws Exception {
        titolo.setStatoTitolo(StatoTitolo.DA_INCASSARE);
        incasso.setStatoIncasso(null);
        titolo.incassa(incasso);
    }

    @Before
    public void setUp() {
        titolo = new TitoloLloyds();
        incasso = new DettaglioIncassoTitoloLloyds();
        incasso.setDataIncasso(new Date());
        incasso.setImportoIncassoEuroCent(BigDecimal.valueOf(Math.random() * 10000));
        incasso.setMezzoPagamento(MezzoPagamento.ASSEGNO);
        incasso.setStatoIncasso(StatoIncasso.EFFETTIVO.toString());
        FunzioniAbilitate fa = new FunzioniAbilitate();
        fa.setIncassoTitolo(true);
        titolo.setFunzioniAbilitate(fa);
    }

    @After
    public void after() {
    }

}
