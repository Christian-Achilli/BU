package com.malice.db.spring;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class ReceiverCaptor<T> extends Receiver<T> {
    
    private T response;
    private ServerFailure serverFailure;
    
    @Override
    public void onSuccess(T response) {
        this.response = response;
    }
    
    @Override
    public void onFailure(ServerFailure error) {
        this.serverFailure = error;
    }
    
    public T getResponse() {
        return response;
    }
    
    public ServerFailure getServerFailure() {
        return serverFailure;
    }
}
