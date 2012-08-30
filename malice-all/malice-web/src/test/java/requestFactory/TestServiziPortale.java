package requestFactory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.security.core.Authentication;

import com.google.gwt.core.client.GWT;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.kp.malice.MalicePropertyContainer;
import com.kp.malice.authentication.MaliceAuthenticationProvider;
import com.kp.malice.entities.business.ContraentePolizzaLloyds.Gender;
import com.kp.malice.entities.business.TitoloLloyds;
import com.kp.malice.entities.business.TitoloLloyds.StatoTitolo;
import com.kp.malice.entities.persisted.CompPtf;
import com.kp.malice.entities.persisted.GarPrst;
import com.kp.malice.entities.persisted.HibernateSessionFactoryUtil;
import com.kp.malice.entities.persisted.Mov;
import com.kp.malice.entities.persisted.Mov.Source;
import com.kp.malice.entities.persisted.Mov.StatoMovimento;
import com.kp.malice.entities.persisted.OggAsct;
import com.kp.malice.entities.persisted.Pol;
import com.kp.malice.entities.persisted.Pol.StatoPolizza;
import com.kp.malice.entities.persisted.Pol.TipoCoass;
import com.kp.malice.entities.persisted.Psn;
import com.kp.malice.entities.persisted.PunVnd;
import com.kp.malice.entities.persisted.RapCntn;
import com.kp.malice.entities.persisted.RapCntn.TipoContraenza;
import com.kp.malice.entities.persisted.RecordIdentifier;
import com.kp.malice.entities.persisted.RipDttTtl;
import com.kp.malice.entities.persisted.SezPrst;
import com.kp.malice.entities.persisted.TtlCtb;
import com.kp.malice.entities.persisted.VrnPol;
import com.kp.malice.entities.persisted.VrnRipRch;
import com.kp.malice.factories.IncassoLloydsFactory;
import com.kp.malice.repositories.DatabaseGatewayLloyds;
import com.kp.malice.repositories.TitoliRepository;
import com.kp.malice.shared.MaliceRequestFactory;
import com.kp.malice.shared.MaliceRequestFactory.ServiziPortale;
import com.kp.malice.shared.MezzoPagamento;
import com.kp.malice.shared.StatoIncasso;
import com.kp.malice.shared.proxies.DettaglioIncassoTitoloProxy;
import com.kp.malice.shared.proxies.NewTitoloProxy;
import com.kp.malice.shared.proxies.ScritturaContabileProxy;
import com.kp.malice.shared.proxies.WelcomeInfoProxy;
import com.kp.malice.useCases.RecuperaScrittureContabiliRma;

import database.AuthForTest;


@SuppressWarnings("unchecked")
public class TestServiziPortale {

   private static final Logger log = Logger.getLogger(TestServiziPortale.class);

   private static long TTL_CTB_RECORD_UNDER_TEST;
   private MaliceRequestFactory factory;
   private TitoliRepository titoliRepository;

   @Before
   public void setup() {
       factory = MaliceRequestFactoryHelper.getService(MaliceRequestFactory.class);
       Injector inj = Guice.createInjector(new AbstractModule() {

           @Override
           protected void configure() {
               bind(RecuperaScrittureContabiliRma.class);
               bind(IncassoLloydsFactory.class);
               bind(MaliceAuthenticationProvider.class);
               bind(Authentication.class).to(AuthForTest.class);
               bind(TitoliRepository.class).to(DatabaseGatewayLloyds.class);
           }
       });
       titoliRepository = inj.getInstance(TitoliRepository.class);
       MaliceAuthenticationProvider authProvider = inj.getInstance(MaliceAuthenticationProvider.class);
       Authentication auth = inj.getInstance(Authentication.class);
       authProvider.authenticate(auth);
   }

   @After
   public void tearDown() {
       HibernateSessionFactoryUtil.closeSession();
   }

   @Test
   public void invioChiusura() throws Exception {
       factory.serviziPortale().inviaChiusura("test").fire();
   }

