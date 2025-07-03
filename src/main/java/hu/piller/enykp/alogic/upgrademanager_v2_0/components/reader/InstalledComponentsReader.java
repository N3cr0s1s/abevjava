package hu.piller.enykp.alogic.upgrademanager_v2_0.components.reader;

import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeBusinessException;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeTechnicalException;
import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataproviders.VersionDataProvider;
import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataproviders.installedcomponents.FrameSystemVersionDataProvider;
import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataproviders.installedcomponents.OrgresourceVersionDataProvider;
import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataproviders.installedcomponents.TemplateHelpVersionDataProvider;
import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataproviders.installedcomponents.TemplateVersionDataProvider;
import java.util.Collection;
import java.util.Vector;

public class InstalledComponentsReader implements ComponentsReader {
   public Collection getComponents() {
      Vector var1 = new Vector();
      VersionDataProvider[] var2 = new VersionDataProvider[]{new FrameSystemVersionDataProvider(), new TemplateVersionDataProvider(), new TemplateHelpVersionDataProvider(), new OrgresourceVersionDataProvider()};

      for(int var3 = 0; var3 < var2.length; ++var3) {
         try {
            var2[var3].collect();
         } catch (UpgradeTechnicalException var5) {
         } catch (UpgradeBusinessException var6) {
         }

         var1.addAll(var2[var3].getCollection());
      }

      return var1;
   }

   public String[] getOrgsNotConnected() {
      return new String[0];
   }

   public void setOrgs(String[] var1) {
   }
}
