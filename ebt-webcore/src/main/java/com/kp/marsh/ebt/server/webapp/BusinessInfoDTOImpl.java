package com.kp.marsh.ebt.server.webapp;

import com.kp.marsh.ebt.server.webapp.data.domain.BusinessInformation;
import com.kp.marsh.ebt.shared.BusinessInfoValueType;
import com.kp.marsh.ebt.shared.dto.BusinessInfoDTO;

public class BusinessInfoDTOImpl extends BusinessInfoDTO {

	@Override
	public boolean equals(Object businessInformation) {
		if(this.getBusinessInfoId() == ((BusinessInformation)businessInformation).getId()){
			
		}
		return false;
	};
	
	@Override
	public BusinessInfoValueType getValueType() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
