package com.kp.marsh.ebt.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.kp.marsh.ebt.shared.BusinessInfoValueType;

public class ResetInfoDTO extends BusinessInfoDTO implements IsSerializable{

	
	@Deprecated /*Left only for RPC compatibility*/
	public ResetInfoDTO() {
		super();
	}
	
	public ResetInfoDTO(int clientId, int productId, Integer bInfoId, String productValue, String lobId) {
		super(bInfoId, productValue, productId, clientId, lobId);
	}
	
	@Override
	public BusinessInfoValueType getValueType() {
		return BusinessInfoValueType.RESET;
	}

}
