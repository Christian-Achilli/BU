package com.kp.malice.shared;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.kp.malice.client.ui.resources.MaliceResources;
import com.kp.malice.shared.proxies.IncassoTitoloProxy;

public enum StatoIncasso {
    EFFETTIVO, SOSPESO;

    public static StatoIncasso fromString(String s) {
        for (StatoIncasso tp : StatoIncasso.values()) {
            if (tp.name().equals(s)) {
                return tp;
            }
        }
        return null;
    }

    /**
     * Deve essere chiamato solo lato client (GWT)
     * @return
     */
    public ImageResource getImageResource() {
        ImageResource imgRes = null;
        if (GWT.isClient()) {
            switch (this) {
            case EFFETTIVO:
                imgRes = MaliceResources.INSTANCE.greenBotton();
                break;
            case SOSPESO:
                imgRes = MaliceResources.INSTANCE.yellowBotton();
                break;
            default:
                break;
            }
        }
        return imgRes;
    }

    public static StatoIncasso decodeFromIncasso(IncassoTitoloProxy object) {
        return fromString(object.getStatoIncasso());
    }
}
