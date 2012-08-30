package functionality;

import java.io.IOException;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * {@link http://c.gwt-examples.com} 
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestGwtAppLoading extends TestCase {

    private static final String DEV_MODE_URL = "http://127.0.0.1:8888/MaliceWeb.html?gwt.codesvr=127.0.0.1:9997";
    private static final String AWS_TEST_URL = "http://pgar-test2.elasticbeanstalk.com/";
    private static final String LOCAL_TEST_URL = "http://127.0.0.1:8080/portale-gar-2.6.2";
    private static MaliceApplicationDriver applicationDriver;

    @BeforeClass
    public static void initChromeWebDriver() throws IOException {

        applicationDriver = new MaliceApplicationDriver(new GwtApplicationChromeDriver(), DEV_MODE_URL,
                "palermo.880@agenzie.realemutua.it", "sole457");

        //        applicationDriver = new MaliceApplicationDriver(new GwtApplicationChromeDriver(), AWS_TEST_URL,
        //                "palermo.880@agenzie.realemutua.it", "sole457");
    }

    @AfterClass
    public static void theEnd() {
        //        applicationDriver.quitDriver();
    }

    @Before
    public void before() {
        //Login
    }

    @After
    public void after() {
        //Logout
    }

    @Test
    public void testElement() {

        // navigate to gwt app - change this to your app url
        try {
            applicationDriver.startUpApplication();
            Thread.sleep(2000);
            applicationDriver.login();
            Thread.sleep(2000);
            applicationDriver.selectTabTitoli();
            Thread.sleep(2000);
            applicationDriver.apriDettaglioTitolo("SEAAAAA8LBC", "01");
            Thread.sleep(2000);
            applicationDriver.incassa();
            //            for (int i = 0; i < 2; i++) {
            //                Thread.sleep(2000);
            //                String dateStart = "10/12/2010";
            //                String dateEnd = "10/01/2011";
            //                DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
            //                DateTime d1 = dtf.parseDateTime(dateStart);
            //                DateTime d2 = dtf.parseDateTime(dateEnd);
            //                applicationDriver.cercaTitoliNelPeriodo(d1, d2);
            //                applicationDriver.selectTabChiusure();
            //                Thread.sleep(2000);
            //                applicationDriver.selectTabBenvenuto();
            //                Thread.sleep(2000);
            //            }
            applicationDriver.logout();
            System.out.println("finished");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
}