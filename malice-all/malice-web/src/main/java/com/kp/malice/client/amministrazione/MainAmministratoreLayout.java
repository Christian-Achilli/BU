package com.kp.malice.client.amministrazione;

import org.adamtacy.client.ui.effects.examples.Show;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class MainAmministratoreLayout extends Composite {

    private static MainAmministratoreLayoutUiBinder uiBinder = GWT.create(MainAmministratoreLayoutUiBinder.class);
    @UiField
    SimplePanel header;
    @UiField
    TabLayoutPanel tabs;
    private ScrollPanel tab1 = new ScrollPanel();

    private AcceptsOneWidget toolbarDisplay = new AcceptsOneWidget() {
        @Override
        public void setWidget(IsWidget activityWidget) {
            Widget widget = Widget.asWidgetOrNull(activityWidget);
            header.setVisible(widget != null);
            header.setWidget(widget);
        }
    };

    private AcceptsOneWidget tab1Display = new AcceptsOneWidget() {
        @Override
        public void setWidget(IsWidget activityWidget) {
            Widget widget = Widget.asWidgetOrNull(activityWidget);
            tab1.setVisible(widget != null);
            tab1.setWidget(widget);
        }
    };

    interface MainAmministratoreLayoutUiBinder extends UiBinder<Widget, MainAmministratoreLayout> {
    }

    public MainAmministratoreLayout() {
        initWidget(uiBinder.createAndBindUi(this));
        tabs.add(tab1);
        tabs.selectTab(tab1);
        setVisible(false);
        tabs.setAnimationDuration(0);
    }

    public AcceptsOneWidget getTab1Display() {
        return tab1Display;
    }

    public AcceptsOneWidget getToolbarDisplay() {
        return toolbarDisplay;
    }

    public void visualizza() {
        Show show2 = new Show(getElement());
        show2.play();
        setVisible(true);
    }

}
