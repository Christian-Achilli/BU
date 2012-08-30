package com.kp.malice.useCases;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.DateFormats;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WriteException;

import com.kp.malice.boundaries.services.PortaleServiceBoundary;
import com.kp.malice.entities.miscellaneous.ScritturaContabileRma;

/*
 * Questa class ha lo scopo di popolare il file excel, chiamando il creatore di file excel.
 * Il creatore prende in pasto 1 vettore (nome delle colonne), 3 matrici (1-Valori Tipo Stringa, 2-Valori tipo Numero, 3-Valori tipo Data) 
 * scansiona una riga alla volta. 
 */

public class ScaricoIncassiExcelGenerator {

    private final PortaleServiceBoundary dispaccio;

    @Inject
    public ScaricoIncassiExcelGenerator(PortaleServiceBoundary dispaccio) {
        this.dispaccio = dispaccio;
    }

    public void generaExcelSuOutputStream(Date d, OutputStream path) throws NumberFormatException, Exception {
        ArrayList<Label> nomiColonne = creaListaHeader(); //vettore dei titoli
        ArrayList<ArrayList<Label>> righeDiStringhe = new ArrayList<ArrayList<Label>>(); //matrice di stringhe
        ArrayList<ArrayList<Number>> righeDiValoriNumerici = new ArrayList<ArrayList<Number>>(); //matrice di numeri
        ArrayList<ArrayList<DateTime>> righeDiDate = new ArrayList<ArrayList<DateTime>>(); //matrice di date
        List<ScritturaContabileRma> results = dispaccio.findListByDataRegistrazioneIncasso(d);
        generaContenutoPerFileExcel(results, righeDiStringhe, righeDiValoriNumerici, righeDiDate);
        PopolaFileExcel.scriviDati(path, nomiColonne, righeDiStringhe, righeDiValoriNumerici, righeDiDate);
    }

    private void generaContenutoPerFileExcel(List<ScritturaContabileRma> results,
            ArrayList<ArrayList<Label>> righeDiStringhe, ArrayList<ArrayList<Number>> righeDiValoriNumerici,
            ArrayList<ArrayList<DateTime>> righeDiDate) throws Exception, WriteException {
        int i = 1;
        for (ScritturaContabileRma result : results) { //numero di righe di ciascuna matrice è pari ad idList
            WritableCellFormat cf = formattaNumero(i);
            righeDiStringhe.add(creaListaStringValues(result, i)); //compongo una riga della matrice di stringhe
            righeDiValoriNumerici.add(creaListaNumberValues(result, cf, i)); //compongo una riga della matrice di numeri
            righeDiDate.add(creaListaDate(result, i)); //compongo una riga della matrice di date
            i++;
        }

    }

    private ArrayList<Label> creaListaHeader() { //metodo per riempire il vettore dei titoli
        /* Scrivo i Titoli in ciascuna cella*/
        ArrayList<Label> labels = new ArrayList<Label>();
        labels.add(new Label(0, 0, "Tipo Operazione"));
        labels.add(new Label(1, 0, "Stato incasso"));
        labels.add(new Label(2, 0, "Mezzo pagamento"));
        labels.add(new Label(3, 0, "Premi [EUR]"));
        labels.add(new Label(4, 0, "Incassi [EUR]"));
        labels.add(new Label(5, 0, "Abbuoni [EUR]"));
        labels.add(new Label(6, 0, "Provvigioni [EUR]"));
        labels.add(new Label(7, 0, "Num. Registrazioni"));
        labels.add(new Label(8, 0, "Data Registrazione"));
        return labels;
    }

    private ArrayList<Label> creaListaStringValues(ScritturaContabileRma result, int i) { //metodo per riempire la riga della matrice di stringhe
        ArrayList<Label> stringValues = new ArrayList<Label>();
        stringValues.add(new Label(0, i, result.getTipoOperazione()));
        stringValues.add(new Label(1, i, result.getStatoIncasso()));
        stringValues.add(new Label(2, i, result.getCodMzzPagm()));
        return stringValues;
    }

    private ArrayList<Number> creaListaNumberValues(ScritturaContabileRma result, WritableCellFormat cf2, int i) { //metodo per riempire la riga della matrice di numeri
        ArrayList<Number> numericValues = new ArrayList<Number>();
        numericValues.add(new Number(3, i, to2Decimal(result.getPremiEuroCent().movePointLeft(2).floatValue()), cf2));
        numericValues.add(new Number(4, i, to2Decimal(result.getImportiEuroCent().movePointLeft(2).floatValue()), cf2));
        numericValues.add(new Number(5, i, to2Decimal(result.getAbbuoniEuroCent().movePointLeft(2).floatValue()), cf2));
        numericValues.add(new Number(6, i, to2Decimal(result.getProvvigioniEuroCent().movePointLeft(2).floatValue()), cf2));
        numericValues.add(new Number(7, i, result.getCounter().intValue(), cf2));
        return numericValues;
    }
    
    private double to2Decimal(Float value) {
		if (value==null)
			return value=(float) 0;
		return Math.round(value*100.0) / 100.0;
	}

    private ArrayList<DateTime> creaListaDate(ScritturaContabileRma result, int i) throws WriteException { //metodo per riempire la riga della matrice di date
        ArrayList<DateTime> dateValues = new ArrayList<DateTime>();
        WritableCellFormat cf3 = formattaData(i);
        dateValues.add(new DateTime(8, i, result.getTmstInsRig(), cf3));
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
