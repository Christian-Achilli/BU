/**
 * 
 */
package com.kp.marsh.ebt.server.webapp.data.domain;

import com.kp.marsh.ebt.shared.dto.OwnerType;

/**
 * @author christianachilli
 *
 */
public class ClientExecutive extends InformationOwners {

    public ClientExecutive() {
        super();
    }

    @Override
    public String getOwnerType() {

        return OwnerType.CE.name();
    }

}
