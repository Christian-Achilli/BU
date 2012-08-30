package com.kp.marsh.ebt.server.importer.csvimport;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class XmlActualsConfigFile extends FileReader {

	public XmlActualsConfigFile() throws FileNotFoundException {
		super("src/main/resources/pzmap.actuals.xml");
	}
	
}
