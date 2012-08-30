package com.kp.marsh.ebt.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.kp.marsh.ebt.shared.BusinessInfoValueType;

public class AchievedInfoDTO extends BusinessInfoDTO implements IsSerializable {

    @Deprecated
    /*left for RPC compatibility*/
    public AchievedInfoDTO() {
    }

    public AchievedInfoDTO(int clientId, int productId, int bInfoId, String productValue, String lobId) {
        super(bInfoId, productValue, productId, clientId, lobId);
    }

    @Override
    public BusinessInfoValueType getValueType() {
        return BusinessInfoValueType.ACHIEVED;
    }

}
