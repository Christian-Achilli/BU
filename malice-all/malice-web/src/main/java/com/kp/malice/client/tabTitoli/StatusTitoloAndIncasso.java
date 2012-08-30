package com.kp.malice.client.tabTitoli;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.kp.malice.client.ui.resources.MaliceResources;

public enum StatusTitoloAndIncasso {

    DA_INCASSARE(-1), INCASSATO(1), ANNULLATO(1), CONSOLIDATO(0), INCASSATO_SOSPESO(1), CONSOLIDATO_SOSPESO(0) ; // TODO VERIFICARE CON I NUOVI STATI

    private int gruppoIncasso;

    public int getGruppoIncasso() {
        return gruppoIncasso;
    }

    private StatusTitoloAndIncasso() {
    }

    private StatusTitoloAndIncasso(int gruppoIncasso) {
        this.gruppoIncasso = gruppoIncasso;
    }

    /**
     * Deve essere chiamato solo lato client (GWT)
     * @return
     */
    public ImageResource getImageResource() {
        ImageResource imgRes = null;
        if (GWT.isClient()) {
            switch (this) {
            case ANNULLATO:
                imgRes = MaliceResources.INSTANCE.greyBotton();
                break;
            case DA_INCASSARE:
                imgRes = MaliceResources.INSTANCE.yellowBotton();
                break;
            case INCASSATO:
                imgRes = MaliceResources.INSTANCE.greenBotton();
                break;
            case INCASSATO_SOSPESO:
                imgRes = MaliceResources.INSTANCE.greenSuspendedBotton();
                break;
            case CONSOLIDATO:
                imgRes = MaliceResources.INSTANCE.blueBotton();
                break;
            case CONSOLIDATO_SOSPESO:
                imgRes = MaliceResources.INSTANCE.blueSuspendedBotton();
                break;
            default:
                break;
            }
        }
        return imgRes;
    }

    public static StatusTitoloAndIncasso fromString(String s) {
        for (int i = 0; i < StatusTitoloAndIncasso.values().length; i++) {
            if (StatusTitoloAndIncasso.values()[i].name().equals(s)) {
                return StatusTitoloAndIncasso.values()[i];
            }
        }
        return null;
    }

    public static String toString(StatusTitoloAndIncasso st) {
        return st.name();
    }

}
