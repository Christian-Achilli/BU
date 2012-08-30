package com.kp.marsh.ebt.client.webapp.ui;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * @author dariobrambilla
 * Istogramma con frecce e satellite per la rappresentazione dell'indice di saturazione 
 */
public class IstogrammaFrecceSatellite extends Composite implements IndiciGraficiConstant {
    private final static int AREA_WIDTH = 70; //larghezza canvas
    private final static double CENTER_WIDTH = AREA_WIDTH / 2;

    //barra riferimento
    private final static double RIFERIMENTO_MARGINLEFT = (AREA_WIDTH - RIFERIMENTO_WIDTH) / 2;

    //text del titolo
    private final static String TITLE_1_ROW = "Saturazione";
    private final static String TITLE_2_ROW = "di potenziale";

    //approssimazioni frutto di prove empiriche a video
    private final static double APPROSSIMAZIONE_ALTEZZA_TESTO_GENERICO = 6;
    private final static double APPROSSIMAZIONE_ALTEZZA_CENTRO_TESTO_GENERICO = APPROSSIMAZIONE_ALTEZZA_TESTO_GENERICO / 2;
    private final static double TEXT_TITLE_1_ROW_APPROSSIMAZIONE_TESTO = 2.2;
    private final static double TEXT_TITLE_1_ROW_ALTEZZA_DALLA_BASE = 12;
    private final static double TEXT_TITLE_2_ROW_APPROSSIMAZIONE_TESTO = 1.9;
    private final static double TEXT_TITLE_2_ROW_ALTEZZA_DALLA_BASE = 4;
    private final static double INDICE_TOP_ALTEZZA_DAL_ALTO = 8;

    //indici
    private int indiceSaturazione = 0;
    private int indiceMediaNazionale = 0;

    Context2d ctx;

    /**
     * Costruttore che crea il canvas e il contesto sul quale operare
     */
    @UiConstructor
    public IstogrammaFrecceSatellite() {
        HTMLPanel vp = new HTMLPanel("");
        //creating and setting canvas
        Canvas c = Canvas.createIfSupported();
        c.setCoordinateSpaceHeight(AREA_HEIGTH);
        c.setCoordinateSpaceWidth(AREA_WIDTH);
        c.setHeight("" + AREA_HEIGTH);
        c.setWidth("" + AREA_WIDTH);
        //crating context2d
        ctx = c.getContext2d();
        //disegno dei vari elementi
        draw();
        //add context to panel
        vp.add(c);
        initWidget(vp);
    }

    /**
     * Gestisce l'animazione per mezzo di un timer che rigenera ogni volta il disegno dell'istogramma
     * @param indiceSaturazioneEndPoint valore dell'indice di saturazione
     * @param indiceMediaEndPoint valore dell'indice di media nazionale
     */
    public void animateIstogramArea(final int indiceSaturazioneEndPoint, final int indiceMediaEndPoint) {
        final Timer timer = new Timer() {
            @Override
            public void run() {
                if (indiceSaturazione == indiceSaturazioneEndPoint && indiceMediaNazionale == indiceMediaEndPoint)
                    this.cancel();
                else {
                    ctx.clearRect(0, 0, AREA_WIDTH, AREA_HEIGTH);
                    //frecce
                    if (indiceSaturazione < indiceSaturazioneEndPoint)
                        indiceSaturazione++;
                    else if (indiceSaturazione > indiceSaturazioneEndPoint)
                        indiceSaturazione--;
                    if (indiceSaturazione > MAX)
                        indiceSaturazione = indiceSaturazioneEndPoint;
                    else if (indiceSaturazione < MIN)
                        indiceSaturazione = indiceSaturazioneEndPoint;

                    //satellite					
                    if (indiceMediaNazionale < indiceMediaEndPoint)
                        indiceMediaNazionale++;
                    else if (indiceMediaNazionale > indiceMediaEndPoint)
                        indiceMediaNazionale--;
                    if (indiceMediaNazionale > MAX)
                        indiceMediaNazionale = indiceMediaEndPoint;
                    else if (indiceMediaNazionale < MIN)
                        indiceMediaNazionale = indiceMediaEndPoint;
                    draw();
                }
            }
        };
        timer.scheduleRepeating(25);
    }

    /**
     * Settai valori per i due indici e chiama il metodo draw() per disegnare l'istogramma
     * @param indiceSaturazione
     * @param indiceMediaNazionale
     */
    public void draw(int indiceSaturazione, int indiceMediaNazionale) {
        this.indiceMediaNazionale = indiceMediaNazionale;
        this.indiceSaturazione = indiceSaturazione;
        draw();
    }

    /**
     * Disegna l'istogramma chiamando uno per volta i metodi di disegno dei singoli elementi
     */
    private void draw() {
        ctx.clearRect(0, 0, AREA_WIDTH, AREA_HEIGTH);
        drawBarraRiferimento();
        drawDelta();
        drawArrows();
        drawIndiceFrecce();
        drawSatelliteStruttura();
        drawSatelliteText();
    }

