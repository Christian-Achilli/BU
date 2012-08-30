package com.kp.malice.client.ui.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource.ImportedWithPrefix;
import com.google.gwt.user.cellview.client.CellTable;

public interface ListeResources extends CellTable.Resources {
	
	public final static ListeResources INSTANCE = GWT.create(ListeResources.class);

    /**
     * The styles used in this widget.
     */
    @Source(ListaStyle.DEFAULT_CSS)
    ListaStyle cellTableStyle();
    
    /**
     * Styles used by this widget.
     */
    @ImportedWithPrefix("gwt-CellTable")
    public interface ListaStyle extends CellTable.Style {
    	/**
    	 * The path to the default CSS styles used by this resource.
    	 */
    	String DEFAULT_CSS = "com/kp/malice/client/ui/resources/css/ListaTitoliCellTableForChrome.css";
    }
  }

