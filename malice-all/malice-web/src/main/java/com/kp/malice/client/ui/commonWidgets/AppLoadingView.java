package com.kp.malice.client.ui.commonWidgets;

import org.adamtacy.client.ui.effects.events.EffectCompletedEvent;
import org.adamtacy.client.ui.effects.events.EffectCompletedHandler;
import org.adamtacy.client.ui.effects.examples.Fade;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.kp.malice.client.ui.resources.MaliceResources;

public final class AppLoadingView extends PopupPanel implements IAppLoadingViewDisplay
{
    private final FlowPanel container = new FlowPanel();

    public AppLoadingView()
    {
        final Image ajaxImage = new Image(MaliceResources.INSTANCE.lloyds_logo());
        final Grid grid = new Grid(1, 2);
        grid.setWidget(0, 0, ajaxImage);
        grid.setCellSpacing(30);
        grid.setText(0, 1, "Loading...");
        setModal(true);
        this.container.add(grid);
        add(this.container);
    }

    @Override
    public Widget asWidget()
    {
        return this;
    }

    @Override
    public void hideLoading()
    {
    	Fade fade = new Fade(getElement());
    	fade.play();
    	fade.addEffectCompletedHandler(new EffectCompletedHandler() {
			
			@Override
			public void onEffectCompleted(EffectCompletedEvent event) {
				hide();
				
			}
		});
    }

    @Override
    public void showLoading()
    {
        center();
        show();
    }

}
