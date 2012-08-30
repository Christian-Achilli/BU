package funzioniBusiness;

import java.math.BigDecimal;
import java.util.Date;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.kp.malice.entities.business.IncassoTitoloLloyds;
import com.kp.malice.entities.business.TitoloLloyds;
import com.kp.malice.entities.business.IncassoTitoloLloyds.MezzoPagamento;
import com.kp.malice.entities.business.IncassoTitoloLloyds.StatoIncasso;
import com.kp.malice.entities.business.TitoloLloyds.StatoTitolo;

public class TestAnnulloTitolo {

    @Test
    public void annulla_Il_titolo() throws Exception {
        Date dataDiAnnullo = new Date();
        String nota = "nota di annullo";
        titolo.annulla(dataDiAnnullo, nota);
        Assert.assertEquals(dataDiAnnullo, titolo.getDataAnnulloTitolo());
        Assert.assertEquals(nota, titolo.getNotaAnnulloTitolo());
        Assert.assertEquals(StatoTitolo.ANNULLATO, titolo.getStatoTitolo());
    }

    @Test
    public void revoca_annullo_del_titolo() throws Exception {
        Date dataDiAnnullo = new Date();
        String nota = "nota di annullo";
        titolo.annulla(dataDiAnnullo, nota);
        titolo.revocaAnnullo();
        Assert.assertNull(titolo.getDataAnnulloTitolo());
        Assert.assertEquals(nota, titolo.getNotaAnnulloTitolo());
        Assert.assertEquals(StatoTitolo.DA_INCASSARE, titolo.getStatoTitolo());
    }

    private TitoloLloyds titolo;
    private IncassoTitoloLloyds incasso;

    @Before
    public void setUp() {
        titolo = new TitoloLloyds();
        incasso = new IncassoTitoloLloyds();
        incasso.setDataIncasso(new Date());
        incasso.setImportoIncassoEuroCent(BigDecimal.valueOf(Math.random() * 10000));
        incasso.setMezzoPagamento(MezzoPagamento.ASSEGNO);
        incasso.setStatoIncasso(StatoIncasso.EFFETTIVO.toString());
    }

    @After
    public void after() {
    }

}
