/**
 * 
 */
package com.kp.marsh.ebt.client.webapp.ui;

import org.adamtacy.client.ui.effects.examples.BlindDown;

import com.google.gwt.user.client.Command;

/**
 * @author christianachilli
 * Fix a bug in BlindDown when defines a clip size: Simply setting the clip size doesn't work!!
 */
public class CustomBlindDown extends BlindDown {

    int shadowSize = 15;

    public CustomBlindDown() {
        super();
    }

    @Override
    public void setUpEffect() {
        super.setUpEffect(new Command() {
            @Override
            public void execute() {
                int height = effectElements.get(0).getOffsetHeight() + shadowSize;
                int width = effectElements.get(0).getOffsetWidth();
                setStartClip("rect(0px," + width + "px,0px,0px)");
                setEndClip("rect(0px," + width + "px," + height + "px,0px)");
            }
        });
    }

}
