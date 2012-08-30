package com.kp.malice.client.ui.toolbar;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.kp.malice.client.ui.UIMaliceUtil;
import com.kp.malice.client.ui.UtcSafetyDateBox;
import com.kp.malice.client.ui.gwtEvent.CalendarioValueChangeEvent;
import com.kp.malice.client.ui.gwtEvent.CalendarioValueChangeEvent.CalendarioValueChangeHandler;
import com.kp.malice.client.ui.gwtEvent.DownloadEvent;
import com.kp.malice.client.ui.gwtEvent.DownloadHandler;
import com.kp.malice.shared.MaliceUtil;

public class ToolbarScritturaContabile extends Composite implements ValueChangeHandler<Date> {

    private static ToolbarScritturaContabileUiBinder uiBinder = GWT.create(ToolbarScritturaContabileUiBinder.class);

    interface ToolbarScritturaContabileUiBinder extends UiBinder<Widget, ToolbarScritturaContabile> {
    }

    @UiField
    Button downlaodButton;
    @UiField
    UtcSafetyDateBox dataRegistrazione;

    public ToolbarScritturaContabile() {
        //        dataRegistrazione = new UtcSafetyDateBox();
        initWidget(uiBinder.createAndBindUi(this));
        dataRegistrazione.setFormat(new DateBox.DefaultFormat(MaliceUtil.getDayMonthYearFormat()));
        dataRegistrazione.getElement().setAttribute("readonly", "readonly");
        dataRegistrazione.getDatePicker().addValueChangeHandler(this);
        init();
    }

    public void init() {
        dataRegistrazione.setValue(new Date());
    }

    @UiHandler("downlaodButton")
    void onDownload(ClickEvent clickEvent) {
        GWT.log("ToolbarScritturaContabile.onDownload: catch ClickEvent");
        GWT.log("ToolbarScritturaContabile.onDownload: fire ClickEvent");
        Date date = dataRegistrazione.getDatePicker().getValue();
        GWT.log("date to download: " + date);
        DownloadEvent downloadEvent = new DownloadEvent();
        downloadEvent.setDate(date);
        fireEvent(downloadEvent);
    }

    @Override
    public void onValueChange(final ValueChangeEvent<Date> event) {
        GWT.log("ToolbarScritturaContabile.onValueChange: catch ValueChangeEvent");
        Date start = dataRegistrazione.getValue();
        if (UIMaliceUtil.isAfterToday(start)) {
            Window.alert("La date inserita non e' valida.");
        } else {
            GWT.log("ToolbarScritturaContabile.onValueChange: fire CalendarioValueChangeEvent");
            CalendarioValueChangeEvent calendarioValueChangeEvent = new CalendarioValueChangeEvent(
                    ((UtcSafetyDateBox) dataRegistrazione).getLocalPlus2HoursDate());
            fireEvent(calendarioValueChangeEvent);
        }
    }

    public HandlerRegistration addCalendarioValueChangeHandler(CalendarioValueChangeHandler handler) {
        return addHandler(handler, CalendarioValueChangeEvent.TYPE);
    }

    public HandlerRegistration addDownloadHandler(DownloadHandler handler) {
        return addHandler(handler, DownloadEvent.TYPE);
    }

    public void visualizzaErrore(String error) {

    }

    public void nascondiBottoneDownload() {
        downlaodButton.setVisible(false);
    }

    public void visualizzaBottoneDownload() {
        downlaodButton.setVisible(true);
    }
}
