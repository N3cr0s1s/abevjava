package hu.piller.enykp.alogic.upgrademanager_v2_0.components;

import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataconverters.VersionData;
import java.util.Vector;

public class NewComponents extends DownloadableComponents {
   public NewComponents(DownloadableComponents var1) {
      super(var1);
      this.calcNew();
   }

   public NewComponents() {
      this.calcNew();
   }

   private void calcNew() {
      Vector var1 = new Vector();
      CurrentComponents var4 = new CurrentComponents();
      int var5 = this.components.size();
      int var6 = var4.components.size();

      for(int var7 = 0; var7 < var5; ++var7) {
         VersionData var2 = (VersionData)this.components.get(var7);
         int var8 = 0;

         boolean var9;
         for(var9 = false; var8 < var6; ++var8) {
            VersionData var3 = (VersionData)var4.components.get(var8);
            if (!this.isNew(var3, var2)) {
               var9 = true;
               break;
            }
         }

         if (!var9) {
            var1.add(var2);
         }
      }

      this.components.clear();
      this.components.addAll(var1);
   }

   private boolean isNew(VersionData var1, VersionData var2) {
      return !var2.greaterThan(var1) && !var1.greaterThan(var2) && !var1.equalsCompatible(var2);
   }
}
