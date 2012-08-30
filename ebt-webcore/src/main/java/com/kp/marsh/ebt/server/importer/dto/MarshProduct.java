/**
 * 
 */
package com.kp.marsh.ebt.server.importer.dto;

/**
 * @author christianachilli
 */
public class MarshProduct {

	String name; // ebt name of the product as reported in the table M_LOB_PRODUCT
	
	long actual;
	
	long achieved;
	
	String year; 

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getActual() {
		return actual;
	}

	public void setActual(long actual) {
		this.actual = actual;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public void setAchieved(long l) {
		this.achieved = l;
		
	}
	
	public long getAchieved() {
		return achieved;
	}
	
	
}
