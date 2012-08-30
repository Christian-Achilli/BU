package requestFactory;

import static org.mockito.Mockito.verify;

import java.rmi.ServerError;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.ArgumentCaptor;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.server.ServiceLayer;
import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;
import com.google.web.bindery.requestfactory.server.SimpleRequestProcessor;
import com.google.web.bindery.requestfactory.server.testing.InProcessRequestTransport;
import com.google.web.bindery.requestfactory.shared.Locator;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.web.bindery.requestfactory.shared.ServiceLocator;
import com.google.web.bindery.requestfactory.vm.RequestFactorySource;
import com.kp.malice.boundaries.locators.TitoloLocator;
import com.kp.malice.boundaries.services.PortaleServiceBoundary;
import com.kp.malice.repositories.DatabaseGatewayLloyds;
import com.kp.malice.repositories.IncassiRepository;
import com.kp.malice.repositories.TitoliRepository;
import com.kp.malice.shared.MaliceRequestFactory;
import com.kp.malice.shared.MaliceRequestFactory.ServiziPortale;
import com.kp.malice.useCases.DispatcherServiziPortale;

@SuppressWarnings("unchecked")
public class MaliceRequestFactoryHelper {

    private static Injector injector = Guice.createInjector(new InnerGuiceModule());
    private static GuiceServiceLocator guiceServiceLocator = injector.getInstance(GuiceServiceLocator.class);

    static class InnerGuiceModule extends AbstractModule {
        @Provides
        @Singleton
        public ServiceLayer getServiceLayer() {
            return ServiceLayer.create(new MockServiceDecorator());
        }

        @Provides
        @Singleton
        public SimpleRequestProcessor getSimpleRequestProcessor() {
            return new SimpleRequestProcessor(getServiceLayer());
        }

        @Provides
        public ServiziPortale createServiziPortaleContext(MaliceRequestFactory factory) {
            return factory.serviziPortale();
        }

        @Provides
        @Singleton
        public MaliceRequestFactory createMaliceRequestFactory(EventBus eventBus) {
            MaliceRequestFactory factory = RequestFactorySource.create(MaliceRequestFactory.class);
            SimpleRequestProcessor processor = new SimpleRequestProcessor(getService(ServiceLayer.class));
            factory.initialize(eventBus, new InProcessRequestTransport(processor));
            return factory;
        }

        @Override
        protected void configure() {
            bind(PortaleServiceBoundary.class).to(DispatcherServiziPortale.class);
            bind(TitoloLocator.class);
            bind(TitoliRepository.class).to(DatabaseGatewayLloyds.class);
            bind(IncassiRepository.class).to(DatabaseGatewayLloyds.class);
            bind(EventBus.class).to(SimpleEventBus.class);
            bind(GuiceServiceLocator.class);
        }
    }

    private static class GuiceServiceLocator implements ServiceLocator {

        private final Map<Class<?>, Object> services = new HashMap<Class<?>, Object>();

        @Override
        public Object getInstance(Class<?> clazz) {
            // Make sure to return always the same mocked instance for each requested type
            Object result = services.get(clazz);
            if (result == null) {
                result = injector.getInstance(clazz);
                services.put(clazz, result);
            }
            return result;
        }
    }

    private static class MockServiceDecorator extends ServiceLayerDecorator {
        @Override
        public <T extends ServiceLocator> T createServiceLocator(Class<T> clazz) {
            return (T) guiceServiceLocator;
        }

        @Override
        public <T extends Locator<?, ?>> T createLocator(Class<T> clazz) {
            return getService(clazz);
        }
    }

    /**
     * Returns the same service instance as used by the RequestFactory internals.
     */
    public static <T> T getService(Class<T> serviceClass) {
        T result = (T) guiceServiceLocator.getInstance(serviceClass);
        return result;
    }

    /**
     * Returns the value passed to {@link Receiver#onSuccess(Object)}
     */
    public static <T> T captureResult(Receiver<T> receiver) {
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(receiver).onSuccess((T) captor.capture());
        return (T) captor.getValue();
    }

    public static ServerError captureServerError(Receiver<ServerError> receiver) {
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(receiver).onFailure((ServerFailure) captor.capture());
        return (ServerError) captor.getValue();
    }

    public static <T> List<T> captureResultList(Receiver<List<T>> receiver) {
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(receiver).onSuccess((List<T>) captor.capture());
        return (List<T>) captor.getValue();
    }
    //    public static <T> T captureFailure(Receiver<T> receiver) {
    //        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
    //        verify(receiver).onFailure(error)((T) captor.capture());
    //        return (T) captor.getValue();
    //    }
}
