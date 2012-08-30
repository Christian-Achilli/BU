package useCases;

import java.math.BigDecimal;
import java.util.Date;

import org.joda.time.MutableDateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kp.malice.entities.business.IncassoTitoloLloyds.MezzoPagamento;
import com.kp.malice.entities.business.IncassoTitoloLloyds.StatoIncasso;
import com.kp.malice.entities.business.IncassoTitoloLloyds;
import com.kp.malice.entities.business.TitoloLloyds;
import com.kp.malice.entities.business.TitoloLloyds.StatoTitolo;
import com.kp.malice.entities.miscellaneous.DettaglioIncassoTitoloLloyds;
import com.kp.malice.entities.persisted.HibernateSessionFactoryUtil;
import com.kp.malice.entities.persisted.Incs;
import com.kp.malice.repositories.DatabaseGatewayLloyds;
import com.kp.malice.repositories.IncassiRepository;
import com.kp.malice.repositories.TitoliRepository;
import com.kp.malice.useCases.FunzioniTitolo;

public class Test_UseCase_Funzioni_Titolo {
    
    @Test
    public void controlla_che_incasso_venga_eliminato() throws Exception {
    	HibernateSessionFactoryUtil.beginTransaction();
        incasso.setStatoIncasso(StatoIncasso.EFFETTIVO.toString());
        TitoloLloyds titoloFromDB = titoliRepository.findTitolo(1L);
        useCaseFunzioniSuTitolo.incassa(titoloFromDB, incasso);
        HibernateSessionFactoryUtil.commitTransaction();
        HibernateSessionFactoryUtil.beginTransaction();
        Assert.assertNotNull(titoloFromDB);
        Assert.assertNotNull(titoloFromDB.getUltimoIncassoCheHaMessoIlTitoloInStatoIncassato());
        Assert.assertNotNull(titoloFromDB.getDataIncassoMessaInCopertura());
        useCaseFunzioniSuTitolo.storna(titoloFromDB);
        HibernateSessionFactoryUtil.commitTransaction();
    }
    
