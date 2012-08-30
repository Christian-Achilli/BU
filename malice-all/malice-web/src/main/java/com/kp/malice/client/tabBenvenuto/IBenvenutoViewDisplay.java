/**
 * 
 */
package com.kp.malice.client.tabBenvenuto;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.kp.malice.shared.proxies.LinkProxy;

public interface IBenvenutoViewDisplay extends IsWidget {

    void setMeseAperto(String label);

    void setTitoliDaIncassare(String numTitoli);

    void setIncassiInSospeso(String numIncassi);

    void setTotPremiIncassatiAnnoInCorso(String tot);

    void setTotProvvigioniIncassateAnnoInCorso(String tot);

    void initLinksDocumenti(List<LinkProxy> links);

    void initLinksTutorialDocumenti(List<LinkProxy> links);

    void initLinksTutorialVideo(List<LinkProxy> links);
}
