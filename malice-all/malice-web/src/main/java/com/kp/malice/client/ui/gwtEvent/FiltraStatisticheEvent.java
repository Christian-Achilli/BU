package com.kp.malice.client.ui.gwtEvent;

import java.util.Date;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.kp.malice.client.ui.gwtEvent.FiltraStatisticheEvent.FiltraStatisticheHandler;

public class FiltraStatisticheEvent extends GwtEvent<FiltraStatisticheHandler> {
	private final Date startingDate;
	private final Date endingDate;
	private final String broker;

	
	public FiltraStatisticheEvent(Date startingDate, Date endingDate, String broker) {
		super();
		this.startingDate = startingDate;
		this.endingDate = endingDate;
		this.broker = broker;
	}

	public interface FiltraStatisticheHandler extends EventHandler {
		void onFiltraStatistiche(FiltraStatisticheEvent filtraStatisticheEvent);
	}

	public static final Type<FiltraStatisticheHandler> TYPE = new Type<FiltraStatisticheHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<FiltraStatisticheHandler> getAssociatedType() {
		return (Type) TYPE;
	}

	@Override
	protected void dispatch(FiltraStatisticheHandler handler) {
		handler.onFiltraStatistiche(this);
	}

	public Date getStartingDate() {
		return startingDate;
	}

	public Date getEndingDate() {
		return endingDate;
	}

	public String getBroker() {
		return broker;
	}
}