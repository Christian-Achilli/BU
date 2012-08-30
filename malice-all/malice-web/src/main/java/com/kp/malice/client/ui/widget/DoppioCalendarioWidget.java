package com.kp.malice.client.ui.widget;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.gwt.user.datepicker.client.DateBox;
import com.kp.malice.client.MaliceDebugIds;
import com.kp.malice.client.ui.UtcSafetyDateBox;
import com.kp.malice.client.ui.gwtEvent.DoppioCalendarioValueChangeEvent;
import com.kp.malice.client.ui.gwtEvent.DoppioCalendarioValueChangeEvent.DoppioCalendarioValueChangeHandler;
import com.kp.malice.shared.MaliceUtil;

public class DoppioCalendarioWidget extends Composite implements ValueChangeHandler<Date> {

    private static CalendariFiltroWidgetUiBinder uiBinder = GWT.create(CalendariFiltroWidgetUiBinder.class);

    interface CalendariFiltroWidgetUiBinder extends UiBinder<Widget, DoppioCalendarioWidget> {
    }

    @UiField
    public UtcSafetyDateBox dateBoxStart;
    @UiField
    public UtcSafetyDateBox dateBoxEnd;
    @UiField
    public Label labelCalendario;

    public DoppioCalendarioWidget(final String attributeName, DateTimeFormat dateFormat) {
        initWidget(uiBinder.createAndBindUi(this));
        //imposto il formato
        dateBoxStart.setFormat(new DateBox.DefaultFormat(dateFormat));
        dateBoxEnd.setFormat(new DateBox.DefaultFormat(dateFormat));
        //rendo i campi non modificabili
        dateBoxStart.getElement().setAttribute("readonly", "readonly");
        dateBoxEnd.getElement().setAttribute("readonly", "readonly");
        dateBoxStart.getDatePicker().addValueChangeHandler(this);
        dateBoxEnd.getDatePicker().addValueChangeHandler(this);
        dateBoxStart.ensureDebugId(MaliceDebugIds.START_DATE);
        dateBoxEnd.ensureDebugId(MaliceDebugIds.END_DATE);
    }

    @Override
    public void onValueChange(final ValueChangeEvent<Date> event) {
        //validazione
        Date start = dateBoxStart.getValue();
        Date finish = dateBoxEnd.getValue();
        //verifico che le date inserite siano significative
        if (CalendarUtil.getDaysBetween(start, finish) >= 0) {
            if (event.getSource() == dateBoxStart) {
                GWT.log("CalendarioValueChangeEvent fired./n" + dateBoxStart.getValue().toString());
            } else if (event.getSource() == dateBoxEnd) {
                GWT.log("CalendarioValueChangeEvent fired./n" + dateBoxEnd.getValue().toString());
            }
            fireEvent(new DoppioCalendarioValueChangeEvent(((UtcSafetyDateBox) dateBoxStart).getLocalPlus2HoursDate(),
                    ((UtcSafetyDateBox) dateBoxEnd).getLocalPlus2HoursDate()));
        } else {
            Window.alert("Le date inserite non rappresentano un periodo di tempo valido.");
        }
    }

    public void initPrimoEUltimoMeseCorrente() {
        dateBoxStart.setValue(MaliceUtil.getDataAlPrimoDelMeseCorrente());
        dateBoxEnd.setValue(MaliceUtil.getDataAllUltimoDelMeseCorrente());
        labelCalendario.setText("Decorrenza dal");
    }

    public void init13mesiDaPrimoMeseCorrente() {
        dateBoxStart.setValue(MaliceUtil.getData13MonthAgo());
        dateBoxEnd.setValue(MaliceUtil.getDataAlPrimoDelMeseCorrente());
        labelCalendario.setText("Periodo");
    }

    public void initPlus10Minus30FromToday() {
        dateBoxStart.setValue(MaliceUtil.getTodayPlus(-20));
        dateBoxEnd.setValue(MaliceUtil.getTodayPlus(10));
        labelCalendario.setText("Decorrenza dal");
    }

    public Date getDateStart() {
        return dateBoxStart.getValue();
    }

    public Date getDateEnd() {
        return dateBoxEnd.getValue();
    }

    public HandlerRegistration addDoppioCalendarioValueChangeHandler(DoppioCalendarioValueChangeHandler handler) {
        return addHandler(handler, DoppioCalendarioValueChangeEvent.TYPE);
    }

}
