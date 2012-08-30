/**
 * 
 */
package com.kp.marsh.ebt.client.webapp.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.kp.marsh.ebt.client.webapp.ui.resources.Resources;
import com.kp.marsh.ebt.client.webapp.ui.utils.ViewUtils;
import com.kp.marsh.ebt.client.webapp.ui.validators.InsertPopUpInputVerifier;
import com.kp.marsh.ebt.shared.PotentialValue;
import com.kp.marsh.ebt.shared.dto.BrokerPotBalanceInfoDTO;
import com.kp.marsh.ebt.shared.dto.BusinessInfoDTO;
import com.kp.marsh.ebt.shared.dto.CompanyPotBalanceInfoDTO;
import com.kp.marsh.ebt.shared.dto.PotentialInfoDTO;
import com.kp.marsh.ebt.shared.dto.ResetInfoDTO;

/**
 * @author christianachilli
 * The potential pop up to insert relevant potential data
 */
public class InsertPopUp extends PopupPanel {

    private static InsertPopUpUiBinder uiBinder = GWT.create(InsertPopUpUiBinder.class);

    interface InsertPopUpUiBinder extends UiBinder<Widget, InsertPopUp> {
    }

    ProductPillWidget callerPill; // the pill that calls the pop up

    BusinessInfoDTO newBusInfo; // the business info created with the user input data of this pop up

    @UiField
    TextBox inputValue;

    @UiField
    Anchor progrInt;

    @UiField
    Anchor notAppl;

    @UiField
    Anchor inAltraCopertura;

    @UiField
    Anchor aggiungiNota;

    @UiField
    Anchor broker;

    @UiField
    Anchor compagnia;

    private boolean isBroker = false;

    private boolean isCompagnia = false;

    private Integer oldInfoId;// non pu� essere l'id di un final balance. Nel caso whatInfoAmI sia un final balance, questo deve essere l'id della sua related business info

    public InsertPopUp() {
        super(false, true);
        add(uiBinder.createAndBindUi(this));
        inputValue.setMaxLength(5);

        aggiungiNota.addStyleName(Resources.INSTANCE.popup().aggNota());

    }

    @UiHandler("aggiungiNota")
    void onAggiungiNotaClick(ClickEvent e) {
        hideInsertPopUp();
        callerPill.showNotesPopUp();
        // should the onclose event be sunk?
    }

    @UiHandler("inputValue")
    void onInputValueKeyUp(KeyUpEvent e) {
        if (!inputValue.getText().isEmpty()) {
            if ("Valore?".equals(inputValue.getText())) {
                inputValue.setText("Valore?");
                inputValue.selectAll();
                inputValue.setFocus(true);
            } else if (!InsertPopUpInputVerifier.isValidChar(inputValue.getText())) { // numbers starting with 0 are not allowed
                inputValue.setText(inputValue.getText().subSequence(0, inputValue.getText().length() - 1).toString()); // remove the last char
            }
            if (InsertPopUpInputVerifier.isValidInput(inputValue.getText()))
                newBusInfo = new PotentialInfoDTO(callerPill.getTheProductValueIContain().getmClient().getClientId(),
                        callerPill.getTheProductValueIContain().getId(), oldInfoId, ViewUtils.decodeToEuro(inputValue
                                .getText()), callerPill.getTheProductValueIContain().getParentId());

        } else {
            newBusInfo = new ResetInfoDTO(callerPill.getTheProductValueIContain().getmClient().getClientId(),
                    callerPill.getTheProductValueIContain().getId(), oldInfoId, "", callerPill
                            .getTheProductValueIContain().getParentId());
        }
        //per far s� che il testo della pillola sia allineato con la input text del pop up
        callerPill.setPillLabelText(inputValue.getText());
        if (callerPill.azurePillStyle.equals(callerPill.potText.getStyleName())
                && callerPill.potText.getText().isEmpty()) {
            callerPill.potText.setText("...");
        }
        if (e.getNativeKeyCode() == KeyCodes.KEY_ENTER && InsertPopUpInputVerifier.isValidInput(inputValue.getText())) {
            if (isBroker && InsertPopUpInputVerifier.isValidInput(inputValue.getText())
                    && !inputValue.getText().isEmpty()) {
                isBroker = false;
                newBusInfo = new BrokerPotBalanceInfoDTO(callerPill.getTheProductValueIContain().getmClient()
                        .getClientId(), callerPill.getTheProductValueIContain().getId(), oldInfoId,
                        ViewUtils.decodeToEuro(inputValue.getText()), callerPill.getTheProductValueIContain()
                                .getParentId());
            } else if (isCompagnia && InsertPopUpInputVerifier.isValidInput(inputValue.getText())
                    && !inputValue.getText().isEmpty()) {
                isCompagnia = false;
                newBusInfo = new CompanyPotBalanceInfoDTO(callerPill.getTheProductValueIContain().getmClient()
                        .getClientId(), callerPill.getTheProductValueIContain().getId(), oldInfoId,
                        ViewUtils.decodeToEuro(inputValue.getText()), callerPill.getTheProductValueIContain()
                                .getParentId());
            }

            hideInsertPopUp();
        }
    }

