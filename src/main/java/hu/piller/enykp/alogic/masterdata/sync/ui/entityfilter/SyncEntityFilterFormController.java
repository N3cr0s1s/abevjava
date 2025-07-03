package hu.piller.enykp.alogic.masterdata.sync.ui.entityfilter;

import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.core.EntityException;
import hu.piller.enykp.alogic.masterdata.core.EntityHome;
import hu.piller.enykp.alogic.masterdata.core.MasterData;
import hu.piller.enykp.alogic.masterdata.sync.download.MasterDataDownload;
import hu.piller.enykp.alogic.masterdata.sync.download.MasterDataDownloadException;
import java.util.ArrayList;

public class SyncEntityFilterFormController {
   public void send(String[] var1) throws MasterDataDownloadException {
      MasterDataDownload.getInstance().sendMasterDataDownloadRequest(var1);
   }

   public Entity[] load() {
      ArrayList var1 = new ArrayList();
      String[] var2 = new String[]{"Társaság", "Egyéni vállalkozó", "Magánszemély"};
      String[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];

         try {
            Entity[] var7 = EntityHome.getInstance().findByTypeAndMasterData(var6, new MasterData[0]);
            Entity[] var8 = var7;
            int var9 = var7.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               Entity var11 = var8[var10];
               var1.add(var11);
            }
         } catch (EntityException var12) {
            var12.printStackTrace();
         }
      }

      return (Entity[])var1.toArray(new Entity[var1.size()]);
   }
}
