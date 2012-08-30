package funzioniBusiness;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.kp.malice.entities.business.IncassoTitoloLloyds;
import com.kp.malice.entities.business.TitoloLloyds;
import com.kp.malice.entities.business.IncassoTitoloLloyds.MezzoPagamento;
import com.kp.malice.entities.business.IncassoTitoloLloyds.StatoIncasso;
import com.kp.malice.entities.business.TitoloLloyds.StatoTitolo;
import com.kp.malice.entities.miscellaneous.DettaglioIncassoTitoloLloyds;

public class TestStornoTitolo {

    @Test(expected = UnsupportedOperationException.class)
    public void non_stornare_se_non_incassato() throws Exception {
        titoloDaStornare.storna();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void non_stornare_se_non_editabile() throws Exception {
        titoloDaStornare.incassa(incasso);
        titoloDaStornare.storna();
    }

    @Test
    public void test_titolo_stornato_torna_DA_INCASSARE() throws Exception {
        titoloDaStornare.incassa(incasso);
        titoloDaStornare.storna();
        Assert.assertEquals(StatoTitolo.DA_INCASSARE, titoloDaStornare.getStatoTitolo());
    }

    private TitoloLloyds titoloDaStornare;
    private DettaglioIncassoTitoloLloyds incasso;

    @Before
    public void setUp() {
        titoloDaStornare = new TitoloLloyds();
        titoloDaStornare.setStatoTitolo(StatoTitolo.DA_INCASSARE);
        incasso = new DettaglioIncassoTitoloLloyds();
        incasso.setDataIncasso(new Date());
        incasso.setImportoIncassoEuroCent(BigDecimal.valueOf(Math.random() * 10000));
        incasso.setMezzoPagamento(MezzoPagamento.ASSEGNO);
        incasso.setStatoIncasso(StatoIncasso.EFFETTIVO.toString());
    }

}
