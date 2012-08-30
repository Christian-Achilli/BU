package com.kp.malice.client.ui.widget;

public class ErrorAlertMessage extends AlertMessage {
    
    public ErrorAlertMessage() {
        super();
        message.setAttribute("style", "border: 1px solid #FF0000;");
    }
    
    public void show(String textMessage){
        setMessageText(textMessage);
        getElement().setAttribute("opacity", "1");
        getElement().getStyle().setZIndex(999);
    }
}
