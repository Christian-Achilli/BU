package com.kp.malice.client.ui.gwtEvent;

import java.util.Date;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.kp.malice.client.ui.gwtEvent.DoppioCalendarioValueChangeEvent.DoppioCalendarioValueChangeHandler;

public class DoppioCalendarioValueChangeEvent extends GwtEvent<DoppioCalendarioValueChangeHandler> {
    private final Date startDate;
    private final Date endDate;

    public interface DoppioCalendarioValueChangeHandler extends EventHandler {
        void onValueChange(DoppioCalendarioValueChangeEvent calendarioValueChangeEvent);
    }

    public DoppioCalendarioValueChangeEvent(Date startDate, Date endDate) {
        super();
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static final Type<DoppioCalendarioValueChangeHandler> TYPE = new Type<DoppioCalendarioValueChangeHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<DoppioCalendarioValueChangeHandler> getAssociatedType() {
        return (Type) TYPE;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    @Override
    protected void dispatch(DoppioCalendarioValueChangeHandler handler) {
        handler.onValueChange(this);
    }
}