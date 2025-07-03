package hu.piller.enykp.util.ssl.anyktrustmanagerprovider;

import javax.net.ssl.TrustManager;

public interface IAnykTrustManagerProvider {
   TrustManager[] getTrustManagers() throws AnykTrustManagerProviderException;
}
