package com.kp.marsh.ebt.client.webapp.ui.views.manager;

import java.util.ArrayList;

import org.adamtacy.client.ui.effects.events.EffectCompletedEvent;
import org.adamtacy.client.ui.effects.events.EffectCompletedHandler;
import org.adamtacy.client.ui.effects.examples.BlindUp;

import pl.rmalinowski.gwt2swf.client.ui.SWFWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.kp.marsh.ebt.client.webapp.ui.CustomBlindDown;
import com.kp.marsh.ebt.client.webapp.ui.NavigationBoxWidget;
import com.kp.marsh.ebt.client.webapp.ui.TotalsSumUp;
import com.kp.marsh.ebt.client.webapp.ui.activityintf.IHeaderBarDisplay;
import com.kp.marsh.ebt.client.webapp.ui.resources.Resources;
import com.kp.marsh.ebt.shared.dto.LineOfBusiness;
import com.kp.marsh.ebt.shared.dto.NavigationDTO;
import com.kp.marsh.ebt.shared.dto.ProductTotalsManager;

public class HeaderBarViewImpl extends Composite implements IHeaderBarDisplay {

    private static HeaderBarViewImplUiBinder uiBinder = GWT.create(HeaderBarViewImplUiBinder.class);

    interface HeaderBarViewImplUiBinder extends UiBinder<Widget, HeaderBarViewImpl> {
    }

    @UiField
    SpanElement loggedUser;

    @UiField
    Image logout;

    @UiField
    Image histogramOff;

    @UiField
    Image histogramOn;

    @UiField
    Image pillolaOff;

    @UiField
    Image pillolaOn;

    @UiField
    Image gaugesOff;

    @UiField
    Image gaugesOn;

    @UiField
    DivElement totalBox;

    @UiField
    TotalsSumUp totalsSumUp;

    @UiField
    DivElement navigationBox;

    @UiField
    NavigationBoxWidget navigationBoxWidget;

    @UiField
    Image navigationBoxOn;

    @UiField
    Image navigationBoxOff;

    @UiField
    Image totaliLOBVisibili;

    @UiField
    Image totaliLOBNascosti;

    @UiField
    Image tutorialOff;

    @UiField
    Image tutorialOn;

    @UiField
    VerticalPanel videoTutorial;

    @UiField
    DivElement videoBox;

    private SWFWidget video;

    private CustomBlindDown sdTotali;

    private BlindUp suTotali;

    private CustomBlindDown showComboNav;

    private BlindUp suComboNav;

    private IHeaderBarDisplay.Listener listener;

    static {
        Resources.INSTANCE.mainStructure().ensureInjected();
    }

    public IHeaderBarDisplay.Listener getListener() {
        return listener;
    }