    /**
     * Disegna la barra compresa tra l'indice di media nazionale e l'indice di saturazione con i colori dovuti a seconda della differenza tra i due indici
     */
    private void drawDelta() {
        //gestione colori
        int bottom = indiceMediaNazionale;
        int delta = indiceSaturazione - indiceMediaNazionale;
        //h altezza dell'intervallo da colorare
        int h = delta;
        String color = VERDE;
        if (delta < -10) {
            color = ARANCIONE;
            bottom = indiceSaturazione;
            h = delta * -1;
        } else if (delta < 0) {
            color = GIALLO;
            bottom = indiceSaturazione;
            h = delta * -1;
        } else if (delta < 10) {
            color = GIALLO;
        }
        if (bottom < MIN)
            bottom = MIN;
        if ((h + bottom) > MAX)
            h = MAX - bottom;
        //non dovrebbe succere che h<0 perch la media non pu˜ essere maggiore di MAX
        if (h < 0)
            h = 0; //altrimenti si disegna oltre il limite superiore della barra grigia
        ctx.setFillStyle(color);
        ctx.fillRect(RIFERIMENTO_MARGINLEFT, RIFERIMENTO_BASE - bottom - h, RIFERIMENTO_WIDTH, h);
    }

    /**
     * Disegna la barra di riferimento 0-100
     */
    private void drawBarraRiferimento() {
        //struttura
        ctx.setFillStyle(RIFERIMENTO_COLOR);
        ctx.fillRect(RIFERIMENTO_MARGINLEFT, RIFERIMENTO_MARGIN_TOP, RIFERIMENTO_WIDTH, MAX);
        //indici numerici
        TextManager textManager = new TextManager(ctx, INDICI_TEXT_COLOR, FONT, CENTER_WIDTH,
                INDICE_TOP_ALTEZZA_DAL_ALTO, MAX_STR, APPROSSIMAZIONE_ALTEZZA_CENTRO_TESTO_GENERICO, 1);
        textManager.disegna();
        textManager.setTesto(MIN_STR);
        textManager.setY(RIFERIMENTO_TEXT_Y);
        textManager.setApprossimazioneY(1);
        textManager.disegna();
        //titolo barra
        ctx.setFont(FONT);
        ctx.setFillStyle(TITLE_COLOR);
        //		ctx.fillText(TITLE_1_ROW, CENTER_WIDTH-TITLE_1_ROW.length()*TEXT_TITLE_1_ROW_APPROSSIMAZIONE_TESTO, AREA_HEIGTH-TEXT_TITLE_1_ROW_ALTEZZA_DALLA_BASE);
        //		ctx.fillText(TITLE_2_ROW, CENTER_WIDTH-TITLE_2_ROW.length()*TEXT_TITLE_2_ROW_APPROSSIMAZIONE_TESTO, AREA_HEIGTH-TEXT_TITLE_2_ROW_ALTEZZA_DALLA_BASE);
    };

    /**
     * Disegna la struttura del satellite: cerchio e rettangolo
     */
    private void drawSatelliteStruttura() {
        int indice = indiceMediaNazionale;
        //normalizzazione dei valori per evitare che escano dal range di rappresentazione 0-100
        if (indiceMediaNazionale >= MAX)
            indice = MAX;
        else if (indiceMediaNazionale <= MIN)
            indice = MIN;
        //CERCHIO
        ctx.beginPath();
        ctx.arc(CENTER_WIDTH, RIFERIMENTO_BASE - indice, SATELLITE_CERCHIO_RADIUS, 0, 360);
        ctx.closePath();
        ctx.setFillStyle(SATELLITE_COLOR);
        ctx.fill();
        //RETTANGOLO SATELLITE
        ctx.beginPath();
        ctx.rect(CENTER_WIDTH - SATELLITE_RETTANGOLO_WIDTH / 2, RIFERIMENTO_BASE - indice - SATELLITE_RETTANGOLO_HEIGHT
                / 2, SATELLITE_RETTANGOLO_WIDTH, SATELLITE_RETTANGOLO_HEIGHT);
        ctx.setFillStyle(SATELLITE_COLOR);
        ctx.fill();
        ctx.closePath();
    }

