package hu.piller.enykp.alogic.upgrademanager_v2_0.gui;

import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataconverters.VersionData;

public class UpgradeTableEntry {
   protected boolean install;
   protected VersionData vd;

   public UpgradeTableEntry(VersionData var1) {
      this.vd = var1;
      this.install = false;
   }

   public UpgradeTableEntry(VersionData var1, boolean var2) {
      this.vd = var1;
      this.install = var2;
   }

   public VersionData getVersionData() {
      return this.vd;
   }

   public boolean isInstall() {
      return this.install;
   }

   public void setInstall(boolean var1) {
      this.install = var1;
   }

   public void invertInstallFlag() {
      this.install = !this.install;
   }
}
