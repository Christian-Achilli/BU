package test.deployed;

import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.vm.RequestFactorySource;

public class IntegrationTestHelper {

  private static final String URL_TO_GWT_SERVLET = "http://localhost:8888/gwtRequest";

  /**
   * Creates a {@link RequestFactory}.
   */
  public static <T extends RequestFactory> T create( Class<T> requestFactoryClass ) {
    T factory = RequestFactorySource.create( requestFactoryClass );
    factory.initialize( new SimpleEventBus(), new HttpPostTransport( URL_TO_GWT_SERVLET ) );
    return factory;
  }
  
}