    /**
     * Stampa a video nel centro del satellite del valore dell'indice di media nazionale
     */
    private void drawSatelliteText() {
        //TESTO SATELLITE
        ctx.setFont(FONT);
        ctx.setFillStyle(SATELLITE_TEXT_COLOR);
        //normalizzazione dei valori per evitare che escano dal range di rappresentazione 0-100
        if (indiceMediaNazionale >= MAX)
            ctx.fillText("" + indiceMediaNazionale, CENTER_WIDTH - ("" + indiceMediaNazionale).length()
                    * APPROSSIMAZIONE_LARGHEZZA_TESTO, RIFERIMENTO_BASE - MAX
                    + APPROSSIMAZIONE_ALTEZZA_CENTRO_TESTO_GENERICO);
        else if (indiceMediaNazionale <= MIN)
            ctx.fillText("" + indiceMediaNazionale, CENTER_WIDTH - ("" + indiceMediaNazionale).length()
                    * APPROSSIMAZIONE_LARGHEZZA_TESTO, RIFERIMENTO_BASE - MIN
                    + APPROSSIMAZIONE_ALTEZZA_CENTRO_TESTO_GENERICO);
        else
            ctx.fillText("" + indiceMediaNazionale, CENTER_WIDTH - ("" + indiceMediaNazionale).length()
                    * APPROSSIMAZIONE_LARGHEZZA_TESTO, RIFERIMENTO_BASE - indiceMediaNazionale
                    + APPROSSIMAZIONE_ALTEZZA_CENTRO_TESTO_GENERICO);
    }

    /**
     * Stampa a video dell'indice di saturazione al lato destro delle frecce
     */
    private void drawIndiceFrecce() {
        ctx.setFont(FONT);
        ctx.setFillStyle(FRECCE_COLOR);
        //normalizzazione dei valori per evitare che escano dal range di rappresentazione 0-100
        if (indiceSaturazione >= MAX)
            ctx.fillText("" + indiceSaturazione, RIFERIMENTO_MARGINLEFT + RIFERIMENTO_WIDTH
                    + DISTANZA_TESTO_INDICE_FRECCE, RIFERIMENTO_BASE - MAX
                    + APPROSSIMAZIONE_ALTEZZA_CENTRO_TESTO_GENERICO);
        else if (indiceSaturazione <= MIN)
            ctx.fillText("" + indiceSaturazione, RIFERIMENTO_MARGINLEFT + RIFERIMENTO_WIDTH
                    + DISTANZA_TESTO_INDICE_FRECCE, RIFERIMENTO_BASE - MIN
                    + APPROSSIMAZIONE_ALTEZZA_CENTRO_TESTO_GENERICO);
        else
            ctx.fillText("" + indiceSaturazione, RIFERIMENTO_MARGINLEFT + RIFERIMENTO_WIDTH
                    + DISTANZA_TESTO_INDICE_FRECCE, RIFERIMENTO_BASE - indiceSaturazione
                    + APPROSSIMAZIONE_ALTEZZA_CENTRO_TESTO_GENERICO);
    }

    /**
     * Disegna le frecce (indice saturazione)
     */
    private void drawArrows() {
        int indice = indiceSaturazione;
        //normalizzazione dei valori per evitare che escano dal range di rappresentazione 0-100
        if (indiceSaturazione >= MAX)
            indice = MAX;
        else if (indiceSaturazione <= MIN)
            indice = MIN;
        drawLeftArrow(RIFERIMENTO_MARGINLEFT, RIFERIMENTO_BASE - indice);
        drawRightArrow(RIFERIMENTO_MARGINLEFT + RIFERIMENTO_WIDTH, RIFERIMENTO_BASE - indice);
    }

    /**
     * Disegna freccia destra
     * @param startingPointX ascissa del vertice
     * @param startingPointY ordinata del vertice
     */
    private void drawRightArrow(double startingPointX, double startingPointY) {
        ctx.beginPath();
        ctx.moveTo(startingPointX + FRECCE_HEIGTH, startingPointY - FRECCE_HEIGTH / 2);
        ctx.lineTo(startingPointX + FRECCE_HEIGTH, startingPointY - FRECCE_HEIGTH / 2);
        ctx.lineTo(startingPointX + FRECCE_HEIGTH, startingPointY + FRECCE_HEIGTH / 2);
        ctx.lineTo(startingPointX, startingPointY);
        ctx.closePath();
        ctx.setFillStyle(FRECCE_COLOR);
        ctx.fill();
    }

    /**
     * Disegna reccia sinistra
     * @param startingPointX ascissa del vertice
     * @param startingPointY ordinata del vertice
     */
    private void drawLeftArrow(double startingPointX, double startingPointY) {
        ctx.beginPath();
        ctx.moveTo(startingPointX - FRECCE_HEIGTH, startingPointY - FRECCE_HEIGTH / 2);
        ctx.lineTo(startingPointX - FRECCE_HEIGTH, startingPointY - FRECCE_HEIGTH / 2);
        ctx.lineTo(startingPointX - FRECCE_HEIGTH, startingPointY + FRECCE_HEIGTH / 2);
        ctx.lineTo(startingPointX, startingPointY);
        ctx.closePath();
        ctx.setFillStyle(FRECCE_COLOR);
        ctx.fill();
    }
}