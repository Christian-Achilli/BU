package com.kp.malice.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.kp.malice.client.ui.gwtEvent.MaliceChangeEvent;
import com.kp.malice.client.ui.gwtEvent.MaliceChangeHandler;

public class MaliceSuggestBox extends SuggestBox {

	public MaliceSuggestBox(SuggestOracle oracle) {
		super(oracle);
		sinkEvents(Event.ONPASTE);
		getTextBox().setStylePrimaryName("peoplebox");
		getTextBox().addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				GWT.log("MaliceSuggestBox.onKeyUp: catch KeyUpEvent");
				GWT.log("MaliceSuggestBox.onKeyUp: fire MaliceChangeEvent");
				fireEvent(new MaliceChangeEvent());
			}
		});
		setText("");
	}

	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
		switch (DOM.eventGetType(event)) {
		case Event.ONPASTE:
			GWT.log("MaliceSuggestBox.onBrowserEvent: catched  ONPASTE event");
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				@Override
				public void execute() {
					GWT.log("MaliceSuggestBox.onBrowserEvent: fire ValueChangeEvent");
					 fireEvent(new MaliceChangeEvent());
				}
			});
			break;
		}
	}

	public HandlerRegistration addMaliceChangeHandler(
			MaliceChangeHandler handler) {
		return addHandler(handler, MaliceChangeEvent.TYPE);
	}

}
