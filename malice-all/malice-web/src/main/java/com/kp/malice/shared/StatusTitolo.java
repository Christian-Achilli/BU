package com.kp.malice.shared;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.kp.malice.client.ui.resources.MaliceResources;

public enum StatusTitolo {

    DA_INCASSARE(-1), INCASSATO(1), ANNULLATO(1), CONSOLIDATO(0); // TODO VERIFICARE CON I NUOVI STATI

    private int gruppoIncasso;

    public int getGruppoIncasso() {
        return gruppoIncasso;
    }

    private StatusTitolo() {
    }

    private StatusTitolo(int gruppoIncasso) {
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
            case CONSOLIDATO:
                imgRes = MaliceResources.INSTANCE.blueBotton();
                break;

            default:
                break;
            }
        }
        return imgRes;
    }

    public static StatusTitolo fromString(String s) {
        for (int i = 0; i < StatusTitolo.values().length; i++) {
            if (StatusTitolo.values()[i].name().equals(s)) {
                return StatusTitolo.values()[i];
            }
        }
        return null;
    }

    public static String toString(StatusTitolo st) {
        return st.name();
    }

}
