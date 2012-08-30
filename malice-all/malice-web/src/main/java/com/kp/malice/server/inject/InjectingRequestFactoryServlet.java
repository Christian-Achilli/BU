package com.kp.malice.server.inject;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.server.DefaultExceptionHandler;
import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

/**
 * An implementation of the RequestFactoryServlet that is using Guice to inject the {@link InjectingServiceLayerDecorator}, so
 * it lets the {@link InjectingServiceLayerDecorator} to control the request processing.
 * <p/>
 * This servlet is bound in the {@link RequestFactoryInjectingModule} and could be injected
 *
 * @see RequestFactoryInjectingModule
 *
 */
@SuppressWarnings("serial")
@Singleton
class InjectingRequestFactoryServlet extends RequestFactoryServlet {

    /**
     * Plugs the injectable decorator @{link InjectingServiceLayerDecorator} and the {@link DefaultExceptionHandler}, so the dependencies could be injected by the DI framework.
     */
    @Inject
    public InjectingRequestFactoryServlet(InjectingServiceLayerDecorator serviceLayerDecorator) {
        super(new LoquaciousExceptionHandler(), serviceLayerDecorator);
    }

    static class LoquaciousExceptionHandler implements ExceptionHandler {

        private static final Logger LOG = Logger.getLogger(LoquaciousExceptionHandler.class);

        @Override
        public ServerFailure createServerFailure(Throwable throwable) {
            ServerFailure sf = null;
            for (StackTraceElement st : throwable.getStackTrace()) {
                if (st.getClassName().startsWith("com.kp.malice")) {
                    sf = new ServerFailure("ERROR IN MALICE METHOD: " + st.getMethodName() + " class: "
                            + st.getClassName() + " line: " + st.getLineNumber(), throwable.getClass().getName(), null,
                            true);
                    break;
                }
            }
            LOG.error("Server error", throwable);
            return sf != null ? sf : new ServerFailure(throwable.getMessage(), throwable.getClass().getName(), null,
                    true);
        }

    }

}
