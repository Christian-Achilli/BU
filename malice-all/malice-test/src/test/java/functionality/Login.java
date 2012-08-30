package functionality;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.AfterClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/*
 * This class is mented for login to portale-gar
 * Uses testng for parallel testing the simulation through webdriver.
 */

public class Login {

    public WebDriver driver;

    @Parameters({ "browser" })
    @BeforeClass
    public void setup(String browser) throws MalformedURLException {
        DesiredCapabilities capability = null;

        if (browser.equalsIgnoreCase("chrome")) {
            System.out.println("chrome");
            capability = DesiredCapabilities.chrome();
            capability.setBrowserName("chrome");
            capability.setPlatform(org.openqa.selenium.Platform.ANY);
            //capability.setVersion("");
        }

        if (browser.equalsIgnoreCase("firefox")) {
            System.out.println("firefox");
            capability = DesiredCapabilities.firefox();
            capability.setBrowserName("firefox");
            capability.setPlatform(org.openqa.selenium.Platform.ANY);
            //capability.setVersion("");
        }

        if (browser.equalsIgnoreCase("iexplore")) {
            System.out.println("iexplore");
            capability = DesiredCapabilities.internetExplorer();
            capability.setBrowserName("iexplore");
            capability.setPlatform(org.openqa.selenium.Platform.WINDOWS);
            //capability.setVersion("");
        }

        driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capability);

    }

    @Parameters({ "username", "password" })
    @Test
    public void loginToPortaleGar(String userName, String pass) throws InterruptedException {
        Thread.sleep(3000);
        // Introduco l'email
        WebElement element = driver.findElement(By.name("j_username"));
        element.sendKeys(userName);
        //Introduco la password
        element = driver.findElement(By.name("j_password"));
        element.sendKeys(pass);
        driver.findElement(By.linkText("Entra")).click();
    }

    //    @Parameters({ "username", "password" })
    //    @Test
    //    public void loginToPortaleGar(String userName, String pass) throws InterruptedException {
    //        driver.navigate().to("http://pgar-test2.elasticbeanstalk.com/");
    //        Thread.sleep(3000);
    //        // Introduco l'email
    //        WebElement element;
    //        element = driver.findElement(By.name("j_username"));
    //        element.sendKeys(userName);
    //        //Introduco la password
    //        element = driver.findElement(By.name("j_password"));
    //        element.sendKeys(pass);
    //        driver.findElement(By.linkText("Entra")).click();
    //    }

    @Test
    public void recuperaTitoli() throws InterruptedException {
        int numMesiPrima = 4;
        Thread.sleep(1000);
        //click sul Tab TITOLI
        driver.findElement(By.xpath("//a[contains(text(),'Titoli')]")).click();
        //cerco la data Giusta
        driver.findElement(By.cssSelector("input.gwt-DateBox")).click();
        WebElement mesePrecedente = driver.findElement(By
                .xpath("//div[@class='datePickerPreviousButton datePickerPreviousButton-up']/div[@class='html-face']"));
        WebElement giorno = driver.findElement(By.xpath("//td[@class='datePickerDay '][td=19]"));
        giorno.click();
        //        for (int mPrecedente = 0; mPrecedente < numMesiPrima; mPrecedente++) {
        //            mesePrecedente.click();
        //        }
        //        driver.findElement(By.xpath("//td/table/tbody/tr[4]/td[4]")).click();
        //        driver.findElement(By.xpath("//a[contains(text(),'Titoli')]")).click();
    }

    //    @Test
    //    public void scaricaTitoli(){
    //        driver.findElement(By.cssSelector("button.GASURGJAN.GASURGJDL")).click();
    //    }

    //    @Test
    //    public void eseguiChiusura() {
    //        driver.findElement(By.linkText("Chiusure")).click();
    //        driver.findElement(By.xpath("//div[5]/div/div/div/div/div[2]/div/div/div/div/div/table/tbody/tr/td[4]/div")).click();
    //        driver.findElement(By.xpath("//div[2]/div[2]/div/div/div/div/div/table/tbody/tr/td[2]"));
    //    }

    //    private String getIncassati() throws InterruptedException {
    //        //mi sposto sulla pagina di benvenuto e ritorno il numero degli incassati
    //        driver.findElement(By.linkText("Titoli")).click();
    //        String incassati = driver.findElement(By.xpath("//div[h1='Titoli Incassati']/div[@class='gwt-Label']")).getText();
    //        return incassati;
    //    }
    //    private String getSospesi() throws InterruptedException {
    //        //mi sposto sulla pagina di benvenuto e ritorno il numero degli incassati
    //        driver.findElement(By.linkText("Benvenuto")).click();
    //        String sospesi = driver.findElement(By.xpath("//div[h1='Titoli Sospesi']/div[@class='gwt-Label']")).getText();
    //        return sospesi;
    //    }
    //    private String getPremiIncassati() throws InterruptedException {
    //        //mi sposto sulla pagina di benvenuto e ritorno il numero degli incassati
    //        driver.findElement(By.linkText("Benvenuto")).click();
    //        String premiIncassatiPre = driver.findElement(By.xpath("//div[h1='Premi Incassati']/div[@class='gwt-Label']")).getText();
    //        return premiIncassatiPre;
    //    }
    //    private String getTotaleProvvigioni() throws InterruptedException {
    //        //mi sposto sulla pagina di benvenuto e ritorno il numero degli incassati
    //        driver.findElement(By.linkText("Benvenuto")).click();
    //        String totaleProvvigioni = driver.findElement(By.xpath("//div[h1='Totale Provvigioni']/div[@class='gwt-Label']")).getText();
    //        return totaleProvvigioni;
    //    }
    //    private String getTitoloPremiIncassati() throws InterruptedException {
    //        //mi sposto sulla pagina dei titoli e ritorno il numero degli incassati
    //        driver.findElement(By.linkText("Titoli")).click();
    //        String premiIncassati = driver.findElement(By.xpath("//td[8]/div")).getText();
    //        return premiIncassati;
    //    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
