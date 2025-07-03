package hu.piller.enykp.util.ssl.anyktrustmanagerprovider;

import hu.piller.enykp.util.ssl.anyktrustmanagerprovider.anykts.AnykTrustManagerProviderImplAnykts;
import hu.piller.enykp.util.ssl.anyktrustmanagerprovider.configurable.AnykTrustManagerProviderImplConfigurable;

public class AnykTrustManagerProvider {
   private AnykTrustManagerProvider() {
   }

   public static IAnykTrustManagerProvider getProvider() {
      return (IAnykTrustManagerProvider)(AnykTrustManagerProviderImplConfigurable.hasCertsProvidedByUser() ? new AnykTrustManagerProviderImplConfigurable() : new AnykTrustManagerProviderImplAnykts());
   }
}
