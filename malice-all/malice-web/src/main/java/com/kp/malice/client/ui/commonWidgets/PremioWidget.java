package com.kp.malice.client.ui.commonWidgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.kp.malice.shared.MaliceUtil;
import com.kp.malice.shared.proxies.CertificatoLloydsProxy;
import com.kp.malice.shared.proxies.NewTitoloProxy;

public class PremioWidget extends Composite implements IsWidget {

    private static PremioTitoloUiBinder uiBinder = GWT.create(PremioTitoloUiBinder.class);

    interface PremioTitoloUiBinder extends UiBinder<Widget, PremioWidget> {
    }

    @UiField
    SpanElement rischio;
    @UiField
    SpanElement premioNetto;
    @UiField
    SpanElement accessori;
    @UiField
    SpanElement tasse;
    @UiField
    SpanElement premioLordo;
    @UiField
    SpanElement commissioniSuNetto;
    @UiField
    SpanElement commissioniSuAccessori;

    public PremioWidget(NewTitoloProxy newTitoloProxy, String classAttirbute) {
        initWidget(uiBinder.createAndBindUi(this));
        CertificatoLloydsProxy certificatoLloyds = newTitoloProxy.getCertificatoLloyds();
        rischio.setInnerText("" + certificatoLloyds.getRischio());
        premioNetto.setInnerText(MaliceUtil.getEuroFromBigDecimalCent(newTitoloProxy.getNetEuroCent()));
        accessori.setInnerText(MaliceUtil.getEuroFromBigDecimalCent(newTitoloProxy.getAccessoriEuroCent()));
        tasse.setInnerText(MaliceUtil.getEuroFromBigDecimalCent(newTitoloProxy.getTaxesEuroCent()));
        premioLordo
                .setInnerText(MaliceUtil.getEuroFromBigDecimalCent(MaliceUtil.getPremioLordoEuroCent(newTitoloProxy)));
        commissioniSuNetto.setInnerText(MaliceUtil.getEuroFromBigDecimalCent(newTitoloProxy
                .getCommissionsOnNetEuroCent()));
        commissioniSuAccessori.setInnerText(MaliceUtil.getEuroFromBigDecimalCent(newTitoloProxy
                .getCommissionsOnAccessoriEuroCent()));
    }
}
