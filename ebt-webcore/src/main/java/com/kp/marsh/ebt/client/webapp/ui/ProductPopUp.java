/**
 * 
 */
package com.kp.marsh.ebt.client.webapp.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author christianachilli
 * Opens When click on a product label
 */
public class ProductPopUp extends PopupPanel {

    private static ProductPopUpUiBinder uiBinder = GWT.create(ProductPopUpUiBinder.class);

    interface ProductPopUpUiBinder extends UiBinder<Widget, ProductPopUp> {
    }

    public ProductPopUp() {
        super(true);
        add(uiBinder.createAndBindUi(this));
    }

    @UiField
    DivElement productDescription;

    //	private static ProductPopUp pd;
    //	
    //	public static ProductPopUp getInstance() {
    //		if(null == pd) {
    //			pd = new ProductPopUp();
    //		}
    //		return pd;
    //	}
    //	
    //	public static void reset() {// siccome è statico, se non chiudo il browser ad ogni ripartenza viene aggiunto un close handler
    //		pd = null;
    //	
    //	}

    public void setProductDescription(String itemName) {
        productDescription.setInnerHTML(itemName);

    }

}
