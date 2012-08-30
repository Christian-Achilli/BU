package com.kp.marsh.ebt.server.importer.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

public class FileUtils {
	private static Logger logger = Logger.getLogger(FileUtils.class);

	public static void copyFile(String fileOriginePath, String fileDestinazionePath) throws IOException{
		FileInputStream fis = new FileInputStream(fileOriginePath);
		FileOutputStream fos = new FileOutputStream(fileDestinazionePath);
		logger.debug("file origine: "+fileOriginePath);
		logger.debug("file destinazione: "+fileDestinazionePath);
		copyFileInputStream(fis, fos);
	}

	public static void copyFile(File fileOrigine, File fileDestinazione) throws IOException{
		FileInputStream fis = new FileInputStream(fileOrigine);
		FileOutputStream fos = new FileOutputStream(fileDestinazione);
		logger.debug("file origine: "+fileOrigine.getName());
		logger.debug("file destinazione: "+fileDestinazione.getName());
		copyFileInputStream(fis, fos);
	}


	private static void copyFileInputStream(FileInputStream fileISOrigine, FileOutputStream fileOSDestinazione) throws IOException{
		logger.debug("copying file.......");
		byte [] dati = new byte[fileISOrigine.available()];
		fileISOrigine.read(dati);
		fileOSDestinazione.write(dati);
		fileISOrigine.close();
		fileOSDestinazione.close();
	}

	public static boolean deleteFile(String filePath) throws IOException{
		return new File(filePath).delete();
	}

	public static void moveFile(String fileOriginePath, String fileDestinazionePath) throws IOException{
		if(!fileOriginePath.equals(fileDestinazionePath)) {
			copyFile(fileOriginePath, fileDestinazionePath);
			if(deleteFile(fileOriginePath))
				logger.debug("File "+fileOriginePath+" cancellato.");
			else
				logger.error("Impossible cancellare il file "+fileOriginePath);
		}
	}

	public static void moveFile(File fileOrigine, File fileDestinazione) throws IOException{
		copyFile(fileOrigine, fileDestinazione);
		if(fileOrigine.delete())
			logger.debug("File "+fileOrigine.getName()+" cancellato.");
		else
			logger.error("Impossible cancellare il file "+fileOrigine.getName());
	}
}
