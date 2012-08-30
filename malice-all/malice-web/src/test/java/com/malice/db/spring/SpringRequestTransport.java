package com.malice.db.spring;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.google.web.bindery.requestfactory.shared.RequestTransport;
import com.google.web.bindery.requestfactory.shared.RequestTransport.TransportReceiver;

public class SpringRequestTransport implements RequestTransport {
	private final SpringRequestFactoryServlet requestFactoryServlet;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;

	public SpringRequestTransport(SpringRequestFactoryServlet
			requestFactoryServlet) {
		this.requestFactoryServlet = requestFactoryServlet;
	}

	public void setRequest(MockHttpServletRequest request) {
		this.request = request;
	}

	public void setResponse(MockHttpServletResponse response) {
		this.response = response;
	}

	@Override
	public void send(String payload, TransportReceiver receiver) {
		try {
			request.setContentType("application/json");
			request.setCharacterEncoding("UTF-8");
			request.setContent(payload.getBytes());
			requestFactoryServlet.handleRequest(request, response);
			String contentAsString = response.getContentAsString();
			receiver.onTransportSuccess(contentAsString);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}