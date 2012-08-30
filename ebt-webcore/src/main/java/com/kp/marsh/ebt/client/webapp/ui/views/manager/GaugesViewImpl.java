package com.kp.marsh.ebt.client.webapp.ui.views.manager;

import java.util.ArrayList;
import java.util.HashMap;

import org.adamtacy.client.ui.effects.events.EffectCompletedEvent;
import org.adamtacy.client.ui.effects.events.EffectCompletedHandler;
import org.adamtacy.client.ui.effects.examples.Fade;
import org.adamtacy.client.ui.effects.examples.Show;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.kp.marsh.ebt.client.webapp.ui.IstogrammaFrecceSatellite;
import com.kp.marsh.ebt.client.webapp.ui.LoaderWidget;
import com.kp.marsh.ebt.client.webapp.ui.Manometro3Lancette;
import com.kp.marsh.ebt.client.webapp.ui.activityintf.IGaugesViewDisplay;
import com.kp.marsh.ebt.client.webapp.ui.resources.Resources;
import com.kp.marsh.ebt.shared.dto.SintesiDto;

public class GaugesViewImpl extends Composite implements IGaugesViewDisplay {

    private static GaugesViewImplUiBinder uiBinder = GWT.create(GaugesViewImplUiBinder.class);

    interface GaugesViewImplUiBinder extends UiBinder<Widget, GaugesViewImpl> {
    }

    static {
        Resources.INSTANCE.gauges().ensureInjected();
    }

    @UiField(provided = true)
    Grid rootElement = new Grid(4, 3);

    @UiField
    SpanElement ufficio;

    @UiField
    SpanElement ce;

    @UiField
    SpanElement gc;

    @UiField
    SpanElement descrUff;

    @UiField
    SpanElement descrCe;

    @UiField
    SpanElement descrGc;

    @UiField
    DivElement appWrapper;

    @UiField
    LoaderWidget loaderWidget;

    private Show showEffect;

    private Fade fadeEffect;

    String[] lobNames = null;

    private HashMap<String, Manometro3Lancette> gaugeByLobNameMap = new HashMap<String, Manometro3Lancette>();

    private HashMap<String, IstogrammaFrecceSatellite> istogrammaByLobNameMap = new HashMap<String, IstogrammaFrecceSatellite>();

    private HashMap<String, Label> labelByLobNameMap = new HashMap<String, Label>(); // mappa
                                                                                     // delle
                                                                                     // label
                                                                                     // del
                                                                                     // titolo
                                                                                     // del
                                                                                     // riquadro,quella
                                                                                     // che
                                                                                     // contiene
                                                                                     // il
                                                                                     // nome
                                                                                     // della
                                                                                     // lob

    private HashMap<String, Label[]> lavoratiDaLavorareLabelMap = new HashMap<String, Label[]>();// in
                                                                                                 // pos
                                                                                                 // 0
                                                                                                 // la
                                                                                                 // label
                                                                                                 // dei
                                                                                                 // potenziali,
                                                                                                 // in
                                                                                                 // pos
                                                                                                 // 1
                                                                                                 // la
                                                                                                 // label
                                                                                                 // dei
                                                                                                 // da
                                                                                                 // lavorare

