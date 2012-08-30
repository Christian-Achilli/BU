package com.kp.malice.client.tabAgenzie;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.kp.malice.client.ui.commonWidgets.ListaAgenzieWidget;
import com.kp.malice.client.ui.commonWidgets.MainAgenteLayout;
import com.kp.malice.client.ui.gwtEvent.SelezioneAgenziaEvent;
import com.kp.malice.shared.proxies.AgencyProxy;

public class AgenzieView extends Composite implements IAgenzieViewDisplay {

	private static SelectAgenziaUiBinder uiBinder = GWT.create(SelectAgenziaUiBinder.class);

	interface SelectAgenziaUiBinder extends UiBinder<Widget, AgenzieView> {
	}

	IAgenzieViewDisplay.Listener listener;

	@UiField
	AgenzieToolbar toolbarSelectAgenzia;

	@UiField
	ListaAgenzieWidget listaAgenzieWidget;

	@Inject
	public AgenzieView(MainAgenteLayout tabManager) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("listaAgenzieWidget")
	public void onSelezioneAgenzia(SelezioneAgenziaEvent event) {
		GWT.log("SlecetAgenziaView.onSelezioneAgenzia");
		listener.onSelezioneAgenzia(event.getAgencyProxy());
	}

	@Override
	public void setListener(Listener listener) {
		this.listener = listener;
	}

	public Listener getListener() {
		return listener;
	}

	@Override
	public void setNumAgenzieTrovate(int size) {
		toolbarSelectAgenzia.setNumAgenzieTrovate(size);
	}

	@Override
	public CellTable<AgencyProxy> getTabellaTitoli() {
		return listaAgenzieWidget.getTabella();
	}
}
