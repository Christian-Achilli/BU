package com.kp.malice.client.ui.commonWidgets;

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
import com.kp.malice.shared.StatoChiusura;

public class StatoChiusuraImageResourceCell extends AbstractCell<StatoChiusura> {

    private static ImageResourceRenderer renderer;

    public StatoChiusuraImageResourceCell() {
        super("click", "keydown");
        if (renderer == null) {
            renderer = new ImageResourceRenderer();
        }

    }

    @Override
    public void onBrowserEvent(Context context, Element parent, StatoChiusura value, NativeEvent event,
            ValueUpdater<StatoChiusura> valueUpdater) {
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
    public void render(Context context, StatoChiusura value, SafeHtmlBuilder sb) {
        // Get the view data.
        ImageResource imgRes = value.getImageResource();
        SafeHtml html = SafeHtmlUtils.fromTrustedString(AbstractImagePrototype.create(imgRes).getHTML());
        sb.append(html);

    }

}