   @Test
   @Ignore
   public void testRecuperoInfoTitoliDaIncassare() throws Exception {
       factory.serviziPortale().getWelcomeInfo().fire(new Receiver<WelcomeInfoProxy>() {

           @Override
           public void onSuccess(WelcomeInfoProxy response) {
               Assert.assertNotNull(response.getMeseAperto());
               Assert.assertNotNull(response.getNumIncassiSospesi());
               Assert.assertNotNull(response.getNumTtlDaIncassare());
               Assert.assertNotNull(response.getTotPremiDaIncassareEuroCent());
               Assert.assertNotNull(response.getTotPremiIncassatiAnnoEuroCent());
               Assert.assertNotNull(response.getTotPremiInSospesoEuroCent());
               Assert.assertNotNull(response.getTotProvvigioniIncassateAnnoEuroCent());
           }

       });
   }

   @Test
   public void recuperaIncassiDaScritturaContabile() throws Exception {
       org.joda.time.DateTime dt = new org.joda.time.DateTime(2012, 4, 30, 0, 0);
       factory.serviziPortale().findListByDataRegistrazioneIncasso(dt.toDate())
               .fire(new Receiver<List<ScritturaContabileProxy>>() {

                   @Override
                   public void onSuccess(List<ScritturaContabileProxy> response) {
                       factory.serviziPortale().findListIncassiAggregati(response.get(0)).fire(new Receiver<List<DettaglioIncassoTitoloProxy>>() {

                                   @Override
                                   public void onSuccess(List<DettaglioIncassoTitoloProxy> response) {
                                	   log.debug("recuperaIncassiDaScritturaContabile SUCCESS");
                                   }

                                   @Override
                                   public void onFailure(ServerFailure error) {
                                       fail("RECUPERO INCASSI FAIL");
                                   }
                               });

                   }

                   @Override
                   public void onFailure(ServerFailure error) {
                       fail("RECUPERO SCRITTURE FAIL");
                   }
               });
   }

   @Test
   @Ignore
   public void test_ciclo_di_vita_del_titolo() throws Exception {
       creaCertificatoPerTest();
       annulla_titolo();
       revoca_annullo_titolo();
       incassa_titolo_SOSPESO();
       recupera_titolo_sospeso();
       storna_titolo();
   }

   private void creaCertificatoPerTest() throws Exception {
       long id = creaCertificatoSuDbPerTest();
       HibernateSessionFactoryUtil.closeSession();
       TitoloLloyds titolo = titoliRepository.findTitolo(id);
       assertNotNull(titolo);
       TTL_CTB_RECORD_UNDER_TEST = id;
   }

   private void annulla_titolo() throws Exception {
       NewTitoloProxy titoloProxy = getTitoloUnderTest();
       Date dataDiAnnullamento = new Date();
       factory.serviziPortale().annulloTitolo(titoloProxy, dataDiAnnullamento, "nota di test").with(propsForDettaglio)
               .fire(new Receiver<Void>() {

                   @Override
                   public void onSuccess(Void response) {
                       GWT.log("ANNULLO OK");
                   }

                   @Override
                   public void onFailure(ServerFailure error) {
                       fail("ANNULLO FAIL");

                   }
               });
   }

   private String[] propsForDettaglio = new String[] { "certificatoLloyds", "funzioniAbilitate",
           "ultimoIncassoCheHaMessoIlTitoloInStatoIncassato", "filieraLloyds", "incassiOrderByDataInserimentoDesc" };

   private NewTitoloProxy getTitoloUnderTest() {
       Receiver<NewTitoloProxy> receiver = mock(Receiver.class);
       ServiziPortale sp = factory.serviziPortale();
       sp.findTitolo(TTL_CTB_RECORD_UNDER_TEST).with(propsForDettaglio).fire(receiver);
       NewTitoloProxy titoloProxy = MaliceRequestFactoryHelper.captureResult(receiver);
       assertNotNull(titoloProxy);
       return titoloProxy;
   }

   private void revoca_annullo_titolo() throws Exception {
       NewTitoloProxy titoloProxy = getTitoloUnderTest();
       factory.serviziPortale().revocaAnnulloTitolo(titoloProxy).with(propsForDettaglio).fire(new Receiver<Void>() {

           @Override
           public void onSuccess(Void response) {
               GWT.log("REVOCA ANNULLO OK");
           }

           @Override
           public void onFailure(ServerFailure error) {
               fail("REVOCA ANNULLO FAIL:" + error.getStackTraceString());
           }
       });
   }

