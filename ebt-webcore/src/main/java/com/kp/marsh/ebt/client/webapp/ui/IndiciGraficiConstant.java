package com.kp.marsh.ebt.client.webapp.ui;

/**
 * @author dariobrambilla
 * Interfaccia che definisce una serie di constanti comuni per la generazione dell'istogramma e del manometro
 */
public interface IndiciGraficiConstant {
    final int AREA_HEIGTH = 130; //altezza canvas
    final String FONT = "11.0px 'Arial'";
    final String TITLE_COLOR = "rgb(133, 133, 133)";
    //animazione
    final int MS_REFRESH = 1;

    //barra o arco di riferimento
    final int RIFERIMENTO_MARGIN_TOP = 15;
    final int MAX = 100;
    final int MIN = 0;
    final String MAX_STR = "%";//""+MAX;
    final String MIN_STR = "";//""+MIN;
    final String INDICI_TEXT_COLOR = "rgb(195, 195, 195)";
    final String RIFERIMENTO_COLOR = "#e4e4e4";//rgb(235, 235, 235)";
    final double RIFERIMENTO_BASE = MAX + RIFERIMENTO_MARGIN_TOP;
    final double RIFERIMENTO_WIDTH = 15;
    final double RIFERIMENTO_TEXT_Y = RIFERIMENTO_BASE + 14;

    //colori
    final String VERDE = "#abdc64";//"rgb(140, 200, 66)";
    final String GIALLO = "rgb(248, 244, 92)";
    final String ARANCIONE = "#ff5b5b";//"rgb(255,0,0)";//"rgb(255, 153, 51)";

    //frecce
    final double FRECCE_WIDTH = 7.8; //h triangle
    final double FRECCE_HEIGTH = 9; //base triangle
    final String FRECCE_COLOR = "rgb(28, 28, 26)";

    //satellite
    final String SATELLITE_COLOR = "#666666";
    final String SATELLITE_TEXT_COLOR = "#FFFFFF";
    final double SATELLITE_CERCHIO_RADIUS = RIFERIMENTO_WIDTH / 2;
    final double SATELLITE_RETTANGOLO_WIDTH = SATELLITE_CERCHIO_RADIUS * 3;
    final double SATELLITE_RETTANGOLO_HEIGHT = 3;

    //approssimazioni frutto di prove empiriche a video
    final double APPROSSIMAZIONE_ALTEZZA_TESTO = 2.8;
    final double APPROSSIMAZIONE_LARGHEZZA_TESTO = 2.7;
    final double DISTANZA_TESTO_INDICE_FRECCE = 11;
}
