package com.kp.marsh.ebt.client.admin.ui;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ControllTabPanel extends Composite {

	public ControllTabPanel() {
		// Create a tab panel
		TabLayoutPanel tabPanel = new TabLayoutPanel(2.5, Unit.EM);
		tabPanel.setAnimationDuration(1000);

		// Add a home tab
		FormComando fc = new FormComando();
		tabPanel.add(fc, "CONTROLL CENTER");

		// Add a tab
		HTML moreInfo = new HTML("JUST AN IDEA!");
		tabPanel.add(moreInfo, "HISTORY");

		// Return the content
		tabPanel.selectTab(0);
		tabPanel.ensureDebugId("cwTabPanel");

		ResizeLayoutPanel resizePanel = new ResizeLayoutPanel();
		resizePanel.setPixelSize(1200, 850);
		resizePanel.setWidget(tabPanel);
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(resizePanel);
		
		HTML titleHtml = new HTML("ebt-importerWebInterface");
		titleHtml.setStyleName("title");
		
		VerticalPanel vp = new VerticalPanel();
		vp.setWidth(""+RootPanel.get().getOffsetWidth()+"px");
		vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vp.add(titleHtml);
		vp.add(hp);
		vp.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_CENTER);
		initWidget(vp);
	}
}
