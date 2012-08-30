/**
 * 
 */
package com.kp.marsh.ebt.client.webapp.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author christianachilli
 * Opens When click on a product label
 */
public class PostItPopUp extends PopupPanel {

    private static ProductPopUpUiBinder uiBinder = GWT.create(ProductPopUpUiBinder.class);

    interface ProductPopUpUiBinder extends UiBinder<Widget, PostItPopUp> {
    }

    public PostItPopUp() {
        super(true);
        add(uiBinder.createAndBindUi(this));
        addCloseHandler(new CloseHandler<PopupPanel>() {

            @Override
            public void onClose(CloseEvent<PopupPanel> event) {
                setPostItNote(getPostItNote().trim());

            }
        });
    }

    @UiField
    TextArea postIt;

    @UiField
    Anchor xClose;

    @UiHandler("xClose")
    void closePostIt(ClickEvent e) {
        setPostItNote(getPostItNote().trim());
        hide();
    }

    public String getPostItNote() {
        return postIt.getText();
    }

    public void setPostItNote(String note) {
        this.postIt.setText(note);
    }

    public TextArea getPostIt() {
        return postIt;
    }

    public void setPostIt(TextArea postIt) {
        this.postIt = postIt;
    }

}
