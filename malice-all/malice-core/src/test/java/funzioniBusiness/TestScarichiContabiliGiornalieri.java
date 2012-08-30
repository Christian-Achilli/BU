package funzioniBusiness;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import jxl.write.WritableWorkbook;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kp.malice.boundaries.services.PortaleServiceBoundary;
import com.kp.malice.entities.miscellaneous.ScritturaContabileRma;
import com.kp.malice.repositories.DatabaseGatewayLloyds;
import com.kp.malice.repositories.TitoliRepository;
import com.kp.malice.useCases.DispatcherServiziPortale;
import com.kp.malice.useCases.PopolaFileExcel;
import com.kp.malice.useCases.ScaricoIncassiExcelGenerator;
import com.kp.malice.useCases.ScaricoTitoliExcelGenerator;

public class TestScarichiContabiliGiornalieri {

	private DispatcherServiziPortale dispaccio;
	private ScaricoTitoliExcelGenerator classUnderTest;

	@Before
	public void setUp() {
		Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(PortaleServiceBoundary.class).to(DispatcherServiziPortale.class).asEagerSingleton();
				bind(TitoliRepository.class).to(DatabaseGatewayLloyds.class);
				bind(PopolaFileExcel.class);
			}

		});
		dispaccio = injector.getInstance(DispatcherServiziPortale.class);
		classUnderTest = injector.getInstance(ScaricoTitoliExcelGenerator.class);
	}


	@Test
	public void scrivi_un_titolo_su_foglio_Excel() throws Exception{
		Date d = new Date();

		List<ScritturaContabileRma> result = dispaccio.findListByDataRegistrazioneIncasso(d);
		ScaricoIncassiExcelGenerator scarico = new ScaricoIncassiExcelGenerator(dispaccio);
		Calendar c = Calendar.getInstance();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Incassi ");
		stringBuilder.append(c.getTime().toLocaleString());
		stringBuilder.append(".xls");
		String nomeFile = stringBuilder.toString();
		FileOutputStream file = new FileOutputStream(nomeFile);
		scarico.generaExcelSuOutputStream(d, file);
		file.flush();
		file.close();
	}

}
