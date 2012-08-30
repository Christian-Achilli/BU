package useCases;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.security.core.Authentication;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kp.malice.authentication.MaliceAuthenticationProvider;
import com.kp.malice.entities.business.ChiusuraMensileLio;
import com.kp.malice.entities.persisted.ChrMslLio;
import com.kp.malice.entities.persisted.HibernateSessionFactoryUtil;
import com.kp.malice.factories.ChiusuraMensileLioFactory;
import com.kp.malice.factories.EstrattoContoFactory;
import com.kp.malice.factories.FilieraLloydsFactory;
import com.kp.malice.repositories.ChiusureMensiliLioRepository;
import com.kp.malice.repositories.DatabaseGatewayLloyds;
import com.kp.malice.repositories.EstrattoContoRepository;
import com.kp.malice.repositories.TitoliRepository;
import com.kp.malice.useCases.DispatcherServiziPortale;
import com.kp.malice.useCases.FunzioniChiusura;

import database.AuthForTest;

public class Test_Invio_Chiusura {

    DispatcherServiziPortale dispatcher;
    AuthForTest auth;
    private FunzioniChiusura useCase;
    private EstrattoContoRepository repoEC;
    private EstrattoContoFactory factoryEC;
    private ChiusureMensiliLioRepository chrRepo;
    private ChiusuraMensileLioFactory chrFactory;
    private FilieraLloydsFactory filieraFactory;

    @Test
    public void inviaChiusura() throws Exception {
        ChiusuraMensileLio chr = new ChiusuraMensileLio();
        chr.setId(20L);
        useCase.inviaChiusura(chr, auth.getPuntiVendita().get(0).getAgenzia());
    }

    @Test
    @Ignore
    public void creaEstrattoConto() throws Exception {
        DateTime periodo = new DateTime(3012, 3, 1, 0, 0);
        HibernateSessionFactoryUtil.beginTransaction();
        useCase.creaChiusuraSeNonEsisteNelPeriodoCercatoONelPrimoPeriodoSuccessivoDisponibile(auth.getPuntiVendita()
                .get(0).getAgenzia(), periodo.toDate());
        HibernateSessionFactoryUtil.commitTransaction();
    }

    @Test
    @Ignore
    public void trova_quella_da_inviare_recuperando_la_piu_vecchia_chiusura_aperta() throws Exception {
        DateTime periodo = new DateTime(3012, 3, 1, 0, 0);
        useCase.creaChiusuraSeNonEsisteNelPeriodoCercatoONelPrimoPeriodoSuccessivoDisponibile(auth.getPuntiVendita()
                .get(0).getAgenzia(), periodo.toDate());
        periodo = new DateTime(3012, 4, 1, 0, 0);
        useCase.creaChiusuraSeNonEsisteNelPeriodoCercatoONelPrimoPeriodoSuccessivoDisponibile(auth.getPuntiVendita()
                .get(0).getAgenzia(), periodo.toDate());
        ChiusuraMensileLio daInviare = chrRepo.getNextToBeSent(auth.getPuntiVendita().get(0).getAgenzia());
        long id1 = daInviare.getId();
        Assert.assertEquals(3, daInviare.getReferenceCalendarMonth());
        dispatcher.inviaChiusura("nota");
        daInviare = chrRepo.getNextToBeSent(auth.getPuntiVendita().get(0).getAgenzia());
        long id2 = daInviare.getId();
        Assert.assertEquals(4, daInviare.getReferenceCalendarMonth());
        cleanUpTable(id1, id2);
    }

    private void cleanUpTable(long id1, long id2) {
        HibernateSessionFactoryUtil.beginTransaction();
        HibernateSessionFactoryUtil.getSession().delete(
                HibernateSessionFactoryUtil.getSession().load(ChrMslLio.class, id1));
        HibernateSessionFactoryUtil.getSession().delete(
                HibernateSessionFactoryUtil.getSession().load(ChrMslLio.class, id2));
        HibernateSessionFactoryUtil.commitTransaction();
    }

    @After
    public void fine() {
    }

    @Before
    public void inizio() {
        Injector inj = Guice.createInjector(new AbstractModule() {

            @Override
            protected void configure() {
                bind(FunzioniChiusura.class);
                bind(EstrattoContoRepository.class);
                bind(EstrattoContoFactory.class);
                bind(ChiusureMensiliLioRepository.class).asEagerSingleton();
                bind(ChiusuraMensileLioFactory.class);
                bind(MaliceAuthenticationProvider.class);
                bind(TitoliRepository.class).to(DatabaseGatewayLloyds.class).asEagerSingleton();
                bind(Authentication.class).to(AuthForTest.class);
                bind(FilieraLloydsFactory.class);
            }
        });
        useCase = inj.getInstance(FunzioniChiusura.class);
        repoEC = inj.getInstance(EstrattoContoRepository.class);
        chrRepo = inj.getInstance(ChiusureMensiliLioRepository.class);
        chrFactory = inj.getInstance(ChiusuraMensileLioFactory.class);
        dispatcher = inj.getInstance(DispatcherServiziPortale.class);
        MaliceAuthenticationProvider authProvider = inj.getInstance(MaliceAuthenticationProvider.class);
        auth = (AuthForTest) inj.getInstance(Authentication.class);
        authProvider.authenticate(auth);
        filieraFactory = inj.getInstance(FilieraLloydsFactory.class);
    }

}