    @UiHandler("inputValue")
    void onInputValueClick(ClickEvent e) {
        if (!inputValue.getText().isEmpty()) {
            if (InsertPopUpInputVerifier.isValidInput(inputValue.getText()))
                newBusInfo = new PotentialInfoDTO(callerPill.getTheProductValueIContain().getmClient().getClientId(),
                        callerPill.getTheProductValueIContain().getId(), oldInfoId, ViewUtils.decodeToEuro(inputValue
                                .getText()), callerPill.getTheProductValueIContain().getParentId());
        } else {
            newBusInfo = new ResetInfoDTO(callerPill.getTheProductValueIContain().getmClient().getClientId(),
                    callerPill.getTheProductValueIContain().getId(), oldInfoId, "", callerPill
                            .getTheProductValueIContain().getParentId());
        }
        if (callerPill.azurePillStyle.equals(callerPill.potText.getStyleName())
                && callerPill.potText.getText().isEmpty()) {
            callerPill.potText.setText("...");
        }
    }

    @UiHandler("progrInt")
    void onProgrIntClick(ClickEvent e) {
        GWT.log("" + callerPill.getTheProductValueIContain().getWhatInfoAmI().getBusinessInfoId());
        inputValue.setText("");

        newBusInfo = new PotentialInfoDTO(callerPill.getTheProductValueIContain().getmClient().getClientId(),
                callerPill.getTheProductValueIContain().getId(), oldInfoId,
                PotentialValue.INTERNATIONAL_PROGRAM.getLabelValue(), callerPill.getTheProductValueIContain()
                        .getParentId());
        hideInsertPopUp();
    }

    @UiHandler("notAppl")
    void onNotApplClick(ClickEvent e) {
        GWT.log("" + callerPill.getTheProductValueIContain().getWhatInfoAmI().getBusinessInfoId());
        inputValue.setText("");

        newBusInfo = new PotentialInfoDTO(callerPill.getTheProductValueIContain().getmClient().getClientId(),
                callerPill.getTheProductValueIContain().getId(), oldInfoId,
                PotentialValue.NOT_APPLICABLE.getLabelValue(), callerPill.getTheProductValueIContain().getParentId());
        hideInsertPopUp();
    }

    @UiHandler("inAltraCopertura")
    void onInAltraCoperturaClick(ClickEvent e) {
        GWT.log("" + callerPill.getTheProductValueIContain().getWhatInfoAmI().getBusinessInfoId());
        inputValue.setText("");

        newBusInfo = new PotentialInfoDTO(callerPill.getTheProductValueIContain().getmClient().getClientId(),
                callerPill.getTheProductValueIContain().getId(), oldInfoId,
                PotentialValue.IN_ALTRA_COPERTURA.getLabelValue(), callerPill.getTheProductValueIContain()
                        .getParentId());
        hideInsertPopUp();
    }

