/**
 * 
 */
package com.kp.marsh.ebt.client.webapp.ui;

import com.google.gwt.canvas.dom.client.Context2d;

/**
 * @author dariobrambilla
 * Classe che stampa a video il testo e ha i metodi per traslarlo e ruotarlo
 */
public class TextManager extends CanvasAbstractManager {

    private double x;
    private double y;
    private double approssimazioneX;
    private double approssimazioneY;
    private String color;
    private String font;
    private String testo;

    /**
     * Costruttore che sette le varibili per la stampa a video del testo
     * @param ctx contesto
     * @param color colore
     * @param font tipo di carattere
     * @param x ascissa del centro del testo
     * @param y ordinata della base del testo
     * @param testo testo da stampare a video
     * @param approssimazioneX approssimazione per la traslazione orizzontale
     * @param approssimazioneY approssimazione per la traslazione verticale
     */
    public TextManager(Context2d ctx, String color, String font, double x, double y, String testo,
            double approssimazioneX, double approssimazioneY) {
        super(ctx);
        this.x = x;
        this.y = y;
        this.color = color;
        this.font = font;
        this.testo = testo;
        this.approssimazioneX = approssimazioneX;
        this.approssimazioneY = approssimazioneY;
    }

    /* (non-Javadoc)
     * @see com.kp.marsh.ebt.ebt.client.ui.CanvasAbstractManager#disegna()
     */
    @Override
    protected void disegna() {
        ctx.beginPath();
        ctx.setFont(font);
        ctx.setFillStyle(color);
        ctx.fillText(testo, x - testo.length() * approssimazioneX, y + approssimazioneY);
        ctx.closePath();
    }

    //setter methods
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setApprossimazioneX(double approssimazioneX) {
        this.approssimazioneX = approssimazioneX;
    }

    public void setApprossimazioneY(double approssimazioneY) {
        this.approssimazioneY = approssimazioneY;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }
}
