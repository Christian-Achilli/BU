package com.kp.malice.shared;

import java.util.ArrayList;


public class Documento {
	
	private String name;
	
	private String downloadUrl;
	
	private ArrayList<Documento> lista;

	public Documento() {
		name = "testDoc";
		downloadUrl = "testDownloadUrl";
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public ArrayList<Documento> getLista() {
		return lista;
	}

}
