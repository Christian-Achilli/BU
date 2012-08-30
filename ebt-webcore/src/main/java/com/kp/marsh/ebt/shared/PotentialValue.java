/**
 * 
 */
package com.kp.marsh.ebt.shared;

import java.util.ArrayList;
import java.util.List;

/**
 * @author christianachilli
 * Available potential values
 */
public enum PotentialValue {

	VALUE(""), 
	NOT_APPLICABLE("NA"),
	IN_ALTRA_COPERTURA("IAC"),
	INTERNATIONAL_PROGRAM("PI");
	
	private String labelValue; // the string displayed on the view
	
	private PotentialValue(String labelvalue) {
		this.labelValue = labelvalue;
	}
	
	public String getLabelValue() {
		return labelValue;
	}
	
	public static List<String> getOutOfScope_NameList() {
		List<String> lstStr = new ArrayList<String>();
		lstStr.add(IN_ALTRA_COPERTURA.getLabelValue());
		lstStr.add(NOT_APPLICABLE.getLabelValue());
		lstStr.add(INTERNATIONAL_PROGRAM.getLabelValue());
		return lstStr;
	}
	
	public static PotentialValue fromString(String s) {
		
		for (int i = 0; i < PotentialValue.values().length; i++) {
			PotentialValue buf = PotentialValue.values()[i];
			if(buf.getLabelValue().equals(s)) {
				return buf;
			}
				
		}
		
		return VALUE;
	}
	
}
