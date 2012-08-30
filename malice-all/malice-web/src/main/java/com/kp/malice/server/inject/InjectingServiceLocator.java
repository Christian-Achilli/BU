package com.kp.malice.server.inject;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.web.bindery.requestfactory.shared.ServiceLocator;

public class InjectingServiceLocator implements ServiceLocator {

    private final Injector injector;

    @Inject
    public InjectingServiceLocator(Injector injector) {
        this.injector = injector;
    }

    public Object getInstance(Class<?> clazz) {
        Object serviceInstance = injector.getInstance(clazz);
        return serviceInstance;
    }

}