    @UiHandler("broker")
    void onBrokerClick(ClickEvent e) {
        if (!InsertPopUpInputVerifier.isValidInput(inputValue.getText()) || inputValue.getText().isEmpty()) {
            //disabled
            inputValue.setText("Valore?");
            inputValue.setFocus(true);
            inputValue.selectAll();
            isBroker = true;
        } else {
            //cambia il tipo di potenziale
            newBusInfo = new BrokerPotBalanceInfoDTO(
                    callerPill.getTheProductValueIContain().getmClient().getClientId(), callerPill
                            .getTheProductValueIContain().getId(), oldInfoId, ViewUtils.decodeToEuro(inputValue
                            .getText()), callerPill.getTheProductValueIContain().getParentId());
            hideInsertPopUp();
        }
    }

    @UiHandler("compagnia")
    void onCompagniaClick(ClickEvent e) {
        if (!InsertPopUpInputVerifier.isValidInput(inputValue.getText()) || inputValue.getText().isEmpty()) {
            inputValue.setText("Valore?");
            inputValue.setFocus(true);
            inputValue.selectAll();
            isCompagnia = true;
        } else {
            // cambia il tipo di potenziale
            newBusInfo = new CompanyPotBalanceInfoDTO(callerPill.getTheProductValueIContain().getmClient()
                    .getClientId(), callerPill.getTheProductValueIContain().getId(), oldInfoId,
                    ViewUtils.decodeToEuro(inputValue.getText()), callerPill.getTheProductValueIContain().getParentId());
            hideInsertPopUp();
        }
    }

    /**
     * Show the pop up to insert potential information.
     */
    public void showInsertPopUp() {
        //reset della business information prodotta dall'insermento dati sul pop up
        newBusInfo = null;
        oldInfoId = null;

        isCompagnia = false;
        isBroker = false;

        //imposto l'eventuale id della business info da aggiornare
        if (callerPill.getTheProductValueIContain().getWhatInfoAmI() != null) {
            oldInfoId = callerPill.getTheProductValueIContain().getWhatInfoAmI().getBusinessInfoId();
        }

        // imposto la posizione del pop up
        setPopupPosition(callerPill.getAbsoluteLeft() + callerPill.getOffsetWidth() - 10, callerPill.getAbsoluteTop());
        show();

        //imposto il valore della input text del pop up: se � un numero lo mostro, altrimenti no
        if (InsertPopUpInputVerifier.isValidChar(callerPill.getPillLabelText()))
            inputValue.setText(callerPill.getPillLabelText());
        else
            inputValue.setText("");
        // imposto il focus
        inputValue.setFocus(true);
        inputValue.selectAll();

    }

    /**
     * Hide the pop up
     */
    private void hideInsertPopUp() {

        // creo la business info derivata dal''input utente: analizzo l'input utente
        if (null != newBusInfo) {
            switch (newBusInfo.getValueType()) {
            case POTENTIAL:
                GWT.log("Commit di un potential del valore di " + newBusInfo.getValue());
                break;
            case BROKER_POT:
                GWT.log("Commit di un Broker del valore di " + newBusInfo.getValue());
                break;
            case COMPANY_POT:
                GWT.log("Commit di un Company del valore di " + newBusInfo.getValue());
                break;
            case RESET: // il reset viene creato e quindo passato all'activity per committarlo solo se sono presenti delle note
                GWT.log("Commit di un Reset del valore di " + newBusInfo.getValue());
                break;

            default:
                break;
            }

            // imposto eventuali note sulla nuova business info
            newBusInfo.setPostItText(callerPill.getProductPostIt().getPostItNote());

            // imposto la business info che verr� committata
            callerPill.getTheProductValueIContain().changeBusinessInfo(newBusInfo);
        }
        hide();
    }

    public ProductPillWidget getCallerPill() {
        return callerPill;
    }

    public void setCallerPill(ProductPillWidget callerPill) {
        this.callerPill = callerPill;
    }

}
