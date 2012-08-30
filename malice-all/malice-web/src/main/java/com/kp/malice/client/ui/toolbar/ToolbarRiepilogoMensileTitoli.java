/**
 * 
 */
package com.kp.malice.client.ui.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Visualizzato nella toolbar in benvenuto e grafici
 */
public class ToolbarRiepilogoMensileTitoli extends Composite {

    private static ToolbarRiepilogoMensileTitoliUiBinder uiBinder = GWT
            .create(ToolbarRiepilogoMensileTitoliUiBinder.class);

    interface ToolbarRiepilogoMensileTitoliUiBinder extends UiBinder<Widget, ToolbarRiepilogoMensileTitoli> {
    }

    @UiField
    Label mese;

    @UiField
    Label titoliIncassati;

    @UiField
    Label titoliSospesi;

    @UiField
    Label totProvvigioniIncassateAnno;

    @UiField
    Label totPremiIncassatiAnno;

    public ToolbarRiepilogoMensileTitoli() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setCorrenteMeseAperto(String date) {
        this.mese.setText(date);
    }

    public void setFormattedTitoliSospesi(String num) {
        this.titoliSospesi.setText(num);
    }

    public void setFormattedTitoliIncassati(String num) {
        this.titoliIncassati.setText(num);
    }

    public void setFormattedTotPremiIncassatiAnno(String val) {
        this.totPremiIncassatiAnno.setText(val);
    }

    public void setFormattedTotProvvigioniIncassateAnno(String val) {
        this.totProvvigioniIncassateAnno.setText(val);
    }
}
