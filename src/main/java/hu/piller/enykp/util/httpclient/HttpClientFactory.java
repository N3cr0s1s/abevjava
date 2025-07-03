package hu.piller.enykp.util.httpclient;

import hu.piller.enykp.util.proxy.ProxySettings;
import hu.piller.enykp.util.ssl.AnykSSLConfigurator;
import hu.piller.enykp.util.ssl.anyktrustmanagerprovider.AbstractAnykTrustManagerProvider;
import hu.piller.enykp.util.ssl.anyktrustmanagerprovider.AnykTrustManagerProvider;
import hu.piller.enykp.util.ssl.anyktrustmanagerprovider.AnykTrustManagerProviderException;
import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpProtocolParams;

public final class HttpClientFactory {
   private HttpClientFactory() {
   }

   public static HttpClient createWithAnykConfig() throws HttpClientFactoryException {
      if (System.getProperty("kau.trace") != null) {
         System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
         System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
         System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.wire", "DEBUG");
         System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.impl.conn", "DEBUG");
         System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.impl.client", "DEBUG");
         System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.client", "DEBUG");
         System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "DEBUG");
      }

      SchemeRegistry var0 = new SchemeRegistry();
      var0.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

      KeyStore var1;
      try {
         var1 = ((AbstractAnykTrustManagerProvider)AnykTrustManagerProvider.getProvider()).getKeyStore();
      } catch (AnykTrustManagerProviderException var10) {
         throw new HttpClientFactoryException("HTTP kezelő komponens létrehozási hiba!", var10);
      }

      HttpClientSSLSocketFactory var2;
      try {
         var2 = new HttpClientSSLSocketFactory(var1, AnykSSLConfigurator.getProtocol());
         var2.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
      } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException | UnrecoverableKeyException var9) {
         throw new HttpClientFactoryException("SSL kezelő komponens létrehozási hiba!", var9);
      }

      var0.register(new Scheme("https", var2, 443));
      BasicHttpParams var3 = new BasicHttpParams();
      HttpProtocolParams.setVersion(var3, HttpVersion.HTTP_1_1);
      HttpProtocolParams.setContentCharset(var3, "UTF-8");
      ThreadSafeClientConnManager var4 = new ThreadSafeClientConnManager(var3, var0);
      DefaultHttpClient var5 = new DefaultHttpClient(var4);
      if (ProxySettings.getInstance().isProxyEnabled()) {
         HttpHost var6 = new HttpHost(ProxySettings.getInstance().getHost(), ProxySettings.getInstance().getPort());
         if (ProxySettings.getInstance().isProxyAuthenticated()) {
            BasicCredentialsProvider var7 = new BasicCredentialsProvider();
            var7.setCredentials(new AuthScope(var6), new UsernamePasswordCredentials(ProxySettings.getInstance().getUsername(), ProxySettings.getInstance().getPassword()));
            ((DefaultHttpClient)var5).setCredentialsProvider(var7);
         }

         var5.getParams().setParameter("http.route.default-proxy", var6);
         String var11 = System.getProperty("http.nonProxyHosts");
         if (var11 != null) {
            ProxySelectorRoutePlanner var8 = new ProxySelectorRoutePlanner(var5.getConnectionManager().getSchemeRegistry(), new HttpClientFactory.UrlCheckProxySelector(var11.trim().toLowerCase()));
            ((DefaultHttpClient)var5).setRoutePlanner(var8);
         }
      }

      var5.getParams().setParameter("http.protocol.cookie-policy", "compatibility");
      return var5;
   }

   static class UrlCheckProxySelector extends ProxySelector {
      private String[] nonProxyHosts;

      public UrlCheckProxySelector(String var1) {
         if (var1 != null) {
            if (var1.contains("|")) {
               this.nonProxyHosts = var1.split("|");
            } else {
               this.nonProxyHosts = new String[]{var1};
            }
         }

      }

      public List<Proxy> select(URI var1) {
         Object var2 = new ArrayList();
         String var3 = var1.getHost();
         String[] var4 = this.nonProxyHosts;
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String var7 = var4[var6];
            if (var3.matches(var7.replaceAll("\\*", "(.*)"))) {
               if (this.isTraceable()) {
                  System.out.println("## NOPROXY " + var1 + " , " + var7);
               }

               ((List)var2).add(Proxy.NO_PROXY);
               break;
            }
         }

         if (((List)var2).isEmpty()) {
            if (this.isTraceable()) {
               System.out.println("## WITHPROXY " + var1);
            }

            var2 = ProxySelector.getDefault().select(var1);
         }

         return (List)var2;
      }

      public void connectFailed(URI var1, SocketAddress var2, IOException var3) {
      }

      private boolean isTraceable() {
         return System.getProperty("kau.trace") != null || System.getProperty("datagate.trace") != null;
      }
   }
}
