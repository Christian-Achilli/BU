package com.kp.malice.client.ui.widget;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageResourceRenderer;
import com.kp.malice.shared.StatusTitolo;

public class StatoTitoloImageResourceCell extends AbstractCell<StatusTitolo> {

    private static ImageResourceRenderer renderer;

    public StatoTitoloImageResourceCell() {
        super("click", "keydown");
        if (renderer == null) {
            renderer = new ImageResourceRenderer();
        }

    }

    @Override
    public void onBrowserEvent(Context context, Element parent, StatusTitolo value, NativeEvent event,
            ValueUpdater<StatusTitolo> valueUpdater) {
        // Check that the value is not null.
        if (value == null) {
            return;
        }

        // On click, perform the same action that we perform on enter.
        if ("click".equals(event.getType())) {
            this.onEnterKeyDown(context, parent, value, event, valueUpdater);
        }
    }

    @Override
    public void render(Context context, StatusTitolo value, SafeHtmlBuilder sb) {
        // Get the view data.
        ImageResource imgRes = value.getImageResource();
        SafeHtml html = SafeHtmlUtils.fromTrustedString(AbstractImagePrototype.create(imgRes).getHTML());
        sb.append(html);
    }

}
