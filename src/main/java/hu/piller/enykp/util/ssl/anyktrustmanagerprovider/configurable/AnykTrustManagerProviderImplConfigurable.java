package hu.piller.enykp.util.ssl.anyktrustmanagerprovider.configurable;

import hu.piller.enykp.util.ssl.anyktrustmanagerprovider.AbstractAnykTrustManagerProvider;
import hu.piller.enykp.util.ssl.anyktrustmanagerprovider.AnykTrustManagerProviderException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class AnykTrustManagerProviderImplConfigurable extends AbstractAnykTrustManagerProvider {
   private static final String TRUSTED_CERTS_SYSTEM_PROPERTY = "trusted.certs";
   private static final String CERT_LIST_SEPARATOR = ";";

   public static boolean hasCertsProvidedByUser() {
      return System.getProperty("trusted.certs") != null;
   }

   public KeyStore getKeyStore() throws AnykTrustManagerProviderException {
      String[] var1 = this.getCertFileNames();
      X509Certificate[] var2 = this.loadCertificates(var1);

      try {
         KeyStore var3 = KeyStore.getInstance(KeyStore.getDefaultType());
         var3.load((InputStream)null, (char[])null);

         for(int var4 = 0; var4 < var2.length; ++var4) {
            var3.setCertificateEntry(Integer.toString(var4), var2[var4]);
         }

         return var3;
      } catch (KeyStoreException var5) {
         throw new AnykTrustManagerProviderException(var5.getMessage());
      } catch (CertificateException var6) {
         throw new AnykTrustManagerProviderException(var6.getMessage());
      } catch (NoSuchAlgorithmException var7) {
         throw new AnykTrustManagerProviderException(var7.getMessage());
      } catch (IOException var8) {
         throw new AnykTrustManagerProviderException(var8.getMessage());
      }
   }

   private String[] getCertFileNames() throws AnykTrustManagerProviderException {
      String var1 = System.getProperty("trusted.certs");
      if (var1 == null) {
         throw new AnykTrustManagerProviderException("Nincsenek megadva a megbízható tanúsítványok!");
      } else {
         String[] var2 = var1.split(";");
         if (var2 != null && var2.length >= 1) {
            return var2;
         } else {
            throw new AnykTrustManagerProviderException("A megbízható tanúsítványokat rosszul adták meg!");
         }
      }
   }

   private X509Certificate[] loadCertificates(String[] var1) throws AnykTrustManagerProviderException {
      X509Certificate[] var2 = new X509Certificate[var1.length];

      try {
         int var3 = 0;
         CertificateFactory var10 = CertificateFactory.getInstance("X509");
         String[] var5 = var1;
         int var6 = var1.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String var8 = var5[var7];
            var2[var3++] = (X509Certificate)var10.generateCertificate(new FileInputStream(var8));
         }

         return var2;
      } catch (Exception var9) {
         String var4 = String.format("A rendszer tulajdonságként megadott tanúsítványok betöltése nem sikerült : %1$s", var9.getMessage());
         throw new AnykTrustManagerProviderException(var4);
      }
   }
}
