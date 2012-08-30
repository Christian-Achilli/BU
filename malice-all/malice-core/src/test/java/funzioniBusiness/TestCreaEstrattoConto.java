package funzioniBusiness;

import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kp.malice.repositories.DatabaseGatewayLloyds;
import com.kp.malice.repositories.TitoliRepository;
import com.kp.malice.useCases.CreaEstrattiContoLio;

public class TestCreaEstrattoConto {

    private TitoliRepository repo;
    private CreaEstrattiContoLio classUnderTest;

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(new AbstractModule() {

            @Override
            protected void configure() {
                bind(TitoliRepository.class).to(DatabaseGatewayLloyds.class).asEagerSingleton();
                bind(CreaEstrattiContoLio.class);
            }

        });
        repo = injector.getInstance(TitoliRepository.class);
        classUnderTest = injector.getInstance(CreaEstrattiContoLio.class);
    }

    @Test
    public void scrivi_un_titolo_su_foglio_Excel() throws Exception {
        Calendar c = Calendar.getInstance();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Estratto Conto ");
        stringBuilder.append(c.getTime().toLocaleString());
        stringBuilder.append(".xls");
        String nomeFile = stringBuilder.toString();
        CreaEstrattiContoLio scarico = new CreaEstrattiContoLio();
        String parameterValue = "1,2,3,4,5,6,7,8,9";
        String[] idTitoliString = parameterValue.split(",");
        List<String> idList = Arrays.asList(idTitoliString);
        FileOutputStream file = new FileOutputStream(nomeFile);
        //        scarico.generaExcelSuOutputStream(idList, file); // TODO
        file.flush();
        file.close();
    }
}
