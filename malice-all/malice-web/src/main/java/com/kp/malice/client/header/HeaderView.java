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
import com.kp.malice.client.tabAgenzie.AgenzieToolbar;
import com.kp.malice.client.tabBenvenuto.BenvenutoToolbar;
import com.kp.malice.client.tabChiusure.ChiusureToolbarChiusure;
import com.kp.malice.client.tabSintesi.SintesiToolbar;
import com.kp.malice.client.tabTitoli.TitoliToolbarDettaglio;
import com.kp.malice.client.tabTitoli.TitoliToolbarRicerca;
import com.kp.malice.client.ui.commonWidgets.MainAgenteLayout;
import com.kp.malice.client.ui.resources.MaliceResources;

public class HeaderView extends Composite implements IHeaderViewDisplay {

    private static HeaderUiBinder uiBinder = GWT.create(HeaderUiBinder.class);

    interface HeaderUiBinder extends UiBinder<Widget, HeaderView> {
    }

    IHeaderViewDisplay.Listener listener;

    //    @UiField(provided = true)
    AgenzieToolbar toolbarSelectAgenzia; // TAB TITOLI
    
    //    @UiField(provided = true)
    TitoliToolbarRicerca toolbarRicerca; // TAB TITOLI

    //    @UiField(provided = true)
    SintesiToolbar toolbarRicercaStatistiche; // TAB STATISTICHE

    //    @UiField
    BenvenutoToolbar toolbarSintesiMensileTitoli; // TAB BENVENUTO

    //    @UiField(provided = true)
    TitoliToolbarDettaglio toolbarDettaglioTitolo; // TAB TITOLI

    //    @UiField(provided = true)
    ChiusureToolbarChiusure toolbarBottoneChiudiMese; // TAB CHIUSURA

    @UiField
    Anchor username;

    @UiField
    Anchor aSelectAgenzia;
    
    @UiField
    LIElement liSelectAgenzia;
    
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
        tabList.add(liSelectAgenzia);
        tabList.add(liBenvenuto);
        tabList.add(liChiusure);
        tabList.add(liStatistiche);
        tabList.add(liTitoli);
        tabList.add(liIncassi);
        situazioneToolbarVisibleWidgetsChiusureTab.put(toolbarSelectAgenzia, false);
        situazioneToolbarVisibleWidgetsChiusureTab.put(toolbarDettaglioTitolo, false);
        situazioneToolbarVisibleWidgetsChiusureTab.put(toolbarBottoneChiudiMese, false);
        situazioneToolbarVisibleWidgetsChiusureTab.put(toolbarRicerca, false);
        situazioneToolbarVisibleWidgetsChiusureTab.put(toolbarRicercaStatistiche, false);
        aBenvenuto.ensureDebugId(MaliceDebugIds.BENVENUTO_DBG_ID);
        aTitoli.ensureDebugId(MaliceDebugIds.TITOLI_DBG_ID);
        aChiusure.ensureDebugId(MaliceDebugIds.CHIUSURE_DBG_ID);
        onTabSelectAgenziaSelected(null);
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

    @UiHandler("aSelectAgenzia")
    void onTabSelectAgenziaSelected(ClickEvent e) {
    	applyStyleToTab(liSelectAgenzia);
    	tabManager.selectSelectAgenzia();
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
