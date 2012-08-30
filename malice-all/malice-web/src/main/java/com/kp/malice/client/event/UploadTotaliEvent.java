package com.kp.malice.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class UploadTotaliEvent extends GwtEvent<UploadTotaliEvent.Handler> {
	
	public interface Handler extends EventHandler{
		public void onUpload(UploadTotaliEvent event);
	}
	
	int numTot;

	public static Type<UploadTotaliEvent.Handler> TYPE = new Type<UploadTotaliEvent.Handler>();
	
	
	public UploadTotaliEvent(int numTot) {
		this.numTot = numTot;
	}

	@Override
	public Type<UploadTotaliEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	public int getTotali() {
		return numTot;
	}

	@Override
	protected void dispatch(UploadTotaliEvent.Handler handler) {
		handler.onUpload(this);
	}
}
