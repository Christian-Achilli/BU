package com.kp.malice.client.tabAgenzie;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.HasData;
import com.kp.malice.shared.proxies.AgencyProxy;

public interface IAgenzieViewDisplay extends IsWidget {

	/**
	 * Metodi dell'activity resi accessibili al display
	 */
	public interface Listener {

		void onSelezioneAgenzia(AgencyProxy agencyProxy);

	}

	void setListener(Listener listener);

	void setNumAgenzieTrovate(int size);

	HasData<AgencyProxy> getTabellaTitoli();
}