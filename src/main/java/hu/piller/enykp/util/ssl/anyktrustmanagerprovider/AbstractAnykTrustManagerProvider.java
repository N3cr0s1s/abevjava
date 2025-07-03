package hu.piller.enykp.util.ssl.anyktrustmanagerprovider;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public abstract class AbstractAnykTrustManagerProvider implements IAnykTrustManagerProvider {
   public TrustManager[] getTrustManagers() throws AnykTrustManagerProviderException {
      try {
         TrustManagerFactory var1 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
         var1.init(this.getKeyStore());
         return var1.getTrustManagers();
      } catch (KeyStoreException var2) {
         throw new AnykTrustManagerProviderException();
      } catch (NoSuchAlgorithmException var3) {
         throw new AnykTrustManagerProviderException();
      }
   }

   public abstract KeyStore getKeyStore() throws AnykTrustManagerProviderException;
}
