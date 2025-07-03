package hu.piller.enykp.alogic.upgrademanager_v2_0.components;

import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataconverters.VersionData;
import java.util.Vector;

public class CurrentComponents extends InstalledComponents {
   private VersionData currentFrameworkVersionData;

   public CurrentComponents() {
      this.calcCurrent();
   }

   private void calcCurrent() {
      Vector var3 = new Vector();

      for(int var4 = 0; var4 < this.components.size(); ++var4) {
         VersionData var1 = (VersionData)this.components.get(var4);
         boolean var5 = false;

         for(int var6 = 0; var6 < this.components.size() && !var5; ++var6) {
            VersionData var2 = (VersionData)this.components.get(var6);
            if (var4 != var6 && var2.greaterThan(var1)) {
               var5 = true;
            }
         }

         if (!var5) {
            var3.add(var1);
            if ("Framework".equals(var1.getCategory())) {
               this.currentFrameworkVersionData = var1;
            }
         }
      }

      this.components.clear();
      this.components.addAll(var3);
   }

   public VersionData getCurrentFrameworkVersionData() {
      return this.currentFrameworkVersionData;
   }
}