    public GaugesViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));

        showEffect = new Show(appWrapper);
        showEffect.setDuration(0.5);
        fadeEffect = new Fade(appWrapper);
        fadeEffect.setDuration(0.5);

        showEffect.addEffectCompletedHandler(new EffectCompletedHandler() {

            @Override
            public void onEffectCompleted(EffectCompletedEvent event) {
                loaderWidget.setVisible(false);

            }
        });

        initView();

        // initUIElements();
        setVisible(ufficio, false);
        setVisible(ce, false);
        setVisible(gc, false);
    }

    private void initUIElements() {

        int cont = 0;
        for (int i = 0; i < rootElement.getRowCount(); i++) {
            for (int j = 0; j < rootElement.getColumnCount(); j++) {
                if (cont < lobNames.length) {
                    // instanzio i widgets e li aggiungo al pannello verticale
                    // Image loader = new
                    // Image(Resources.INSTANCE.circleLoader());
                    // loader.setStyleName(Resources.INSTANCE.gauges().pulsLoader());
                    Label lobName = new Label();
                    lobName.setHorizontalAlignment(lobName.ALIGN_LEFT);
                    lobName.setStyleName(Resources.INSTANCE.gauges().gaugeLabelCell());
                    lobName.setVisible(false);
                    IstogrammaFrecceSatellite isto = new IstogrammaFrecceSatellite();
                    Manometro3Lancette manom = new Manometro3Lancette();
                    VerticalPanel vp = new VerticalPanel();
                    vp.setHeight("181px");

                    FlowPanel header = new FlowPanel();

                    header.add(lobName);
                    lobName.addStyleName(Resources.INSTANCE.gauges().nomeLob());

                    FlowPanel potStatsPanel = new FlowPanel();
                    // potStatsPanel.add(new Label("Potenziali"));
                    // potStatsPanel.setStylePrimaryName(Resources.INSTANCE.gauges().potenzialeLabel());
                    //

                    HorizontalPanel valorizzatiPanel = new HorizontalPanel();
                    HorizontalPanel daValorizzarePanel = new HorizontalPanel();

                    potStatsPanel.add(valorizzatiPanel);
                    potStatsPanel.add(daValorizzarePanel);

                    header.add(potStatsPanel);

                    Label potLab = new Label("Valorizzati: ");
                    potLab.setStylePrimaryName(Resources.INSTANCE.gauges().potenzialiLabel());

                    Label daLavLab = new Label("Da valorizzare:  ");
                    daLavLab.setStylePrimaryName(Resources.INSTANCE.gauges().daLavorareLabel());

                    Label potenzialiValLabel = new Label();
                    potenzialiValLabel.setVisible(false);
                    potenzialiValLabel.setStylePrimaryName(Resources.INSTANCE.gauges().numPotenziali());

                    valorizzatiPanel.add(potLab);
                    valorizzatiPanel.add(potenzialiValLabel);

                    Label daLavorareValLabel = new Label();
                    daLavorareValLabel.setVisible(false);
                    daLavorareValLabel.setStylePrimaryName(Resources.INSTANCE.gauges().numDaLavorare());

                    daValorizzarePanel.add(daLavLab);
                    daValorizzarePanel.add(daLavorareValLabel);

                    vp.setWidth("286px");
                    HorizontalPanel hp = new HorizontalPanel();
                    hp.add(isto);
                    hp.add(manom);
                    // vp.setCellHorizontalAlignment(loader,
                    // HasHorizontalAlignment.ALIGN_CENTER);
                    // vp.setCellVerticalAlignment(loader,
                    // HasVerticalAlignment.ALIGN_MIDDLE);
                    // vp.add(loader);
                    vp.add(header);
                    vp.add(hp);

                    // imposto il pannello verticale nella cella della
                    // rootElement
                    rootElement.setWidget(i, j, vp);
                    rootElement.getCellFormatter().setStyleName(i, j, Resources.INSTANCE.gauges().gaugeGridCell());

                    // aggiorno le mappe
                    gaugeByLobNameMap.put(lobNames[cont], manom);
                    istogrammaByLobNameMap.put(lobNames[cont], isto);

                    labelByLobNameMap.put(lobNames[cont], lobName);
                    Label[] labArr = new Label[2];
                    labArr[0] = potenzialiValLabel;
                    labArr[1] = daLavorareValLabel;
                    lavoratiDaLavorareLabelMap.put(lobNames[cont], labArr);
                    // loaderLobNameMap.put(lobNames[cont], loader);

                    // consumo una lob
                    cont++;
                } else {
                    break;
                }
            }
        }
    }

    @Override
    public void updateData(ArrayList<SintesiDto> dtoList) {
        for (SintesiDto sintesiDto : dtoList) {
            Manometro3Lancette g = gaugeByLobNameMap.get(sintesiDto.getLobName());
            g.draw(sintesiDto.getIndicePenetrazione(), sintesiDto.getIndicePenetrazioneTotale(),
                    sintesiDto.getIndicePenetrazioneIniziale());

            IstogrammaFrecceSatellite isto = istogrammaByLobNameMap.get(sintesiDto.getLobName());
            isto.draw(sintesiDto.getIndiceSaturazionePotenziale(), sintesiDto.getIndiceSaturazionePotenzialeTotale());

            Label lobLabel = labelByLobNameMap.get(sintesiDto.getLobName());
            lobLabel.setText(sintesiDto.getLobName());

            Label[] lavoratiArr = lavoratiDaLavorareLabelMap.get(sintesiDto.getLobName());
            lavoratiArr[0].setText("" + sintesiDto.getProdottiLavorati());
            lavoratiArr[1].setText("" + sintesiDto.getProdottiTotaliDaLavorare());
            // Image loader = loaderLobNameMap.get(sintesiDto.getLobName());

            // Fade fade = new Fade();
            // fade.setEffectElement(loader.getElement());
            // loader.setVisible(false);
            lavoratiArr[0].setVisible(true);
            lavoratiArr[1].setVisible(true);
            lobLabel.setVisible(true);
            isto.setVisible(true);
            g.setVisible(true);
            // fade.play();
        }
    }

    @Override
    public void showLoaders(boolean show) {
        if (show) {
            showLoaderWithHight();
            if (isVisible(appWrapper)) {
                fadeEffect.play();
            }
        } else {
            if (!isVisible(appWrapper)) {
                setVisible(appWrapper, true);
            }
            showEffect.play();
        }
    }

    @Override
    public void initView() {
        showLoaderWithHight();
        setVisible(appWrapper, false); // per evitareil flickering quando
                                       // consulto gli istogrammi dopo login
                                       // logout
        fadeEffect.play();

    }

    /**
     * 
     */
    private void showLoaderWithHight() {
        loaderWidget.getHtmlPanel().setHeight("" + this.getOffsetHeight());
        loaderWidget.setVisible(true);
    }

    public void setStatusBar(String ufficioSelectedDescription, String ceSelectedDescription,
            String gcSelectedDescription) {
        // UFFICIO
        if (null != ufficioSelectedDescription && !"".equals(ufficioSelectedDescription)) {
            setVisible(ufficio, true);
            descrUff.setInnerHTML(ufficioSelectedDescription);

        } else
            setVisible(ufficio, false);
        // CE
        if (null != ceSelectedDescription && !"".equals(ceSelectedDescription)) {
            setVisible(ce, true);
            descrCe.setInnerHTML(ceSelectedDescription);
        } else
            setVisible(ce, false);
        // GC
        if (null != gcSelectedDescription && !"".equals(gcSelectedDescription)) {
            setVisible(gc, true);
            descrGc.setInnerHTML(gcSelectedDescription);
        } else
            setVisible(gc, false);
    }

    @Override
    public void setLobNames(String[] names) {
        this.lobNames = names;
        initUIElements();
    }

}
