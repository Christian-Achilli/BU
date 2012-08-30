package com.kp.marsh.ebt.server.webapp;


import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public abstract class GuiceRemoteServiceServlet extends RemoteServiceServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private Injector injector;


	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);

		Injector injector = (Injector)config.getServletContext().
		getAttribute( Injector.class.getName() );
		injector.injectMembers( this );
	}


}
