package com.kp.malice.client.ui.widget;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.gwt.user.datepicker.client.DateBox;
import com.kp.malice.client.ui.UtcSafetyDateBox;
import com.kp.malice.client.ui.gwtEvent.CalendarioValueChangeEvent;
import com.kp.malice.client.ui.gwtEvent.CalendarioValueChangeEvent.CalendarioValueChangeHandler;
import com.kp.malice.shared.MaliceUtil;

public class SingoloCalendarioWidget extends Composite implements ValueChangeHandler<Date> {

    private static CalendariFiltroWidgetUiBinder uiBinder = GWT.create(CalendariFiltroWidgetUiBinder.class);

    interface CalendariFiltroWidgetUiBinder extends UiBinder<Widget, SingoloCalendarioWidget> {
    }

    @UiField
    public UtcSafetyDateBox dateBoxStart;
    @UiField
    public Label labelCalendario;

    public SingoloCalendarioWidget(final String attributeName) {
        initWidget(uiBinder.createAndBindUi(this));
        init();
        //imposto il formato
        dateBoxStart.setFormat(new DateBox.DefaultFormat(MaliceUtil.getDayMonthYearFormat()));
        //rendo i campi non modificabili
        dateBoxStart.getElement().setAttribute("readonly", "readonly");
        //aggancio l'evento
        dateBoxStart.getDatePicker().addValueChangeHandler(this);
    }

    @Override
    public void onValueChange(final ValueChangeEvent<Date> event) {
        //validazione
        Date start = dateBoxStart.getValue();
        //verifico che le date inserite siano significative
        if (CalendarUtil.getDaysBetween(start, new Date()) >= 0) {
            GWT.log("SingoloCalendarioWidget.onValueChange:  fire CalendarioValueChangeEvent");
            fireEvent(new CalendarioValueChangeEvent(((UtcSafetyDateBox) dateBoxStart).getLocalPlus2HoursDate()));
        } else {
            Window.alert("La data inserita non rappresenta non e' valida.");
        }
    }

    private void init() {
        dateBoxStart.setValue(new Date());
    }

    public HandlerRegistration addCalendarioValueChangeHandler(CalendarioValueChangeHandler handler) {
        return addHandler(handler, CalendarioValueChangeEvent.TYPE);
    }
}
