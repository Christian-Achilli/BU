package com.kp.marsh.ebt.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.kp.marsh.ebt.shared.BusinessInfoValueType;

public class CompanyPotBalanceInfoDTO extends BusinessInfoDTO implements IsSerializable {

	@Deprecated /*left for RPC compatibility*/
	public CompanyPotBalanceInfoDTO() {
//		super();
	}
	
	
	public CompanyPotBalanceInfoDTO(int clientId, int productId, Integer bInfoId, String productValue, String lobId) {
		super(bInfoId, productValue, productId, clientId, lobId);
	}

	@Override
	public BusinessInfoValueType getValueType() {
		return BusinessInfoValueType.COMPANY_POT;
	}
	
//	public PotentialValue getPotentialValueType() {
//		return PotentialValue.fromString(super.getValue());
//	}

}
