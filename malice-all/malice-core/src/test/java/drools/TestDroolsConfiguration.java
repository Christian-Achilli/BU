package drools;

import org.junit.Assert;
import org.junit.Test;

import com.kp.malice.businessRules.DroolsKnowledgeBaseUtil;
import com.kp.malice.businessRules.FunzioniAbilitate;
import com.kp.malice.businessRules.PvUser;
import com.kp.malice.businessRules.TitoloSnapshot;
import com.kp.malice.entities.business.IncassoTitoloLloyds.StatoIncasso;
import com.kp.malice.entities.business.TitoloLloyds.StatoTitolo;
import com.kp.malice.entities.persisted.Mov.Source;
import com.kp.malice.entities.persisted.Mov.StatoMovimento;

public class TestDroolsConfiguration {

    @Test
    public void test_Rule_1_funzioniDettaglioTitolo_xls() throws Exception {
        TitoloSnapshot ts = new TitoloSnapshot();
        ts.setStatoMovimento(StatoMovimento.ATTESA_PDF);
        ts.setStatoTitolo(StatoTitolo.DA_INCASSARE);
        ts.setSource(Source.XML);
        FunzioniAbilitate s = DroolsKnowledgeBaseUtil.getFunzioniDisponibiliPerTitolo(ts);
        Assert.assertNotNull(s);
        Assert.assertFalse(s.isAnnulloTitolo());
        Assert.assertFalse(s.isIncassoTitolo());
        Assert.assertFalse(s.isStornoIncasso());
        Assert.assertFalse(s.isRecuperoSospeso());
        Assert.assertFalse(s.isCreaRettificaProvvigioni());
        Assert.assertFalse(s.isModificaTitolo());
        Assert.assertFalse(s.isDelegaIncasso());
        Assert.assertFalse(s.isRevocaAnnulloTitolo());
    }

    @Test
    public void test_Rule_2_funzioniDettaglioTitolo_xls() throws Exception {
        TitoloSnapshot ts = new TitoloSnapshot();
        ts.setStatoMovimento(StatoMovimento.LAVORABILE);
        ts.setStatoTitolo(StatoTitolo.DA_INCASSARE);
        ts.setPvUser(PvUser.PV_OWNER);
        ts.setSource(Source.XML);
        FunzioniAbilitate s = DroolsKnowledgeBaseUtil.getFunzioniDisponibiliPerTitolo(ts);
        Assert.assertNotNull(s);
        Assert.assertTrue(s.isAnnulloTitolo());
        Assert.assertTrue(s.isIncassoTitolo());
        Assert.assertFalse(s.isStornoIncasso());
        Assert.assertFalse(s.isRecuperoSospeso());
        Assert.assertTrue(s.isCreaRettificaProvvigioni());
        Assert.assertFalse(s.isModificaTitolo());
        Assert.assertTrue(s.isDelegaIncasso());
        Assert.assertFalse(s.isRevocaAnnulloTitolo());
    }

    @Test
    public void test_Rule_3_funzioniDettaglioTitolo_xls() throws Exception {
        TitoloSnapshot ts = new TitoloSnapshot();
        ts.setStatoMovimento(StatoMovimento.LAVORABILE);
        ts.setStatoTitolo(StatoTitolo.DA_INCASSARE);
        ts.setPvUser(PvUser.PV_OWNER);
        ts.setSource(Source.FORM);
        FunzioniAbilitate s = DroolsKnowledgeBaseUtil.getFunzioniDisponibiliPerTitolo(ts);
        Assert.assertNotNull(s);
        Assert.assertTrue(s.isAnnulloTitolo());
        Assert.assertTrue(s.isIncassoTitolo());
        Assert.assertFalse(s.isStornoIncasso());
        Assert.assertFalse(s.isRecuperoSospeso());
        Assert.assertTrue(s.isCreaRettificaProvvigioni());
        Assert.assertTrue(s.isModificaTitolo());
        Assert.assertTrue(s.isDelegaIncasso());
        Assert.assertFalse(s.isRevocaAnnulloTitolo());
    }

    @Test
    public void test_Rule_5_funzioniDettaglioTitolo_xls() throws Exception {
        TitoloSnapshot ts = new TitoloSnapshot();
        ts.setStatoMovimento(StatoMovimento.LAVORABILE);
        ts.setStatoTitolo(StatoTitolo.INCASSATO);
        ts.setStatoIncasso(StatoIncasso.EFFETTIVO);
        ts.setPvUser(PvUser.PV_DELEGATE);
        FunzioniAbilitate s = DroolsKnowledgeBaseUtil.getFunzioniDisponibiliPerTitolo(ts);
        Assert.assertNotNull(s);
        Assert.assertFalse(s.isAnnulloTitolo());
        Assert.assertFalse(s.isIncassoTitolo());
        Assert.assertTrue(s.isStornoIncasso());
        Assert.assertFalse(s.isRecuperoSospeso());
        Assert.assertFalse(s.isCreaRettificaProvvigioni());
        Assert.assertFalse(s.isModificaTitolo());
        Assert.assertFalse(s.isDelegaIncasso());
        Assert.assertFalse(s.isRevocaAnnulloTitolo());
    }
    
    @Test
    public void test_Rule_9_funzioniDettaglioTitolo_xls() throws Exception {
        TitoloSnapshot ts = new TitoloSnapshot();
        ts.setStatoMovimento(StatoMovimento.LAVORABILE);
        ts.setStatoTitolo(StatoTitolo.CONSOLIDATO);
        ts.setStatoIncasso(StatoIncasso.SOSPESO);
        ts.setPvUser(PvUser.PV_DELEGATE);
        FunzioniAbilitate s = DroolsKnowledgeBaseUtil.getFunzioniDisponibiliPerTitolo(ts);
        Assert.assertNotNull(s);
        Assert.assertFalse(s.isAnnulloTitolo());
        Assert.assertFalse(s.isIncassoTitolo());
        Assert.assertFalse(s.isStornoIncasso());
        Assert.assertTrue(s.isRecuperoSospeso());
        Assert.assertFalse(s.isCreaRettificaProvvigioni());
        Assert.assertFalse(s.isModificaTitolo());
        Assert.assertFalse(s.isDelegaIncasso());
        Assert.assertFalse(s.isRevocaAnnulloTitolo());
    }

}
