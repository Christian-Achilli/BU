package com.malice.db.spring;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.web.bindery.requestfactory.vm.RequestFactorySource;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration({ "classpath:spring/somecontext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class SpringRequestFactoryServletTest<T extends RequestFactory>{

	@Autowired
	SpringRequestFactoryServlet springRequestFactoryServlet;

	protected MockServletConfig servletConfig = new MockServletConfig();
	protected MockServletContext servletContext = new MockServletContext();
	protected T requestFactory;
	private SpringRequestTransport transport;
	private final Class<? extends T> requestFactoryClass;

	public SpringRequestFactoryServletTest(Class<? extends T>
	requestFactoryClass) {
		this.requestFactoryClass = requestFactoryClass;
	}

	@Before
	public void init() {
		requestFactory = create(requestFactoryClass);
		springRequestFactoryServlet.setServletConfig(servletConfig);
		springRequestFactoryServlet.setServletContext(servletContext);

		String[] contexts = new String[] { "classpath:spring/somecontext.xml" };
				XmlWebApplicationContext webApplicationContext = new
				XmlWebApplicationContext();
				webApplicationContext.setConfigLocations(contexts);
				webApplicationContext.setServletContext(servletContext);
				webApplicationContext.refresh();

				servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
						webApplicationContext);
		}

		private T create(Class<? extends T> requestFactoryClass) {
			T t = RequestFactorySource.create(requestFactoryClass);
			transport = new SpringRequestTransport(springRequestFactoryServlet);
			t.initialize(new SimpleEventBus(), transport);
			return t;
		}


		/**
		 * Allows firing a Request synchronously. In case of success, the
result is
		 * returned. Otherwise, a {@link RuntimeException} containing the
server
		 * error message is thrown.
		 */
		public <T> T fire(Request<T> request) {
			return fire(request, null);
		}

		public <T> T fire(Request<T> request, String loginname) {
			ReceiverCaptor<T> receiver = new ReceiverCaptor<T>();
			MockHttpServletRequest httpRequest = new MockHttpServletRequest();
			MockHttpServletResponse httpResponse = new MockHttpServletResponse();
			transport.setRequest(httpRequest);
			transport.setResponse(httpResponse);
			if(loginname != null) {
				httpRequest.setUserPrincipal(new PrincipalMock(loginname));
			}
			request.fire(receiver);

			handleFailure(receiver.getServerFailure());
			return receiver.getResponse();
		}

		private void handleFailure(ServerFailure failure) {
			if (failure != null) {
				throw new RuntimeException(buildMessage(failure));
			}
		}

		private String buildMessage(ServerFailure failure) {
			StringBuilder result = new StringBuilder();
			result.append("Server Error. Type: ");
			result.append(failure.getExceptionType());
			result.append(" Message: ");
			result.append(failure.getMessage());
			result.append(" StackTrace: ");
			result.append(failure.getStackTraceString());
			return result.toString();
		}

	}
