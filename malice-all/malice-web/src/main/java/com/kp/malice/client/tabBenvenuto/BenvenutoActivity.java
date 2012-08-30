package com.kp.malice.client.tabBenvenuto;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.kp.malice.client.event.ChiusuraInviataEvent;
import com.kp.malice.client.event.TitoloUpdatedEvent;
import com.kp.malice.shared.MaliceRequestFactory.ServiziPortale;
import com.kp.malice.shared.MaliceUtil;
import com.kp.malice.shared.TipoLink;
import com.kp.malice.shared.proxies.LinkProxy;
import com.kp.malice.shared.proxies.WelcomeInfoProxy;

public class BenvenutoActivity extends AbstractActivity implements TitoloUpdatedEvent.Handler,
        ChiusuraInviataEvent.Handler {

    private final IBenvenutoViewDisplay display;

    private EventBus eventBus;
    private final Provider<ServiziPortale> serviziTitoloProvider;

    @Inject
    public BenvenutoActivity(IBenvenutoViewDisplay display, EventBus eventBus,
            Provider<ServiziPortale> serviziTitoloProvider) {
        this.display = display;
        this.eventBus = eventBus;
        this.serviziTitoloProvider = serviziTitoloProvider;
        eventBus.addHandler(TitoloUpdatedEvent.TYPE, this);
        eventBus.addHandler(ChiusuraInviataEvent.TYPE, this);
    }

    public void start(final AcceptsOneWidget panel, EventBus eventBus) {
        panel.setWidget(display.asWidget());
        caricaInfo();
    }

    private void caricaInfo() {
        serviziTitoloProvider.get().getWelcomeInfo().fire(new Receiver<WelcomeInfoProxy>() {

            @Override
            public void onSuccess(WelcomeInfoProxy response) {

                display.setMeseAperto(MaliceUtil.formatDateToMeseStringAnno(response.getMeseAperto()));
                display.setTitoliDaIncassare(response.getNumTtlDaIncassare().intValue() + " / "
                        + MaliceUtil.formatFromCentesimiToEuro(response.getTotPremiDaIncassareEuroCent().longValue()));
                display.setIncassiInSospeso(response.getNumIncassiSospesi().intValue() + " / "
                        + MaliceUtil.formatFromCentesimiToEuro(response.getTotPremiInSospesoEuroCent().longValue()));
                display.setTotPremiIncassatiAnnoInCorso(MaliceUtil.formatFromCentesimiToEuro(response
                        .getTotPremiIncassatiAnnoEuroCent().longValue()));
                display.setTotProvvigioniIncassateAnnoInCorso(MaliceUtil.formatFromCentesimiToEuro(response
                        .getTotProvvigioniIncassateAnnoEuroCent().longValue()));

                List<LinkProxy> links = response.getLinks();
                List<LinkProxy> linksDocumenti = new ArrayList<LinkProxy>();
                List<LinkProxy> linksTutorialDocumenti = new ArrayList<LinkProxy>();
                List<LinkProxy> linksTutorialVideo = new ArrayList<LinkProxy>();
                for (LinkProxy linkProxy : links) {
                    if (TipoLink.fromString(linkProxy.getStringTipo()) == TipoLink.DOCUMENT) {
                        linksDocumenti.add(linkProxy);
                    } else if (TipoLink.fromString(linkProxy.getStringTipo()) == TipoLink.TUTORIAL_DOCUMENT) {
                        linksTutorialDocumenti.add(linkProxy);
                    } else if (TipoLink.fromString(linkProxy.getStringTipo()) == TipoLink.TUTORIAL_VIDEO) {
                        linksTutorialVideo.add(linkProxy);
                    }
                }
                display.initLinksDocumenti(linksDocumenti);
                display.initLinksTutorialDocumenti(linksTutorialDocumenti);
                display.initLinksTutorialVideo(linksTutorialVideo);
                //				display.initLinks(MaliceDebugIds.DOC_LLOYDS, composeLabelLinkMatrix(links, MaliceDebugIds.DOC_LLOYDS));
                //				display.initLinks(MaliceDebugIds.DOC_TUTORIAL, composeLabelLinkMatrix(links, MaliceDebugIds.DOC_TUTORIAL));
                //				display.initLinks(MaliceDebugIds.DOC_VIDEO_PORTALE_GAR, composeLabelLinkMatrix(links, MaliceDebugIds.DOC_VIDEO_PORTALE_GAR));
            }

            //			private String[][] composeLabelLinkMatrix(List<String> formattedList, String id) {
            //				ArrayList<String> reducedListForId = trovaGliElementiPerId(formattedList, id);
            //				return convertiLaListaFormattataInMatriceLabelLink(reducedListForId);
            //			}
            //
            //			private String[][] convertiLaListaFormattataInMatriceLabelLink(ArrayList<String> reducedListForId) {
            //				String[][] labelLinkMatrix = new String[reducedListForId.size()][2];
            //				for (int i = 0; i < reducedListForId.size(); i++) {
            //					String[] labelLink = trovaLaCoppiaLabelLink(reducedListForId.get(i));
            //					labelLinkMatrix[i][WelcomeInfo.LABEL_POS] = labelLink[WelcomeInfoProxy.LABEL_POS];
            //					labelLinkMatrix[i][WelcomeInfo.LINK_POS] = labelLink[WelcomeInfoProxy.LINK_POS];
            //				}
            //				return labelLinkMatrix;
            //			}
            //
            //			private String[] trovaLaCoppiaLabelLink(String reducedListElement) {
            //				return reducedListElement.split(WelcomeInfoProxy.KEY_SEP)[1].split(WelcomeInfoProxy.LABEL_LINK_SEP);
            //			}
            //
            //			private ArrayList<String> trovaGliElementiPerId(List<String> formattedList, String id) {
            //				ArrayList<String> reducedListForId = new ArrayList<String>();
            //				for (String formattedListElement : formattedList) {
            //					if (formattedListElement.startsWith(id)) {
            //						reducedListForId.add(formattedListElement);
            //					}
            //				}
            //				return reducedListForId;
            //			}

            @Override
            public void onFailure(ServerFailure error) {
                GWT.log(error.getExceptionType());
            }
        });
    }

    public BenvenutoActivity withPlace(final Place place) {
        return this;
    }

    @Override
    public void onChiusuraInviata(ChiusuraInviataEvent event) {
        caricaInfo();
    }

    @Override
    public void onTitoloUpdated(TitoloUpdatedEvent event) {
        caricaInfo();
    }

}