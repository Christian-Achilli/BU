package com.kp.marsh.ebt.shared;

/**
 * @author christianachilli
 *
 * Values applicable to value_type column in BUSINESS_INFORMATION table.
 * Those values describe the meaning of the value in the related value_amount column.
 * value_amount will not be null only when value_type is FINAL_BALANCE, POTENTIAL or ACHIEVED
 *
 *
 */
public enum BusinessInfoValueType {

	FINAL_BALANCE,// consuntivo
	POTENTIAL, 
	ACHIEVED,
	BROKER_POT,
	COMPANY_POT, 
	RESET;// nothing declared yet, a new business information must be created. RESET cannot be committed

	public static BusinessInfoValueType fromString(String testString) {
		for (BusinessInfoValueType bInfo : BusinessInfoValueType.values()) {
			if(bInfo.name().equals(testString)) {
				return bInfo;
			}
		}
		return null;
	}
	
}
