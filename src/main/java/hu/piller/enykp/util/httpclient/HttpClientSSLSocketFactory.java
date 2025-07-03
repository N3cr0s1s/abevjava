package hu.piller.enykp.util.httpclient;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;

public class HttpClientSSLSocketFactory extends SSLSocketFactory {
   private SSLContext sslContext;

   public HttpClientSSLSocketFactory(KeyStore var1, String var2) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
      super(var1);
      TrustManagerFactory var3 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      var3.init(var1);
      this.sslContext = SSLContext.getInstance(var2);
      this.sslContext.init((KeyManager[])null, var3.getTrustManagers(), (SecureRandom)null);
   }

   public Socket createSocket(Socket var1, String var2, int var3, boolean var4) throws IOException, UnknownHostException {
      return this.sslContext.getSocketFactory().createSocket(var1, var2, var3, var4);
   }

   public Socket createSocket() throws IOException {
      return this.sslContext.getSocketFactory().createSocket();
   }
}
