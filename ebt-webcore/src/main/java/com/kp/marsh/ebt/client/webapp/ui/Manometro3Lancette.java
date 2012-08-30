package com.kp.marsh.ebt.client.webapp.ui;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * @author dariobrambilla
 * Manometro per la rappresentazione dell'indice di penetrazione
 */
public class Manometro3Lancette extends Composite implements IndiciGraficiConstant {
    private final static double AREA_WIDTH = 215; //larghezza canvas
    private final static double AREA_CENTER_X = AREA_WIDTH / 2;

    //arco riferimento
    private final static double RIFERIMENTO_RADIUS = 70;
    private final static double RIFERIMENTO_CENTER_LEFT = AREA_CENTER_X - RIFERIMENTO_RADIUS;
    private final static double RIFERIMENTO_CENTER_RIGHT = AREA_CENTER_X + RIFERIMENTO_RADIUS;

    //cerchio
    private final static double CERCHIO_RIFERIMENTO_RADIUS = RIFERIMENTO_WIDTH / 2;
    private final static String CERCHIO_RIFERIMENTO_COLOR = "rgb(86, 86, 85)";

    //text
    private final static double TITLE_Y = RIFERIMENTO_TEXT_Y + 17;
    private final static String TITLE = "Indice di penetrazione";

    //linea
    private final static double LINEA_WIDTH = FRECCE_HEIGTH / 4;

    //linea tratteggiata
    private final static String LINEA_TRATTEGGIATA_COLOR = "rgb(86, 86, 85)";//"rgb(83, 144, 201)";
    private final static int LINEA_TRATTEGGIATA_N_SEGMENTI = 7;

    //approssimazioni frutto di prove empiriche a video	
    private final static double APPROSSIMAZIONE_TEXT_TITLE = 2.2;

    private int indicePenetrazioneAttuale = 0;
    private int indicePenetrazionePrecedente = 0;
    private int indiceMediaNazionale = 0;

    Context2d ctx;

