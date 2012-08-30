package com.kp.malice.useCases;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class PopolaFileExcel {


    public static void scriviDati(OutputStream path, ArrayList<Label> nomiColonne,
            ArrayList<ArrayList<Label>> righeDiStringValues, ArrayList<ArrayList<Number>> righeDiNumericValues,
            ArrayList<ArrayList<DateTime>> righeDiDate) throws IOException, WriteException {

        WorkbookSettings ws = new WorkbookSettings();
        ws.setLocale(new Locale("en", "EN"));

        WritableWorkbook wb = Workbook.createWorkbook(path, ws);
        WritableSheet sh = wb.createSheet("Elaborato", 0);

        if (nomiColonne.size() != 0) {
            sh = riempiHeader(sh, nomiColonne);
            if (0 != righeDiStringValues.size()) {
                int size = righeDiStringValues.size();
                for (int j = 0; j < size; j++) {
                    if (0 != righeDiNumericValues.size()) {
                        if (0 != righeDiDate.size()) {
                            ArrayList<Label> stringValues = righeDiStringValues.get(j);
                            ArrayList<Number> numericValues = righeDiNumericValues.get(j);
                            ArrayList<DateTime> dateValues = righeDiDate.get(j);
                            sh = riempiBody(sh, stringValues, numericValues, dateValues, j + 1);
                        } else {
                            ArrayList<Label> stringValues = righeDiStringValues.get(j);
                            ArrayList<Number> numericValues = righeDiNumericValues.get(j);
                            ArrayList<DateTime> dateValues = null;
                            sh = riempiBody(sh, stringValues, numericValues, dateValues, j + 1);
                        }
                    } else if (0 != righeDiDate.size()) {
                        ArrayList<Label> stringValues = righeDiStringValues.get(j);
                        ArrayList<Number> numericValues = null;
                        ArrayList<DateTime> dateValues = righeDiDate.get(j);
                        sh = riempiBody(sh, stringValues, numericValues, dateValues, j + 1);
                    } else {
                        ArrayList<Label> stringValues = righeDiStringValues.get(j);
                        ArrayList<Number> numericValues = null;
                        ArrayList<DateTime> dateValues = null;
                        sh = riempiBody(sh, stringValues, numericValues, dateValues, j + 1);
                    }
                }
            }
        }

        wb.write();
        wb.close();

    }

    public static WritableSheet riempiHeader(WritableSheet sh, ArrayList<Label> nomiColonne) throws WriteException {
        WritableCellFormat cf = formattaHeader();
        //		ArrayList<Label> nomiColonne = creaListaHeader();
        int j = 0;
        for (Label label : nomiColonne) {
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
        cf.setWrap(true);
        cf.setAlignment(Alignment.CENTRE);
        cf.setBackground(Colour.YELLOW);
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

}
