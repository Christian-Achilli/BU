package functionality;

import java.util.Arrays;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.kp.malice.client.MaliceDebugIds;

public class GwtApplicationChromeDriver {

    private static final Logger log = Logger.getLogger(GwtApplicationChromeDriver.class);

    private static final String GWT_EXTENSION_LOCATION = "/Users/christianachilli/Library/Application Support/Google/Chrome/Default/Extensions/jpjpnpmbddbjkfaccnmhnkdgjideieim/1.0.9738_0/Darwin-gcc3/gwtDev.plugin";
    private static final String CHROME_DRIVER_FILE = "/Users/christianachilli/MAVEN/malice-all/malice-test/chromedriver";
    private final WebDriver driver;

    public GwtApplicationChromeDriver() {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE);
        // chrome driver will load with no extras, so lets tell it to load with gwtdev plugin
        // get plugin here in chrome url type > "chrome://plugins/" > hit top right details + > find gwt dev mode pluging location:
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        String gwtDevPluginPath = "--load-plugin=" + GWT_EXTENSION_LOCATION;
        capabilities.setCapability("chrome.switches", Arrays.asList(gwtDevPluginPath));
        // init driver with GWT Dev Plugin
        driver = new ChromeDriver(capabilities);
    }

    public void openBrowserAtUrl(String url) {
        driver.get(url);
    }

    public WebElement clickElementWithDebugId(String debugId) throws Exception {
        WebElement _element = driver.findElement(By.id(MaliceDebugIds.GWT_COMMON_DBG_PREFIX + debugId));
        _element.click();
        return _element;
    }

    public void quitDriver() {
        driver.quit();
    }

    public void sendKeysToElementWithHtmlName(String name, String keys) {
        WebElement element = driver.findElement(By.name(name));
        element.sendKeys(keys);
    }

    public void clickFirstElementDisplayingText(String displayedTExt) {
        driver.findElement(By.linkText(displayedTExt)).click();
    }

    public void selezionaData(DateTime _dataDaSelezionare) throws Exception {
        selezionaMeseEGiornoSulCalendario(_dataDaSelezionare);
    }

    private boolean isLoaderVisible() {
        try {
            WebElement _element = driver.findElement(By
                    .id(MaliceDebugIds.GWT_COMMON_DBG_PREFIX + MaliceDebugIds.LOADER));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void selezionaGiorno(DateTime _dataDaSelezionare) throws Exception {
        try {
            WebElement giornoElement = driver.findElement(By.xpath("//td[@class='datePickerDay ' and (text()='"
                    + _dataDaSelezionare.getDayOfMonth() + "')]"));
            giornoElement.click();
            attendi(1500);
            while (isLoaderVisible()) {
                log.info("LAODER ANCORA ATTIVO");
                attendi(1000);
            }
        } catch (Exception e) {
            log.info("UNRESOLVED XPATH: " + "//td[@class='datePickerDay ' and (text()='"
                    + _dataDaSelezionare.getDayOfMonth() + "')]");
            throw e;
        }
    }

    private void selezionaMeseEGiornoSulCalendario(DateTime _dataDaSelezionare) throws Exception {
        DateTime meseVisualizzato = getMeseVisualizzatoInCalendario();
        int differenzaMesi = Months.monthsBetween(meseVisualizzato, _dataDaSelezionare).getMonths();
        if (differenzaMesi > 0) {
            spostaMeseAvanti(differenzaMesi);
        } else if (differenzaMesi < 0) {
            spostaMeseIndiertro(Math.abs(differenzaMesi));
        }
        selezionaGiorno(_dataDaSelezionare);
    }

    private void spostaMeseAvanti(int differenzaMesi) {
        for (int i = 0; i < differenzaMesi; i++) {
            WebElement avantiMese = driver.findElement(By.xpath("//div[@class='html-face' and (text()='»')]"));
            avantiMese.click();
        }
    }

    private void spostaMeseIndiertro(int differenzaMesi) {
        for (int i = 0; i < differenzaMesi; i++) {
            WebElement indietroMese = driver.findElement(By.xpath("//div[@class='html-face' and (text()='«')]"));
            indietroMese.click();
        }
    }

    private DateTime getMeseVisualizzatoInCalendario() {
        WebElement calendarioHeader = driver.findElement(By.xpath("//td[@class='datePickerMonth']"));
        DateTimeFormatter dtf = DateTimeFormat.forPattern("MMM yyyy").withLocale(Locale.ITALY);
        DateTime meseVisualizzato = dtf.parseDateTime(calendarioHeader.getText());
        return meseVisualizzato;
    }

    private void attendi(long lapse) {
        try {
            Thread.sleep(lapse);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("MMM yyyy");
        DateTimeFormatter dtfLocalized = dtf.withLocale(Locale.ITALY);
        DateTime meseVisualizzato = dtfLocalized.parseDateTime("lug 2012");
        System.out.println(meseVisualizzato);
    }
}