    /**
     * Costruttore che crea il canvas e il contesto sul quale operare
     */
    @UiConstructor
    public Manometro3Lancette() {
        HTMLPanel vp = new HTMLPanel("");
        //creating and setting canvas
        Canvas c = Canvas.createIfSupported();
        c.setCoordinateSpaceHeight(AREA_HEIGTH);
        c.setCoordinateSpaceWidth((int) AREA_WIDTH);
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
     * Verifica e ritorna il medesimo valore dell'indice quando questo  compreso tra 0 e 100 altrimenti ritorna il valore dell'estremo + vicino.
     * @param indice di cui verificare se appartiene al range di valore permessi
     * @return il valore dell'indice passato come parmetro o il nuovo valore se necessario
     */
    private int normalizzaRange(int indice) {
        if (indice >= MAX)
            return MAX;
        else if (indice <= MIN)
            return MIN;
        return indice;
    }

    /**
     * Calcola i radianti corrispettivi del valore passato in ingresso sulla scala da 0 a 100
     * @param i indice per cui calcolare il valore dell'angolo di rappresentazione
     * @return l'angolo in radianti
     */
    private double getRadiusFromIndex(int i) {
        double result = Math.PI * i / 100;
        return result;
    }

    /**
     * Dato un valore in ingresso ne calcola l'angolo in radianti per la rappresentazione sull'arco di riferimento
     * @param value di cui calcolare l'angolo di rappresentazione
     * @return angolo in radianti
     */
    private double normalizzaPerSistemaRiferimento(int value) {
        int result = normalizzaRange(value);
        return normalizzaOrientamento(getRadiusFromIndex(result));
    }

    /**
     * Calcola l'angolo in radianti per il nostro sistema di riferimento che pone lo 0 (se costruiamo virtualmente un sistema si assi cartesiano centrato nel centro dell'arco di rifeimento) sull'asse delle ascisse negative.
     * @param angolo in radianti del nostro sistema di riferimento
     * @return il nuovo angolo in radianti basato su un sitema di riferimento con lo 0 posto sull'asse delle ascisse positivo
     */
    private double normalizzaOrientamento(double angolo) {
        return angolo + Math.PI;
    }

    /**
     * Gestisce l'animazione per mezzo di un timer che rigenera ogni volta il disegno del manometro
     * @param indicePenetrazioneAttualeEndPoint
     * @param indiceMediaEndPoint
     * @param indicePenetrazionePrecedenteEndPoint
     */
    public void animate(final int indicePenetrazioneAttualeEndPoint, final int indiceMediaEndPoint,
            final int indicePenetrazionePrecedenteEndPoint) {
        final Timer timer = new Timer() {
            @Override
            public void run() {
                if (indicePenetrazioneAttuale == indicePenetrazioneAttualeEndPoint
                        && indiceMediaNazionale == indiceMediaEndPoint
                        && indicePenetrazionePrecedente == indicePenetrazionePrecedenteEndPoint)
                    this.cancel();
                else {
                    ctx.clearRect(0, 0, AREA_WIDTH, AREA_HEIGTH);
                    //frecce nere (pentrazione attuale)
                    if (indicePenetrazioneAttuale < indicePenetrazioneAttualeEndPoint)
                        indicePenetrazioneAttuale++;
                    else if (indicePenetrazioneAttuale > indicePenetrazioneAttualeEndPoint)
                        indicePenetrazioneAttuale--;
                    if (indicePenetrazioneAttuale > MAX)
                        indicePenetrazioneAttuale = indicePenetrazioneAttualeEndPoint;
                    else if (indicePenetrazioneAttuale < MIN)
                        indicePenetrazioneAttuale = indicePenetrazioneAttualeEndPoint;

                    //freccia azzurra (penetrazione precedente)
                    if (indicePenetrazionePrecedente < indicePenetrazionePrecedenteEndPoint)
                        indicePenetrazionePrecedente++;
                    else if (indicePenetrazionePrecedente > indicePenetrazionePrecedenteEndPoint)
                        indicePenetrazionePrecedente--;
                    if (indicePenetrazionePrecedente > MAX)
                        indicePenetrazionePrecedente = indicePenetrazionePrecedenteEndPoint;
                    else if (indicePenetrazionePrecedente < MIN)
                        indicePenetrazionePrecedente = indicePenetrazionePrecedenteEndPoint;

                    //satellite	(media nazionale)		
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
        timer.scheduleRepeating(MS_REFRESH);
    }

    /**
     * Settai valori per i tre indici e chiama il metodo draw() per disegnare l'istogramma
     * @param indicePenetrazioneAttuale
     * @param indiceMediaNazionale
     * @param indicePenetrazionePrecedente
     */
    public void draw(int indicePenetrazioneAttuale, int indiceMediaNazionale, int indicePenetrazionePrecedente) {
        this.indiceMediaNazionale = indiceMediaNazionale;
        this.indicePenetrazioneAttuale = indicePenetrazioneAttuale;
        this.indicePenetrazionePrecedente = indicePenetrazionePrecedente;
        draw();
    }

    /**
     * Disegna il manometro chiamando uno per volta i metodi di disegno dei singoli elementi
     */
    private void draw() {
        ctx.clearRect(0, 0, AREA_WIDTH, AREA_HEIGTH);
        //		drawTitle();
        drawArcoRiferimento();
        drawIndicatoreAttuale();
        drawIndicatorePrecedente();
        drawDelta();
        drawSatellite();
        drawCerchioRiferimento();
    }

    /**
     * Disegna la parte di arco compresa tra l'indicatore di media nazionale e l'indicatore di penetrazione attuale con i colori dovuti a seconda dei valori dei due indici
     */
    private void drawDelta() {
        //gestione colori in base alla differenza di valore tra i due indici
        int start = indiceMediaNazionale;
        int end = indicePenetrazioneAttuale;
        int delta = indicePenetrazioneAttuale - indiceMediaNazionale;
        String color = VERDE;
        if (delta < -10) {
            color = ARANCIONE;
            start = indicePenetrazioneAttuale;
            end = indiceMediaNazionale;
        } else if (delta < 0) {
            color = GIALLO;
            start = indicePenetrazioneAttuale;
            end = indiceMediaNazionale;
        } else if (delta < 10) {
            color = GIALLO;
        }
        //disegno dell'arco
        ArcoManager arcoManager = new ArcoManager(ctx, AREA_CENTER_X, RIFERIMENTO_BASE, RIFERIMENTO_RADIUS,
                normalizzaPerSistemaRiferimento(start), normalizzaPerSistemaRiferimento(end), RIFERIMENTO_WIDTH, color);
        arcoManager.disegna();
    }

    /**
     * Stampa il titolo del manometro
     */
    private void drawTitle() {
        //titolo
        TextManager textManager = new TextManager(ctx, TITLE_COLOR, FONT, AREA_CENTER_X, TITLE_Y, TITLE,
                APPROSSIMAZIONE_TEXT_TITLE, 1);
        textManager.disegna();
    }

    /**
     * Disegna l'arco di riferimento e i valori di minimo e massimo negli estremi
     */
    private void drawArcoRiferimento() {
        //arco
        ArcoManager arcoManager = new ArcoManager(ctx, AREA_CENTER_X, RIFERIMENTO_BASE, RIFERIMENTO_RADIUS,
                normalizzaPerSistemaRiferimento(MIN), normalizzaPerSistemaRiferimento(MAX), RIFERIMENTO_WIDTH,
                RIFERIMENTO_COLOR);
        arcoManager.disegna();
        //indici numerici
        TextManager textManager = new TextManager(ctx, INDICI_TEXT_COLOR, FONT, RIFERIMENTO_CENTER_LEFT,
                RIFERIMENTO_TEXT_Y, MIN_STR, APPROSSIMAZIONE_LARGHEZZA_TESTO, 1);
        textManager.disegna();
        textManager.setTesto(MAX_STR);
        textManager.setX(RIFERIMENTO_CENTER_RIGHT);
        textManager.disegna();
    };

    /**
     * Disegna il cerchio centrale da cui partono le line degli indicatori di penetrazione
     */
    private void drawCerchioRiferimento() {
        CerchioManager cerchioManager = new CerchioManager(ctx, AREA_CENTER_X, RIFERIMENTO_BASE,
                CERCHIO_RIFERIMENTO_RADIUS, CERCHIO_RIFERIMENTO_COLOR);
        cerchioManager.disegna();
    }

    /**
     * Disegna il satellite (un cerchio, un rettangolo e il testo del valore posto nel centro della figura)
     */
    private void drawSatellite() {
        double radianti = normalizzaPerSistemaRiferimento(indiceMediaNazionale);
        //cerchio satellite
        CerchioManager cerchioManager = new CerchioManager(ctx, AREA_CENTER_X - RIFERIMENTO_RADIUS, RIFERIMENTO_BASE,
                SATELLITE_CERCHIO_RADIUS, SATELLITE_COLOR);
        cerchioManager.ruotaDisegna(AREA_CENTER_X, RIFERIMENTO_BASE, radianti);
        //rettnagolo satellite
        RettangoloManager rettangoloManager = new RettangoloManager(ctx, AREA_CENTER_X - RIFERIMENTO_RADIUS
                - SATELLITE_RETTANGOLO_WIDTH / 2, RIFERIMENTO_BASE - SATELLITE_RETTANGOLO_HEIGHT / 2,
                SATELLITE_RETTANGOLO_WIDTH, SATELLITE_RETTANGOLO_HEIGHT, SATELLITE_COLOR);
        rettangoloManager.ruotaDisegna(AREA_CENTER_X, RIFERIMENTO_BASE, radianti);
        //testo satellite
        double x = AREA_CENTER_X - RIFERIMENTO_RADIUS;
        TextManager textManager = new TextManager(ctx, SATELLITE_TEXT_COLOR, FONT, x, RIFERIMENTO_BASE, ""
                + indiceMediaNazionale, APPROSSIMAZIONE_LARGHEZZA_TESTO, APPROSSIMAZIONE_ALTEZZA_TESTO);
        textManager.traslaByRadiantiDisegna(x, RIFERIMENTO_BASE, AREA_CENTER_X, RIFERIMENTO_BASE,
                getRadiusFromIndex(normalizzaRange(indiceMediaNazionale)));
    }

    /**
     * Disegna l'indicatore di penetrazione precedente (una linea, una freccia e il testo del valore)
     */
    private void drawIndicatorePrecedente() {
        double radianti = normalizzaPerSistemaRiferimento(indicePenetrazionePrecedente);
        boolean destra = true;
        //linea
        LineaTratteggiataManager lineaTratteggiataManager = new LineaTratteggiataManager(ctx, AREA_CENTER_X
                - RIFERIMENTO_RADIUS + (RIFERIMENTO_WIDTH / 2) + FRECCE_HEIGTH, RIFERIMENTO_BASE, AREA_CENTER_X
                - CERCHIO_RIFERIMENTO_RADIUS, RIFERIMENTO_BASE, LINEA_WIDTH, LINEA_TRATTEGGIATA_COLOR,
                LINEA_TRATTEGGIATA_N_SEGMENTI);
        lineaTratteggiataManager.ruotaDisegna(AREA_CENTER_X, RIFERIMENTO_BASE, radianti);
        //frecce
        TriangoloManager triangoloDestro = new TriangoloManager(ctx, RIFERIMENTO_CENTER_LEFT - RIFERIMENTO_WIDTH / 2
                + RIFERIMENTO_WIDTH, RIFERIMENTO_BASE, FRECCE_HEIGTH, LINEA_TRATTEGGIATA_COLOR, destra);
        triangoloDestro.ruotaDisegna(AREA_CENTER_X, RIFERIMENTO_BASE, radianti);
        //testo
        double x = AREA_CENTER_X - RIFERIMENTO_RADIUS - RIFERIMENTO_WIDTH - FRECCE_HEIGTH;
        if (Math.abs(indicePenetrazionePrecedente - indicePenetrazioneAttuale) < 10)
            x -= 11;
        TextManager textManager = new TextManager(ctx, LINEA_TRATTEGGIATA_COLOR, FONT, x, RIFERIMENTO_BASE, ""
                + indicePenetrazionePrecedente, APPROSSIMAZIONE_LARGHEZZA_TESTO, APPROSSIMAZIONE_ALTEZZA_TESTO);
        textManager.traslaByRadiantiDisegna(x, RIFERIMENTO_BASE, AREA_CENTER_X, RIFERIMENTO_BASE,
                getRadiusFromIndex(normalizzaRange(indicePenetrazionePrecedente)));
    }

    /**
     * Disegna l'indicatore di penetrazione attuale (una linea, due frecce e il testo del valore)
     */
    private void drawIndicatoreAttuale() {
        double x = RIFERIMENTO_CENTER_LEFT - RIFERIMENTO_WIDTH / 2;
        double radianti = normalizzaPerSistemaRiferimento(indicePenetrazioneAttuale);
        //linea
        LineaManager lineaManager = new LineaManager(ctx, AREA_CENTER_X - CERCHIO_RIFERIMENTO_RADIUS, RIFERIMENTO_BASE,
                AREA_CENTER_X - RIFERIMENTO_RADIUS + (RIFERIMENTO_WIDTH / 2) + FRECCE_HEIGTH / 2, RIFERIMENTO_BASE,
                LINEA_WIDTH, VERDE);
        lineaManager.ruotaDisegna(AREA_CENTER_X, RIFERIMENTO_BASE, radianti);
        //frecce
        boolean destra = true;
        TriangoloManager triangoloSinistro = new TriangoloManager(ctx, x, RIFERIMENTO_BASE, FRECCE_HEIGTH, VERDE,
                !destra);
        triangoloSinistro.ruotaDisegna(AREA_CENTER_X, RIFERIMENTO_BASE, radianti);
        TriangoloManager triangoloDestro = new TriangoloManager(ctx, x + RIFERIMENTO_WIDTH, RIFERIMENTO_BASE,
                FRECCE_HEIGTH, VERDE, destra);
        triangoloDestro.ruotaDisegna(AREA_CENTER_X, RIFERIMENTO_BASE, radianti);
        //testo
        double xx = AREA_CENTER_X - RIFERIMENTO_RADIUS - RIFERIMENTO_WIDTH - FRECCE_HEIGTH;
        TextManager textManager = new TextManager(ctx, VERDE, FONT, xx, RIFERIMENTO_BASE, ""
                + indicePenetrazioneAttuale, APPROSSIMAZIONE_LARGHEZZA_TESTO, APPROSSIMAZIONE_ALTEZZA_TESTO);
        textManager.traslaByRadiantiDisegna(xx, RIFERIMENTO_BASE, AREA_CENTER_X, RIFERIMENTO_BASE,
                getRadiusFromIndex(normalizzaRange(indicePenetrazioneAttuale)));
    }
}