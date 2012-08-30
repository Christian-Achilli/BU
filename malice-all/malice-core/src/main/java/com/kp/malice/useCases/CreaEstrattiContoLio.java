package com.kp.malice.useCases;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.DateFormats;
import jxl.write.DateTime;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.kp.malice.entities.business.ChiusuraMensileLio;
import com.kp.malice.entities.business.ContraentePolizzaLloyds;
import com.kp.malice.entities.business.EstrattoContoLio;
import com.kp.malice.entities.business.TitoloLloyds;

public class CreaEstrattiContoLio {

    @Inject
    public CreaEstrattiContoLio() {
    }

    public File generaExcel(ChiusuraMensileLio chr, EstrattoContoLio ec, File estrattoContoExcel,
            List<TitoloLloyds> titoli) throws Exception {
        ArrayList<Label> nomiColonne = creaListaHeader(); //vettore dei titoli
        ArrayList<ArrayList<Label>> righeDiStringhe = new ArrayList<ArrayList<Label>>(); //matrice di stringhe
        ArrayList<ArrayList<Number>> righeDiValoriNumerici = new ArrayList<ArrayList<Number>>(); //matrice di numeri
        ArrayList<ArrayList<DateTime>> righeDiDate = new ArrayList<ArrayList<DateTime>>(); //matrice di date
        generaContenutoPerFileExcel(chr, ec, righeDiStringhe, righeDiValoriNumerici, righeDiDate, titoli);
        popolaFileExcePerEstrattoConto(estrattoContoExcel, nomiColonne, righeDiStringhe, righeDiValoriNumerici,
                righeDiDate);
        return estrattoContoExcel;
    }

    private void popolaFileExcePerEstrattoConto(File path, ArrayList<Label> nomiColonne,
            ArrayList<ArrayList<Label>> righeDiStringhe, ArrayList<ArrayList<Number>> righeDiValoriNumerici,
            ArrayList<ArrayList<DateTime>> righeDiDate) throws IOException, WriteException {

        WorkbookSettings ws = new WorkbookSettings();
        ws.setLocale(new Locale("en", "EN"));

        WritableWorkbook wb = Workbook.createWorkbook(path, ws);
        WritableSheet sh = wb.createSheet("TabellaTitoli", 0);
        int size = righeDiStringhe.size();
        sh = riempiHeader(sh, nomiColonne);
        if (0 != righeDiValoriNumerici.size())
            if (0 != righeDiDate.size())
                for (int j = 0; j < size; j++) {
                    ArrayList<Label> stringValues = righeDiStringhe.get(j);
                    ArrayList<Number> numericValues = righeDiValoriNumerici.get(j);
                    ArrayList<DateTime> dateValues = righeDiDate.get(j);
                    sh = riempiBody(sh, stringValues, numericValues, dateValues, j + 1);
                }

        // scrivo la linea di separazione       
        WritableCellFormat cf = new WritableCellFormat();
        cf.setBackground(Colour.GREY_25_PERCENT);
        int i = 0;
        while (i <= 23) {
            Label cellaVuota = new Label(i, righeDiStringhe.size() + 1, "", cf);
            sh.addCell(cellaVuota);
            i++;
        }

        // scrivo la linea dei risultuati
        scriviRisultati(righeDiStringhe, sh);

        wb.write();
        wb.close();
    }

