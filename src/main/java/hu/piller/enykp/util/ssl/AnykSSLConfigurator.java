package hu.piller.enykp.util.ssl;

import hu.piller.enykp.util.ssl.anyktrustmanagerprovider.AnykTrustManagerProvider;
import hu.piller.enykp.util.ssl.anyktrustmanagerprovider.AnykTrustManagerProviderException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;

public class AnykSSLConfigurator {
   private static final String PROTOCOL_TLS_V12 = "TLSv1.2";

   public static String getProtocol() {
      return "TLSv1.2";
   }

   public static void initSSL() throws AnykSSLConfiguratorException {
      String var0 = getProtocol();
      System.setProperty("https.protocols", var0);

      String var2;
      try {
         SSLContext var1 = SSLContext.getInstance(var0);
         var1.init((KeyManager[])null, AnykTrustManagerProvider.getProvider().getTrustManagers(), (SecureRandom)null);
         HttpsURLConnection.setDefaultSSLSocketFactory(var1.getSocketFactory());
      } catch (AnykTrustManagerProviderException var3) {
         var2 = "Az ÁNYK által elfogadott tanúsítványok betöltése nem sikerült : %1$s";
         throw new AnykSSLConfiguratorException(String.format(var2, var3.getMessage()));
      } catch (KeyManagementException | NoSuchAlgorithmException var4) {
         var2 = "Az ÁNYK SSL kapcsolat üzemkész állapotba hozása nem sikerült : %1$s";
         throw new AnykSSLConfiguratorException(String.format(var2, var4.getMessage()));
      }
   }
}
