package com.kp.malice.client.tabStatistiche;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.kp.malice.shared.proxies.LioReferenceCodeProxy;

public interface IStatisticheViewDisplay extends IsWidget {
	public interface Listener {

		void ricaricaStatistiche(Date startingDate, Date endingDate, String broker);
	}

	public void plotLineGraph(String stringaJSON);

	public void plotIstogramGraph(String stringaJSON);

	public void showMontanti();

	public void showProvvigioni();

	public void setListener(IStatisticheViewDisplay.Listener listener);

	public void setComboBoxBrokerToolbarStatistiche(List<LioReferenceCodeProxy> lioReferenceCodeProxyList);
}