    private void scriviRisultati(ArrayList<ArrayList<Label>> righeDiStringhe, WritableSheet sh) throws WriteException,
            RowsExceededException {
        WritableCellFormat cf;
        cf = formattaHeader();
        cf.setBorder(Border.NONE, BorderLineStyle.MEDIUM);
        cf.setBackground(Colour.WHITE);
        Label risultatiStringa = null;
        risultatiStringa = new Label(3, righeDiStringhe.size() + 2, "Numero di record", cf);
        sh.addCell(risultatiStringa);
        risultatiStringa = new Label(10, righeDiStringhe.size() + 2, "Importi totali", cf);
        sh.addCell(risultatiStringa);
        Number numeroRecord = new Number(6, righeDiStringhe.size() + 2, righeDiStringhe.size(), cf);
        sh.addCell(numeroRecord);
        StringBuilder ultimaCella = new StringBuilder();
        ArrayList<Formula> risultatiNumero = new ArrayList<Formula>();
        ultimaCella.append("N" + String.valueOf(righeDiStringhe.size() + 1));
        risultatiNumero.add(new Formula(13, righeDiStringhe.size() + 2, "SUM(N2:" + ultimaCella + ")"));
        ultimaCella.deleteCharAt(0);
        ultimaCella.insert(0, "O");
        risultatiNumero.add(new Formula(14, righeDiStringhe.size() + 2, "SUM(O2:" + ultimaCella + ")"));
        ultimaCella.deleteCharAt(0);
        ultimaCella.insert(0, "P");
        risultatiNumero.add(new Formula(15, righeDiStringhe.size() + 2, "SUM(P2:" + ultimaCella + ")"));
        ultimaCella.deleteCharAt(0);
        ultimaCella.insert(0, "Q");
        risultatiNumero.add(new Formula(16, righeDiStringhe.size() + 2, "SUM(Q2:" + ultimaCella + ")"));
        ultimaCella.deleteCharAt(0);
        ultimaCella.insert(0, "R");
        risultatiNumero.add(new Formula(17, righeDiStringhe.size() + 2, "SUM(R2:" + ultimaCella + ")"));
        ultimaCella.deleteCharAt(0);
        ultimaCella.insert(0, "S");
        risultatiNumero.add(new Formula(18, righeDiStringhe.size() + 2, "SUM(S2:" + ultimaCella + ")"));
        ultimaCella.deleteCharAt(0);
        ultimaCella.insert(0, "T");
        risultatiNumero.add(new Formula(19, righeDiStringhe.size() + 2, "SUM(T2:" + ultimaCella + ")"));
        ultimaCella.deleteCharAt(0);
        ultimaCella.insert(0, "V");
        risultatiNumero.add(new Formula(21, righeDiStringhe.size() + 2, "SUM(V2:" + ultimaCella + ")"));
        ultimaCella.deleteCharAt(0);
        ultimaCella.insert(0, "X");
        risultatiNumero.add(new Formula(23, righeDiStringhe.size() + 2, "SUM(X2:" + ultimaCella + ")"));

        for (Formula formula : risultatiNumero) {
            formula.setCellFormat(cf);
            sh.addCell(formula);
        }
    }

    private static WritableSheet riempiHeader(WritableSheet sh, ArrayList<Label> nomiColonne) throws WriteException {
        int j = 0;
        for (Label label : nomiColonne) {
            WritableCellFormat cf = formattaHeader();
            if (j == 1 || j == 9 || j == 12 || j == 16 || j >= 18)
                cf.setBackground(Colour.LIGHT_TURQUOISE2);
            label.setCellFormat(cf);
            sh.addCell(label);
            CellView cv = sh.getColumnView(j);
            cv.setAutosize(true);
            sh.setColumnView(j, cv);
            j++;
        }
        return sh;
    }

    private static WritableCellFormat formattaHeader() throws WriteException {

        WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
        WritableCellFormat cf = new WritableCellFormat(wf);
        cf.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
        cf.setWrap(true);
        cf.setAlignment(Alignment.CENTRE);
        cf.setBackground(Colour.YELLOW2);
        cf.setVerticalAlignment(VerticalAlignment.CENTRE);
        return cf;
    }

    private static WritableSheet riempiBody(WritableSheet sh, ArrayList<Label> stringValues,
            ArrayList<Number> numericValues, ArrayList<DateTime> dateValues, int i) throws WriteException {

        /* Scrivo tutte le stringhe */

        //		ArrayList<Label> stringValues = creaListaStringValues(ct, i);

        WritableCellFormat cf1 = formattaClassica(i);
        for (Label stringValue : stringValues) {
            stringValue.setCellFormat(cf1);
            sh.addCell(stringValue);
        }

        /* Scrivo tutti i float */

        //		ArrayList<Number> numericValues = creaListaNumberValues(ct,cf2,i);
        for (Number numericValue : numericValues) {
            sh.addCell(numericValue);
        }
        if (null != dateValues) {
            for (DateTime dateValue : dateValues) {
                if (null != dateValue)
                    sh.addCell(dateValue);
                else {
                    Label cellaVuota = new Label(12, i, "", cf1);
                    sh.addCell(cellaVuota);
                }
            }
        }
        /* Scrivo tutte le date */

        return sh;
    }

