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
public class ArcoManager extends CanvasAbstractManager {
    final private double x;
    final private double y;
    final private double radius;
    final private double degreeEnd;
    final private double degreeStart;
    final private double width;
    final private String color;

    public ArcoManager(Context2d ctx, double x, double y, double radius, double degreeStart, double degreeEnd,
            double width, String color) {
        super(ctx);
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.degreeEnd = degreeEnd;
        this.degreeStart = degreeStart;
        this.width = width;
        this.color = color;
    }

    /* (non-Javadoc)
     * @see com.kp.marsh.ebt.ebt.client.ui.CanvasAbstractManager#disegna()
     */
    @Override
    public void disegna() {
        ctx.beginPath();
        ctx.arc(x, y, radius, degreeStart, degreeEnd, false);
        ctx.setLineWidth(width);
        ctx.setStrokeStyle(color);
        ctx.stroke();
        ctx.closePath();
    }
}
