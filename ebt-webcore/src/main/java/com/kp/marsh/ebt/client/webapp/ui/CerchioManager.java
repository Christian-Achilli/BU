/**
 * 
 */
package com.kp.marsh.ebt.client.webapp.ui;

import com.google.gwt.canvas.dom.client.Context2d;

/**
 * @author dariobrambilla
 * Classe che disegna un triangolo e ha i metodi per ruotarlo
 *
 */
public class CerchioManager extends CanvasAbstractManager {
    final private double x;
    final private double y;
    final private double radius;
    final private String color;

    /**
     * @param ctx contesto
     * @param x ascissa 
     * @param y
     * @param radius
     * @param color
     */
    public CerchioManager(Context2d ctx, double x, double y, double radius, String color) {
        super(ctx);
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    /* (non-Javadoc)
     * @see com.kp.marsh.ebt.ebt.client.ui.CanvasAbstractManager#disegna()
     */
    @Override
    public void disegna() {
        ctx.beginPath();
        ctx.arc(x, y, radius, 0, Math.PI * 2);
        ctx.setFillStyle(color);
        ctx.fill();
        ctx.closePath();
    }
}
