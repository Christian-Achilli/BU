package com.kp.malice.client.event;

import java.util.Date;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class CaricaDatiStatisticheEvent extends GwtEvent<CaricaDatiStatisticheEvent.Handler> {

    public interface Handler extends EventHandler {
        public void onCaricaDatiStatistiche(CaricaDatiStatisticheEvent event);
    }

    private Date startDate;

    private Date endDate;

    public static Type<CaricaDatiStatisticheEvent.Handler> TYPE = new Type<CaricaDatiStatisticheEvent.Handler>();

    public CaricaDatiStatisticheEvent(Date startDate, Date endDate) {
        super();
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public Type getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(CaricaDatiStatisticheEvent.Handler handler) {
        handler.onCaricaDatiStatistiche(this);
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

}
