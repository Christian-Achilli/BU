package com.kp.malice.shared;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.adamtacy.client.ui.effects.events.EffectCompletedEvent;
import org.adamtacy.client.ui.effects.events.EffectCompletedHandler;
import org.adamtacy.client.ui.effects.examples.Fade;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.gwt.user.datepicker.client.DateBox;
import com.kp.malice.client.ui.widget.ErrorAlertMessage;
import com.kp.malice.client.ui.widget.Loader;
import com.kp.malice.client.ui.widget.SuccessAlertMessage;
import com.kp.malice.shared.proxies.NewTitoloProxy;

/**
 * @author dariobrambilla
 * Implementazione di metodi che ritornano le date d'intervallo per caricare i titoli da db.
 * Frutto il widget DateBox per calolare le date
 */
public class MaliceUtil {

    static final NumberFormat currencyNumberFormat = NumberFormat.getCurrencyFormat("EUR");
    static final NumberFormat currencyPercentFormat = NumberFormat.getCurrencyFormat("EUR");

    private static Loader loader;

    public final static String EMPTY_STRING = "- -";

    public final static int ROW_X_PAG = 15;

    public static BigDecimal calcolaAbbuonoEuroCent(NewTitoloProxy object) {
        BigDecimal bd = BigDecimal.ZERO;
        if (object.getUltimoIncassoCheHaMessoIlTitoloInStatoIncassato() != null) {
            BigDecimal totale = object.getNetEuroCent().add(object.getAccessoriEuroCent())
                    .add(object.getTaxesEuroCent());
            BigDecimal incassato = MaliceUtil.getIncassoValueEuroCent(object);
            bd = totale.subtract(incassato);
        }
        return bd;
    }

    public static native boolean isChromeBrowser()/*-{
		var is_chrome = navigator.userAgent.toLowerCase().indexOf('chrome') > -1;
		return is_chrome;
	}-*/;
    	
    /**
     * Crea il popup per il download della lista dei titoli
     * @param list
     */
    public static void downloadListaTitoli(String stringToGet) {
        GWT.log("MAliceUtil.downloadListaTitoli");
        if (stringToGet.toString() != null && !"".equals(stringToGet))
            Window.open(GWT.getModuleBaseURL() + "download?lista=" + stringToGet, "Scarico_Titoli", "");
    }

    public static String formatDate(Date dateToFormat, DateTimeFormat dateFormat) {
        DateBox box = new DateBox();
        box.setFormat(new DateBox.DefaultFormat(dateFormat));
        box.setValue(dateToFormat);
        String dateStringFormatted = box.getTextBox().getValue();
        return dateStringFormatted.toUpperCase();
    }

