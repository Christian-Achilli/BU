package database;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kp.malice.entities.business.LioReferenceCode;
import com.kp.malice.factories.FilieraLloydsFactory;

public class FileraLloydsFactoryTest {
	private FilieraLloydsFactory filieraLloydsFactory;
	
	public FileraLloydsFactoryTest() {
		Injector inj = Guice.createInjector(new AbstractModule() {

            @Override
            protected void configure() {
                bind(FilieraLloydsFactory.class);
            }
        });
		this.filieraLloydsFactory = inj.getInstance(FilieraLloydsFactory.class);
	}

	@Test
	public void getListLioReferenceCodeTest() {
		Assert.assertTrue(true);
		List<LioReferenceCode> lioReferenceCodes = filieraLloydsFactory.getListLioReferenceCode();
		Assert.assertEquals(4, lioReferenceCodes.size());
	}

}
