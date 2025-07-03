package hu.piller.enykp.util.ssl.anyktrustmanagerprovider.anykts;

import hu.piller.enykp.util.ssl.anyktrustmanagerprovider.AbstractAnykTrustManagerProvider;
import hu.piller.enykp.util.ssl.anyktrustmanagerprovider.AnykTrustManagerProviderException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class AnykTrustManagerProviderImplAnykts extends AbstractAnykTrustManagerProvider {
   private static final String ANYKTS_PATH = "resources/anykts";
   private static final String ANYKTS_CREDENTIALS = "anykanyk";

   public KeyStore getKeyStore() throws AnykTrustManagerProviderException {
      try {
         KeyStore var1 = KeyStore.getInstance(KeyStore.getDefaultType());
         var1.load(ClassLoader.getSystemResourceAsStream("resources/anykts"), "anykanyk".toCharArray());
         return var1;
      } catch (KeyStoreException var2) {
         throw new AnykTrustManagerProviderException();
      } catch (NoSuchAlgorithmException var3) {
         throw new AnykTrustManagerProviderException();
      } catch (CertificateException var4) {
         throw new AnykTrustManagerProviderException();
      } catch (IOException var5) {
         throw new AnykTrustManagerProviderException();
      }
   }
}
