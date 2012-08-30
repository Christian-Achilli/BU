package com.kp.marsh.ebt.client.webapp.ui;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Rectangle;
import org.vaadin.gwtgraphics.client.shape.Text;

import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.kp.marsh.ebt.client.webapp.ui.utils.ViewUtils;

public class IstogramWidget extends Composite {

    final static int AREAWIDTH = 75;
    final static int AREAHEIGTH = 75;
    final static String POTTEXTCOLOR = "#1F82C0";
    final static String ACHTEXTCOLOR = "white";
    final static String TEXTFONTFAMILY = "Arial";
    final static int TEXTSIZE = 12;
    final static int CANVASMARGINLEFT = 15;
    final static int CANVASMARGINTOP = TEXTSIZE + 2;
    final static int CANVASWIDTH = 45;
    final static String CANVASCOLORBLUE = "#98C82D";
    final static String CANVASCOLORGREEN = "#1F82C0";
    final static String CANVASCOLORDARKGREEN = "#306426";
    final static String CANVASCOLORDARKGREY = "#CCCCCC";
    Rectangle bluRect;
    Rectangle greenRect;
    HTML productName;
    Text potText;
    Text achText;
    int goal;
    int pot;
    int ach;
    int istogramHeight;
    //timer refresh rate, in milliseconds
    static final int refreshRate = 25;

    @UiConstructor
    public IstogramWidget() {
        istogramHeight = 0;
        int paddingTop = 0;
        String potStr = "";
        String achStr = "";
        VerticalPanel vp = new VerticalPanel();
        DrawingArea canvas = new DrawingArea(AREAWIDTH, AREAHEIGTH);
        bluRect = new Rectangle(CANVASMARGINLEFT, paddingTop + CANVASMARGINTOP, CANVASWIDTH, istogramHeight);
        bluRect.setFillColor(CANVASCOLORBLUE);
        bluRect.setStrokeWidth(0);
        bluRect.setRoundedCorners(1);
        greenRect = new Rectangle(CANVASMARGINLEFT, paddingTop + CANVASMARGINTOP, CANVASWIDTH, 0);
        greenRect.setFillColor(CANVASCOLORGREEN);
        greenRect.setStrokeWidth(0);
        greenRect.setRoundedCorners(1);
        potText = new Text(getTextPosition(potStr), paddingTop + TEXTSIZE, potStr);
        potText.setFillColor(POTTEXTCOLOR);
        potText.setFontSize(TEXTSIZE);
        potText.setStrokeColor(POTTEXTCOLOR);
        potText.setFontFamily(TEXTFONTFAMILY);
        potText.setStrokeWidth(0);
        achText = new Text(getTextPosition(achStr), paddingTop + CANVASMARGINTOP + (istogramHeight + TEXTSIZE / 2) / 2,
                achStr);
        achText.setFillColor(ACHTEXTCOLOR);
        achText.setFontSize(TEXTSIZE);
        achText.setStrokeColor(ACHTEXTCOLOR);
        achText.setStrokeWidth(0);
        achText.setFontFamily(TEXTFONTFAMILY);
        productName = new HTML();
        productName.setHeight("52px");
        vp.add(productName);
        vp.add(canvas);
        vp.setCellHorizontalAlignment(productName, HasHorizontalAlignment.ALIGN_CENTER);
        vp.setCellVerticalAlignment(productName, HasVerticalAlignment.ALIGN_TOP);
        vp.setCellVerticalAlignment(canvas, HasVerticalAlignment.ALIGN_BOTTOM);
        canvas.add(bluRect);
        canvas.add(greenRect);
        canvas.add(potText);
        canvas.add(achText);
        initWidget(vp);
    }

    public void setIstogramWidget(int pot, int ach, int hMax, String productNameLabel) {
        goal = ach;
        this.pot = pot;
        this.ach = ach;
        int paddingTop = AREAHEIGTH - hMax;
        istogramHeight = hMax - CANVASMARGINTOP;
        String potStr = ViewUtils.format(pot);
        String achStr = ViewUtils.format(ach);
        bluRect.setY(paddingTop + CANVASMARGINTOP);
        bluRect.setHeight(istogramHeight);
        greenRect.setY(paddingTop + CANVASMARGINTOP);
        greenRect.setHeight(convertToPixel(pot, ach));
        achText.setX(getTextPosition(achStr));
        achText.setY(paddingTop + CANVASMARGINTOP + (istogramHeight + TEXTSIZE / 2) / 2);
        achText.setText(achStr);
        potText.setX(getTextPosition(potStr));
        potText.setY(paddingTop + TEXTSIZE);
        potText.setText(potStr);
        productName.setHTML("<p>" + productNameLabel + "</p>");

        if (ach == 0) {
            achText.setVisible(false);
        } else {
            achText.setVisible(true);
        }
        if (pot == 0) {
            potText.setVisible(false);
        } else {
            potText.setVisible(true);
        }

        if (pot == 0 && ach == 0) { // prodotto non lavorato o nessun ach disponibile
            bluRect.setFillColor(CANVASCOLORDARKGREY);
            greenRect.setFillColor(CANVASCOLORDARKGREY);
        } else if (ach >= pot) {
            bluRect.setFillColor(CANVASCOLORDARKGREEN);
            greenRect.setFillColor(CANVASCOLORDARKGREEN);
        } else if (ach < pot) {
            greenRect.setFillColor(CANVASCOLORGREEN);
            bluRect.setFillColor(CANVASCOLORBLUE);
        }
    }

    /**
     * @param pot
     * @param ach
     * @return l'altezza in pixel del rettangolo verde proporzionata all'altezza dell'istogramma e al valore dell'achieved rispetto al potenziale
     */
    private int convertToPixel(int pot, int ach) {
        return pot != 0 ? istogramHeight - (istogramHeight * ach / pot) : 0;
    }

    /**
     * @param potStr
     * @return la posizione del testo centrata sull'isogramma
     */
    private int getTextPosition(String potStr) {
        return CANVASMARGINLEFT + (CANVASWIDTH - potStr.length() * (TEXTSIZE / 2)) / 2;
    }

    public void setGoal(final int goal) {
        this.goal = goal;
        // setup timer
        final Timer timer = new Timer() {
            @Override
            public void run() {
                refresh(this);
            }
        };
        timer.scheduleRepeating(refreshRate);
    }

    private void refresh(Timer timer) {
        if (ach == goal) {
            timer.cancel();
            return;
        } else if (goal > ach) {
            ach++;
            if (ach > pot + 1) {
                ach = goal;
            }
            if (pot < ach) {
                bluRect.setFillColor(CANVASCOLORDARKGREEN);
                greenRect.setFillColor(CANVASCOLORDARKGREEN);
            }
        } else {
            ach--;
            if (ach > pot + 1) {
                ach = pot;
            }
            if (pot == ach) {
                greenRect.setFillColor(CANVASCOLORGREEN);
                bluRect.setFillColor(CANVASCOLORBLUE);
            }
        }
        String achStr = String.valueOf(ach);
        achText.setX(getTextPosition(achStr));
        achText.setText(achStr);
        if (pot != 0) {
            greenRect.setHeight(istogramHeight - (istogramHeight * ach / pot));
        } else
            greenRect.setHeight(0);
    }
}
