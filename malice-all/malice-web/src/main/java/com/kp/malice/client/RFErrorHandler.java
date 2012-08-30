package com.kp.malice.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.kp.malice.shared.MaliceUtil;

public class RFErrorHandler {
	public static void handle(ServerFailure error) {
		GWT.log("RFErrorHandler handle, redirect to login page");
//		Window.alert("Sessione scaduta.");
		MaliceUtil.showError("Sessione scaduta");
		Window.Location.assign("login.html");
//		String message = error.getMessage();
//		String messageHtml = message.substring(17, (message.length()-1));
//		Window.alert(messageHtml);
//		HTML html = new HTML();
//		Window.alert("html: "+messageHtml);
//		NodeList<Node> nodeList = html.getElement().getChildNodes();
//		for(int i=0;i<nodeList.getLength();i++){
//			Node node = nodeList.getItem(i);
//			String nodeName = node.getNodeName();
//			Window.alert("nodeName: "+nodeName);
//			if(nodeName.equals("body")){
//				for (int j = 0; j < node.getChildCount(); j++) {
//					Node bodyChild = node.getChild(j);
//					String bodyChildName = bodyChild.getNodeName();
//					Window.alert("bodyChildName: "+bodyChildName);
//					if(bodyChildName.equals("pre")){
//						String value = bodyChild.getNodeValue();
//						Window.alert("value: "+value);
//						if(value.equals(MaliceConstant.getErrorSessioneScaduta())){
//							Window.alert("codeError: "+value);
//							GWT.log("codeError: "+value);
//							Window.alert("Sessione scaduta.");
//							Window.Location.assign("login.html");
//						}
//					}
//				}
//			}
//		}
	}
}
