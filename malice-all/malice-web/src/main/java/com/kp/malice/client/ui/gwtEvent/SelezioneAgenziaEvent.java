package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.kp.malice.client.ui.gwtEvent.SelezioneAgenziaEvent.SelezioneAgenziaHandler;
import com.kp.malice.shared.proxies.AgencyProxy;
import com.kp.malice.shared.proxies.NewTitoloProxy;

public class SelezioneAgenziaEvent extends GwtEvent<SelezioneAgenziaHandler> {
	
	public interface SelezioneAgenziaHandler extends EventHandler {
	    void onSelezioneAgenzia(SelezioneAgenziaEvent selezioneAgenziaEvent);
	  }
	
    private AgencyProxy agencyProxy;

    public SelezioneAgenziaEvent(AgencyProxy agencyProxy) {
        super();
        this.agencyProxy = agencyProxy;
    }

    public static final Type<SelezioneAgenziaHandler> TYPE = new Type<SelezioneAgenziaHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<SelezioneAgenziaHandler> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(SelezioneAgenziaHandler handler) {
        handler.onSelezioneAgenzia(this);
    }

	public AgencyProxy getAgencyProxy() {
		return agencyProxy;
	}

	public void setAgencyProxy(AgencyProxy agencyProxy) {
		this.agencyProxy = agencyProxy;
	}
    
}