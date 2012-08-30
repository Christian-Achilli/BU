package com.kp.malice.useCases;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
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

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import com.kp.malice.entities.business.AgenziaRMA;
import com.kp.malice.entities.business.MaliceUserAuthenticated;
import com.kp.malice.entities.business.PuntoVenditaRMAPerLloyds;
import com.kp.malice.entities.business.TitoloLloyds;
import com.kp.malice.repositories.TitoliRepository;

/*
 * Questa class ha lo scopo di popolare il file excel, chiamando il creatore di file excel.
 * Il creatore prende in pasto 1 vettore (nome delle colonne), 3 matrici (1-Valori Tipo Stringa, 2-Valori tipo Numero, 3-Valori tipo Data) 
 * scansiona una riga alla volta. 
 */

public class ScaricoTitoliExcelGenerator {
    private static Logger log = Logger.getLogger(ScaricoIncassiExcelGenerator.class);
    private final TitoliRepository titoliRepository;

    @Inject
    public ScaricoTitoliExcelGenerator(TitoliRepository titoliRepository) {
        this.titoliRepository = titoliRepository;
    }

    public void generaExcelSuOutputStream(List<String> idList, OutputStream path) throws NumberFormatException,
            Exception {
        log.debug("ScaricoTitoliEcelGenerator.generaExcelSuOutputStream");
        ArrayList<Label> nomiColonne = creaListaHeader(); // vettore dei titoli
        ArrayList<ArrayList<Label>> righeDiStringhe = new ArrayList<ArrayList<Label>>(); // matrice
        // di
        // stringhe
        ArrayList<ArrayList<Number>> righeDiValoriNumerici = new ArrayList<ArrayList<Number>>(); // matrice
        // di
        // numeri
        ArrayList<ArrayList<DateTime>> righeDiDate = new ArrayList<ArrayList<DateTime>>(); // matrice
        // di
        // date
        generaContenutoPerFileExcel(idList, righeDiStringhe, righeDiValoriNumerici, righeDiDate);
        PopolaFileExcel.scriviDati(path, nomiColonne, righeDiStringhe, righeDiValoriNumerici, righeDiDate);
    }

    private void generaContenutoPerFileExcel(List<String> idList, ArrayList<ArrayList<Label>> righeDiStringhe,
            ArrayList<ArrayList<Number>> righeDiValoriNumerici, ArrayList<ArrayList<DateTime>> righeDiDate)
            throws Exception, WriteException {
        int i = 1;
        for (String idTitolo : idList) { // numero di righe di ciascuna matrice
            // è pari ad idList
            TitoloLloyds titolo = titoliRepository.findTitolo(Long.valueOf(idTitolo)); // verifica dei permessi
            long idUoaTitolo = titolo.getCertificatoLloyds().getFilieraLloyds().getAgenziaRma().getId();
            //          if (!titoloBelongToUser(idUoaTitolo))
            //          continue;
            EstrattoTitolo ct = new EstrattoTitolo(titolo);
            righeDiStringhe.add(creaListaStringValues(ct, i)); // compongo una
            // riga della
            // matrice di
            // stringhe
            righeDiValoriNumerici.add(creaListaNumberValues(ct, formattaNumero(i), i)); // compongo una riga della matrice
            // di numeri
            righeDiDate.add(creaListaDate(ct, i)); // compongo una riga della
            // matrice di date
            i++;
        }
    }

    private boolean titoloBelongToUser(long idUoaTitolo) { // metodo per
        // verificare i
        // permessi
        MaliceUserAuthenticated authenticatedUser = (MaliceUserAuthenticated) SecurityContextHolder.getContext()
                .getAuthentication();
        List<PuntoVenditaRMAPerLloyds> puntiVenditaUtente = authenticatedUser.getAgenzia().getPuntiVendita();
        for (PuntoVenditaRMAPerLloyds puntoVenditaRMAPerLloyds : puntiVenditaUtente) {
            AgenziaRMA age = puntoVenditaRMAPerLloyds.getAgenzia();
            if (age.getId() == idUoaTitolo)
                return true;
        }
        return false;
    }

