package hu.piller.enykp.alogic.upgrademanager_v2_0.components.reader;

import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.upgrademanager_v2_0.Directories;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeBusinessException;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeLogger;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeTechnicalException;
import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataconverters.VersionData;
import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataproviders.downloadablecomponents.DownloadableVersionDataProvider;
import hu.piller.enykp.util.base.Tools;
import me.necrocore.abevjava.NecroFile;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

public class OfflineDownloadableComponentsReader implements ComponentsReader {
   UpgradeLogger logger = UpgradeLogger.getInstance();
   private String[] orgs;

   public void setOrgs(String[] var1) {
      this.orgs = var1;
   }

   public String[] getOrgsNotConnected() {
      return new String[0];
   }

   public Collection getComponents() {
      Vector var1 = new Vector();
      DownloadableVersionDataProvider var2 = new DownloadableVersionDataProvider(this.getOfflineUpgradeURL());

      try {
         var2.collect();
      } catch (UpgradeTechnicalException var4) {
         this.logger.log(var4.getMessage());
      } catch (UpgradeBusinessException var5) {
         this.logger.log(var5.getMessage());
      }

      var1.addAll(this.filterOrganizations(var2.getCollection()));
      return var1;
   }

   private Collection filterOrganizations(Collection var1) {
      Vector var2 = new Vector();
      if (var1.isEmpty()) {
         return var1;
      } else {
         Vector var3 = (Vector)((Object[])((Object[])OrgInfo.getInstance().getOrgNames()))[0];
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            VersionData var5 = (VersionData)var4.next();
            if (var3.contains(var5.getOrganization()) && this.isUpgradeable(var5.getOrganization())) {
               var2.add(var5);
            }
         }

         return var2;
      }
   }

   private URL getOfflineUpgradeURL() {
      URL var1 = null;

      try {
         var1 = (new NecroFile(Directories.getDownloadPath() + File.separator + "enyk_gen.xml")).toURL();
      } catch (MalformedURLException var3) {
         Tools.eLog(var3, 1);
      }

      return var1;
   }

   private boolean isUpgradeable(String var1) {
      boolean var2 = false;
      if (this.orgs == null) {
         var2 = true;
      } else {
         String[] var3 = this.orgs;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if (var1.equals(var6)) {
               var2 = true;
               break;
            }
         }
      }

      return var2;
   }
}
