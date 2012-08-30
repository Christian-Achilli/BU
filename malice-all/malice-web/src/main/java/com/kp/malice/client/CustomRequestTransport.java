package com.kp.malice.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.web.bindery.requestfactory.gwt.client.DefaultRequestTransport;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.kp.malice.shared.MaliceUtil;

public class CustomRequestTransport extends DefaultRequestTransport {

    @Override
    public void send(String payload, TransportReceiver receiver) {
        doBeforeSend();
        super.send(payload, wrap(receiver));
    }

    private TransportReceiver wrap(final TransportReceiver delegate) {
        return new TransportReceiver() {

            public void onTransportSuccess(String payload) {
                doOnSuccess();
                delegate.onTransportSuccess(payload);
            }

            public void onTransportFailure(ServerFailure failure) {
                doOnFailure(failure);
                delegate.onTransportFailure(failure);
            }
        };
    }

    protected void doBeforeSend() {
        // Some processing before the request is send
        Document.get().getBody().getStyle().setCursor(Cursor.WAIT);
        MaliceUtil.creaLoader();
    }

    protected void doOnSuccess() {
        // Some processing on success
        Document.get().getBody().getStyle().setCursor(Cursor.DEFAULT);
        MaliceUtil.eliminaLoader();
    }

    protected void doOnFailure(ServerFailure failure) {
        // Some processing on failure
        Document.get().getBody().getStyle().setCursor(Cursor.DEFAULT);
        MaliceUtil.eliminaLoader();
    }
}
