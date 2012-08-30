/**
 * 
 */
package com.kp.marsh.ebt.client.webapp.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.kp.marsh.ebt.client.webapp.ui.utils.ViewUtils;
import com.kp.marsh.ebt.shared.dto.LightInfoOwnerDto;
import com.kp.marsh.ebt.shared.dto.LightProductInfoDto;

/**
 * @author christianachilli
 *
 */
public class RowElementsTableIsto extends Composite {

    private static RowElementsTableIstoUiBinder uiBinder = GWT.create(RowElementsTableIstoUiBinder.class);

    interface RowElementsTableIstoUiBinder extends UiBinder<Widget, RowElementsTableIsto> {
    }

    @UiField
    Label rowName;

    @UiField
    Label totPot;

    @UiField
    Label totAch;

    @UiField
    IstogramWidget isto_1;

    @UiField
    IstogramWidget isto_2;

    @UiField
    IstogramWidget isto_3;

    @UiField
    IstogramWidget isto_4;

    @UiField
    IstogramWidget isto_5;

    @UiField
    IstogramWidget isto_6;

    @UiField
    IstogramWidget isto_7;

    @UiField
    IstogramWidget isto_8;

    private IstogramWidget[] productArray;

    public RowElementsTableIsto(LightInfoOwnerDto entityInfo) {
        initWidget(uiBinder.createAndBindUi(this));

        productArray = new IstogramWidget[8];
        productArray[0] = this.isto_1;
        productArray[1] = this.isto_2;
        productArray[2] = this.isto_3;
        productArray[3] = this.isto_4;
        productArray[4] = this.isto_5;
        productArray[5] = this.isto_6;
        productArray[6] = this.isto_7;
        productArray[7] = this.isto_8;

        updateHistograms(entityInfo);
    }

    public void updateHistograms(LightInfoOwnerDto entityInfo) {
        this.rowName.setText(entityInfo.getIdentificativo());
        setTotalPotential(entityInfo.getTotalePotenzialeEuro());
        setTotalAchieved(entityInfo.getTotaleAchievedEuro());
        for (int i = 0; i < productArray.length; i++) {
            IstogramWidget productWidget = productArray[i];
            LightProductInfoDto productInfo = entityInfo.getOrderedProductArray()[i];
            if (productInfo != null) {
                //TODO inserire l'etichetta col nome del prodotto
                productWidget.setVisible(true);
                productWidget.setIstogramWidget(productInfo.getEuroPotValue(), productInfo.getEuroAchValue(), 75,
                        productInfo.getProductName()); // TODO gestire le altezze massime in scala a seconda del valore di potenziale
            } else {
                productWidget.setVisible(false);
            }
        }
    }

    /**
     * Inserisce l'achieved tenendo conto che  espresso in euro mentre la visualizzazione deve essere in migliaia di euro con una cifra decimale
     * @param achieved
     */
    private void setTotalAchieved(int achieved) {
        totAch.setText(achieved != 0 ? ViewUtils.format(achieved) : "");

    }

    /**
     * Inserisce il potenziale tenendo conto che  espresso in euro mentre la visualizzazione deve essere in migliaia di euro con una cifra decimale
     * @param potential
     */
    private void setTotalPotential(int potential) {
        totPot.setText(potential != 0 ? ViewUtils.format(potential) : "");

    }

    /**
     * Agiorna gli istogrammi con i valori passati in argomento.
     * Controlla anche che l'entitˆ cui l'argomento fa rifermineto sia la stessa rappresentata  in questo row element
     * @param lightInfoOwnerDto
     */
    public void update(LightInfoOwnerDto lightInfoOwnerDto) {
        if (lightInfoOwnerDto.getIdentificativo().equals(rowName.getText())) {
            updateHistograms(lightInfoOwnerDto);

        } else {
            GWT.log("Elemento non corretto per l'aggiornamento dell'istogramma: si riferisce a "
                    + lightInfoOwnerDto.getIdentificativo() + " invece che a " + rowName.getText());

        }

    }

}
