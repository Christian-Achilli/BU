package com.kp.malice.client.tabStatistiche;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.kp.malice.client.ui.gwtEvent.FiltraStatisticheEvent;
import com.kp.malice.client.ui.gwtEvent.VisualizzaPremiEvent;
import com.kp.malice.client.ui.gwtEvent.VisualizzaProvvigioniEvent;
import com.kp.malice.client.ui.toolbar.ToolbarRicercaStatistiche;
import com.kp.malice.shared.proxies.LioReferenceCodeProxy;

/**
 * @author christianachilli
 * 
 */
public class StatisticheView extends Composite implements IStatisticheViewDisplay {

	private static StatisticheViewUiBinder uiBinder = GWT.create(StatisticheViewUiBinder.class);

	interface StatisticheViewUiBinder extends UiBinder<Widget, StatisticheView> {
	}

	private IStatisticheViewDisplay.Listener listener;

	@UiField
	ToolbarRicercaStatistiche toolbarRicercaStatistiche;

	@UiField
	IstogrammiPremiEProvvigioniWidget istogrammiPremiEProvvigioni;

	@UiField
	LineaMontanteEProvvigioniWidget lineaMontanteEProvvigioni;

	@Inject
	public StatisticheView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("toolbarRicercaStatistiche")
	void onVisualizzaPremi(VisualizzaPremiEvent e) {
		GWT.log("catch visualizzaPremi");
		showProvvigioni();
	}

	@UiHandler("toolbarRicercaStatistiche")
	void onvisualizzaProvvigioni(VisualizzaProvvigioniEvent e) {
		GWT.log("catch visualizzaProvvigioni");
		showMontanti();
	}

	@UiHandler("toolbarRicercaStatistiche")
	void onFiltraStatistiche(FiltraStatisticheEvent event) {
		GWT.log("StatistichView.onFiltraStatistiche: catch FiltraStatisticheEvent");
		listener.ricaricaStatistiche(event.getStartingDate(), event.getEndingDate(), event.getBroker());
	}

	@Override
	public void plotLineGraph(String stringaJSON) {
		lineaMontanteEProvvigioni.plotLineaGraph(stringaJSON);
	}

	@Override
	public void plotIstogramGraph(String stringaJSON) {
		istogrammiPremiEProvvigioni.plotIstogramGraph(stringaJSON);
	}

	@Override
	public void showMontanti() {
		istogrammiPremiEProvvigioni.setVisible(false);
		lineaMontanteEProvvigioni.setVisible(true);
	}

	@Override
	public void showProvvigioni() {
		istogrammiPremiEProvvigioni.setVisible(true);
		lineaMontanteEProvvigioni.setVisible(false);
	}

	public void setListener(IStatisticheViewDisplay.Listener listener) {
		this.listener = listener;
	}

	@Override
	public void setComboBoxBrokerToolbarStatistiche(List<LioReferenceCodeProxy> lioReferenceCodeProxyList) {
		toolbarRicercaStatistiche.setBrokerBox(lioReferenceCodeProxyList);		
	}

}
