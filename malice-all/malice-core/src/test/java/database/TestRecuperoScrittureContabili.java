package database;

import org.junit.Before;
import org.springframework.security.core.Authentication;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kp.malice.authentication.MaliceAuthenticationProvider;
import com.kp.malice.factories.IncassoLloydsFactory;
import com.kp.malice.useCases.RecuperaScrittureContabiliRma;

public class TestRecuperoScrittureContabili {

    private RecuperaScrittureContabiliRma underTest;

//    @Test
//    public void provaQuery() throws Exception {
//        underTest = new RecuperaScrittureContabiliRma(new IncassoLloydsFactory(), new Disp);
//        org.joda.time.DateTime dt = new org.joda.time.DateTime(2012, 4, 23, 0, 0);
//        List<ScritturaContabileRma> lista = underTest.getListaScrittureByDataRegistrazione(dt.toDate());
//        underTest.getListaIncassiByScritturaContabile(lista.get(0));
//
//    }

    @Before
    public void setUp() {
        Injector inj = Guice.createInjector(new AbstractModule() {

            @Override
            protected void configure() {
                bind(RecuperaScrittureContabiliRma.class);
                bind(IncassoLloydsFactory.class);
                bind(MaliceAuthenticationProvider.class);
                bind(Authentication.class).to(AuthForTest.class);

            }
        });
        underTest = inj.getInstance(RecuperaScrittureContabiliRma.class);
        MaliceAuthenticationProvider authProvider = inj.getInstance(MaliceAuthenticationProvider.class);
        Authentication auth = inj.getInstance(Authentication.class);
        authProvider.authenticate(auth);
    }
}
