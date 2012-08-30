package test.deployed;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.http.HttpStatus;

import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.RequestTransport;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class HttpPostTransport implements RequestTransport {

  private final String urlToGwtServlet;

  public HttpPostTransport( String urlToGwtServlet ) {
    this.urlToGwtServlet = urlToGwtServlet;
  }

  @Override
  public void send( String payload, TransportReceiver receiver ) {
    try {
      PostMethod post = createPostMethod( payload );
      int result = new HttpClient().executeMethod( post );
      handleResult( result, post, receiver );
      post.releaseConnection();
    } catch( Exception e ) {
      receiver.onTransportFailure( new ServerFailure( e.getMessage(), e.getClass().getName(), "",
          true ) );
    }
  }

  private PostMethod createPostMethod( String payload ) throws UnsupportedEncodingException {
    PostMethod post = new PostMethod( urlToGwtServlet );
    RequestEntity requestEntity = new StringRequestEntity( payload,
        RequestFactory.JSON_CONTENT_TYPE_UTF8, "UTF-8" );
    post.setRequestEntity( requestEntity );
    return post;
  }

  private void handleResult( int result, PostMethod post, TransportReceiver receiver )
      throws IOException {
    if( result == HttpStatus.SC_OK ) {
      receiver.onTransportSuccess( post.getResponseBodyAsString() );
    } else {
      receiver.onTransportFailure( new ServerFailure( "Server returned " + result ) );
    }
  }
}