    public static String formatDateToDayMeseAnno(Date dateToFormat) {
        DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.YEAR_MONTH_DAY);
        return formatDate(dateToFormat, dateFormat);
    }

    /**
     * @param dateToFormat data fornattata in stringa
     * @return la data formattata nel seguente modo: dic 2012
     */
    public static String formatDateToMeseStringAnno(Date dateToFormat) {
        DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.YEAR_MONTH_ABBR);
        return formatDate(dateToFormat, dateFormat);
    }

    /**
     * Formatta il numero espresso in centesivi in euro come valuta
     * @param centesimi (long) numero della valuta espresso in centesimi
     * @return valore valuta convertita in euro
     */
    public static String formatFromCentesimiToEuro(Long centesimi) {
        return currencyNumberFormat.format(BigDecimal.valueOf(centesimi).movePointLeft(2));
    }

    /**
     * Formatta il numero espresso in centesivi in euro come valuta
     * @param centesimi valore valuta espresso in centesimi
     * @return valore valuta convertita in euro
     */
    public static String formatFromCentesimiToEuro(String centesimi) {
        return formatFromCentesimiToEuro(Long.parseLong(centesimi));
    }

    public static Date getData13MonthAgo() {
        return getDataNMonthAgo(13);
    }
    
    public static Date getDataNMonthAgo(int months) {
    	Date nMonthAgo = new DateBox().getDatePicker().getCurrentMonth();
    	CalendarUtil.addMonthsToDate(nMonthAgo, -months);
    	resetTimeTo000000(nMonthAgo);
    	return nMonthAgo;
    }

    public static Date getTodayPlus(int range) {
        Date todayPlus = new Date();
        addDaysToDate(todayPlus, range);
        return todayPlus;
    }

    public static int getDaysBetween(Date start, Date finish) {
        // Convert the dates to the same time
        start = copyDate(start);
        resetTime(start);
        finish = copyDate(finish);
        resetTime(finish);

        long aTime = start.getTime();
        long bTime = finish.getTime();

        long adjust = 60 * 60 * 1000;
        adjust = (bTime > aTime) ? adjust : -adjust;

        return (int) ((bTime - aTime + adjust) / (24 * 60 * 60 * 1000));
    }

    public static Date copyDate(Date date) {
        if (date == null) {
            return null;
        }
        Date newDate = new Date();
        newDate.setTime(date.getTime());
        return newDate;
    }

    private static void resetTime(Date date) {
        long msec = date.getTime();
        msec = (msec / 1000) * 1000;
        date.setTime(msec);

        // Daylight savings time occurs at midnight in some time zones, so we reset
        // the time to noon instead.
        date.setHours(12);
        date.setMinutes(0);
        date.setSeconds(0);
    }

    @SuppressWarnings("deprecation")
    // GWT requires Date
    public static void addDaysToDate(Date date, int days) {
        date.setDate(date.getDate() + days);
    }

    @SuppressWarnings("deprecation")
    // GWT requires Date
    public static void addMonthsToDate(Date date, int months) {
        if (months != 0) {
            int month = date.getMonth();
            int year = date.getYear();

            int resultMonthCount = year * 12 + month + months;
            int resultYear = resultMonthCount / 12;
            int resultMonth = resultMonthCount - resultYear * 12;

            date.setMonth(resultMonth);
            date.setYear(resultYear);
        }
    }

    public static Date getDataAllUltimoDelMeseCorrente() {
        Date meseSuccessivo = new Date();
        addMonthsToDate(meseSuccessivo, 1);
        resetTimeTo000000(meseSuccessivo);
        return meseSuccessivo;
    }

    /**
     * @return la data al primo giorno del mese corrente
     */
    public static Date getDataAlPrimoDelMeseCorrente() {
        Date date = new Date();
        resetTimeTo000000(date);
        return date;
    }
    
    /**
     * @return la data al primo dell'anno corrente
     */
    public static Date getDataAlPrimoDellAnnoCorrente() {
    	Date date = new Date();
    	date.setMonth(0);
    	date.setDate(1);
    	date.setHours(0);
    	date.setMinutes(0);
    	date.setSeconds(0);
    	return date;
    }
    
    /**
     * @return la data all'ultimo dell'anno corrente
     */
    public static Date getDataAllUltimoDellAnnoCorrente() {
    	Date date = new Date();
    	date.setMonth(11);
    	date.setDate(31);
    	date.setHours(23);
    	date.setMinutes(59);
    	date.setSeconds(59);
    	return date;
    }

    public static DateTimeFormat getDayMonthYearFormat() {
        return DateTimeFormat.getFormat("dd/MM/yyyy");
    }

    public static String getEuroFromBigDecimal(BigDecimal centesimi) {
        if (null != centesimi)
            return currencyNumberFormat.format(centesimi);
        return currencyNumberFormat.format(BigDecimal.ZERO);
    }

    public static String getEuroFromBigDecimalCent(BigDecimal centesimi) {
        if (null != centesimi)
            return currencyNumberFormat.format(centesimi.movePointLeft(2));
        return currencyNumberFormat.format(BigDecimal.ZERO);
    }

    public static BigDecimal getIncassoValueEuroCent(NewTitoloProxy object) {
        BigDecimal bd = BigDecimal.ZERO;
        if (null != object.getUltimoIncassoCheHaMessoIlTitoloInStatoIncassato()
                && null != object.getUltimoIncassoCheHaMessoIlTitoloInStatoIncassato().getImportoIncassoEuroCent())
            bd = object.getUltimoIncassoCheHaMessoIlTitoloInStatoIncassato().getImportoIncassoEuroCent();
        return bd;
    }

    public static DateTimeFormat getMonthYearFormat() {
        return DateTimeFormat.getFormat("MM/yyyy");
    }

    public static NumberFormat getNumberCurrencyFormat() {
        return currencyNumberFormat;
    }

    public static String getPersonaDescription(Gender g) {
        if (isPersonaFisica(g))
            return "FISICA";
        return "GIURIDICA";
    }

    public static BigDecimal getPremioLordoEuroCent(NewTitoloProxy object) {
        return object.getNetEuroCent().add(object.getAccessoriEuroCent().add(object.getTaxesEuroCent()));
    }

    public static String getStringDateFormatted(Date date) {
        String dateStringFormatted = "";
        if (null != date) {
            //sfrutto il datebox per formattare la data non avendo a disposizione (in gwt) le librerie java che servirebbero
            DateTimeFormat format = DateTimeFormat.getFormat("dd/MM/yyyy");
            DateBox box = new DateBox();
            box.setFormat(new DateBox.DefaultFormat(format));
            box.setValue(date);
            dateStringFormatted = box.getTextBox().getValue();
        }
        return dateStringFormatted;
    }

    public static String getTodayDateToString() {
        return formatDateToMeseStringAnno(new Date());
    }

    /**
     * @return ritorna la data al tempo T=0 (scelto il 01-01-2011). Serve per caricare le chiusure
     */
    public static Date getZeroTimeDate() {
        Date c = new Date();
        c.setMonth(Calendar.JANUARY);
        c.setYear(111);
        c.setDate(1);
        return c;
    }

    public static boolean isPersonaFisica(Gender g) {
        return !(Gender.C == g);
    }

    @SuppressWarnings("deprecation")
    private static void resetTimeTo000000(Date date) {
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
    }

    public static void showError(String text) {
        ErrorAlertMessage message = new ErrorAlertMessage();
        message.show(text);
    }

    public static void showSuccess() {
        SuccessAlertMessage message = new SuccessAlertMessage();
        message.show();
    }

    public static void eliminaLoader() {
        if (loader != null)
            eliminaConFadeEffect(loader, 0, 0.7);
        loader = null;
    }

    public static void creaLoader() {
        if (loader == null) {
            loader = new Loader();
        }
        if (RootPanel.get().getWidgetIndex(loader) == -1) {
            RootPanel.get().add(loader);
        }
    }

    public static void eliminaConFadeEffect(final Widget w, int delay, double duration) {
        w.getElement().getStyle().setZIndex(999);
        Fade fade = new Fade(w.getElement());
        fade.setDuration(duration);
        if (delay > 0)
            fade.play(delay);
        else
            fade.play();
        fade.addEffectCompletedHandler(new EffectCompletedHandler() {
            @Override
            public void onEffectCompleted(EffectCompletedEvent event) {
                RootPanel.get().remove(w);
                w.getElement().getStyle().setZIndex(-1);
            }
        });
    }
}