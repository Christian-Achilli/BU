package database;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kp.malice.authentication.MaliceAuthenticationProvider;
import com.kp.malice.entities.business.TitoloLloyds;
import com.kp.malice.factories.IncassoLloydsFactory;
import com.kp.malice.repositories.DatabaseGatewayLloyds;
import com.kp.malice.repositories.TitoliRepository;
import com.kp.malice.useCases.DispatcherServiziPortale;

public class TestRicercaTitoli {

    @Test
    public void test_ricerca_titoli_in_periodo_di_copertura() throws Exception {
        DateTime start = new DateTime();
        MutableDateTime startM = start.toMutableDateTime();
        startM.addYears(-1);
        MutableDateTime end = start.toMutableDateTime();
        end.addYears(1);
        List<TitoloLloyds> lista = dispatcher.findAllTitoliInPeriodoInizioCopertura(startM.toDate(), end.toDate());

        Assert.assertNotNull(lista);
        Assert.assertFalse(lista.isEmpty());
    }

    DispatcherServiziPortale dispatcher;

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(new AbstractModule() {

            @Override
            protected void configure() {
                bind(TitoliRepository.class).to(DatabaseGatewayLloyds.class);
                bind(IncassoLloydsFactory.class);
                bind(MaliceAuthenticationProvider.class);
                bind(Authentication.class).to(AuthForTest.class);
            }

        });
        dispatcher = injector.getInstance(DispatcherServiziPortale.class);
        MaliceAuthenticationProvider authProvider = injector.getInstance(MaliceAuthenticationProvider.class);
        Authentication auth = injector.getInstance(Authentication.class);
        authProvider.authenticate(auth);
    }
}
