package com.kp.malice.client.ui.gwtEvent;

import java.util.Date;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.kp.malice.client.ui.gwtEvent.CalendarioValueChangeEvent.CalendarioValueChangeHandler;

public class CalendarioValueChangeEvent extends GwtEvent<CalendarioValueChangeHandler> {
    private final Date rangeDate;

    public interface CalendarioValueChangeHandler extends EventHandler {
        void onValueChange(CalendarioValueChangeEvent calendarioValueChangeEvent);
    }

    public CalendarioValueChangeEvent(Date date) {
        super();
        this.rangeDate = date;
    }

    public static final Type<CalendarioValueChangeHandler> TYPE = new Type<CalendarioValueChangeHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<CalendarioValueChangeHandler> getAssociatedType() {
        return (Type) TYPE;
    }

    public Date getDate() {
        return rangeDate;
    }

    @Override
    protected void dispatch(CalendarioValueChangeHandler handler) {
        handler.onValueChange(this);
    }
}