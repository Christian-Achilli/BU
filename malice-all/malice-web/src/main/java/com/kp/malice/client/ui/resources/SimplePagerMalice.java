package com.kp.malice.client.ui.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.view.client.HasRows;
import com.google.gwt.view.client.Range;

public class SimplePagerMalice extends SimplePager {

	  public SimplePagerMalice(TextLocation center, Resources pagerResources,
			boolean b, int i, boolean c) {
		super(center, pagerResources, b, i, c);
	}

	/**
	   * Get the text to display in the pager that reflects the state of the pager.
	   * 
	   * @return the text
	   */
		@Override
	  protected String createText() {
	    // Default text is 1 based.
	    NumberFormat formatter = NumberFormat.getFormat("#,###");
	    HasRows display = getDisplay();
	    Range range = display.getVisibleRange();
	    int pageStart = range.getStart() + 1;
	    int pageSize = range.getLength();
	    int dataSize = display.getRowCount();
	    int endIndex = Math.min(dataSize, pageStart + pageSize - 1);
	    endIndex = Math.max(pageStart, endIndex);
	    boolean exact = display.isRowCountExact();
	    return formatter.format(pageStart) + "-" + formatter.format(endIndex)
	        + (exact ? " di " : " di circa ") + formatter.format(dataSize);
	  }


		public interface PagerResources extends SimplePager.Resources {
			
			public final static PagerResources INSTANCE = GWT.create(PagerResources.class);

			
			@Source("img/allback_button.png")
			ImageResource simplePagerFirstPage();

			@Source("img/allforward_button.png")
			ImageResource simplePagerLastPage();

			@Source("img/back_button.png")
			ImageResource simplePagerPreviousPage();

			@Source("img/forward_button.png")
			ImageResource simplePagerNextPage();
			
			@Source("img/allback_button.png")
			ImageResource simplePagerFirstPageDisabled();

			@Source("img/allforward_button.png")
			ImageResource simplePagerLastPageDisabled();

			@Source("img/back_button.png")
			ImageResource simplePagerPreviousPageDisabled();

			@Source("img/forward_button.png")
			ImageResource simplePagerNextPageDisabled();

			
			@Source("css/stilePaginatore.css")
			Style simplePagerStyle();
			
			
		}

		
		
}
