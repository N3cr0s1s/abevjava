package hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataproviders.installedcomponents;

import hu.piller.enykp.alogic.fileutil.TemplateChecker;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.orghandler.SuccessorException;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeBusinessException;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeTechnicalException;
import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataconverters.VersionData;
import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataproviders.VersionDataProvider;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.Version;
import hu.piller.enykp.util.filelist.EnykFileList;
import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

public abstract class InstalledVersionDataProvider extends VersionDataProvider {
   protected File path;
   protected Object[] filters;
   protected IPropertyList globalSettings = PropertyList.getInstance();

   public InstalledVersionDataProvider(String var1) {
      super("INSTALLED", var1);
   }

   public void collect() throws UpgradeBusinessException, UpgradeTechnicalException {
      EnykFileList var1 = EnykFileList.getInstance();
      TemplateChecker var2 = TemplateChecker.getInstance();
      Vector var3 = new Vector();
      Object[] var4 = var1.list(this.path.toString(), this.filters);
      if (this.hasResult(var4)) {
         for(int var5 = 0; var5 < var4.length; ++var5) {
            String var6 = (String)((Object[])((Object[])var4[var5]))[0];
            Hashtable var7 = (Hashtable)((Hashtable)((Object[])((Object[])var4[var5]))[1]).get("docinfo");

            try {
               VersionData var8 = new VersionData();
               var8.setCategory(this.getCategory());
               var8.setDescription(Tools.getString(var7.get("name"), "(nincs adat)"));
               var8.setFiles(new String[]{var6});
               var8.setName(Tools.getString(var7.get("id"), "(nincs adat)"));
               String var9 = Tools.getString(var7.get("org"), "(nincs adat)");

               try {
                  if (OrgInfo.getInstance().hasSuccessor(var9)) {
                     try {
                        var9 = OrgInfo.getInstance().getSuccessorOrgId(var9);
                     } catch (SuccessorException var11) {
                        throw new UpgradeBusinessException(var11.getMessage());
                     }
                  }

                  var8.setOrganization(var9);
                  var8.setSourceCategory(this.getSourceCategory());
                  var8.setVersion(new Version(Tools.getString(var7.get("ver"), "0.0.0")));
                  var3.add(var8);
               } catch (IllegalArgumentException var12) {
                  Tools.eLog(var12, 0);
               }
            } catch (Exception var13) {
               ErrorList.getInstance().writeError(new Integer(48378), "Hiba a sablonverzió megállapításakor - " + var6, 0, (Exception)null, (Object)null);
            }
         }
      }

      this.setCollection(var3);
   }

   private boolean hasResult(Object[] var1) {
      return var1 != null && var1.length > 0;
   }
}
