package com.kp.malice.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.kp.malice.client.ui.gwtEvent.AnnullaEvent;
import com.kp.malice.client.ui.gwtEvent.AnnullaHandler;

public class TestWidget extends Composite {

	public TestWidget() {
		GWT.log("TestWidget 3");
		initWidget(this);
//		super(oracle);
//		sinkEvents(Event.ONPASTE);
//		getTextBox().setStylePrimaryName("peoplebox");
//		getTextBox().addKeyUpHandler(new KeyUpHandler() {
//			public void onKeyUp(KeyUpEvent event) {
//				GWT.log("onKeyUp: catch KeyUpEvent");
//				fireEvent(new AnnullaEvent());
//			}
//		});
//		setText("");
	}

	public CheckBox getTextBox() {
		// TODO Auto-generated method stub
		return null;
	}
	
//	public HandlerRegistration addAnnullaHandler(AnnullaHandler handler) {
//        return addHandler(handler, AnnullaEvent.TYPE);
//    }

}
