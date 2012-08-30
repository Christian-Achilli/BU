/**
 * 
 */
package com.kp.marsh.ebt.client.webapp.ui;

import com.google.gwt.canvas.dom.client.Context2d;

/**
 * @author dariobrambilla
 * Classe che disegna un rettangolo e ha i metodi per ruotarlo
 */
public class RettangoloManager extends CanvasAbstractManager {
    final private double x;
    final private double y;
    final private double width;
    final private double hight;
    final private String color;

    /**
     * Costruttore che setta le varibili per il disegno del rettangolo
     * @param ctx contesto
     * @param x ascissa del vertice sinistro
     * @param y ordinata del vertice superiore
     * @param width larghezza del rettangolo
     * @param hight altezza del rettangolo
     * @param color colore del rettangolo
     */
    public RettangoloManager(Context2d ctx, double x, double y, double width, double hight, String color) {
        super(ctx);
        this.x = x;
        this.y = y;
        this.width = width;
        this.hight = hight;
        this.color = color;
    }

    /* (non-Javadoc)
     * @see com.kp.marsh.ebt.ebt.client.ui.CanvasAbstractManager#disegna()
     */
    @Override
    protected void disegna() {
        ctx.beginPath();
        ctx.rect(x, y, width, hight);
        ctx.setFillStyle(color);
        ctx.fill();
        ctx.closePath();
    }
}
