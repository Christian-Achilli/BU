/**
 * 
 */
package com.kp.marsh.ebt.client.webapp.ui;

import com.google.gwt.canvas.dom.client.Context2d;

/**
 * @author dariobrambilla
 * Classe che disegna una linea e ha i metodi per ruotarla e traslarla
 *
 */
public class LineaManager extends CanvasAbstractManager {

    final private double x1;
    final private double y1;
    final private double x2;
    final private double y2;
    final private double width;
    final private String color;

    /**
     * Costruttore che setta le variabili per la rappresentazione della linea
     * @param ctx contesto
     * @param x1 ascissa del primo estremo
     * @param y1 ordinata del primo estremo
     * @param x2 ascissa del secondo estremo
     * @param y2 ordinata del secondo estremo
     * @param width spessore della linea in pixel
     * @param color colore per lo spessore
     */
    public LineaManager(Context2d ctx, double x1, double y1, double x2, double y2, double width, String color) {
        super(ctx);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.width = width;
        this.color = color;
    }

    /* (non-Javadoc)
     * @see com.kp.marsh.ebt.ebt.client.ui.CanvasAbstractManager#disegna()
     */
    @Override
    protected void disegna() {
        ctx.beginPath();
        ctx.moveTo(x1, y1);
        ctx.lineTo(x2, y2);
        ctx.setLineWidth(width);
        ctx.setStrokeStyle(color);
        ctx.stroke();
        ctx.closePath();
    }
}