    private ArrayList<Label> creaListaHeader() { // metodo per riempire il
        // vettore dei titoli
        /* Scrivo i Titoli in ciascuna cella */
        ArrayList<Label> labels = new ArrayList<Label>();
        labels.add(new Label(0, 0, "Numero Certificato"));
        labels.add(new Label(1, 0, "Appendice"));
        labels.add(new Label(2, 0, "Referente"));
        labels.add(new Label(3, 0, "Contraente"));
        labels.add(new Label(4, 0, "Codice Collaboratore"));
        labels.add(new Label(5, 0, "Premio Netto [EUR]"));
        labels.add(new Label(6, 0, "Premio Accessori [EUR]"));
        labels.add(new Label(7, 0, "Imposte [EUR]"));
        labels.add(new Label(8, 0, "Premio Lordo [EUR]"));
        labels.add(new Label(9, 0, "Provvigioni Netto [EUR]"));
        labels.add(new Label(10, 0, "Provvigioni Accessori [EUR]"));
        labels.add(new Label(11, 0, "Data Effetto Titolo"));
        labels.add(new Label(12, 0, "Data Incasso"));
        labels.add(new Label(13, 0, "Stato Titolo"));
        labels.add(new Label(14, 0, "Stato Incasso"));
        labels.add(new Label(15, 0, "Tipo Coassicurazione"));
        return labels;
    }

    private ArrayList<Label> creaListaStringValues(EstrattoTitolo ct, int i) { // metodo
        // per
        // riempire
        // la
        // riga
        // della
        // matrice
        // di
        // stringhe
        ArrayList<Label> stringValues = new ArrayList<Label>();
        stringValues.add(new Label(0, i, ct.getNumeroCerticato()));
        stringValues.add(new Label(1, i, ct.getNumeroAppendice()));
        stringValues.add(new Label(2, i, ct.getReferente()));
        stringValues.add(new Label(3, i, ct.getContraente()));
        stringValues.add(new Label(4, i, ct.getCodCollaboratore()));
        stringValues.add(new Label(13, i, ct.getStatoTitolo()));
        stringValues.add(new Label(14, i, ct.getStatoIncasso()));
        stringValues.add(new Label(15, i, ct.getTipoCoassicurazione()));
        return stringValues;
    }

    private ArrayList<Number> creaListaNumberValues(EstrattoTitolo ct, WritableCellFormat cf2, int i) { // metodo per riempire la riga
        // della matrice di numeri
        ArrayList<Number> numericValues = new ArrayList<Number>();
        numericValues.add(new Number(5, i, to2Decimal(ct.getPremioNetto()), cf2));
        numericValues.add(new Number(6, i, to2Decimal(ct.getPremioAccessori()), cf2));
        numericValues.add(new Number(7, i, to2Decimal(ct.getImposte()), cf2));
        numericValues.add(new Number(8, i, to2Decimal(ct.getPremioLordo()), cf2));
        numericValues.add(new Number(9, i, to2Decimal(ct.getProvvigioniNetto()), cf2));
        numericValues.add(new Number(10, i, to2Decimal(ct.getProvvigioniAccessori()), cf2));
        log.debug(ct.getPremioNetto() + " - " + ct.getPremioAccessori() + " - " + ct.getImposte() + " - "
                + ct.getPremioLordo() + " - " + ct.getProvvigioniNetto() + " - " + ct.getProvvigioniAccessori());
        return numericValues;
    }

    private double to2Decimal(Float value) {
        if (value == null)
            return value = (float) 0;
        return Math.round(value * 100.0) / 100.0;
    }

