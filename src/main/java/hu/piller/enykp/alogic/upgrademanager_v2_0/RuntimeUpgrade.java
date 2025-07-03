package hu.piller.enykp.alogic.upgrademanager_v2_0;

import hu.piller.enykp.alogic.fileutil.TemplateChecker;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.orghandler.OrgResource;
import hu.piller.enykp.alogic.orghandler.SuccessorException;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.alogic.settingspanel.upgrade.UpgradeFunction;
import hu.piller.enykp.alogic.upgrademanager_v2_0.components.DownloadableComponents;
import hu.piller.enykp.alogic.upgrademanager_v2_0.components.event.ComponentProcessingEvent;
import hu.piller.enykp.alogic.upgrademanager_v2_0.components.event.ComponentProcessingEventListener;
import hu.piller.enykp.alogic.upgrademanager_v2_0.gui.JAutoUpdateInfoDialog;
import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataconverters.VersionData;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Version;
import hu.piller.enykp.util.filelist.EnykFileInfo;
import me.necrocore.abevjava.NecroFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.SwingUtilities;

public class RuntimeUpgrade {
   static final String SEPARATOR = "\n\n";
   private static RuntimeUpgrade runtime;

   private RuntimeUpgrade() {
   }

   public static synchronized RuntimeUpgrade getInstance() {
      if (runtime == null) {
         runtime = new RuntimeUpgrade();
      }

      return runtime;
   }

   public boolean isOperative(BookModel var1) {
      VersionData var2 = getCurrTemplateVersion(var1.loadedfile);
      Vector var3 = (Vector)PropertyList.getInstance().get("revoked.cache");
      if (var3 != null && !var3.isEmpty()) {
         for(int var4 = 0; var4 < var3.size(); ++var4) {
            VersionData var5 = (VersionData)var3.get(var4);
            if (var2.getName().equals(var5.getName()) && var2.getVersion().compareTo(var5.getVersion()) <= 0) {
               return false;
            }
         }
      }

      return true;
   }

