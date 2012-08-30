package com.kp.malice.client.ui.gwtEvent;

import java.util.Date;

import com.google.gwt.event.shared.GwtEvent;
import com.kp.malice.shared.proxies.NewTitoloProxy;

public class DownloadEvent extends GwtEvent<DownloadHandler> {
	
	private Date date;
	private Object object;

    public DownloadEvent() {
		super();
	}

	public static final Type<DownloadHandler> TYPE = new Type<DownloadHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<DownloadHandler> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(DownloadHandler handler) {
        handler.onDownloadListaTitoli(this);
    }

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}