    public HeaderBarViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));

    }

    @UiHandler("histogramOff")
    void showHistograms(ClickEvent e) {
        // histograms on
        histogramOn.setVisible(true);
        histogramOff.setVisible(false);
        // gauges off
        gaugesOn.setVisible(false);
        gaugesOff.setVisible(true);
        if (pillolaOff.isVisible() || pillolaOn.isVisible()) // perchè se non ha gruppi commerciali da inserire non bisogna visualizzare le pillole
            pillolaOff.setVisible(true);
        pillolaOn.setVisible(false);
        initTotaliLobDisplay();
        totaliLOBNascosti.setVisible(true);
        disabilitaFunzioneTutorial();
        listener.switchToHistograms();
        abilitaFunzioneBussola();
        if (isVisible(navigationBox))
            suComboNav.play();
    }

    @UiHandler("gaugesOff")
    void showGauges(ClickEvent e) {
        attivaFunzioneManometri();
        abilitaFunzioneIstogrammi();

        abilitaFunzioneBussola();

        if (pillolaOff.isVisible() || pillolaOn.isVisible()) // perchè se non ha gruppi commerciali da inserire non bisogna visualizzare le pillole
            pillolaOff.setVisible(true);
        pillolaOn.setVisible(false);
        initTotaliLobDisplay();
        disabilitaFunzioneTutorial();
        listener.switchToGauges();
        if (isVisible(navigationBox))
            suComboNav.play();
    }

    @UiHandler("pillolaOff")
    void showPills(ClickEvent e) {
        // gauges on
        gaugesOn.setVisible(false);
        gaugesOff.setVisible(true);
        // histograms off
        abilitaFunzioneIstogrammi();
        pillolaOn.setVisible(true);
        pillolaOff.setVisible(false);
        initTotaliLobDisplay();
        totaliLOBNascosti.setVisible(true);
        abilitaFunzioneTutorial();
        disabilitaBussola();
        listener.switchToPills();
        if (isVisible(navigationBox))
            suComboNav.play();
    }

    @UiHandler("logout")
    void logOut(ClickEvent e) {
        listener.logout();
    }

    @Override
    public void setListener(IHeaderBarDisplay.Listener listener) {
        this.listener = listener;

    }

    @Override
    public void setUserName(String username) {
        loggedUser.setInnerText(username);

    }

    @Override
    public void showLoader(boolean b) {
        GWT.log("Header bar: The loader is not yet implemented");

    }

    @UiHandler("navigationBoxOn")
    public void onNavigationBoxOnClick(ClickEvent e) {
        abilitaFunzioneBussola();
        suComboNav.play();
    }

    @UiHandler("navigationBoxOff")
    public void onNavigationBoxOffClick(ClickEvent e) {
        GWT.log("navigationBoxOff  clikkata");
        navigationBoxOn.setVisible(true);
        navigationBoxOff.setVisible(false);
        if (!isVisible(navigationBox)) {
            setVisible(navigationBox, true);
        }
        showComboNav.play();
        if (isVisible(totalBox)) { // disabilita l'icona
            hideTotaliLOBHandler(null);

        }
    }

    @UiHandler("tutorialOn")
    public void onTutorialOnClick(ClickEvent e) {
        abilitaFunzioneTutorial();
        videoTutorial.setVisible(false);
        videoBox.setClassName("");

    }

    @UiHandler("tutorialOff")
    public void onTutorialOffClick(ClickEvent e) {
        tutorialOff.setVisible(false);
        tutorialOn.setVisible(true);
        videoTutorial.setVisible(true);
        videoBox.setClassName(Resources.INSTANCE.mainStructure().videoTutorialDiv());

    }

    @UiHandler("totaliLOBVisibili")
    public void hideTotaliLOBHandler(ClickEvent e) {
        totaliLOBVisibili.setVisible(false);
        totaliLOBNascosti.setVisible(true);
        hideLOBTotals();

    }

    @UiHandler("totaliLOBNascosti")
    public void showTotaliLOBHandler(ClickEvent e) {
        totaliLOBVisibili.setVisible(true);
        totaliLOBNascosti.setVisible(false);
        showLOBTotals();

    }

    private void showLOBTotals() {
        if (!isVisible(totalBox)) {
            setVisible(totalBox, true);
        }
        sdTotali.play();
        if (isVisible(navigationBox)) { // disabilita l'icona
            abilitaFunzioneBussola();
            suComboNav.play();

        }

    }

    private void hideLOBTotals() {
        suTotali.play();

    }

    /**
     * init delle animazioni dei totali per lob e totalBox.setVisible(false)
     */
    private void initTotaliLobDisplay() {
        totaliLOBVisibili.setVisible(false);
        totaliLOBNascosti.setVisible(false);
        /**
         * clip: rect(top, right, bottom, left);
         */
        sdTotali = new CustomBlindDown();
        suTotali = new BlindUp();
        sdTotali.setDuration(0.5);
        suTotali.setDuration(0.5);
        sdTotali.setEffectElement(totalBox);
        suTotali.setEffectElement(totalBox);
        suTotali.addEffectCompletedHandler(new EffectCompletedHandler() {

            @Override
            public void onEffectCompleted(EffectCompletedEvent event) {
                setVisible(totalBox, false);

            }
        });
        if (isVisible(totalBox))
            suTotali.play();

    }

    /**
     * init delle animazioni delle combo per la navigazione
     */
    private void initComboNavDisplay() {
        navigationBoxOn.setVisible(false);
        navigationBoxOff.setVisible(false);
        /**
         * clip: rect(top, right, bottom, left);
         */
        showComboNav = new CustomBlindDown();
        suComboNav = new BlindUp();
        showComboNav.setDuration(0.5);
        suComboNav.setDuration(0.5);
        showComboNav.setEffectElement(navigationBox);
        suComboNav.setEffectElement(navigationBox);
        suComboNav.addEffectCompletedHandler(new EffectCompletedHandler() {

            @Override
            public void onEffectCompleted(EffectCompletedEvent event) {
                setVisible(navigationBox, false);

            }
        });
        navigationBoxWidget.setHeaderBarVIew(this); //TODO è ok?
        listener.inizializzaComboUffici();
        setVisible(navigationBox, false);

    }

    @Override
    public void configureSubTotals(ArrayList<ArrayList<LineOfBusiness>> lobByPageList) {
        totalsSumUp.createHeadlineTables(lobByPageList); // creo i titoli delle lob per il div a discesa
        totalsSumUp.createSubtotalsTables(lobByPageList); // creo le tabelle e i radio butto per totali di colonna
    }

    @Override
    public void setPotentialTotal(int newTotal) {
        totalsSumUp.setPotentialTotal(newTotal);

    }

    @Override
    public void setAchievedTotal(int totAch) {
        totalsSumUp.setAchievedTotal(totAch);

    }

    @Override
    public void refreshTotals(ProductTotalsManager totalsManager) {
        totalsSumUp.refreshTotals(totalsManager);

    }

    @Override
    public void goToPage(int pageIndex) {
        totalsSumUp.gotToPage(pageIndex);

    }

    @Override
    public void initManagerIcons() {
        totalsSumUp.initView();
        abilitaFunzioneIstogrammi();
        attivaFunzioneManometri();
        disabilitaFunzionePillole();
        disabilitaFunzioneTutorial();

        abilitaFunzioneBussola();

        initTotaliLobDisplay();
        //		totaliLOBNascosti.setVisible(true);
        initComboNavDisplay();
        navigationBoxOff.setVisible(true);

    }

    @Override
    public void initManagerWithClientsIcons() {
        totalsSumUp.initView();
        abilitaFunzioneIstogrammi();

        abilitaFunzioneBussola();

        // gauges on (default)
        attivaFunzioneManometri();
        abilitaFunzionePillole();
        abilitaFunzioneTutorial();
        initVideoTutorial();
        initTotaliLobDisplay();
        initComboNavDisplay();
        navigationBoxOff.setVisible(true);

    }

    private void abilitaFunzioneBussola() {
        //navigationBox
        navigationBoxOn.setVisible(false);
        navigationBoxOff.setVisible(true);
    }

    @Override
    public void initCEIcons() {
        totalsSumUp.initView();
        disabilitaFunizoneIstogrammi();
        disabilitaFunzioneManometri();
        disabilitaFunzionePillole();// essendo l'unica funzione disponibile non serve visualizzarne l'icona
        initVideoTutorial();
        abilitaFunzioneTutorial();
        initTotaliLobDisplay();
        totaliLOBNascosti.setVisible(true);
        initComboNavDisplay();

    }

    private void disabilitaBussola() {
        navigationBoxOff.setVisible(false);
        navigationBoxOn.setVisible(false);
        suComboNav.play();

    }

    private void initVideoTutorial() {
        //il video è da inizializzare solo se il manager ha dei clienti
        disabilitaFunzioneTutorial();
        if (null == video) {
            video = new SWFWidget("flash/help.swf", 1004, 698);// la dimensione la si prende dallo screencast
            videoTutorial.add(video);
        }
        videoTutorial.setVisible(false);

    }

    /**
     * Visualizza sulla barra la funzione istogrammi con l'icona di 'spento'
     */
    private void abilitaFunzioneIstogrammi() {
        histogramOn.setVisible(false);
        histogramOff.setVisible(true);
        abilitaFunzioneBussola();
    }

    /**
     * Fa scomparire dalla barra la funzione tutorial 
     */
    private void disabilitaFunzioneTutorial() {
        tutorialOn.setVisible(false);
        tutorialOff.setVisible(false);
        videoTutorial.setVisible(false);// se è in visione lo nascondo
        videoBox.setClassName("");
    }

    private void abilitaFunzioneTutorial() {
        tutorialOn.setVisible(false);
        tutorialOff.setVisible(true);

    }

    private void disabilitaFunizoneIstogrammi() {
        histogramOn.setVisible(false);
        histogramOff.setVisible(false);

    }

    /**
     * Nasconde dalla barra le icone con le pillole
     */
    private void disabilitaFunzionePillole() {
        pillolaOn.setVisible(false);
        pillolaOff.setVisible(false);

    }

    /**
     * Visualizza sulla barra le icone con le pillole spente
     */
    private void abilitaFunzionePillole() {
        pillolaOn.setVisible(false);
        pillolaOff.setVisible(true);

    }

    /**
     * Nasconde dalla barra le icone con i manometri
     */
    private void disabilitaFunzioneManometri() {
        gaugesOn.setVisible(false);
        gaugesOff.setVisible(false);

    }

    /**
     * Visualizza i manometri con l'icona di 'acceso'
     */
    private void attivaFunzioneManometri() {
        gaugesOn.setVisible(true);
        gaugesOff.setVisible(false);

    }

    @Override
    public void updateUff(ArrayList<NavigationDTO> result) {
        navigationBoxWidget.popolaListBoxUffici(result);

    }

    @Override
    public void updateCE(ArrayList<NavigationDTO> result) {
        navigationBoxWidget.popolaListBoxCE(result);

    }

    @Override
    public void updateGC(ArrayList<NavigationDTO> result) {
        navigationBoxWidget.popolaListBoxGC(result);

    }

    @Override
    public void initUserOffice(String loggedUserOfficeId) {
        navigationBoxWidget.selectUserOffice(loggedUserOfficeId);

    }

    @Override
    public void stopAnimation() {
        navigationBoxWidget.stopAnimation();

    }

}
