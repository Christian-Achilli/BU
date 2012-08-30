package com.kp.marsh.ebt.server.importer.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;

import org.apache.log4j.Logger;

public class SearchFile {

	private Logger log = Logger.getLogger(SearchFile.class);
	private File trovato;
	private String fileName;
	private File startingDir;

	public SearchFile(String fileName) {
		this.fileName = fileName;
		this.startingDir = new File(System.getProperty("user.dir"));
	}
	
	public SearchFile(String fileName, String startingDir) {
		this.fileName = fileName;
		this.startingDir = new File(startingDir);
	}
	
	public File search(){
		StringBuilder lista = null;
		try {
			lista = sortList(startingDir);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		log.debug("");
		log.debug(lista);
		log.debug("ritorno: "+trovato.getName()+"    path: "+trovato.getPath());
		return trovato;
	}

	private void listingDir(File dir, int livello, StringBuilder sb) {
		String spazio = "";
		File[] elementi = dir.listFiles();
		for (int l = 0; l < livello; l++) {
			spazio += "  |_";
		}
		// Arrays.sort(elementi, new myComparator());
		for (File files : elementi) {
			sb.append(spazio).append(files.getName()).append("\n");
			if(fileName.equals(files.getName())){
				if(trovato==null)
					trovato = new File(files.getName());
				log.debug(files.getName()+" trovato");
			}
			if (files.isDirectory()) {
				listingDir(files, livello + 1, sb);
			}
		}
	}

	private StringBuilder sortList(File dir) throws FileNotFoundException {
		validateDir(dir);
		StringBuilder sb = new StringBuilder();
		listingDir(dir, 0, sb);
		return sb;
	}

	private void validateDir(File dir) throws FileNotFoundException {
		if (dir == null) {
			throw new IllegalArgumentException("Directory should not be null.");
		}
		if (!dir.exists()) {
			throw new FileNotFoundException("Directory does not exist: " + dir);
		}
		if (!dir.isDirectory()) {
			throw new IllegalArgumentException("Is not a directory: " + dir);
		}
		if (!dir.canRead()) {
			throw new IllegalArgumentException("Directory cannot be read: "
					+ dir);
		}
	}
}
