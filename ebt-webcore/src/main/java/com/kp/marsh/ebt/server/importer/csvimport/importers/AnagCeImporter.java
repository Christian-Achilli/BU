package com.kp.marsh.ebt.server.importer.csvimport.importers;

import java.io.Reader;

import net.sf.flatpack.DataSet;
import net.sf.flatpack.DefaultParserFactory;
import net.sf.flatpack.Parser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.kp.marsh.ebt.server.importer.csvimport.AnagraficheCE;
import com.kp.marsh.ebt.server.importer.csvimport.CsvCeDTO;
import com.kp.marsh.ebt.server.importer.dao.ImportController;
import com.kp.marsh.ebt.server.importer.model.MOffCeAccounts;

public class AnagCeImporter extends CsvImporter {
    private static Log log = LogFactory.getLog(AchievedImporter.class);
    private String configFile;
    private ImportController importControllerDao;

    /**
     * @param csvSource the csv source file reader
     * @param xmlConfigFile the configuration file for FlatPack
     */
    @Inject
    public AnagCeImporter(@AnagraficheCE String fileName, ImportController importControllerDAO) {
        this.configFile = fileName;
        this.importControllerDao = importControllerDAO;

    }

    /**
     * Inizializza la tabella M_OFF_CE_ACCOUNTS
     */
    @Override
    public void clearTable() {
        log.debug("Pulisco la table M_OFF_CE_ACCOUNTS");
        importControllerDao.pulisciAnagCe();

    }

    /* (non-Javadoc)
     * @see com.kp.marsh.csvimport.ICsvImport#doImport(java.io.Reader)
     */
    @Override
    public String doImport(Reader csvSource) throws Exception {

        int imported = 0;
        try {
            Parser parser = DefaultParserFactory.getInstance().newDelimitedParser(getPzMapStream(configFile),
                    csvSource, ',', '"', true);
            DataSet data = parser.parse();
            log.debug(" Righe nel file csv: " + data.getRowCount());
            for (int i = 0; i < data.getRowCount(); i++) {
                if (data.next()) {
                    // creazione del DTO
                    CsvCeDTO temp = new CsvCeDTO();
                    String codEuroSys = data.getString("Accexe");
                    if (codEuroSys == null || codEuroSys.length() <= 0) {
                        log.debug("ATTENZIONE!!!! non trovato codice euroSys per ce alla riga " + (i + 1)
                                + " del file csv che si sta importando. Il CE in questione non verrˆ importato!!!");
                        continue;
                    }
                    String codUfficio = data.getString("CodSoc");
                    if (codUfficio == null || codUfficio.length() <= 0) {
                        log.debug("ATTENZIONE!!!! non trovato codice ufficio per ce alla riga " + (i + 1)
                                + " del file csv che si sta importando. Il CE in questione non verrˆ importato!!!");
                        continue;
                    }
                    temp.setCodiceEurosys(codEuroSys);
                    temp.setCodiceUfficio(codUfficio);

                    //Creazione dell'oggetto Hibernate
                    MOffCeAccounts mOffCeAccounts = new MOffCeAccounts();
                    mOffCeAccounts.setCodiceUfficio(temp.getCodiceUfficio());
                    mOffCeAccounts.setCodiceEurosys(temp.getCodiceEurosys());
                    mOffCeAccounts.setAdUsername("");
                    mOffCeAccounts.setNomeIdentificativo("");
                    mOffCeAccounts.setNomeUfficio("");

                    importControllerDao.saveUpdate(mOffCeAccounts);
                    imported++;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return "" + imported + " record importati\n";
    }
}