   private void incassa_titolo_SOSPESO() throws Exception {
       NewTitoloProxy titoloProxy = getTitoloUnderTest();
       ServiziPortale sp;
       sp = factory.serviziPortale();
       DettaglioIncassoTitoloProxy incasso = sp.create(DettaglioIncassoTitoloProxy.class);
       incasso.setDataIncasso(new Date());
       incasso.setImportoIncassoEuroCent(new BigDecimal("1122334455"));
       incasso.setStringMezzoPagamento(MezzoPagamento.ASSEGNO.name());
       incasso.setStatoIncasso(StatoIncasso.SOSPESO.name());
       sp.incassaTitolo(titoloProxy, incasso).with(propsForDettaglio).fire(new Receiver<Void>() {

           @Override
           public void onSuccess(Void response) {
               log.debug("INCASSO OK");
           }

           @Override
           public void onFailure(ServerFailure error) {
               fail("INCASSO FAIL");

           }
       });
       TitoloLloyds titoloLloyds = titoliRepository.findTitolo(TTL_CTB_RECORD_UNDER_TEST);
       assertNotNull(titoloLloyds);
   }

   private void recupera_titolo_sospeso() throws Exception {
       NewTitoloProxy titoloProxy = getTitoloUnderTest();
       factory.serviziPortale().recuperaTitoloSospeso(titoloProxy, new BigDecimal("32323232"), null, null)
               .with(propsForDettaglio).fire(new Receiver<Void>() {

                   @Override
                   public void onSuccess(Void response) {
                       GWT.log("RECUPERO SOSPESO OK");
                   }

                   @Override
                   public void onFailure(ServerFailure error) {
                       fail("RECUPERO SOSPESO FAIL:" + error.getStackTraceString());
                   }
               });
   }

   private void storna_titolo() throws Exception {
       NewTitoloProxy titoloProxy = getTitoloUnderTest();
       factory.serviziPortale().stornaIncassoTitolo(titoloProxy).with(propsForDettaglio).fire(new Receiver<Void>() {

           @Override
           public void onSuccess(Void response) {
               log.debug("STORNO OK");
           }

           @Override
           public void onFailure(ServerFailure error) {
               fail("STORNO FAIL");
           }
       });
   }

   @Test
   @Ignore
   public void recupera_lista_titoli_in_periodo_copertura() throws Exception {
       Date fine = new Date();
       Calendar inizio = Calendar.getInstance();
       inizio.add(Calendar.YEAR, -1);
       ServiziPortale request = factory.serviziPortale();
       Request<List<NewTitoloProxy>> req = request.findAllTitoliInPeriodoInizioCopertura(inizio.getTime(), fine).with(
               propsForDettaglio);
       req.fire(new Receiver<List<NewTitoloProxy>>() {
           @Override
           public void onSuccess(List<NewTitoloProxy> listaTitoli) {
               log.debug("success RequestFactory retrieved " + listaTitoli.size() + " newTitoloProxy");

           }

           public void onFailure(ServerFailure error) {
               fail("caricaTitoli failure!");
           }
       });
   }

   @Test
   @Ignore
   public void compare_proxy_and_repository_findById_methods() throws Exception {
       Receiver<NewTitoloProxy> receiver = mock(Receiver.class);
       factory.serviziPortale().findTitolo(TTL_CTB_RECORD_UNDER_TEST).with(propsForDettaglio).fire(receiver);
       NewTitoloProxy titoloProxy = MaliceRequestFactoryHelper.captureResult(receiver);
       assertNotNull(titoloProxy);
       TitoloLloyds titoloLloyds = titoliRepository.findTitolo(TTL_CTB_RECORD_UNDER_TEST);
       assertNotNull(titoloLloyds);
       //TODO finire il test per verificare se titoloLoys ha gli stessi valori di titoloProxy
       //TODO UN TEST PI√π SIGNIFICATIVO √® PARTIRE DALL'XML E VERIFICARLO CONTRO TITOLO PROXY
   }