   public String checkUpgrade(String var1, UpgradeFunction var2) {
      StringBuilder var3 = new StringBuilder("");
      String var4 = var1;
      if (!MainFrame.updateRequired) {
         System.out.println("WEB UPGRADE DISABLED!");
         return var1;
      } else if (!this.isAutoUpdateEnabled(var2)) {
         return var1;
      } else {
         final VersionData var5 = getCurrTemplateVersion(new NecroFile(var1));
         final VersionData var6 = getOrgresourceVersion(var5.getOrganization());
         final VersionData var7 = getUpdateVersion(var5);
         final VersionData var8 = getUpdateVersion(var6);
         boolean var9 = true;
         final JAutoUpdateInfoDialog var10 = new JAutoUpdateInfoDialog();
         if (var7 != null || var8 != null) {
            SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                  var10.setVisible(true);
               }
            });
         }

         if (var8 != null) {
            SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                  StringBuilder var1 = new StringBuilder();
                  if (var6.getVersion().equals(new Version("0.0"))) {
                     var1.append(OrgInfo.getInstance().getOrgShortnameByOrgID(var8.getOrganization())).append(" paraméterállomány letöltése");
                  } else {
                     var1.append(OrgInfo.getInstance().getOrgShortnameByOrgID(var8.getOrganization())).append(" paraméterállomány frissítése: ").append(var6.getVersion()).append(" -> ").append(var8.getVersion());
                  }

                  var10.setText(var1.toString());
               }
            });
            if (this.update(var8)) {
               var3.append("A(z) '").append(OrgInfo.getInstance().getOrgShortnameByOrgID(var6.getOrganization())).append("' szervezet paraméter fájl ");
               if (var6.getVersion().equals(new Version("0.0"))) {
                  OrgInfo.getInstance().mountType(var6.getOrganization());
                  var3.append("letöltése sikeres.");
               } else {
                  var3.append(var6.getVersion()).append(" verziójának ").append(var8.getVersion()).append(" verzióra frissítése sikeres.");
               }

               var3.append("\n\n");
            } else {
               var3.append("A(z) '").append(OrgInfo.getInstance().getOrgShortnameByOrgID(var6.getOrganization())).append("' szervezet paraméter fájl ");
               if (var6.getVersion().equals(new Version("0.0"))) {
                  var9 = false;
                  var3.append("letöltése sikertelen.\nParaméterfájl hiányában a sablon frissítésre nem kerül sor.");
               } else {
                  var3.append(var6.getVersion()).append(" verziójának ").append(var8.getVersion()).append(" verzióra frissítése sikertelen.");
               }

               var3.append("\n\n");
            }
         }

         if (var9 && var7 != null) {
            SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                  String var1 = OrgInfo.getInstance().getOrgShortnameByOrgID(var7.getOrganization()) + " " + var7.getName() + " sablon frissítése: " + var5.getVersion() + " -> " + var7.getVersion();
                  var10.setText(var1);
               }
            });
            StringBuilder var11 = new StringBuilder("");
            var11.append("A(z) '").append(OrgInfo.getInstance().getOrgShortnameByOrgID(var7.getOrganization())).append("' szervezet '").append(var7.getName()).append("' sablon ").append(var5.getVersion()).append(" verziójának ");
            if (this.update(var7)) {
               String var12 = this.getFilename(var7);
               if (var12 != null) {
                  var4 = var12;
                  var11.append(var7.getVersion()).append(" verzióra frissítése sikeres.");
               } else {
                  var11.append(var7.getVersion()).append(" verzióra frissítése sikeres, de az új verzió nem tölthető be");
               }
            } else {
               var11.append(" verziójú sablonjának frissítése sikertelen.");
            }

            var3.append(var11);
         }

         this.refreshStatusBar();
         var10.dispose();
         if (!"".equals(var3.toString())) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, var3.toString(), "Automatikus frissítés - tájékoztatás", 1);
         }

         return var4;
      }
   }

   private boolean isAutoUpdateEnabled(UpgradeFunction var1) {
      boolean var2 = true;
      boolean var3 = SettingsStore.getInstance().get("upgrade") != null ? SettingsStore.getInstance().get("upgrade").containsKey(var1.getCmd()) : false;
      if (var3) {
         var2 = Boolean.parseBoolean(SettingsStore.getInstance().get("upgrade", var1.getCmd()));
      }

      return var2;
   }

   private String getFilename(VersionData var1) {
      String var2 = null;

      try {
         String[] var3 = TemplateChecker.getInstance().getTemplateFileNames4CheckUpdate(var1.getName(), var1.getVersion().toString(), var1.getOrganization());
         if (var3 != null && var3.length == 1) {
            var2 = var3[0];
         }
      } catch (Exception var4) {
         UpgradeLogger.getInstance().log(var4);
         ErrorList.getInstance().writeError("Frissítés", "A sablon fájlneve nem határozható meg az automatikus frissítéshez", IErrorList.LEVEL_ERROR, var4, (Object)null);
      }

      return var2;
   }

   private boolean update(VersionData var1) {
      boolean var2 = true;
      Vector var3 = new Vector();
      var3.add(var1);
      DownloadableComponents var4 = new DownloadableComponents(var3);
      RuntimeUpgrade.InstallListener var5 = new RuntimeUpgrade.InstallListener();
      var4.addComponentProcessedEventListener(var5);

      try {
         var4.install();
         var2 = var5.isSuccess();
         if (var2) {
            this.removeFromCache(var1);
         }
      } catch (UpgradeBusinessException var11) {
         UpgradeLogger.getInstance().log(" Automatikus frissítés: hiba a(z) " + var1.getOrganization() + " " + var1.getName() + " " + var1.getVersion() + " frissítésekor");
         String var7 = (new SimpleDateFormat("[yyyy.MM.dd] [kk:mm:ss.SSS]")).format(new Date());
         ErrorList.getInstance().writeError("Frissítés", var7 + " Hiba a(z) " + var1.getOrganization() + " " + var1.getName() + " " + var1.getVersion() + " frissítésekor", IErrorList.LEVEL_ERROR, var11, (Object)null);
      } finally {
         var4.removeComponentProcessedEventListener(var5);
      }

      return var2;
   }

   private void refreshStatusBar() {
      if (PropertyList.getInstance().get("upg.cache") != null) {
         MainFrame.thisinstance.mp.removeFrissitesMessage();
         if (this.isEmptyUpgradeCache()) {
            MainFrame.thisinstance.mp.removeFrissitesButton();
         } else {
            MainFrame.thisinstance.mp.addFrissitesButton();
         }

         MainFrame.thisinstance.mp.updateUI();
      }
   }

   private boolean isEmptyUpgradeCache() {
      Vector var1 = (Vector)PropertyList.getInstance().get("upg.cache");
      return var1 == null ? true : var1.isEmpty();
   }

   private void removeFromCache(VersionData var1) {
      ((Vector)PropertyList.getInstance().get("upg.cache")).remove(var1);
   }

   private static VersionData getOrgresourceVersion(String var0) {
      VersionData var1 = new VersionData();
      OrgResource var2 = (OrgResource)((Hashtable)OrgInfo.getInstance().getOrgList()).get(var0);
      if (var2 != null) {
         var1.setCategory("Orgresource");
         var1.setDescription("");
         var1.setFiles(getFiles(var2));
         var1.setLocation(getLocation(var2));
         var1.setName(var0 + "Resources");
         var1.setOrganization(var0);
         var1.setSourceCategory("INSTALLED");
         var1.setVersion(new Version(var2.getVersion()));
      } else {
         var1.setCategory("Orgresource");
         var1.setDescription("");
         var1.setFiles(new String[]{var0 + "Resources_v0.0.jar"});
         var1.setLocation(Directories.getOrgresourcesPath());
         var1.setName(var0 + "Resources");
         var1.setOrganization(var0);
         var1.setSourceCategory("INSTALLED");
         var1.setVersion(new Version("0.0"));
      }

      return var1;
   }

   private static String[] getFiles(OrgResource var0) {
      String[] var1 = new String[]{var0.getPath().substring(var0.getPath().lastIndexOf(File.separator) + 1)};
      return var1;
   }

   private static String getLocation(OrgResource var0) {
      return var0.getPath().substring(0, var0.getPath().lastIndexOf(File.separator));
   }

   private static VersionData getCurrTemplateVersion(File var0) {
      VersionData var1 = null;
      if (var0.exists()) {
         EnykFileInfo var2 = new EnykFileInfo(new BookModel());
         Hashtable var3 = (Hashtable)((Hashtable)var2.getFileInfo(var0)).get("docinfo");
         var1 = new VersionData();
         var1.setCategory("Template");
         var1.setDescription("");
         var1.setFiles(new String[0]);
         var1.setLocation("");
         var1.setName((String)var3.get("id"));
         String var4 = (String)var3.get("org");
         if (OrgInfo.getInstance().hasSuccessor(var4)) {
            try {
               var4 = OrgInfo.getInstance().getSuccessorOrgId(var4);
            } catch (SuccessorException var6) {
               var6.printStackTrace();
            }
         }

         var1.setOrganization(var4);
         var1.setSourceCategory("INSTALLED");
         var1.setVersion(new Version((String)var3.get("ver")));
      }

      return var1;
   }

   public static VersionData getUpdateVersion(VersionData var0) {
      try {
         Vector var1 = new Vector((Vector)PropertyList.getInstance().get("upg.cache"));
         if (var1 == null || var1.isEmpty()) {
            return null;
         }

         for(int var2 = 0; var2 < var1.size(); ++var2) {
            if (((VersionData)var1.get(var2)).greaterThan(var0)) {
               return (VersionData)var1.get(var2);
            }
         }
      } catch (Exception var3) {
         UpgradeLogger.getInstance().log("A frissitesi gyorsadattar nem elerheto. A betoltes alatt allo nyomtatvanybol lehet ujabb verzio!");
      }

      return null;
   }

   class InstallListener implements ComponentProcessingEventListener {
      private boolean success = true;

      public void componentProcessed(ComponentProcessingEvent var1) {
         if (var1.getState() == 1) {
            this.success = false;
         }

      }

      public boolean isSuccess() {
         return this.success;
      }
   }
}
