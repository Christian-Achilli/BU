/**
 * 
 */
package com.kp.marsh.ebt.client.webapp.ui;

import com.google.gwt.canvas.dom.client.Context2d;

/**
 * @author dariobrambilla
 * Classe che disegna una linea tratteggiata e ha i metodi per ruotarla e traslarla
 */
public class LineaTratteggiataManager extends CanvasAbstractManager {
    final private double x1;
    final private double y1;
    final private double x2;
    final private double y2;
    final private double width;
    final private int numeroSegmenti;
    final private String color;

    /**
     * @param ctx contesto
     * @param x1 ascissa del primo estremo
     * @param y1 ordinata del primo estremo
     * @param x2 ascissa del secondo estremo
     * @param y2 ordinata del secondo estremo
     * @param width spessore dei tratteggi in pixel
     * @param color colore per lo spessore dei tratteggi
     * @param numeroSegmenti numero dei segmenti (tratteggi) in cui separare la linea
     */
    public LineaTratteggiataManager(Context2d ctx, double x1, double y1, double x2, double y2, double width,
            String color, int numeroSegmenti) {
        super(ctx);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.width = width;
        this.numeroSegmenti = numeroSegmenti;
        this.color = color;
    }

    /* (non-Javadoc)
     * @see com.kp.marsh.ebt.ebt.client.ui.CanvasAbstractManager#disegna()
     */
    @Override
    protected void disegna() {
        double distanza = Math.abs(x2 - x1);
        double lunghezzaSegmento = distanza / (numeroSegmenti * 2);
        double xa = x1;
        double xb = xa + lunghezzaSegmento;
        for (int i = 0; i < numeroSegmenti; i++) {
            ctx.beginPath();
            ctx.moveTo(xa, y1);
            ctx.lineTo(xb, y2);
            ctx.setLineWidth(width);
            ctx.setStrokeStyle(color);
            ctx.stroke();
            ctx.closePath();
            xa = xb + lunghezzaSegmento;
            xb = xa + lunghezzaSegmento;
        }
    }
}