   private static final String CODICE_CH_PIN1_PIN2 = "104512-PXB";

   private long creaCertificatoSuDbPerTest() {
       CompPtf lloyds = HibernateSessionFactoryUtil.getPersistedInstance(CompPtf.class,
               MalicePropertyContainer.getLloydsRecordId());
       PunVnd puntVnd = HibernateSessionFactoryUtil.getPersistedInstance(PunVnd.class, 1l);
       Pol pol = salvaPolConContraentePersonaFisica(lloyds, puntVnd, "TST_" + System.currentTimeMillis());
       salvaTitoloEMovimentoLavorabile(pol, 1);
       return pol.getRecordId();
   }

   private Pol salvaPolConContraentePersonaFisica(CompPtf compPtf, PunVnd punVnd, String numero) {
       RecordIdentifier rid = RecordIdentifier.getInitRecord();
       CompPtf cp = (CompPtf) HibernateSessionFactoryUtil.getSession().get(CompPtf.class, 1l);
       Pol pol = creaPol(punVnd, numero, rid, cp);
       VrnPol vrnPol = creaVersioniPol(punVnd, rid, pol);
       creaContraentePF(rid, vrnPol);
       return pol;
   }

   private VrnPol creaVersioniPol(PunVnd punVnd, RecordIdentifier rid, Pol pol) {
       VrnRipRch vrnRsc = new VrnRipRch(rid, pol, 1, punVnd.getCompPtf().getRecordId().intValue());
       vrnRsc.setCodCompPtfCoas(punVnd.getCompPtf().getRecordId().intValue());
       vrnRsc.setPrctQtaRis(new BigDecimal(100l));
       VrnPol vrnPol = new VrnPol(rid, pol, 1);
       vrnPol.setRegistrationDate(new Date());
       vrnPol.setCodFrz(1);
       vrnPol.setCodStaPol(StatoPolizza.IN_VIGORE);
       vrnPol.setDatEfftPol(new Date());
       Calendar scadenzaPol = Calendar.getInstance();
       scadenzaPol.add(Calendar.YEAR, 1);
       vrnPol.setDatScaOrgnPol(scadenzaPol.getTime());
       vrnPol.setPunVnd(punVnd);
       OggAsct ogg = new OggAsct(rid, vrnPol, 1);
       vrnPol.getOggAscts().add(ogg);
       SezPrst sez = new SezPrst(rid, ogg, 1, 1, "10");
       ogg.getSezPrsts().add(sez);
       GarPrst gar = new GarPrst(rid, sez, 1, "002");
       GarPrst gar2 = new GarPrst(rid, sez, 1, "003");
       pol.getVrnPols().add(vrnPol);
       pol.getVrnRipRchs().add(vrnRsc);
       vrnPol.getOggAscts().add(ogg);
       ogg.getSezPrsts().add(sez);
       sez.getGarPrsts().add(gar);
       sez.getGarPrsts().add(gar2);
       HibernateSessionFactoryUtil.beginTransaction();
       HibernateSessionFactoryUtil.getSession().save(vrnPol);
       HibernateSessionFactoryUtil.getSession().save(vrnRsc);
       HibernateSessionFactoryUtil.getSession().save(ogg);
       HibernateSessionFactoryUtil.getSession().save(sez);
       HibernateSessionFactoryUtil.commitTransaction();
       return vrnPol;
   }

   private Pol creaPol(PunVnd punVnd, String numero, RecordIdentifier rid, CompPtf cp) {
       Pol pol = new Pol(rid, numero, cp, punVnd);
       pol.setPunVnd(punVnd);
       pol.setCodTipCoas(TipoCoass.COMP_PTF);
       pol.setCodiceCoverHolder(CODICE_CH_PIN1_PIN2);
       HibernateSessionFactoryUtil.beginTransaction();
       HibernateSessionFactoryUtil.getSession().save(pol);
       HibernateSessionFactoryUtil.commitTransaction();
       return pol;
   }

