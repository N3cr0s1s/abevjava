package hu.piller.enykp.alogic.masterdata.sync.download.provider;

import hu.piller.enykp.alogic.ebev.datagate.DatagateClient;

public final class MasterDataProviderFactory {
   protected static IMasterDataProvider instance;

   public static IMasterDataProvider getService() {
      if (instance == null) {
         instance = new DatagateClient();
      }

      return instance;
   }
}