    private static WritableCellFormat formattaClassica(int i) throws WriteException {
        WritableCellFormat cf = new WritableCellFormat();
        cf.setWrap(true);
        cf.setAlignment(Alignment.CENTRE);
        if (i % 2 == 0)
            cf.setBackground(Colour.LIGHT_GREEN);
        cf.setVerticalAlignment(VerticalAlignment.CENTRE);
        return cf;
    }

    private void generaContenutoPerFileExcel(ChiusuraMensileLio chr, EstrattoContoLio ec,
            ArrayList<ArrayList<Label>> righeDiStringhe, ArrayList<ArrayList<Number>> righeDiValoriNumerici,
            ArrayList<ArrayList<DateTime>> righeDiDate, List<TitoloLloyds> titoli) throws Exception, WriteException {
        int i = 1;
        for (TitoloLloyds titoloDto : titoli) { //numero di righe di ciascuna matrice è pari ad idList
            WritableCellFormat cf = formattaNumero(i);
            righeDiStringhe.add(creaListaStringValues(ec, titoloDto, i)); //compongo una riga della matrice di stringhe
            righeDiValoriNumerici.add(creaListaNumberValues(titoloDto, cf, i)); //compongo una riga della matrice di numeri
            righeDiDate.add(creaListaDate(chr, titoloDto, i)); //compongo una riga della matrice di date
            i++;
        }
    }

    private ArrayList<Label> creaListaHeader() { //metodo per riempire il vettore dei titoli
        /* Scrivo i Titoli in ciascuna cella*/
        ArrayList<Label> labels = new ArrayList<Label>();
        labels.add(new Label(0, 0, "Nome del Coverholder / Corrispondente"));
        labels.add(new Label(1, 0, "Codice PIN del CH o dell'OMC")); ///////////////////
        labels.add(new Label(2, 0, "Incassi al"));
        labels.add(new Label(3, 0, "Numero del Binder / Numero di Cover Note"));
        labels.add(new Label(4, 0, "Codice del London Broker"));
        labels.add(new Label(5, 0, "Ramo"));
        labels.add(new Label(6, 0, "Numero di Certificato"));
        labels.add(new Label(7, 0, "Numero di appendice"));
        labels.add(new Label(8, 0, "Data di Effetto"));
        labels.add(new Label(9, 0, "  Nome  ")); ///////////////////
        labels.add(new Label(10, 0, "Cognome, Ragione Sociale o Nome completo"));
        labels.add(new Label(11, 0, "  Codice Fiscale  "));
        labels.add(new Label(12, 0, "Valuta")); ///////////////////
        labels.add(new Label(13, 0, "Premio Netto"));
        labels.add(new Label(14, 0, "Accessori (Italy)"));
        labels.add(new Label(15, 0, "Tasse"));
        labels.add(new Label(16, 0, "Premio Lordo")); ///////////////////
        labels.add(new Label(17, 0, "Commissioni Totali"));
        labels.add(new Label(18, 0, "Commissioni corrispondente")); ///////////////////
        labels.add(new Label(19, 0, "Commissioni Broker Consulente della stazione appaltante"));
        labels.add(new Label(20, 0, "Altre trattenute Descrizione"));
        labels.add(new Label(21, 0, "Altre trattenute Importo"));
        labels.add(new Label(22, 0, "Tipo Transazione"));
        labels.add(new Label(23, 0, "Importo pagato"));
        return labels;
    }

