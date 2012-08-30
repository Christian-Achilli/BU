package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.GwtEvent;

public class EditEvent extends GwtEvent<EditHandler> {
    private boolean isEditable;

    public EditEvent(boolean isEditable) {
        super();
        this.isEditable = isEditable;
    }

    public static final Type<EditHandler> TYPE = new Type<EditHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<EditHandler> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(EditHandler handler) {
        handler.onEdit(this);
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }
    
}