    private ArrayList<DateTime> creaListaDate(EstrattoTitolo ct, int i) throws WriteException {
        
        // metodo per riempire la riga della matrice
        // di date
        log.debug("crealistaDate.ScaricoTitoliExcelGenerator, inizio");
        ArrayList<DateTime> dateValues = new ArrayList<DateTime>();
        WritableCellFormat cf3 = formattaData(i);
        dateValues.add(new DateTime(11, i, ct.getEffettoTitolo(), cf3));
        if (null != ct.getIncasso()) {
        	Calendar cd = Calendar.getInstance();
            cd.setTime(ct.getIncasso());
            cd.add(cd.HOUR, 2);
            Date dateToAdd = cd.getTime();
            dateValues.add(new DateTime(12, i, dateToAdd, cf3));
        } else
            dateValues.add(null);
        log.debug("crealistaDate.ScaricoTitoliExcelGenerator, fine");
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

    private class EstrattoTitolo {

        private String referente;
        private String numeroAppendice;
        private String contraente;
        private String codCollaboratore;
        private Float premioNetto;
        private Float premioAccessori;
        private Float imposte;
        private Float premioLordo;
        private Float provvigioniNetto;
        private Float provvigioniAccessori;
        private Date effettoTitolo;
        private Date incasso;
        private String statoIncasso;
        private String statoTitolo;
        private String tipoCoassicurazione;

        private String getReferente() {
            return referente;
        }

        private String getNumeroAppendice() {
            return numeroAppendice;
        }

        private String getContraente() {
            return contraente;
        }

        private String getCodCollaboratore() {
            return codCollaboratore;
        }

        private Float getPremioNetto() {
            return premioNetto;
        }

        private Float getPremioAccessori() {
            return premioAccessori;
        }

        private Float getImposte() {
            return imposte;
        }

        private Float getPremioLordo() {
            return premioLordo;
        }

        private Float getProvvigioniNetto() {
            return provvigioniNetto;
        }

        private Float getProvvigioniAccessori() {
            return provvigioniAccessori;
        }

        private Date getEffettoTitolo() {
            return effettoTitolo;
        }

        private Date getIncasso() {
            return incasso;
        }

        private String getStatoIncasso() {
            return statoIncasso;
        }

        private String getStatoTitolo() {
            return statoTitolo;
        }

        private String getTipoCoassicurazione() {
            return tipoCoassicurazione;
        }

        private String getNumeroCerticato() {
            return numeroCerticato;
        }

        private String numeroCerticato;

        public EstrattoTitolo(TitoloLloyds titolo) {
            numeroCerticato = titolo.getCertificatoLloyds().getNumero();
            try {
                referente = titolo.getCertificatoLloyds().getFilieraLloyds().getReferente();
            } catch (Exception e) {
                e.printStackTrace();
            }
            numeroAppendice = titolo.getNumeroAppendice();
            contraente = titolo.getContraente().getIdentificativo();
            codCollaboratore = titolo.getCodiceSubagente();
            premioNetto = titolo.getNetEuroCent().movePointLeft(2).floatValue();
            premioAccessori = titolo.getAccessoriEuroCent().movePointLeft(2).floatValue();
            imposte = titolo.getTaxesEuroCent().movePointLeft(2).floatValue();
            premioLordo = titolo.getPremioLordoTotaleEuroCent().movePointLeft(2).floatValue();
            provvigioniNetto = titolo.getCommissionsOnNetEuroCent().movePointLeft(2).floatValue();
            provvigioniAccessori = titolo.getCommissionsOnAccessoriEuroCent().movePointLeft(2).floatValue();
            effettoTitolo = titolo.getInceptionDate();
            incasso = titolo.getDataIncassoMessaInCopertura();
            statoIncasso = null;
            if (null != titolo.getUltimoIncassoCheHaMessoIlTitoloInStatoIncassato()) {
                statoIncasso = titolo.getUltimoIncassoCheHaMessoIlTitoloInStatoIncassato().getStatoIncasso();
            }
            statoTitolo = titolo.getStatoTitolo().name();
            tipoCoassicurazione = titolo.getCertificatoLloyds().getTipoCoassicurazione();

        }
    }

}