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
import com.kp.marsh.ebt.server.importer.csvimport.CsvGruppiCommercialiDTO;
import com.kp.marsh.ebt.server.importer.csvimport.GruppiCommerciali;
import com.kp.marsh.ebt.server.importer.dao.ImportController;
import com.kp.marsh.ebt.server.importer.model.MAchieved;

/**
 * @author christianachilli
 *import i  gruppi commerciali da csv a db su M_ACHIEVED
 */
public class GruppiCommercialiImporter extends CsvImporter {

    private static Log log = LogFactory.getLog(GruppiCommercialiImporter.class);

    private String configFile;

    private ImportController importControllerDao;

    @Inject
    public GruppiCommercialiImporter(@GruppiCommerciali String fileName, ImportController importControllerDAO) {
        this.configFile = fileName;
        this.importControllerDao = importControllerDAO;

    }

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
                    CsvGruppiCommercialiDTO temp = new CsvGruppiCommercialiDTO();
                    temp.setCodEysCE(data.getString("EysCE"));
                    temp.setNomeCE(data.getString("NomeCE"));
                    temp.setCodEysCapogruppo(data.getString("EysGc"));
                    temp.setNomeCapogruppo(data.getString("NomeGc"));

                    //Creazione dell'oggetto Hibernate
                    MAchieved mAchieved = new MAchieved();
                    mAchieved.setAeCgruppo(temp.getNomeCE());
                    mAchieved.setIdAeCgruppo(temp.getCodEysCE());
                    mAchieved.setCodiceCapogruppo(temp.getCodEysCapogruppo());
                    mAchieved.setCapogruppo(temp.getNomeCapogruppo());

                    mAchieved.setRamoDescrizione("void");
                    mAchieved.setCodGaranzia("void");
                    mAchieved.setNetRevenues(0d);
                    mAchieved.setAnnoContabile("void");
                    mAchieved.setRamo("void");

                    try {
                        importControllerDao.saveUpdateThrowable(mAchieved);
                        importatiCounter++;
                    } catch (Exception e) {
                        log.error(e + " CE: " + temp.getNomeCE() + " - " + temp.getCodEysCE() + " - "
                                + temp.getCodEysCapogruppo() + " - " + temp.getNomeCapogruppo());
                    }
                } else {
                    log.error("GruppiCommercialiImporter: data.next() false");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        log.info("Importati " + importatiCounter + " di " + rowInTable);
        return "" + rowInTable + " record nel file\n" + importatiCounter + " record importati sul DB";

    }

    @Override
    public void clearTable() {
        log.info("Pulisco la table M_ACHIEVED");
        importControllerDao.pulisciAchieved();

    }

}
