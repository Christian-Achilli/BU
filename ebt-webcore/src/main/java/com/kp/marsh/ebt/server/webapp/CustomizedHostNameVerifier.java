package com.kp.marsh.ebt.server.webapp;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * @author christianachilli
 * Use to accept not signed certificates when calling marsh active directory service
 */
public final class CustomizedHostNameVerifier implements HostnameVerifier {
	public boolean verify(String hostname, SSLSession session) {
		return true;
	}
}