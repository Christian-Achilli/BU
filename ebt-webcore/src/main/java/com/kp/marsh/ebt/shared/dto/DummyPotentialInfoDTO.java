/**
 * 
 */
package com.kp.marsh.ebt.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.kp.marsh.ebt.shared.ClientIdentifier;
import com.kp.marsh.ebt.shared.ProductIdentifier;

/**
 * @author christianachilli
 *
 *When updating a product total that has no info at all, this class helps in giving such info to the display and bring the total back to void
 */
public class DummyPotentialInfoDTO extends PotentialInfoDTO implements IsSerializable {

    @Deprecated
    /*left for RPC compatibility*/
    public DummyPotentialInfoDTO() {
        super();
    }

    public DummyPotentialInfoDTO(ProductIdentifier productId) {
        super(-1, productId.getId(), -1, "0", null);
    }

    public DummyPotentialInfoDTO(ClientIdentifier clientId) {
        super(clientId.getId(), -1, -1, "0", null);
    }

}
