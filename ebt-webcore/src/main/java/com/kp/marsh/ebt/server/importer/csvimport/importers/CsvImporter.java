package com.kp.marsh.ebt.server.importer.csvimport.importers;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.hibernate.util.ConfigHelper;

import com.kp.marsh.ebt.server.importer.util.FileUtils;
import com.kp.marsh.ebt.server.importer.util.IReadProperties;

public abstract class CsvImporter {

	/**
	 * Trigger the procedure that reads the csv file in the configured location (depends on the implementation) and save it to the configured DB in the table whose name is  implementation dependent
	 */
	public abstract String doImport(Reader csvSource) throws Exception;
	
	/**
	 * Clear the table of the correnponding csv file being imported
	 */
	public abstract void clearTable();
	
	/**
	 * @return lo stream di configurazione del tool pzmap
	 */
	public InputStreamReader getPzMapStream(String filename) {
		InputStream is = ConfigHelper.getResourceAsStream(filename);
		return new InputStreamReader(is);
		
	}
	
	
	/**
	 * Pulisce la tabella su Db e importa i file che sio trovano nella directory specificata nel file di properties, spostandoli poi nelle altre dir specificate nel medesimo file
	 * @throws Exception 
	 */
	@Deprecated
	public void importaDa(final IReadProperties rp) throws Exception {
		clearTable();
		gestisciFiles(rp);
		
	}



	/**
	 * Cerca i file e li gestisce filtrando sul nome dei file
	 * @throws Exception 
	 */
	@Deprecated
	private void gestisciFiles(final IReadProperties rp) throws Exception {
		FilenameFilter filenameFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				int l = rp.getPREFISSO().length();
				if(name.length()>=(l+12) && rp.getPREFISSO().equals(name.substring(0,l))){ //12 perchè il resto del nome è composto dalla data + l'estensione
					return true;
				}
				return false;
			}
		};
		File[] files = new File(rp.getWAITING_PATH()).listFiles(filenameFilter);		
		if(files.length <= 0){
			return;

		}
		else if(files.length >= 1) {
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				workOnFile(file, rp);

			}
		}
	}

	/**
	 * Sposta il file prima nella directory in lavorazione e poi terminata la fase di import lo sposta definitivamente nella directory di lavoro completato
	 * @param fileInLavorazione file da importare
	 * @throws Exception  
	 */
	@Deprecated
	private void workOnFile(File fileInLavorazione, final IReadProperties rp) throws Exception {
		try {
			FileUtils.moveFile(rp.getWAITING_PATH()+fileInLavorazione.getName(), rp.getIN_PROGRESS_PATH()+fileInLavorazione.getName());
			FileReader fr = new FileReader(rp.getIN_PROGRESS_PATH()+fileInLavorazione.getName());
			try {
				doImport(fr);

			} catch (Exception e) {
				throw e;

			}
			// se è andato bene sposta il file in 'lavorati'
			FileUtils.moveFile(rp.getIN_PROGRESS_PATH()+fileInLavorazione.getName(), rp.getWORKED_PATH()+fileInLavorazione.getName());

		} catch (IOException e) {
			e.printStackTrace();

		}
	}
	

}