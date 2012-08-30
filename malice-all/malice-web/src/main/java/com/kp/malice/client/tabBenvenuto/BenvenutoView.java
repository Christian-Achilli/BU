package com.kp.malice.client.tabBenvenuto;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.kp.malice.client.MaliceDebugIds;
import com.kp.malice.client.ui.resources.MaliceResources;
import com.kp.malice.client.ui.toolbar.ToolbarRiepilogoMensileTitoli;
import com.kp.malice.client.ui.widget.DownloadDocumentiWidget;
import com.kp.malice.shared.proxies.LinkProxy;

public class BenvenutoView extends Composite implements IBenvenutoViewDisplay {

    private static BenvenutoViewUiBinder uiBinder = GWT.create(BenvenutoViewUiBinder.class);

    interface BenvenutoViewUiBinder extends UiBinder<Widget, BenvenutoView> {
    }

    @UiField(provided = true)
    DownloadDocumentiWidget downloadDocumentiLloyds;
    @UiField(provided = true)
    DownloadDocumentiWidget downloadDocumentiTutorial;
    @UiField(provided = true)
    DownloadDocumentiWidget videoPortaleGar;
    @UiField
    ToolbarRiepilogoMensileTitoli toolbarRiepilogoMensileTitoli;
    @UiField
    Image imgMail;
    @UiField
    Image imgMailBlack;

    // private final HashMap<String, DownloadDocumentiWidget> mappaWidgetLink;

    public BenvenutoView() {
        // mappaWidgetLink = new HashMap<String, DownloadDocumentiWidget>();
        downloadDocumentiLloyds = new DownloadDocumentiWidget(MaliceResources.INSTANCE.main().downloadDoc(),
                MaliceResources.INSTANCE.main().downloadDocTd(), MaliceResources.INSTANCE.main().imageHrefDownloadDoc());
        downloadDocumentiTutorial = new DownloadDocumentiWidget(MaliceResources.INSTANCE.main().downloadDoc(),
                MaliceResources.INSTANCE.main().downloadDocTd(), MaliceResources.INSTANCE.main().imageHrefDownloadDoc());
        videoPortaleGar = new DownloadDocumentiWidget(MaliceResources.INSTANCE.main().downloadVideo(),
                MaliceResources.INSTANCE.main().downloadVideoTd(), MaliceResources.INSTANCE.main().imageHrefVideo());
        initWidget(uiBinder.createAndBindUi(this));
        imgMailBlack.setVisible(false);
        // downloadDocumentiLloyds.ensureDebugId(MaliceDebugIds.DOC_LLOYDS);
        // downloadDocumentiTutorial.ensureDebugId(MaliceDebugIds.DOC_TUTORIAL);
        // videoPortaleGar.ensureDebugId(MaliceDebugIds.DOC_VIDEO_PORTALE_GAR);
        // mappaWidgetLink.put(MaliceDebugIds.DOC_LLOYDS,
        // downloadDocumentiLloyds);
        // mappaWidgetLink.put(MaliceDebugIds.DOC_TUTORIAL,
        // downloadDocumentiTutorial);
        // mappaWidgetLink.put(MaliceDebugIds.DOC_VIDEO_PORTALE_GAR,
        // videoPortaleGar);
    }

    @Override
    public void initLinksDocumenti(List<LinkProxy> links) {
        downloadDocumentiLloyds.setLinks(links);
    }

    @Override
    public void initLinksTutorialDocumenti(List<LinkProxy> links) {
        downloadDocumentiTutorial.setLinks(links);
    }

    @Override
    public void initLinksTutorialVideo(List<LinkProxy> links) {
        videoPortaleGar.setLinks(links);
    }

    @UiHandler("imgMail")
    void onMouseOverImgMail(MouseOverEvent e) {
        GWT.log("BenvenutoView.onMouseOverImgMail");
        imgMail.setVisible(false);
        imgMailBlack.setVisible(true);
    }

    @UiHandler("imgMailBlack")
    void onMouseOutImgMailBlack(MouseOutEvent e) {
        GWT.log("BenvenutoView.onMouseOutImgMail");
        imgMail.setVisible(true);
        imgMailBlack.setVisible(false);
    }

    // private DownloadDocumentiWidget getDownloadWidgetWithId(String id) {
    // return mappaWidgetLink.get(id);
    // }

    // @Override
    // public void initLinks(String downloadWidgetId, String[][]
    // labelLinkMatrix) {
    // DownloadDocumentiWidget widg = getDownloadWidgetWithId(downloadWidgetId);
    // widg.setLinks(labelLinkMatrix);
    // }

    @Override
    public void setMeseAperto(String label) {
        toolbarRiepilogoMensileTitoli.setCorrenteMeseAperto(label);
    }

    @Override
    public void setTitoliDaIncassare(String formatted) {
        toolbarRiepilogoMensileTitoli.setFormattedTitoliIncassati(formatted);

    }

    @Override
    public void setIncassiInSospeso(String formatted) {
        toolbarRiepilogoMensileTitoli.setFormattedTitoliSospesi(formatted);

    }

    @Override
    public void setTotPremiIncassatiAnnoInCorso(String tot) {
        toolbarRiepilogoMensileTitoli.setFormattedTotPremiIncassatiAnno(tot);

    }

    @Override
    public void setTotProvvigioniIncassateAnnoInCorso(String tot) {
        toolbarRiepilogoMensileTitoli.setFormattedTotProvvigioniIncassateAnno(tot);
    }
}
