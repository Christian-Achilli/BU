package com.kp.marsh.ebt.client.webapp;

import org.adamtacy.client.ui.effects.examples.Show;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.kp.marsh.ebt.client.webapp.gin.ManagerWidgetsGinjector;
import com.kp.marsh.ebt.client.webapp.mvp.ApplicationPlaceHistoryMapper;
import com.kp.marsh.ebt.client.webapp.mvp.AuthenticationActivityMapper;
import com.kp.marsh.ebt.client.webapp.mvp.GaugesActivityMapper;
import com.kp.marsh.ebt.client.webapp.mvp.HeaderActivityMapper;
import com.kp.marsh.ebt.client.webapp.mvp.HistogramActivityMapper;
import com.kp.marsh.ebt.client.webapp.mvp.PillActivityMapper;
import com.kp.marsh.ebt.client.webapp.place.AuthenticationPlace;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ManagerExpandedBusinessTool implements EntryPoint {

    private final ManagerWidgetsGinjector injector = GWT.create(ManagerWidgetsGinjector.class);

    private VerticalPanel layoutPanel = new VerticalPanel();

    // login
    private SimplePanel loginPanel = new SimplePanel();

    // vista manager
    private SimplePanel headerPanel = new SimplePanel();
    private SimplePanel gaugePanel = new SimplePanel();
    private SimplePanel histogramPanel = new SimplePanel();
    private SimplePanel pillPanel = new SimplePanel();

    AcceptsOneWidget headerDisplay = new AcceptsOneWidget() {
        @Override
        public void setWidget(IsWidget activityWidget) {
            Widget widget = Widget.asWidgetOrNull(activityWidget);
            //Set Visible
            headerPanel.setVisible(widget != null);
            headerPanel.setWidget(widget);
            Show f = new Show(headerPanel.getElement());
            f.setDuration(1.5);
            f.play();
        }
    };
    //	
    AcceptsOneWidget loginDisplay = new AcceptsOneWidget() {
        @Override
        public void setWidget(IsWidget activityWidget) {
            Widget widget = Widget.asWidgetOrNull(activityWidget);
            loginPanel.setVisible(widget != null);
            loginPanel.setWidget(widget);
            Show f = new Show(loginPanel.getElement());
            f.setDuration(0.5);
            f.play();

        }
    };

    AcceptsOneWidget gaugeDisplay = new AcceptsOneWidget() {
        @Override
        public void setWidget(IsWidget activityWidget) {
            Widget widget = Widget.asWidgetOrNull(activityWidget);
            gaugePanel.setVisible(widget != null);
            gaugePanel.setWidget(widget);
            Show f = new Show(gaugePanel.getElement());
            f.setDuration(1.0);
            f.play();
        }
    };

    AcceptsOneWidget histogramDisplay = new AcceptsOneWidget() {
        @Override
        public void setWidget(IsWidget activityWidget) {
            Widget widget = Widget.asWidgetOrNull(activityWidget);
            histogramPanel.setVisible(widget != null);
            histogramPanel.setWidget(widget);

        }
    };

    AcceptsOneWidget pillDisplay = new AcceptsOneWidget() {
        @Override
        public void setWidget(IsWidget activityWidget) {
            Widget widget = Widget.asWidgetOrNull(activityWidget);
            pillPanel.setVisible(widget != null);
            pillPanel.setWidget(widget);
            Show f = new Show(pillPanel.getElement());
            f.setDuration(1.0);
            f.play();
        }
    };

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {

        EventBus eventBus = injector.getEventBus();

        // setting the header region
        HeaderActivityMapper headerActivityMapper = injector.getHeaderActivityMapper();
        ActivityManager headerActivityManager = new ActivityManager(headerActivityMapper, eventBus);
        headerActivityManager.setDisplay(headerDisplay);

        // setting the login region
        AuthenticationActivityMapper authActivityMapper = injector.getAuthActivityMapper();
        ActivityManager authActivityManager = new ActivityManager(authActivityMapper, eventBus);
        authActivityManager.setDisplay(loginDisplay);

        //setting the gauges region
        GaugesActivityMapper gaugeActivityMapper = injector.getGaugesActivityMapper();
        ActivityManager gaugeActivityManager = new ActivityManager(gaugeActivityMapper, eventBus);
        gaugeActivityManager.setDisplay(gaugeDisplay);

        //setting the histogram region
        HistogramActivityMapper histoActivityMapper = injector.getHistogramActivityMapper();
        ActivityManager histoActivityManager = new ActivityManager(histoActivityMapper, eventBus);
        histoActivityManager.setDisplay(histogramDisplay);

        //setting the pill region
        PillActivityMapper pillActivityMapper = injector.getPillActivityMapper();
        ActivityManager pillActivityManager = new ActivityManager(pillActivityMapper, eventBus);
        pillActivityManager.setDisplay(pillDisplay);

        //		ManagerAppPlaceFactory factory = injector.getAppPlaceFactory();
        //		LoginManagerPlace defaultPlace = new LoginManagerPlace("Silvana Frasson","33","1");

        AuthenticationPlace defaultPlace = new AuthenticationPlace();

        PlaceController placeController = injector.getPlaceController();

        ApplicationPlaceHistoryMapper historyMapper = GWT.create(ApplicationPlaceHistoryMapper.class);
        //		historyMapper.setFactory(factory);

        PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
        historyHandler.register(placeController, eventBus, defaultPlace);

        layoutPanel.add(headerPanel);
        layoutPanel.add(loginPanel);
        layoutPanel.add(gaugePanel);
        layoutPanel.add(histogramPanel);
        layoutPanel.add(pillPanel);

        RootPanel.get().clear();
        RootPanel.get().add(layoutPanel);

        historyHandler.handleCurrentHistory();

    }

}
