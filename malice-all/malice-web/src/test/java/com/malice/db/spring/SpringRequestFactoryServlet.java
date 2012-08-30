package com.malice.db.spring;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.context.ServletContextAware;

import com.google.web.bindery.requestfactory.server.DefaultExceptionHandler;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

@Controller
@Transactional
public class SpringRequestFactoryServlet extends RequestFactoryServlet
implements ServletContextAware, ServletConfigAware{

	private static final ThreadLocal<ServletContext> perThreadContext
	=
	new ThreadLocal<ServletContext>();
	private ServletContext servletContext;
	private ServletConfig servletConfig;

	public SpringRequestFactoryServlet() {
		super(new DefaultExceptionHandler(), new
				SpringServiceLayerDecorator());
	}
	/**
	 * Returns the thread-local {@link ServletContext}
	 *
	 * @return the {@link ServletContext} associated with this servlet
	 */
	public static ServletContext getThreadLocalServletContext() {
		return perThreadContext.get();
	}

	@RequestMapping("/gwtRequest")
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		perThreadContext.set(servletContext);
		super.doPost(request, response);
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Override
	public ServletConfig getServletConfig() {
		return servletConfig;
	}

	@Override
	public void setServletConfig(ServletConfig servletConfig) {
		this.servletConfig = servletConfig;
	}

	@Override
	public ServletContext getServletContext() {
		return servletContext;
	}

}
