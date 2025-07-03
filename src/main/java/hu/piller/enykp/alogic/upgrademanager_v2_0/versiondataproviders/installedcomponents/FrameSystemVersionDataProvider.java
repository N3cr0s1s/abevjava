package hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataproviders.installedcomponents;

import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeBusinessException;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeTechnicalException;
import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataconverters.VersionData;
import hu.piller.enykp.util.base.Version;
import java.util.Vector;

public class FrameSystemVersionDataProvider extends InstalledVersionDataProvider {
   public static final String CATEGORY = "Framework";
   public static final String APPLICATION_OWNER = "NAV";

   public FrameSystemVersionDataProvider() {
      super("Framework");
   }

   public void collect() throws UpgradeBusinessException, UpgradeTechnicalException {
      VersionData var1 = new VersionData();
      var1.setCategory(this.getCategory());
      var1.setDescription("AbevJava Keretrendszer");
      var1.setFiles(new String[0]);
      var1.setName("abevjava");
      var1.setOrganization("NAV");
      var1.setSourceCategory(this.getSourceCategory());
      var1.setVersion(new Version("3.44.0"));
      Vector var2 = new Vector();
      var2.add(var1);
      this.setCollection(var2);
   }
}
