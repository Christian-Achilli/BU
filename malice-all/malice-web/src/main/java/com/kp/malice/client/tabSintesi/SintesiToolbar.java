package com.kp.malice.client.tabSintesi;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.kp.malice.client.ui.gwtEvent.CalendarioValueChangeEvent;
import com.kp.malice.client.ui.gwtEvent.CalendarioValueChangeEvent.CalendarioValueChangeHandler;
import com.kp.malice.client.ui.gwtEvent.FiltraStatisticheEvent;
import com.kp.malice.client.ui.gwtEvent.FiltraStatisticheEvent.FiltraStatisticheHandler;
import com.kp.malice.client.ui.gwtEvent.VisualizzaPremiEvent;
import com.kp.malice.client.ui.gwtEvent.VisualizzaPremiHandler;
import com.kp.malice.client.ui.gwtEvent.VisualizzaProvvigioniEvent;
import com.kp.malice.client.ui.gwtEvent.VisualizzaProvvigioniHandler;
import com.kp.malice.entities.business.LioReferenceCode;
import com.kp.malice.shared.MaliceUtil;
import com.kp.malice.shared.proxies.LioReferenceCodeProxy;

/**
 * Mostra i comandi per filtrare i titoli sulla relativa tabella
 */
public class SintesiToolbar extends Composite {

	private static final String MONTANTE_PROVVIGIONI = "MONTANTE COMMISSIONI";
	private static final String PREMI_INCASSATI_E_COMMISSIONI = "PREMI INCASSATI E COMMISSIONI";
	private static ToolbarRicercaStatisticheUiBinder uiBinder = GWT.create(ToolbarRicercaStatisticheUiBinder.class);

	interface ToolbarRicercaStatisticheUiBinder extends UiBinder<Widget, SintesiToolbar> {
	}

	@UiField(provided = true)
	ListBox monthBox;
	@UiField(provided = true)
	ListBox brokerBox;
	@UiField
	Label grafoTitle;
	@UiField
	Button premiButton;
	@UiField
	Button montanteButton;

	private List<LioReferenceCodeProxy> lioReferenceCodeProxyList;

	public SintesiToolbar() {
		monthBox = new ListBox(false);
		monthBox.setVisibleItemCount(1);
		monthBox.addItem("Anno Corrente");
		monthBox.addItem("Ultimi 12 mesi");
		monthBox.addItem("Ultimi 24 mesi");
		monthBox.addItem("Ultimi 36 mesi");
		// combo broker
		brokerBox = new ListBox(false);
		brokerBox.ensureDebugId("cwListBox-multiBox");
		brokerBox.setVisibleItemCount(1);

		// Add a handler to handle drop box events
		brokerBox.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				// Window.alert(""+brokerBox.getSelectedIndex());
				// listener.caricaDatiByBroker(brokerBox.getItemText(brokerBox.getSelectedIndex()));
			}
		});
		initWidget(uiBinder.createAndBindUi(this));
		// setto la toolbar per la visualizzazione premi incassati e provvigioni
		// (di default)
		premiButton.setVisible(true);
		montanteButton.setVisible(false);
		grafoTitle.setText(PREMI_INCASSATI_E_COMMISSIONI);
		// periodo.init13mesiDaPrimoMeseCorrente();//imposto le date per i due
		// datebox
	}

	public void setBrokerBox(List<LioReferenceCodeProxy> lioReferenceCodeProxyList) {
		this.lioReferenceCodeProxyList = lioReferenceCodeProxyList;
		for (LioReferenceCodeProxy lioReferenceCodeProxy : lioReferenceCodeProxyList) {
			brokerBox.addItem(lioReferenceCodeProxy.getReferente());
		}
	}

	@UiHandler("brokerBox")
	void onBrokerBoxChangeValue(ChangeEvent e) {
		GWT.log("ToolbarRicercaStatistiche.onBrokerBoxChangeValue: ChangeEvent");
		filtraStatistiche();
	}

	@UiHandler("monthBox")
	void onMesiChangeValue(ChangeEvent e) {
		GWT.log("ToolbarRicercaStatistiche.onMesiChangeValue: ChangeEvent");
		filtraStatistiche();
	}

	private void filtraStatistiche() {
		Date startingDate = null;
		Date endingDate = null; 
		switch (monthBox.getSelectedIndex()) {
		case 0:
			startingDate = MaliceUtil.getDataAlPrimoDellAnnoCorrente();
			endingDate = MaliceUtil.getDataAllUltimoDellAnnoCorrente();
			break;
		case 1:
			startingDate = MaliceUtil.getDataNMonthAgo(12);	
			endingDate = new Date();
			break;
		case 2:
			startingDate = MaliceUtil.getDataNMonthAgo(24);			
			endingDate = new Date();
			break;
		case 3:
			startingDate = MaliceUtil.getDataNMonthAgo(36);			
			endingDate = new Date();
			break;
		default:
			GWT.log("WARNING!!! ToolbarRicercaStatistiche.filtraStatistiche: defaul value not expected!!!");
			break;
		}
		GWT.log("throw FiltraStatisticheEvent");
		fireEvent(new FiltraStatisticheEvent(startingDate, endingDate, lioReferenceCodeProxyList.get(brokerBox.getSelectedIndex()).getCodice()));
	}

	@UiHandler("premiButton")
	void onClickPremiButton(ClickEvent e) {
		// bottone
		premiButton.setVisible(false);
		montanteButton.setVisible(true);
		// titolo
		grafoTitle.setText(MONTANTE_PROVVIGIONI);
		GWT.log("throw visualizzaProvvigioni");
		fireEvent(new VisualizzaProvvigioniEvent());
	}

	@UiHandler("montanteButton")
	void onClickMontanteButton(ClickEvent e) {
		// bottone
		montanteButton.setVisible(false);
		premiButton.setVisible(true);
		// titolo
		grafoTitle.setText(PREMI_INCASSATI_E_COMMISSIONI);
		GWT.log("throw visualizzaPremi");
		fireEvent(new VisualizzaPremiEvent());
	}

	public HandlerRegistration addVisualizzaProvvigioniHandler(VisualizzaProvvigioniHandler handler) {
		return addHandler(handler, VisualizzaProvvigioniEvent.TYPE);
	}

	public HandlerRegistration addVisualizzaPremiHandler(VisualizzaPremiHandler handler) {
		return addHandler(handler, VisualizzaPremiEvent.TYPE);
	}

	public HandlerRegistration addCalendarioValueChangeHandler(CalendarioValueChangeHandler handler) {
		return addHandler(handler, CalendarioValueChangeEvent.TYPE);
	}

	public HandlerRegistration addFiltraStatisticheEventHandler(FiltraStatisticheHandler handler) {
		return addHandler(handler, FiltraStatisticheEvent.TYPE);
	}
}
