package com.kp.malice.client.header;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.kp.malice.client.AuthServiceAsync;
import com.kp.malice.client.place.UserPlace;
import com.kp.malice.shared.MaliceRequestFactory;
import com.kp.malice.shared.proxies.NewTitoloProxy;

public class HeaderActivity extends AbstractActivity implements IHeaderViewDisplay.Listener {

    private final MaliceRequestFactory requestFactory;

    private final IHeaderViewDisplay display;

    private final EventBus eventBus;

    private final AuthServiceAsync authService;

    List<NewTitoloProxy> listFromServerSynchronizedWithListaPolizzeActivity = new ArrayList<NewTitoloProxy>();

    @Inject
    public HeaderActivity(IHeaderViewDisplay display, EventBus eventBus, AuthServiceAsync authService,
            MaliceRequestFactory reqFac) {
        this.display = display;
        this.eventBus = eventBus;
        this.authService = authService;
        this.requestFactory = reqFac;
        display.setListener(this);
    }

    public void start(final AcceptsOneWidget panel, EventBus eventBus) {
        panel.setWidget(display.asWidget());
    }

    public HeaderActivity withPlace(final UserPlace place) {
        display.setLabelUtenteSistema(place.getUserName().toUpperCase());
        return this;
    }

    private native void reload() /*-{
		$wnd.location.reload();
    }-*/;

    @Override
    public void logout() {
        reload();
    }

}