package com.kp.malice.shared;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.kp.malice.client.ui.resources.MaliceResources;


public enum StatoChiusura {

    DA_INVIARE, INVIATA, APERTA;

    public static StatoChiusura fromString(String s) {
        for (StatoChiusura enu : values()) {
            if (enu.name().equals(s)) {
                return enu;
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
            case INVIATA:
                imgRes = MaliceResources.INSTANCE.blueBotton();
                break;
            case DA_INVIARE:
                imgRes = MaliceResources.INSTANCE.yellowBotton();
                break;
            case APERTA:
                imgRes = MaliceResources.INSTANCE.greyBotton();
                break;
            default:
                break;
            }
        }
        return imgRes;
    }

}
