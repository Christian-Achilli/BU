package com.kp.marsh.ebt.server.importer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kp.marsh.ebt.client.admin.services.ProcessoreService;
import com.kp.marsh.ebt.server.importer.csvimport.Achieved;
import com.kp.marsh.ebt.server.importer.csvimport.AnagraficheCE;
import com.kp.marsh.ebt.server.importer.csvimport.ElaboraAnagCE;
import com.kp.marsh.ebt.server.importer.csvimport.ElaboraMAchievedPerImportAchieved;
import com.kp.marsh.ebt.server.importer.csvimport.ElaboraMAchievedPerImportGruppiCommerciali;
import com.kp.marsh.ebt.server.importer.csvimport.GruppiCommerciali;
import com.kp.marsh.ebt.server.importer.csvimport.importers.CsvImporter;
import com.kp.marsh.ebt.server.importer.util.IReadProperties;
import com.kp.marsh.ebt.server.webapp.GuiceRemoteServiceServlet;
import com.kp.marsh.ebt.server.webapp.utils.AnnotatedHibernateUtil;

@Singleton
public class ProcessoreServiceImpl extends GuiceRemoteServiceServlet implements ProcessoreService {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static Logger log = Logger.getLogger(ProcessoreServiceImpl.class);

    private ElaboraMAchievedPerImportGruppiCommerciali gruppiCommercialiDAO;
    private ElaboraMAchievedPerImportAchieved ema; // DAO per achieved
    private ElaboraAnagCE anagCE; // DAO per anagrafiche
    private AnnotatedHibernateUtil dbCon; // serve solo per impostare le proprietˆ caricate dal file

    private CsvImporter achievedImporter; // da csv a db M_ACHIEVED

    private CsvImporter anagraficaImporter; // da csv a db M_OFF_CE_ACCOUNTS

    private CsvImporter gruppiCommercialiImporter; // da csv a db M_ACHIEVED 

    @Inject
    public ProcessoreServiceImpl(@Achieved IReadProperties achievedProperties,
            @AnagraficheCE IReadProperties anagraficaProperties, @Achieved CsvImporter achievedImporter,
            @AnagraficheCE CsvImporter anagraficaImporter, @GruppiCommerciali CsvImporter gruppiCommercialiImporter,
            ElaboraMAchievedPerImportAchieved ema, ElaboraAnagCE anagCE,
            ElaboraMAchievedPerImportGruppiCommerciali gruppiCommercialiDAO, AnnotatedHibernateUtil dbCon) {
        this.achievedImporter = achievedImporter;
        this.anagraficaImporter = anagraficaImporter;
        this.ema = ema;
        this.anagCE = anagCE;
        this.dbCon = dbCon;
        this.gruppiCommercialiImporter = gruppiCommercialiImporter;
        this.gruppiCommercialiDAO = gruppiCommercialiDAO;

    }

    @Override
    public String configuraDb(String fileDbConnectionProperties) {

        StringBuffer result = new StringBuffer();

        // elaboro il file dbconnection.properties
        if (!fileDbConnectionProperties.equals("")) {
            log.debug("File dbConnection specificato come argomento: " + fileDbConnectionProperties);
            InputStream is = getInputStream(fileDbConnectionProperties);
            if (is != null) {
                log.info("File dbConnection specificato come argomento trovato.");
                Properties p = new Properties();
                try {
                    p.load(is);
                    Enumeration it = p.keys();
                    while (it.hasMoreElements()) {
                        String key = (String) it.nextElement();
                        String value = p.getProperty(key);
                        result.append("\n\t" + key + " : " + value);
                    }
                    dbCon.setDBConnectionProperties(p);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    result.append(e.getLocalizedMessage());

                } catch (IOException e) {
                    e.printStackTrace();
                    result.append(e.getLocalizedMessage());
                }

            } else {
                log.error("File dbConnection specificato come argomento non trovato.");
                result.append("File non trovato");
            }

        } else {
            log.debug("File dbconnection non specificato come argomento, sara' utilizzata la configurazione di default.");
            result.append("File non trovato, passaggio alla configurazione di default");

        }

        return result.toString();
    }

    /**
     * @param fileDbConnectionProperties
     * @return
     */
    private InputStream getInputStream(String fileDbConnectionProperties) {
        log.debug("Entro in getInputStream con argomento: " + fileDbConnectionProperties);
        InputStream is = null;
        try {
            // un hack potrebbe essere cambiare 89.186.66.106 in 192.168.100.11. forse rimane il problema del certificato.

            URL url = new URL(fileDbConnectionProperties.replaceFirst("89.186.66.106", "192.168.100.11"));
            log.debug("Opening connection to " + url + "...");
            URLConnection urlC = url.openConnection();
            is = urlC.getInputStream();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            log.error("Malformed url!!!!!!", e);

        } catch (IOException e) {
            e.printStackTrace();
            log.error("Io exception!!!", e);

        }
        return is;
    }

    @Override
    public String importaAnagrafica(String fileName) throws Exception {
        anagraficaImporter.clearTable();
        InputStream is = getInputStream(fileName);
        InputStreamReader isr = new InputStreamReader(is);
        StringBuffer result = new StringBuffer(anagraficaImporter.doImport(isr));
        result.append(anagCE.spostaCEDiUfficio()); // elabora la tabella del DB e riversa su information owners l'analisi effettuata
        return result.toString();
    }

    @Override
    public String importaAchieved(String fileName) throws Exception {
        achievedImporter.clearTable();
        InputStream is = getInputStream(fileName);
        InputStreamReader isr = new InputStreamReader(is);
        StringBuffer result = new StringBuffer(achievedImporter.doImport(isr));
        result.append(ema.elaboraAchieved()); // crea o aggiorna su business information i dati di achieved per ogni gruppo commerciale che si trova su information_owners prendendo io dati di achieved da M_ACHIEVED
        return result.toString();
    }

    /* (non-Javadoc)
     * @see com.kp.marsh.ebt.client.importer.services.ProcessoreService#importaGruppiCommerciali(java.lang.String)
     * Pulisce la tabella M_ACHIEVED.
     * Carica il file csv nella tabella M_ACHIEVED
     * Esegue l'import.
     * Sincronizza i gruppi commerciali da disabilitare
     */
    @Override
    public String importaGruppiCommerciali(String fileName) throws Exception {
        gruppiCommercialiImporter.clearTable();
        InputStream is = getInputStream(fileName);
        InputStreamReader isr = new InputStreamReader(is);
        StringBuffer result = new StringBuffer(gruppiCommercialiImporter.doImport(isr)); // carica il file csv su M_ACHIEVED
        result.append(gruppiCommercialiDAO.importaGruppiCommerciali()); // importa i gruppi commerciali su INFORMATION_OWNERS

        //		result.append(gruppiCommercialiDAO.sincronizza());		

        return result.toString();
    }

    @Override
    public String getParametriDB() {
        dbCon.getSessionFactory(); // inizializza i parametri di connessione al db
        Properties p = dbCon.getConfiguration().getProperties();
        StringBuffer result = new StringBuffer();
        Enumeration it = p.keys();
        while (it.hasMoreElements()) {
            String key = (String) it.nextElement();
            String value = p.getProperty(key);
            result.append("\n\t" + key + " : " + value);
        }
        return result.toString();
    }

}
