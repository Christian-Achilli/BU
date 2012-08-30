/**
 * 
 */
package com.kp.marsh.ebt.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.kp.marsh.ebt.shared.BusinessInfoValueType;
import com.kp.marsh.ebt.shared.PotentialValue;

/**
 * @author christianachilli
 *
 * A concrete class for information regarding the potential value for a certain product
 *
 */
public class PotentialInfoDTO extends BusinessInfoDTO implements IsSerializable {

	
	@Deprecated /*Left only for RPC compatibility*/
	public PotentialInfoDTO() {
		super();
	}
	
	
	/**
	 * @param clientId
	 * @param productId
	 * @param bInfoId
	 * @param productValue il valore in euro
	 * @param lobId
	 */
	public  PotentialInfoDTO(int clientId, int productId, Integer bInfoId, String productValue, String lobId) {
		super(bInfoId, productValue, productId, clientId, lobId);
	}
	
	
	public PotentialValue getPotentialValueType() {
		return PotentialValue.fromString(super.getValue());
	}
	
	public BusinessInfoValueType getValueType(){
		return BusinessInfoValueType.POTENTIAL;
	}
	
}
