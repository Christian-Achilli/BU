/**
 * 
 */
package com.kp.marsh.ebt.client.webapp.ui;

import com.google.gwt.canvas.dom.client.Context2d;

/**
 * @author dariobrambilla
 * Classe che disegna un triangolo e ha i metodi per ruotarlo
 */
public class TriangoloManager extends CanvasAbstractManager {

    final private double x;
    final private double y;
    final private double h;
    final private boolean destra; //indica l'orientamento verso destra se true, viceversa se false;
    final private String color;

    /**
     * Costruttore che sette le variabili per disegnare a video il triangolo
     * @param ctx contesto
     * @param x ascissa del vertice del triangolo in base al quale eseguire la rotazione
     * @param y ordinata del vertice del triangolo in base al quale eseguire la rotazione
     * @param h altezza del triangolo
     * @param color colore per il riempimento del triangolo
     * @param destra booleano che indica l'orientamento del triangolo (freccia)
     */
    public TriangoloManager(Context2d ctx, double x, double y, double h, String color, boolean destra) {
        super(ctx);
        this.x = x;
        this.y = y;
        this.h = h;
        this.color = color;
        this.destra = destra;
    }

    /* (non-Javadoc)
     * @see com.kp.marsh.ebt.ebt.client.ui.CanvasAbstractManager#disegna()
     */
    @Override
    protected void disegna() {
        ctx.beginPath();
        double a;
        double b = y - h / 2;
        ;
        double c = y + h / 2;
        if (destra) {
            a = x + h;
        } else {
            a = x - h;
        }
        ctx.moveTo(a, b);
        ctx.lineTo(a, b);
        ctx.lineTo(a, c);
        ctx.lineTo(x, y);
        ctx.setFillStyle(color);
        ctx.fill();
        ctx.closePath();
    }
}
