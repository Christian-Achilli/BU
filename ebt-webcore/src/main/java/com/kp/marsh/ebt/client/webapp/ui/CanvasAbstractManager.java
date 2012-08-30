package com.kp.marsh.ebt.client.webapp.ui;

import com.google.gwt.canvas.dom.client.Context2d;

/**
 * @author dariobrambilla
 * Classe astratta che implementa un metodo per la traslazione e uno per la rotazione e dichiara un metodo per il disegno
 */
public abstract class CanvasAbstractManager {

    Context2d ctx;

    /**
     * Costruttore, setta il contesto (passato come parametro) sul quale vengono eseguite operazioni di traslazione e rotazione 
     * @param ctx 
     */
    public CanvasAbstractManager(Context2d ctx) {
        this.ctx = ctx;
    }

    /**
     * Metodo implementato dalle classi manager per disegnare il proprio tipo di figura e testo 
     */
    abstract void disegna();

    /**
     * Disegna l'elemento ruotato in base al centro di rotazione e all'angolo specificato.
     * @param x ascissa del centro di rotazione
     * @param y ordinata del centro di rotazione
     * @param radianti angolo di rotazion espresso in radianti
     */
    public void ruotaDisegna(double x, double y, double radianti) {
        //salvo il contesto
        ctx.save();
        //traslo e ruoto il contesto
        ctx.translate(x, y);
        ctx.rotate(radianti + Math.PI);
        ctx.translate(-x, -y);
        //disegno la figura tramite contesto opportunamente modificato, cos“ da risultare ruotata
        disegna();
        //ripristino il contesto alla situazione iniziale
        ctx.restore();
    }

    /**
     * Trasla la figura on base al proprio centro e al centro di rotazione
     * @param xCentroFigura ascissa della figura da traslare (il testo pu˜ essere inteso come un rettangolo)
     * @param yCentroFigura ordinata della figura da traslare (il testo pu˜ essere inteso come un rettangolo)
     * @param xCentroRotazione ascissa del centro di rotazione
     * @param yCentroRotazione ordinata del centro di rotazione
     * @param radianti angolo espresso in radianti
     */
    public void traslaByRadiantiDisegna(double xCentroFigura, double yCentroFigura, double xCentroRotazione,
            double yCentroRotazione, double radianti) {
        double deltaX = Math.abs(xCentroRotazione - xCentroFigura);
        double deltaY = Math.abs(yCentroRotazione - yCentroFigura);
        double ip = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        double y = Math.sin(radianti) * ip;
        double x = Math.cos(radianti) * ip;
        //salvo il contesto
        ctx.save();
        //traslo
        ctx.translate(-x + deltaX, -y);
        //disegno la figura tramite contesto opportunamente modificato, cos“ da risultare traslata
        disegna();
        //ripristino il contesto alla situazione iniziale
        ctx.restore();
    }

}