    @Test
    public void controlla_che_incasso_non_venga_eliminato() throws Exception {
    	HibernateSessionFactoryUtil.beginTransaction();
    	incasso.setStatoIncasso(StatoIncasso.EFFETTIVO.toString());
    	TitoloLloyds titoloFromDB = titoliRepository.findTitolo(1L);
    	useCaseFunzioniSuTitolo.incassa(titoloFromDB, incasso);
    	IncassoTitoloLloyds incassoTitoloLloyds = titoloFromDB.getUltimoIncassoCheHaMessoIlTitoloInStatoIncassato();
    	MutableDateTime mdt = new MutableDateTime(incassoTitoloLloyds.getDataInserimento());
    	mdt.addDays(-1);
//    	incassoTitoloLloyds.setDataInserimento(mdt.toDate());
    	long idIncasso = incassoTitoloLloyds.getID();
    	Incs incassoNewDate = (Incs) HibernateSessionFactoryUtil.getSession().load(Incs.class, idIncasso);
    	incassoNewDate.setTmstInsRig(mdt.toDate());
    	HibernateSessionFactoryUtil.getSession().save(incassoNewDate);
    	HibernateSessionFactoryUtil.commitTransaction();
    	HibernateSessionFactoryUtil.beginTransaction();
    	Assert.assertNotNull(titoloFromDB);
    	Assert.assertNotNull(titoloFromDB.getUltimoIncassoCheHaMessoIlTitoloInStatoIncassato());
    	Assert.assertNotNull(titoloFromDB.getDataIncassoMessaInCopertura());
    	TitoloLloyds newTitoloFromDB = titoliRepository.findTitolo(1L);
    	int numIncassi = newTitoloFromDB.getIncassiOrderByDataInserimentoDesc().size();
    	useCaseFunzioniSuTitolo.storna(newTitoloFromDB);
    	Assert.assertEquals(titoliRepository.findTitolo(1L).getIncassiOrderByDataInserimentoDesc().size(), numIncassi);
    	HibernateSessionFactoryUtil.commitTransaction();
    }

//    @Test
//    public void controlla_che_incasso_venga_persistito_con_i_valori_corretti() throws Exception {
//        incasso.setStatoIncasso(StatoIncasso.EFFETTIVO);
//        useCaseFunzioniSuTitolo.incassa(titoloTest, incasso);
//        Assert.assertNotNull(titoloTest.getUltimoIncassoCheHaMessoIlTitoloInStatoIncassato().getId());
//        TitoloLloyds titoloFromDB = titoliRepository.findTitolo(titoloTest.getId());
//        Assert.assertNotNull(titoloFromDB);
//        Assert.assertNotNull(titoloFromDB.getDataIncassoMessaInCopertura());
//        List<IncassoTitoloLloyds> incassiFromDB = incassiRepository.findMovimentiContabiliByTitoloId(titoloFromDB
//                .getId());
//        Assert.assertNotNull(incassiFromDB);
//        Assert.assertFalse(incassiFromDB.isEmpty());
//        Assert.assertEquals(titoloTest.getUltimoIncassoCheHaMessoIlTitoloInStatoIncassato().getImportoIncassoEuroCent()
//                .longValue(), incassiFromDB.get(0).getImportoIncassoEuroCent().longValue());
//        Assert.assertEquals(incasso.getImportoIncassoEuroCent().longValue(), incassiFromDB.get(0)
//                .getImportoIncassoEuroCent().longValue());
//    }
//
//   
//    @Test
//    public void controlla_che_il_titolo_sia_annullato_e_revocato() throws Exception {
//        Date dataDiAnnullo = new Date();
//        String nota = "nota di annullo";
//        useCaseFunzioniSuTitolo.annulla(titoloTest, dataDiAnnullo, nota);
//        TitoloLloyds titoloRecuperatoFromDb = titoliRepository.findTitolo(titoloTest.getId());
//        Assert.assertEquals(StatoTitolo.ANNULLATO, titoloRecuperatoFromDb.getStatoTitolo());
//        Assert.assertEquals(new DateTime(dataDiAnnullo).weekyear(),
//                new DateTime(titoloRecuperatoFromDb.getDataAnnulloTitolo()).weekyear());
//        Assert.assertEquals(nota, titoloRecuperatoFromDb.getNotaAnnulloTitolo());
//        useCaseFunzioniSuTitolo.revocaAnnullo(titoloTest);
//        titoloRecuperatoFromDb = titoliRepository.findTitolo(titoloTest.getId());
//        Assert.assertEquals(StatoTitolo.DA_INCASSARE, titoloRecuperatoFromDb.getStatoTitolo());
//        Assert.assertNull(titoloRecuperatoFromDb.getDataAnnulloTitolo());
//        Assert.assertEquals(nota, titoloRecuperatoFromDb.getNotaAnnulloTitolo());
//    }

    FunzioniTitolo useCaseFunzioniSuTitolo;
    TitoliRepository titoliRepository;
    IncassiRepository incassiRepository;
    private TitoloLloyds titoloTest;
    private DettaglioIncassoTitoloLloyds incasso;

    class TestModule extends AbstractModule {

        @Override
        protected void configure() {
            bind(TitoliRepository.class).to(DatabaseGatewayLloyds.class);
            bind(IncassiRepository.class).to(DatabaseGatewayLloyds.class);
            bind(FunzioniTitolo.class);
        }
    }

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(new TestModule());
        useCaseFunzioniSuTitolo = injector.getInstance(FunzioniTitolo.class);
        titoliRepository = injector.getInstance(TitoliRepository.class);
        incassiRepository = injector.getInstance(IncassiRepository.class);
        try {
            titoloTest = titoliRepository.findTitolo(1L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        incasso = new DettaglioIncassoTitoloLloyds();
        incasso.setDataIncasso(new Date());
        incasso.setMezzoPagamento(MezzoPagamento.BONIFICO);
        incasso.setImportoIncassoEuroCent(new BigDecimal(Math.random() * 10000));
        titoloTest.setStatoTitolo(StatoTitolo.DA_INCASSARE);
    }

    @After
    public void tearDown() {
        HibernateSessionFactoryUtil.closeSession();
    }

}
