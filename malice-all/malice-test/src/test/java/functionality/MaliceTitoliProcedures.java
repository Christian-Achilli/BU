package functionality;

import org.joda.time.DateTime;

import com.kp.malice.client.MaliceDebugIds;

public class MaliceTitoliProcedures {

    private final GwtApplicationChromeDriver driver;

    public MaliceTitoliProcedures(GwtApplicationChromeDriver chromeDriver) {
        this.driver = chromeDriver;
    }

    public void incassa() throws Exception {
        driver.clickElementWithDebugId(MaliceDebugIds.BTN_DTT_TTL_INCASSO);
    }

    public void searchTitoliInTimeLapse(DateTime lapseBegin, DateTime lapseEnd) throws Exception {
        if (null != lapseBegin)
            cercaTitoliDecorrenzaDal(lapseBegin);
        if (null != lapseEnd)
            cercaTitoliDecorrenzaAl(lapseEnd);
    }

    public void apriDettaglioTitolo(String numeroTitolo, String numeroRata) throws Exception {
        driver.clickElementWithDebugId(numeroTitolo + "-" + numeroRata);
    }

    private void cercaTitoliDecorrenzaDal(DateTime dataDaSelezionare) throws Exception {
        apriCalendarioDecorrenzaDal();
        selezionaDataSulCalendario(dataDaSelezionare);
        attendi(3000);
    }

    private void cercaTitoliDecorrenzaAl(DateTime dataDaSelezionare) throws Exception {
        apriCalendarioDecorrenzaAl();
        selezionaDataSulCalendario(dataDaSelezionare);
        attendi(3000);
    }

    private void apriCalendarioDecorrenzaDal() throws Exception {
        driver.clickElementWithDebugId(MaliceDebugIds.START_DATE);
        attendi(1000);
    }

    private void apriCalendarioDecorrenzaAl() throws Exception {
        driver.clickElementWithDebugId(MaliceDebugIds.END_DATE);
        attendi(1000);
    }

    private void selezionaDataSulCalendario(DateTime dataDaSelezionare) throws Exception {
        driver.selezionaData(dataDaSelezionare);
        attendi(1000);
    }

    private void attendi(long lapse) {
        try {
            Thread.sleep(lapse);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
