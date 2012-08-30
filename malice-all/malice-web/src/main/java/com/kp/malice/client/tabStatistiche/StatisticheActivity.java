package com.kp.malice.client.tabStatistiche;

import java.util.Date;
import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.kp.malice.shared.MaliceRequestFactory.ServiziPortale;
import com.kp.malice.shared.MaliceUtil;
import com.kp.malice.shared.proxies.GrafiDataProxy;
import com.kp.malice.shared.proxies.LioReferenceCodeProxy;

public class StatisticheActivity extends AbstractActivity implements IStatisticheViewDisplay.Listener { // VisualizzaStatisticheEvent.Handler,

	private final IStatisticheViewDisplay display;
	private final Provider<ServiziPortale> serviziProvider;
	private final EventBus eventBus;
	private Date start;
	private Date end;
	private String broker;

	@Inject
	public StatisticheActivity(IStatisticheViewDisplay display, EventBus eventBus, Provider<ServiziPortale> serviziProvider) {
		this.display = display;
		this.eventBus = eventBus;
		this.serviziProvider = serviziProvider;
		this.broker = "*";
	}

	public void start(final AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(display.asWidget());
		populateComboBoxBrokerToolbarStatistiche();
		start = MaliceUtil.getDataAlPrimoDellAnnoCorrente();
		end = MaliceUtil.getDataAllUltimoDellAnnoCorrente();
		caricaDati();
		display.showProvvigioni();
		display.setListener(this);
	}

	public StatisticheActivity withPlace(final Place place) {
		return this;
	}

	@Override
	public void ricaricaStatistiche(Date startingDate, Date endingDate, String broker) {
		this.start = startingDate;
		this.end = endingDate;
		this.broker = broker;
		caricaDati();
	}

	private void caricaDati() {
		GWT.log("StatisticheActivity.caricaDati: " + start + " -to- " + end);
		ServiziPortale request = serviziProvider.get();
		Request<GrafiDataProxy> req = request.calculateDataGrafi(start, end, broker);
		try {
			req.fire(new Receiver<GrafiDataProxy>() {
				@Override
				public void onFailure(ServerFailure error) {
					GWT.log("StatisticheActivity.caricaDati RFC failure: " + error.getMessage());
					MaliceUtil.showError("Impossibile caricare le statiche. Riprovare più tardi o contattare il responsabile di sistema.");
				}

				@Override
				public void onSuccess(GrafiDataProxy response) {
					GWT.log("StatisticheActivity.caricaDati RFC success");
					GWT.log(response.getPremiProvvigioni());
					display.plotLineGraph(response.getMontanti());
					display.plotIstogramGraph(response.getPremiProvvigioni());
				}
			});
		} catch (Exception e) {
			GWT.log("StatisticheActivity.caricaDati RFC failure: " + e.getMessage());
			MaliceUtil.showError("Impossibile caricare le statiche. Riprovare più tardi o contattare il responsabile di sistema.");
			e.printStackTrace();
		}
	}

	public void populateComboBoxBrokerToolbarStatistiche() {
		GWT.log("StatisticheActivity.getLioReferenceCodeList");
		ServiziPortale request = serviziProvider.get();
		Request<List<LioReferenceCodeProxy>> req = request.getLioReferenceCodeProxyList();
		try {
			req.fire(new Receiver<List<LioReferenceCodeProxy>>() {
				@Override
				public void onFailure(ServerFailure error) {
					GWT.log("StatisticheActivity.getLioReferenceCodeList RFC failure: " + error.getMessage());
					MaliceUtil.showError("Impossibile caricare la lista dei Broker. Riprovare più tardi o contattare il responsabile di sistema.");
				}

				@Override
				public void onSuccess(List<LioReferenceCodeProxy> lioReferenceCodeProxyList) {
					GWT.log("StatisticheActivity.getLioReferenceCodeList RFC success");
					display.setComboBoxBrokerToolbarStatistiche(lioReferenceCodeProxyList);
				}
			});
		} catch (Exception e) {
			GWT.log("StatisticheActivity.getLioReferenceCodeList RFC failure: " + e.getMessage());
			MaliceUtil.showError("Impossibile caricare la lista dei Broker. Riprovare più tardi o contattare il responsabile di sistema.");
			e.printStackTrace();
		}
	}
}