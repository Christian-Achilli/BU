package com.kp.marsh.ebt.server.importer.csvimport.exception;

import org.apache.log4j.Logger;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.kp.marsh.ebt.server.importer.dao.impl.ImportControllerImpl;

public class ImportException extends Exception implements IsSerializable {
	private static final long serialVersionUID = 8443863850548806627L;
	private static Logger  log = Logger.getLogger(ImportControllerImpl.class);
	
	public ImportException(String e) {
		log.error("ImportException: \n"+e);
	}
}
