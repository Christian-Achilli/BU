package com.kp.malice.client.tabAgenzie;

import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.kp.malice.client.AuthServiceAsync;
import com.kp.malice.client.RFErrorHandler;
import com.kp.malice.client.place.UserPlace;
import com.kp.malice.shared.MaliceRequestFactory.ServiziPortale;
import com.kp.malice.shared.proxies.AgencyProxy;

public class AgenzieActivity extends AbstractActivity implements IAgenzieViewDisplay.Listener {

    private final IAgenzieViewDisplay display;

    private final Provider<ServiziPortale> serviziTitoloProvider;

    private final EventBus eventBus;

    private ListDataProvider<AgencyProxy> agenzieDataProvider = new ListDataProvider<AgencyProxy>();

    @Inject
    public AgenzieActivity(IAgenzieViewDisplay display, EventBus eventBus,
            Provider<ServiziPortale> serviziTitoloProvider) {
        this.display = display;
        this.eventBus = eventBus;
        this.serviziTitoloProvider = serviziTitoloProvider;
        display.setListener(this);
        agenzieDataProvider.addDataDisplay(display.getTabellaTitoli());
    }

    public void start(final AcceptsOneWidget panel, EventBus eventBus) {
        caricaListaAgenzie();
        panel.setWidget(display.asWidget());
    }

    private void caricaListaAgenzie() {
        GWT.log("SelectAgenziaActivity.caricaListaAgenzie");
        ServiziPortale request = serviziTitoloProvider.get();
        Request<List<AgencyProxy>> req = request.findAllAgenzie();
        try {
            req.fire(new Receiver<List<AgencyProxy>>() {
                @Override
                public void onSuccess(List<AgencyProxy> listaAgenzie) {
                    GWT.log("success RequestFactory retrieved " + listaAgenzie.size() + " AgencyProxy");
                    display.setNumAgenzieTrovate(listaAgenzie.size());
                    agenzieDataProvider.setList(listaAgenzie);
                }

                public void onFailure(ServerFailure error) {
                    GWT.log("caricaListaAgenzie failure!");
                    GWT.log(error.getExceptionType());
                    RFErrorHandler.handle(error);
                }
            });
        } catch (Exception e) {
            GWT.log("SelectAgenziaActivity.caricaListaAgenzie() GwtSecurityException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public AgenzieActivity withPlace(final UserPlace place) {
        return this;
    }

    private native void reload() /*-{
                                 $wnd.location.reload();
                                 }-*/;

    @Override
    public void onSelezioneAgenzia(AgencyProxy agencyProxy) {
        GWT.log("SelectAgenziaActivity.onSelezioneAgenzia");
        //carica agenzia
    }

}
