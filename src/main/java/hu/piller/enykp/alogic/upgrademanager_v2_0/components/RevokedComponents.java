package hu.piller.enykp.alogic.upgrademanager_v2_0.components;

import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataconverters.VersionData;
import java.util.Iterator;
import java.util.Vector;

public class RevokedComponents extends DownloadableComponents {
   public RevokedComponents(DownloadableComponents var1) {
      super(var1);
      this.calcRevoked();
   }

   private void calcRevoked() {
      Vector var1 = new Vector();
      CurrentComponents var2 = new CurrentComponents();
      Iterator var3 = var2.components.iterator();

      while(true) {
         VersionData var4;
         do {
            if (!var3.hasNext()) {
               this.components.clear();
               this.components.addAll(var1);
               return;
            }

            var4 = (VersionData)var3.next();
         } while(!this.isRevokable(var4.getCategory()));

         boolean var5 = false;
         Iterator var6 = this.components.iterator();

         while(var6.hasNext()) {
            VersionData var7 = (VersionData)var6.next();
            if (var7.getOrganization().equals(var4.getOrganization()) && var7.getName().equals(var4.getName()) && var7.getCategory().equals(var4.getCategory())) {
               var5 = true;
               break;
            }
         }

         if (!var5) {
            var1.add(var4);
         }
      }
   }

   private boolean isRevokable(String var1) {
      return "Template".equals(var1) || "Help".equals(var1);
   }
}
