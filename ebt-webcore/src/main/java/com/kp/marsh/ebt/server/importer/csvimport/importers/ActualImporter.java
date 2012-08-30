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
import com.kp.marsh.ebt.server.importer.csvimport.Actual;
import com.kp.marsh.ebt.server.importer.csvimport.CsvActualDTO;
import com.kp.marsh.ebt.server.importer.dao.ImportController;
import com.kp.marsh.ebt.server.importer.model.MActuals;

/**
 * @author christianachilli
 * Preleva il file dalla directory specificata
 */
public class ActualImporter {

    private static Log log = LogFactory.getLog(ActualImporter.class);

    private Reader csvSource;

    private Reader xmlConfigFile;

    private ImportController importControllerDao;

    /**
     * @param csvSource the csv source file reader
     * @param xmlConfigFile the configuration file for FlatPack
     */
    @Inject
    public ActualImporter(@Actual Reader csvConfigFile, ImportController importControllerDAO) {
        this.xmlConfigFile = csvConfigFile;
        //		TODO rifattorizzare come achieved importer e anagrafica importer estendendo CvsImporter
        this.importControllerDao = importControllerDAO;
    }

    /**
     * Trigger the procedure that reads the csv file in the configured location (depends on the implementation) and save it to the configured DB in the table named M_ACTUALS. 
     */
    public void doImport() {

        Parser parser = DefaultParserFactory.getInstance().newDelimitedParser(xmlConfigFile, csvSource, ',', '"', true);

        DataSet data = parser.parse();

        log.debug(data.getRowCount());

        for (int i = 0; i < data.getRowCount(); i++) {

            if (data.next()) {

                // creazione del DTO
                CsvActualDTO temp = new CsvActualDTO();
                temp.setAnnoContabile(data.getString("Anno"));
                temp.setCodEysCE(data.getString("EysCE"));
                temp.setNomeCE(data.getString("NomeCE"));
                temp.setCodEysCapogruppo(data.getString("EysGc"));
                temp.setNomeCapogruppo(data.getString("NomeGc"));
                temp.setCodEysGaranzia(data.getString("CodGaranzia"));
                String provvigioni = data.getString("Provvigioni");
                String extraProvvigioni = data.getString("ExtraProvvigioni");
                Double provv = Double.valueOf(provvigioni);
                Double extra = Double.valueOf(extraProvvigioni);

                Double tot = provv + extra;

                if (tot < 0) {

                    log.debug(provv + " + " + extra + " = " + tot);
                }

                temp.setTotaleProvvigioni(tot);

                //Creazione dell'oggetto Hibernate
                MActuals mActual = new MActuals();
                mActual.setAnnoContabile(temp.getAnnoContabile());
                mActual.setAeCgruppo(temp.getNomeCE());
                mActual.setIdAeCgruppo(temp.getCodEysCE());
                mActual.setCodiceCapogruppo(temp.getCodEysCapogruppo());
                mActual.setCapogruppo(temp.getNomeCapogruppo());
                mActual.setCodGaranzia(temp.getCodEysGaranzia());
                mActual.setRamoDescrizione("void");
                mActual.setRamo("void");
                mActual.setNetRevenues(temp.getTotaleProvvigioni());

                importControllerDao.saveUpdate(mActual);
            }

        }

    }

}
