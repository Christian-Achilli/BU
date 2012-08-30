package functionality;

import org.joda.time.DateTime;

import com.kp.malice.client.MaliceDebugIds;

public class MaliceApplicationDriver {

    private final GwtApplicationChromeDriver driver; // make it an interface to use other browsers
    private final String appUrl;
    private String username;
    private String password;
    private final MaliceAuthenticationProcedure authenticationActor;
    private final MaliceTitoliProcedures titoliActor;

    public MaliceApplicationDriver(GwtApplicationChromeDriver chromeDriver, String appUrl, String username,
            String password) {
        this.driver = chromeDriver;
        this.appUrl = appUrl;
        this.username = username;
        this.password = password;
        authenticationActor = new MaliceAuthenticationProcedure(chromeDriver);
        titoliActor = new MaliceTitoliProcedures(chromeDriver);
    }

    public void apriDettaglioTitolo(String numeroTitolo, String numeroRata) throws Exception {
        titoliActor.apriDettaglioTitolo(numeroTitolo, numeroRata);
    }

    public void cercaTitoliNelPeriodo(DateTime from, DateTime until) throws Exception {
        titoliActor.searchTitoliInTimeLapse(from, until);
    }

    public void startUpApplication() {
        driver.openBrowserAtUrl(appUrl);
    }

    public void selectTabTitoli() throws Exception {
        driver.clickElementWithDebugId(MaliceDebugIds.TITOLI_DBG_ID);
    }

    public void selectTabChiusure() throws Exception {
        driver.clickElementWithDebugId(MaliceDebugIds.CHIUSURE_DBG_ID);
    }

    public void selectTabBenvenuto() throws Exception {
        driver.clickElementWithDebugId(MaliceDebugIds.BENVENUTO_DBG_ID);
    }

    public void login() {
        authenticationActor.login(username, password);
    }

    public void logout() {
        authenticationActor.logout();
    }

    public void quitDriver() {
        driver.quitDriver();
    }

    public void incassa() throws Exception {
        titoliActor.incassa();
    }

}
