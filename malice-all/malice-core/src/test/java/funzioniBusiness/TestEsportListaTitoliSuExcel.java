package funzioniBusiness;

import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kp.malice.repositories.DatabaseGatewayLloyds;
import com.kp.malice.repositories.TitoliRepository;
import com.kp.malice.useCases.PopolaFileExcel;
import com.kp.malice.useCases.ScaricoTitoliExcelGenerator;

public class TestEsportListaTitoliSuExcel {

	private TitoliRepository repo;
	private PopolaFileExcel classUnderTest;

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(new AbstractModule() {

            @Override
            protected void configure() {
                bind(TitoliRepository.class).to(DatabaseGatewayLloyds.class).asEagerSingleton();
                bind(ScaricoTitoliExcelGenerator.class);
            }

        });
        repo = injector.getInstance(TitoliRepository.class);
        classUnderTest = injector.getInstance(PopolaFileExcel.class);
    }
	

	@Test
	public void scrivi_un_titolo_su_foglio_Excel() throws Exception{
		Calendar c = Calendar.getInstance();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Titoli ");
		stringBuilder.append(c.getTime().toLocaleString());
		stringBuilder.append(".xls");
		String nomeFile = stringBuilder.toString();
		ScaricoTitoliExcelGenerator scarico = new ScaricoTitoliExcelGenerator(repo);
		String parameterValue="1,2,3,4,5,6,7,8,9";
		StringBuilder stringToGet = new StringBuilder();
        stringToGet.append(parameterValue + ",");
        Date today = new Date();
        stringToGet.append("Lista Titoli Dell' " + 1 + ".xls");
        stringToGet.append(",1"); //Identificatore del tipo di download
		String[] idTitoliString = parameterValue.split(",");
		List<String> idList = Arrays.asList(idTitoliString);
		FileOutputStream file = new FileOutputStream(nomeFile);
		scarico.generaExcelSuOutputStream(idList, file);
		file.flush();
		file.close();
	}
	
}

