package com.kp.malice.client.ui.widget;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageResourceRenderer;
import com.kp.malice.client.tabTitoli.StatusTitoloAndIncasso;

public class StatoTitoloAndIncassoImageResourceCell extends AbstractCell<StatusTitoloAndIncasso> {

    private static ImageResourceRenderer renderer;

    public StatoTitoloAndIncassoImageResourceCell() {
        super("click", "keydown");
        if (renderer == null) {
            renderer = new ImageResourceRenderer();
        }

    }

    @Override
    public void render(Context context, StatusTitoloAndIncasso value, SafeHtmlBuilder sb) {
        // Get the view data.
        ImageResource imgRes = value.getImageResource();
        SafeHtml html = SafeHtmlUtils.fromTrustedString(AbstractImagePrototype.create(imgRes).getHTML());
        sb.append(html);
    }
}
