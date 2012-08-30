/**
 * 
 */
package com.kp.marsh.ebt.client.webapp.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 */
public class StopAnimationEvent extends GwtEvent<StopAnimationEventHandler> {

	public static Type<StopAnimationEventHandler> TYPE = new Type<StopAnimationEventHandler>();
	
	
	public StopAnimationEvent() {}
	
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<StopAnimationEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
		
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
	@Override
	protected void dispatch(StopAnimationEventHandler handler) {
		handler.onStopAnimationEvent(this);

	}

}
