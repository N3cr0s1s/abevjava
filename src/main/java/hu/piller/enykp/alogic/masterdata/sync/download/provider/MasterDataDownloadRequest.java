package hu.piller.enykp.alogic.masterdata.sync.download.provider;

import java.util.ArrayList;
import java.util.List;

public class MasterDataDownloadRequest {
   private List<String> azonositok = new ArrayList();

   public void setAzonositok(String... var1) {
      String[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         if (var5 != null) {
            this.azonositok.add(var5.trim());
         }
      }

   }

   public List<String> getAzonositok() {
      return this.azonositok;
   }
}
