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

public class TestRecuperoTitolo {

    @Test
    public void controlla_se_titolo_recuperabile() throws Exception {
        FunzioniAbilitate funzioniAbilitate = new FunzioniAbilitate();
        funzioniAbilitate.setRecuperoSospeso(true);
        titolo.setFunzioniAbilitate(funzioniAbilitate);
        titolo.setStatoTitolo(StatoTitolo.DA_INCASSARE);
        incasso.setStatoIncasso(StatoIncasso.SOSPESO.toString());
        titolo.incassa(incasso);
        Assert.assertEquals(StatoIncasso.SOSPESO, titolo.getUltimoIncassoCheHaMessoIlTitoloInStatoIncassato()
                .getStatoIncasso());
        Assert.assertTrue(titolo.isIncassato());
    }

    private TitoloLloyds titolo;
    private DettaglioIncassoTitoloLloyds incasso;

    @Before
    public void setUp() {
        titolo = new TitoloLloyds();
        incasso = new DettaglioIncassoTitoloLloyds();
        incasso.setDataIncasso(new Date());
        incasso.setImportoIncassoEuroCent(BigDecimal.valueOf(Math.random() * 10000));
        incasso.setMezzoPagamento(MezzoPagamento.ASSEGNO);
    }

    @After
    public void after() {
    }

}
