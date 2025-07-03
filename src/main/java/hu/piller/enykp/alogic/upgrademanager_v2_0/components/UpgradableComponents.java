package hu.piller.enykp.alogic.upgrademanager_v2_0.components;

import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataconverters.VersionData;
import java.util.Vector;

public class UpgradableComponents extends DownloadableComponents {
   public UpgradableComponents(DownloadableComponents var1) {
      super(var1);
      this.calcUpgradable();
   }

   public UpgradableComponents() {
      this.calcUpgradable();
   }

   private void calcUpgradable() {
      Vector var1 = new Vector();
      CurrentComponents var4 = new CurrentComponents();
      int var5 = this.components.size();
      int var6 = var4.components.size();

      for(int var7 = 0; var7 < var5; ++var7) {
         VersionData var2 = (VersionData)this.components.get(var7);
         boolean var8 = false;

         for(int var9 = 0; var9 < var6 && !var8; ++var9) {
            VersionData var3 = (VersionData)var4.components.get(var9);
            if (this.isUpgrade(var3, var2)) {
               var8 = true;
            }
         }

         if (var8) {
            var1.add(var2);
         }
      }

      this.components.clear();
      this.components.addAll(var1);
   }

   private boolean isUpgrade(VersionData var1, VersionData var2) {
      return var2.greaterThan(var1);
   }

   public boolean hasUpgradeable() {
      return this.components.size() > 0;
   }
}
