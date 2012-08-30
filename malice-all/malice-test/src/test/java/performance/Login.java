package performance;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/*
 * This class is mented for login to portale-gar
 */
public class Login {
    
    public static void main(String[] args) throws Exception {
        //faccio il login
        ArrayList<Boolean> numeroDiSuccessi = new ArrayList<Boolean>();
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver");
        int sleep=10000;
        for (int i = 0; i<2; i++) {
            WebDriver driver = new ChromeDriver();
            Boolean success = loginToPortaleGar(driver, sleep);
            if (success)
                numeroDiSuccessi.add(success);
            sleep -= 5000;
        }
            System.out.println("Numero di Successi:" + numeroDiSuccessi.size());
    }

    private static boolean loginToPortaleGar(WebDriver driver, int sleepTime) throws InterruptedException {
        // Go to kijiji home page
        driver.get("http://127.0.0.1:8080/portale-gar-PROD-2.1/");
        Thread.sleep(sleepTime);
        // Introduco l'email
        WebElement element;
        element = driver.findElement(By.name("j_username"));
        element.sendKeys("palermo.880@agenzie.realemutua.it");
        //Introduco la password
        element = driver.findElement(By.name("j_password"));
        element.sendKeys("123");
        driver.findElement(By.linkText("Entra")).click();
        if (driver.findElement(By.className("gwt-Anchor")).getText().equals("ALBA-103 - OMC: 176121"))
            return true;
        return false;
    }
}