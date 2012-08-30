package com.malice.db.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;
import com.google.web.bindery.requestfactory.shared.Locator;

public class SpringServiceLayerDecorator extends ServiceLayerDecorator
{

	@Override
	public <T extends Locator<?, ?>> T createLocator(Class<T> clazz) {
		ApplicationContext ctx =
				WebApplicationContextUtils.getWebApplicationContext(
						SpringRequestFactoryServlet.getThreadLocalServletContext());
		Object bean = ctx.getBean(clazz);
		return (T) (bean != null ? bean : super.createLocator(clazz));
	}
}
