package com.kp.malice.client.header;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.kp.malice.client.MaliceDebugIds;
import com.kp.malice.client.header.IHeaderViewDisplay;
import com.kp.malice.client.ui.resources.MaliceResources;
import com.kp.malice.client.ui.toolbar.ToolbarChiusureChiusure;
import com.kp.malice.client.ui.toolbar.ToolbarDettaglioTitolo;
import com.kp.malice.client.ui.toolbar.ToolbarRicercaStatistiche;
import com.kp.malice.client.ui.toolbar.ToolbarRicercaTitoli;
import com.kp.malice.client.ui.toolbar.ToolbarRiepilogoMensileTitoli;
import com.kp.malice.client.ui.widget.MainAgenteLayout;

public class HeaderView extends Composite implements IHeaderViewDisplay {

    private static HeaderUiBinder uiBinder = GWT.create(HeaderUiBinder.class);

    interface HeaderUiBinder extends UiBinder<Widget, HeaderView> {
    }

    IHeaderViewDisplay.Listener listener;

    //    @UiField(provided = true)
    ToolbarRicercaTitoli toolbarRicerca; // TAB TITOLI

    //    @UiField(provided = true)
    ToolbarRicercaStatistiche toolbarRicercaStatistiche; // TAB STATISTICHE

    //    @UiField
    ToolbarRiepilogoMensileTitoli toolbarSintesiMensileTitoli; // TAB BENVENUTO

    //    @UiField(provided = true)
    ToolbarDettaglioTitolo toolbarDettaglioTitolo; // TAB TITOLI

    //    @UiField(provided = true)
    ToolbarChiusureChiusure toolbarBottoneChiudiMese; // TAB CHIUSURA

    @UiField
    Anchor username;

    @UiField
    Anchor aBenvenuto;

    @UiField
    LIElement liBenvenuto;

    @UiField
    Anchor aTitoli;

    @UiField
    LIElement liTitoli;

    @UiField
    Anchor aIncassi;

    @UiField
    LIElement liIncassi;

    @UiField
    Anchor aChiusure;

    @UiField
    LIElement liChiusure;

    @UiField
    Anchor aStatistiche;

    @UiField
    LIElement liStatistiche;

    private ArrayList<LIElement> tabList; // mi serve per gestire pi�
    // semplicemente la selezione dei
    // tab

    private final HashMap<Widget, Boolean> situazioneToolbarVisibleWidgetsChiusureTab = new HashMap<Widget, Boolean>();

    private final MainAgenteLayout tabManager;

    @Inject
    public HeaderView(MainAgenteLayout tabManager) {
        this.tabManager = tabManager;
        initWidget(uiBinder.createAndBindUi(this));
        tabList = new ArrayList<LIElement>();
        tabList.add(liBenvenuto);
        tabList.add(liChiusure);
        tabList.add(liStatistiche);
        tabList.add(liTitoli);
        tabList.add(liIncassi);
        situazioneToolbarVisibleWidgetsChiusureTab.put(toolbarDettaglioTitolo, false);
        situazioneToolbarVisibleWidgetsChiusureTab.put(toolbarBottoneChiudiMese, false);
        situazioneToolbarVisibleWidgetsChiusureTab.put(toolbarRicerca, false);
        situazioneToolbarVisibleWidgetsChiusureTab.put(toolbarRicercaStatistiche, false);
        aBenvenuto.ensureDebugId(MaliceDebugIds.BENVENUTO_DBG_ID);
        aTitoli.ensureDebugId(MaliceDebugIds.TITOLI_DBG_ID);
        aChiusure.ensureDebugId(MaliceDebugIds.CHIUSURE_DBG_ID);
    }

    private void applyStyleToTab(LIElement tab) {
        for (LIElement li : tabList) {
            if (li.equals(tab)) {
                li.addClassName(MaliceResources.INSTANCE.main().selected());
            } else {
                li.removeClassName(MaliceResources.INSTANCE.main().selected());
            }
        }
    }

    @UiHandler("aBenvenuto")
    void onTabBenvenutoSelected(ClickEvent e) {
        applyStyleToTab(liBenvenuto);
        tabManager.selectBenvenuto();
    }

    @UiHandler("aIncassi")
    void onTabIncassiSelected(ClickEvent e) {
        applyStyleToTab(liIncassi);
        tabManager.selectIncassi();
    }

    @UiHandler("aTitoli")
    void onTabTitoliSelected(ClickEvent e) {
        applyStyleToTab(liTitoli);
        tabManager.selectTitoli();
    }

    @UiHandler("aChiusure")
    void onTabChiusureSelected(ClickEvent e) {
        applyStyleToTab(liChiusure);
        tabManager.selectChiusure();
    }

    @UiHandler("aStatistiche")
    void onTabStatisticheSelected(ClickEvent e) {
        applyStyleToTab(liStatistiche);
        tabManager.selectStatistiche();
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public Listener getListener() {
        return listener;
    }

    @Override
    public void setLabelTotaleElementiTrovatiRicerca(String value) {
        //        this.toolbarRicerca.setTotaleTrovati(value);
    }

    @Override
    public void setLabelUtenteSistema(String userName) {
        username.setText(userName);
    }

}
