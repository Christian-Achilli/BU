package com.kp.malice.client.ui.widget;

public class SuccessAlertMessage extends AlertMessage {
    public final static String SUCCESS_OPERATION = "Operazione eseguita con successo";

    public SuccessAlertMessage() {
        super();
        message.setAttribute("style", "border: 1px solid #54FF4F;");
    }

    @Override
    public void show() {
        setMessageText(SUCCESS_OPERATION);
        super.show();
    }
}