    private ArrayList<Label> creaListaStringValues(EstrattoContoLio ec, TitoloLloyds ct, int i) { //metodo per riempire la riga della matrice di stringhe
        ArrayList<Label> stringValues = new ArrayList<Label>();
        stringValues
                .add(new Label(0, i, ct.getCertificatoLloyds().getFilieraLloyds().getAgenziaRma().getDescription()));
        stringValues.add(new Label(1, i, ct.getCertificatoLloyds().getFilieraLloyds().getAgenziaRma().getOmcCode()));
        stringValues.add(new Label(3, i, ct.getCertificatoLloyds().getFilieraLloyds().getBindingAuthority()
                .getBrokerRef()));
        stringValues.add(new Label(4, i, ec.getLabel()));
        stringValues.add(new Label(5, i, ct.getCertificatoLloyds().getRischi().get(0).getCodice()));
        stringValues.add(new Label(6, i, ct.getCertificatoLloyds().getNumero()));
        stringValues.add(new Label(7, i, ct.getNumeroAppendice()));
        ContraentePolizzaLloyds contraente = ct.getContraente();
        stringValues.add(new Label(9, i, contraente.isPersonaFisica() ? contraente.getFirstName() : ""));
        stringValues.add(new Label(10, i, contraente.isPersonaFisica() ? contraente.getLastName() : contraente
                .getCompanyName()));
        stringValues.add(new Label(11, i, contraente.getFiscalCode()));
        stringValues.add(new Label(12, i, ct.getCurrency()));
        stringValues.add(new Label(20, i, "")); // inserire descrizione
        stringValues.add(new Label(22, i, "PREMIO + TASSE"));
        return stringValues;
    }

    private ArrayList<Number> creaListaNumberValues(TitoloLloyds et, WritableCellFormat cf2, int i) { //metodo per riempire la riga della matrice di numeri
        ArrayList<Number> numericValues = new ArrayList<Number>();
        numericValues.add(new Number(13, i, et.getNetEuroCent().movePointLeft(2).doubleValue(), cf2));
        numericValues.add(new Number(14, i, et.getAccessoriEuroCent().movePointLeft(2).doubleValue(), cf2));
        numericValues.add(new Number(15, i, et.getTaxesEuroCent().movePointLeft(2).doubleValue(), cf2));
        numericValues.add(new Number(16, i, et.getPremioLordoTotaleEuroCent().movePointLeft(2).doubleValue(), cf2));
        numericValues.add(new Number(17, i, et.getCommissioniTotaliEuroCent().movePointLeft(2).doubleValue(), cf2));
        numericValues.add(new Number(18, i, 0, cf2));
        numericValues.add(new Number(19, i, 0, cf2));
        numericValues.add(new Number(21, i, 0, cf2));
        numericValues.add(new Number(23, i, et.getRimessaLloydsEuroCent().movePointLeft(2).doubleValue(), cf2));
        return numericValues;
    }

    private ArrayList<DateTime> creaListaDate(ChiusuraMensileLio chr, TitoloLloyds ct, int i) throws WriteException { //metodo per riempire la riga della matrice di date
        ArrayList<DateTime> dateValues = new ArrayList<DateTime>();
        WritableCellFormat cf3 = formattaData(i);
        org.joda.time.MutableDateTime ultimoDelMese = new org.joda.time.MutableDateTime(chr.getReferenceYear(),
                chr.getReferenceCalendarMonth(), 1, 0, 0, 0, 0);
        ultimoDelMese.addMonths(1);
        ultimoDelMese.addDays(-1);
        dateValues.add(new DateTime(2, i, ultimoDelMese.toDate(), cf3));
        dateValues.add(new DateTime(8, i, ct.getInceptionDate(), cf3));

        return dateValues;
    }

    private WritableCellFormat formattaNumero(int i) throws WriteException {
        WritableCellFormat cf = new WritableCellFormat(NumberFormats.FLOAT);
        cf.setWrap(true);
        cf.setAlignment(Alignment.CENTRE);
        if (i % 2 == 0)
            cf.setBackground(Colour.LIGHT_GREEN);
        cf.setVerticalAlignment(VerticalAlignment.CENTRE);
        return cf;
    }

    private WritableCellFormat formattaData(int i) throws WriteException {
        WritableCellFormat cf = new WritableCellFormat(DateFormats.FORMAT2);
        cf.setWrap(true);
        cf.setAlignment(Alignment.CENTRE);
        if (i % 2 == 0)
            cf.setBackground(Colour.LIGHT_GREEN);
        cf.setVerticalAlignment(VerticalAlignment.CENTRE);
        return cf;
    }

}
