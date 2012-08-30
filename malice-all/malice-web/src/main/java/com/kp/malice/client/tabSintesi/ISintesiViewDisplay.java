package com.kp.malice.client.tabSintesi;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.kp.malice.shared.proxies.LioReferenceCodeProxy;

public interface ISintesiViewDisplay extends IsWidget {
	public interface Listener {

		void ricaricaStatistiche(Date startingDate, Date endingDate, String broker);
	}

	public void plotLineGraph(String stringaJSON);

	public void plotIstogramGraph(String stringaJSON);

	public void showMontanti();

	public void showProvvigioni();

	public void setListener(ISintesiViewDisplay.Listener listener);

	public void setComboBoxBrokerToolbarStatistiche(List<LioReferenceCodeProxy> lioReferenceCodeProxyList);
}
