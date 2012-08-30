package com.kp.malice.client.ui;

import java.util.Date;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;

public class UIMaliceUtil {

    private static final DateTimeFormat df = DateTimeFormat.getFormat("yyyyMMdd");

    public static void nascondi(DivElement divElement) {
        divElement.getStyle().setDisplay(Style.Display.NONE);
    }

    public static void visualizza(DivElement divElement) {
        divElement.getStyle().setDisplay(Style.Display.BLOCK);
    }

    public static void nascondi(Element element) {
        element.getStyle().setDisplay(Style.Display.NONE);
    }

    public static void visualizza(Element element) {
        element.getStyle().setDisplay(Style.Display.BLOCK);
    }

    public static void nascondi(Button button) {
        button.setVisible(false);
    }

    public static void visualizza(Button button) {
        button.setVisible(true);
    }

    public static void evidenziaPerErrore(Element element) {
        element.setAttribute("style", "border: 1px solid #F70707");
    }

    public static void eliminaStile(Element element) {
        element.setAttribute("style", "");
    }

    public static boolean isAfterToday(Date testDt) {
        long testing = Long.valueOf(df.format(testDt));
        long dtToday = Long.valueOf(df.format(new Date()));
        return testing > dtToday;
    }

    public static boolean isSameDayDate(Date dateA, Date dateB) {
        long d1 = Long.valueOf(df.format(dateA));
        long d2 = Long.valueOf(df.format(dateB));
        return d1 == d2;
    }
}