   private void creaContraentePF(RecordIdentifier rid, VrnPol vrnPol) {
       Psn psn = new Psn(rid);
       psn.setAddressLine1("via di test 15");
       psn.setAddressLine2("presso contrada alta");
       psn.setCity("Monza");
       psn.setCountry("Italy");
       psn.setGender(Gender.M);
       psn.setFiscalCode("CGHDRT56M34L234C");
       psn.setFirstName("Nome");
       psn.setLastName("Cognome");
       psn.setJobTitle("ing");
       psn.setPostCode("20100");
       psn.setRegion("Lombardia");
       HibernateSessionFactoryUtil.beginTransaction();
       HibernateSessionFactoryUtil.getSession().save(psn);
       HibernateSessionFactoryUtil.commitTransaction();
       HibernateSessionFactoryUtil.closeSession();
       RapCntn rapCntn = new RapCntn(rid);
       rapCntn.setPersona(psn);
       rapCntn.setVrnPol(vrnPol);
       rapCntn.setTipoCntn(TipoContraenza.CONTRAENTE);
       HibernateSessionFactoryUtil.beginTransaction();
       HibernateSessionFactoryUtil.getSession().save(rapCntn);
       HibernateSessionFactoryUtil.commitTransaction();
   }

   private void salvaTitoloEMovimentoLavorabile(Pol pol, long l) {
       RecordIdentifier rid = RecordIdentifier.getInitRecord();
       Hibernate.initialize(pol);
       VrnPol vrnPol = pol.getVrnPols().iterator().next();
       VrnRipRch vrnRipRsc = pol.getVrnRipRchs().iterator().next();
       OggAsct ogg = vrnPol.getOggAscts().iterator().next();
       SezPrst sez = ogg.getSezPrsts().iterator().next();
       Set<GarPrst> garanzie = sez.getGarPrsts();
       GarPrst gar = garanzie.iterator().next();
       Mov mov = new Mov(rid, pol, vrnPol, vrnRipRsc, l);
       mov.setSorgente(Source.XML);
       mov.setStatoMovimento(StatoMovimento.LAVORABILE);
       mov.setDatEfftMov(new Date());
       mov.setDatEmiDoc(new Date());
       TtlCtb ttl = new TtlCtb(rid, pol.getPunVnd(), mov, 2012, l);
       ttl.setCodCig("123");
       ttl.setCodSubAge("456");
       ttl.setImpPvgIncAcc(new BigDecimal((123456)));
       ttl.setImpPvgIncNet(new BigDecimal((123456)));
       ttl.setImpTotAcc(new BigDecimal((123456)));
       ttl.setImpTotIpt(new BigDecimal((123456)));
       ttl.setImpTotNet(new BigDecimal((123456)));
       ttl.setStatoTitolo(StatoTitolo.DA_INCASSARE);
       ttl.setPrctPvgIncAcc(new BigDecimal(100));
       ttl.setPrctPvgIncNet(new BigDecimal(10));
       ttl.setDatIniCpr(new Date());
       Calendar scadenzaCpr = Calendar.getInstance();
       scadenzaCpr.add(Calendar.YEAR, 1);
       ttl.setDatScaCpr(scadenzaCpr.getTime());
       mov.getTtlCtbs().add(ttl);
       RipDttTtl ripartoDtt = new RipDttTtl(rid, ttl, gar, vrnRipRsc);
       ripartoDtt.setCodCompPtfCoas(1);
       ripartoDtt.setCodCompPtf(1);
       ripartoDtt.setCodCnlVnd(2);//IPR
       ttl.getRipartoTitoloCoass().add(ripartoDtt);
       HibernateSessionFactoryUtil.beginTransaction();
       HibernateSessionFactoryUtil.getSession().saveOrUpdate(vrnPol);
       HibernateSessionFactoryUtil.getSession().saveOrUpdate(vrnRipRsc);
       HibernateSessionFactoryUtil.commitTransaction();
       HibernateSessionFactoryUtil.beginTransaction();
       HibernateSessionFactoryUtil.getSession().saveOrUpdate(mov);
       HibernateSessionFactoryUtil.getSession().save(ripartoDtt);
       HibernateSessionFactoryUtil.commitTransaction();
   }
}

