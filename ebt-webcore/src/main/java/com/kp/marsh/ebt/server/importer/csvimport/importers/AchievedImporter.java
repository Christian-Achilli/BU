/**
 * 
 */
package com.kp.marsh.ebt.server.importer.csvimport.importers;

import java.io.Reader;

import net.sf.flatpack.DataSet;
import net.sf.flatpack.DefaultParserFactory;
import net.sf.flatpack.Parser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.kp.marsh.ebt.server.importer.csvimport.Achieved;
import com.kp.marsh.ebt.server.importer.csvimport.CsvAchievedDTO;
import com.kp.marsh.ebt.server.importer.dao.ImportController;
import com.kp.marsh.ebt.server.importer.model.MAchieved;

/**
 * @author christianachilli
 * Preleva il file dalla directory specificata
 */
public class AchievedImporter extends CsvImporter {

    private static Log log = LogFactory.getLog(AchievedImporter.class);

    private String configFile;

    private ImportController importControllerDao;

    /**
     * @param csvSource the csv source file reader
     * @param xmlConfigFile the configuration file for FlatPack
     */
    @Inject
    public AchievedImporter(@Achieved String fileName, ImportController importControllerDAO) {
        this.configFile = fileName;
        this.importControllerDao = importControllerDAO;

    }

    @Override
    public void clearTable() {
        log.info("Pulisco la table M_ACHIEVED");
        importControllerDao.pulisciAchieved();

    }

    /**
     * Trigger the procedure that reads the csv file in the configured location (depends on the implementation) and save it to the configured DB in the table named M_ACHIEVED. 
     */
    @Override
    public String doImport(Reader csvSource) throws Exception {
        int importatiCounter = 0;
        int rowInTable = 0;
        try {
            Parser parser = DefaultParserFactory.getInstance().newDelimitedParser(getPzMapStream(configFile),
                    csvSource, ',', '"', true);
            DataSet data = parser.parse();
            rowInTable = data.getRowCount();
            log.info(" Righe nel file csv: " + rowInTable);
            for (int i = 0; i < data.getRowCount(); i++) {
                if (data.next()) {
                    // creazione del DTO
                    CsvAchievedDTO temp = new CsvAchievedDTO();
                    temp.setAnnoContabile(data.getString("Anno"));
                    temp.setCodEysCE("ND");
                    temp.setNomeCE("ND");
                    temp.setCodEysCapogruppo(data.getString("EysGc"));
                    temp.setNomeCapogruppo(data.getString("NomeGc"));
                    temp.setCodEysGaranzia(data.getString("CodGaranzia"));
                    String provvigioni = data.getString("Provvigioni");
                    //					String extraProvvigioni = data.getString("ExtraProvvigioni");
                    Double provv = Double.valueOf(provvigioni);
                    //					Double extra = Double.valueOf(extraProvvigioni);
                    Double tot = provv;
                    if (tot < 0) {
                        log.debug(provv + " = " + tot);
                    }
                    temp.setTotaleProvvigioni(tot);
                    //Creazione dell'oggetto Hibernate
                    MAchieved mAchieved = new MAchieved();
                    mAchieved.setAnnoContabile(temp.getAnnoContabile());
                    mAchieved.setAeCgruppo(temp.getNomeCE());
                    mAchieved.setIdAeCgruppo(temp.getCodEysCE());
                    mAchieved.setCodiceCapogruppo(temp.getCodEysCapogruppo());
                    mAchieved.setCapogruppo(temp.getNomeCapogruppo());
                    mAchieved.setCodGaranzia(temp.getCodEysGaranzia());
                    mAchieved.setRamoDescrizione("void");
                    mAchieved.setRamo("void");
                    mAchieved.setNetRevenues(temp.getTotaleProvvigioni());
                    try {
                        importControllerDao.saveUpdateThrowable(mAchieved);
                        importatiCounter++;
                    } catch (Exception e) {
                        log.error(e + " CE: " + temp.getNomeCE() + " - " + temp.getCodEysCE() + " - "
                                + temp.getCodEysCapogruppo() + " - " + temp.getNomeCapogruppo() + " - "
                                + temp.getCodEysGaranzia() + " - " + temp.getAnnoContabile());
                    }
                } else {
                    log.error("AchievedImporter.doImport: data.next() false");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        log.info("Importati " + importatiCounter + " di " + rowInTable);
        return "" + rowInTable + " record nel file\n" + importatiCounter + " record importati sul DB";
    }

}
