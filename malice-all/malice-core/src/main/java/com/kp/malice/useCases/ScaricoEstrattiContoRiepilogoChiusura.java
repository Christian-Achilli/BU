package com.kp.malice.useCases;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WriteException;

import com.kp.malice.entities.business.EstrattoContoLio;
import com.kp.malice.repositories.ChiusureMensiliLioRepository;

public class ScaricoEstrattiContoRiepilogoChiusura {
	
	private final ChiusureMensiliLioRepository chiusuraMensileRepository;

    @Inject
    public ScaricoEstrattiContoRiepilogoChiusura(ChiusureMensiliLioRepository chiusuraMensileRepository) {
        this.chiusuraMensileRepository = chiusuraMensileRepository;
    }

	public void generaExcelSuOutputStream(String id, OutputStream path) throws Exception {
        ArrayList<Label> nomiColonne = creaListaHeader(); //vettore dei titoli
        ArrayList<ArrayList<Label>> righeDiStringhe = new ArrayList<ArrayList<Label>>(); //matrice di stringhe
        ArrayList<ArrayList<Number>> righeDiValoriNumerici = new ArrayList<ArrayList<Number>>(); //matrice di numeri
        ArrayList<ArrayList<DateTime>> righeDiDate = new ArrayList<ArrayList<DateTime>>(); //matrice di date
        List<EstrattoContoLio> estrattiConto = chiusuraMensileRepository.getListaEstrattiConto(chiusuraMensileRepository.getChiusuraById(id));
        generaContenutoPerFileExcel(estrattiConto, righeDiStringhe, righeDiValoriNumerici, righeDiDate);
        PopolaFileExcel.scriviDati(path, nomiColonne, righeDiStringhe, righeDiValoriNumerici, righeDiDate);
    }

    private void generaContenutoPerFileExcel(List<EstrattoContoLio> results,
        ArrayList<ArrayList<Label>> righeDiStringhe, ArrayList<ArrayList<Number>> righeDiValoriNumerici,
        ArrayList<ArrayList<DateTime>> righeDiDate) throws Exception, WriteException {
        int i = 1;
        for (EstrattoContoLio result : results) { //numero di righe di ciascuna matrice è pari ad idList
            WritableCellFormat cf = formattaNumero(i);
            righeDiStringhe.add(creaListaStringValues(result, i)); //compongo una riga della matrice di stringhe
            righeDiValoriNumerici.add(creaListaNumberValues(result, cf, i)); //compongo una riga della matrice di numeri
//            righeDiDate.add(creaListaDate(result, i)); //compongo una riga della matrice di date
            i++;
        }

    }

    private ArrayList<Label> creaListaHeader() { //metodo per riempire il vettore dei titoli
        /* Scrivo i Titoli in ciascuna cella*/
        ArrayList<Label> labels = new ArrayList<Label>();
        labels.add(new Label(0, 0, "Broker / Cover Holder"));
        labels.add(new Label(1, 0, "Num Titoli"));
        labels.add(new Label(2, 0, "Totale Premi [EUR]"));
        labels.add(new Label(3, 0, "Totale Commissioni [EUR]"));
        labels.add(new Label(4, 0, "Totale Rimessa [EUR]"));
        return labels;
    }

    private ArrayList<Label> creaListaStringValues(EstrattoContoLio result, int i) { //metodo per riempire la riga della matrice di stringhe
        ArrayList<Label> stringValues = new ArrayList<Label>();
        stringValues.add(new Label(0, i, result.getLabel()));
        return stringValues;
    }

    private ArrayList<Number> creaListaNumberValues(EstrattoContoLio result, WritableCellFormat cf2, int i) { //metodo per riempire la riga della matrice di numeri
        ArrayList<Number> numericValues = new ArrayList<Number>();
        numericValues.add(new Number(1, i, result.getTotTitoli(), cf2));
        numericValues.add(new Number(2, i, to2Decimal(result.getTotPremiEuroCent().movePointLeft(2).floatValue()), cf2));
        numericValues.add(new Number(3, i, to2Decimal(result.getTotCommissioniEuroCent().movePointLeft(2).floatValue()), cf2));
        numericValues.add(new Number(4, i, to2Decimal(result.getTotRimessaEuroCent().movePointLeft(2).floatValue()), cf2));
        return numericValues;
    }
    
    private double to2Decimal(Float value) {
		if (value==null)
			return value=(float) 0;
		return Math.round(value*100.0) / 100.0;
	}

//    private ArrayList<DateTime> creaListaDate(EstrattoContoLio result, int i) throws WriteException { //metodo per riempire la riga della matrice di date
//        ArrayList<DateTime> dateValues = new ArrayList<DateTime>();
//        WritableCellFormat cf3 = formattaData(i);
//        dateValues.add(new DateTime(8, i, result.getTmstInsRig(), cf3));
//        return dateValues;
//    }

    private WritableCellFormat formattaNumero(int i) throws WriteException {
        WritableCellFormat cf = new WritableCellFormat(NumberFormats.FLOAT);
        cf.setWrap(true);
        cf.setAlignment(Alignment.CENTRE);
        if (i % 2 == 0)
            cf.setBackground(Colour.LIGHT_GREEN);
        cf.setVerticalAlignment(VerticalAlignment.CENTRE);
        return cf;
    }

//    private WritableCellFormat formattaData(int i) throws WriteException {
//        WritableCellFormat cf = new WritableCellFormat(DateFormats.FORMAT2);
//        cf.setWrap(true);
//        cf.setAlignment(Alignment.CENTRE);
//        if (i % 2 == 0)
//            cf.setBackground(Colour.LIGHT_GREEN);
//        cf.setVerticalAlignment(VerticalAlignment.CENTRE);
//        return cf;
//    }
}
