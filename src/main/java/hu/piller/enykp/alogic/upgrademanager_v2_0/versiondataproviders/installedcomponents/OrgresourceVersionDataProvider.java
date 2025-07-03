package hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataproviders.installedcomponents;

import hu.piller.enykp.alogic.fileutil.TemplateChecker;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.orghandler.OrgResource;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeBusinessException;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeTechnicalException;
import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataconverters.VersionData;
import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataproviders.VersionDataProvider;
import hu.piller.enykp.util.base.Version;
import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

public class OrgresourceVersionDataProvider extends VersionDataProvider {
   public OrgresourceVersionDataProvider() {
      super("INSTALLED", "Orgresource");
   }

   public void collect() throws UpgradeBusinessException, UpgradeTechnicalException {
      Hashtable var1 = (Hashtable)OrgInfo.getInstance().getOrgList();
      Iterator var2 = var1.keySet().iterator();

      VersionData var5;
      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         OrgResource var4 = (OrgResource)var1.get(var3);
         var5 = new VersionData();
         var5.setCategory(this.getCategory());
         var5.setDescription("");
         var5.setFiles(this.getFiles(var4));
         var5.setLocation(this.getLocation(var4));
         var5.setName(var4.getType());
         var5.setOrganization(var3);
         var5.setSourceCategory(this.getSourceCategory());
         var5.setVersion(new Version(var4.getVersion()));
         this.getCollection().add(var5);
      }

      Set var6 = TemplateChecker.getInstance().getTemplatePublishers();
      Iterator var7 = var6.iterator();

      while(var7.hasNext()) {
         String var8 = (String)var7.next();
         if (!var1.containsKey(var8)) {
            var5 = new VersionData();
            var5.setCategory(this.getCategory());
            var5.setDescription("");
            var5.setFiles(new String[0]);
            var5.setLocation("");
            var5.setName(var8 + "Resources");
            var5.setOrganization(var8);
            var5.setSourceCategory(this.getSourceCategory());
            var5.setVersion(new Version("0.0"));
            this.getCollection().add(var5);
         }
      }

   }

   private String[] getFiles(OrgResource var1) {
      String[] var2 = new String[]{var1.getPath().substring(var1.getPath().lastIndexOf(File.separator) + 1)};
      return var2;
   }

   private String getLocation(OrgResource var1) {
      return var1.getPath().substring(0, var1.getPath().lastIndexOf(File.separator));
   }
}
