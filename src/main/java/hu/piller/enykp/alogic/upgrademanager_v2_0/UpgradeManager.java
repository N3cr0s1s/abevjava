package hu.piller.enykp.alogic.upgrademanager_v2_0;

import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.orghandler.SuccessorException;
import hu.piller.enykp.alogic.settingspanel.upgrade.UpgradeFunction;
import hu.piller.enykp.alogic.settingspanel.upgrade.onlineupdate.dao.OrgsOnlineUpdateStatusSettingsStoreDAO;
import hu.piller.enykp.alogic.settingspanel.upgrade.onlineupdate.model.OrgsOnlineUpdateStatus;
import hu.piller.enykp.alogic.upgrademanager_v2_0.components.DownloadableComponents;
import hu.piller.enykp.alogic.upgrademanager_v2_0.components.NewComponents;
import hu.piller.enykp.alogic.upgrademanager_v2_0.components.UpgradableComponents;
import hu.piller.enykp.alogic.upgrademanager_v2_0.gui.JAutoUpdateInfoDialog;
import hu.piller.enykp.alogic.upgrademanager_v2_0.lookupupgrades.ILookUpEventListener;
import hu.piller.enykp.alogic.upgrademanager_v2_0.lookupupgrades.LookUpUpgrades;
import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataconverters.VersionData;
import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataproviders.installedcomponents.TemplateVersionDataProvider;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Version;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.SwingUtilities;

public class UpgradeManager {
   public static final String MODULID = "Frissítés";
   public static final String UPGRADEABLE_CACHE = "upg.cache";
   public static final String REVOKED_CACHE = "revoked.cache";
   private static Thread cacheBuilderThread;

   public static void buildCacheAndNotifyWhenHasUpgrade(ILookUpEventListener var0) {
      LookUpUpgrades var1 = new LookUpUpgrades();
      var1.addLookUpEventListener(var0);
      cacheBuilderThread = new Thread(var1);
      cacheBuilderThread.setName("BG_CACHE_BUILDER");
      cacheBuilderThread.start();
      UpgradeLogger.getInstance().log(Thread.currentThread().getName() + " ### UpgradeManager : frissített és visszavont összetevőket nyilvántartó gyorstár építése elindítva");
   }

   public static void interruptLookUpUpgardes() {
      String var0 = String.format("%1$s ### UpgradeManager : cacheBuilder is null -> %2$s cacheBuilder is alive -> %3$s", Thread.currentThread().getName(), cacheBuilderThread == null, cacheBuilderThread.isAlive());
      UpgradeLogger.getInstance().log(var0);
      if (cacheBuilderThread != null && cacheBuilderThread.isAlive()) {
         cacheBuilderThread.interrupt();
         UpgradeLogger.getInstance().log(Thread.currentThread().getName() + " ### UpgradeManager : frissített és visszavont összetevőket nyilvántartó gyorstár építésének megszakítása folyamatban...");
         long var1 = System.currentTimeMillis();

         try {
            cacheBuilderThread.join();
         } catch (InterruptedException var5) {
            var5.printStackTrace();
         }

         long var3 = System.currentTimeMillis();
         UpgradeLogger.getInstance().log(Thread.currentThread().getName() + " ### UpgradeManager : frissített és visszavont összetevőket nyilvántartó gyorstár építésének megszakítása kész " + (var3 - var1) + " msec alatt");
      }

      SwingUtilities.invokeLater(() -> {
         MainFrame.thisinstance.mp.removeFrissitesMessage();
         MainFrame.thisinstance.mp.updateUI();
      });
      PropertyList.getInstance().set("upg.cache", new Vector());
      PropertyList.getInstance().set("revoked.cache", new Vector());
      PropertyList.getInstance().set("orgs_notconnected.cache", (Object)null);
   }

   public static void waitForVersionDataCacheReady() throws InterruptedException {
   }

   private static boolean isOnlineUpdateEnabled(String var0) throws SuccessorException {
      String var1 = var0;
      if (OrgInfo.getInstance().hasSuccessor(var0)) {
         var1 = OrgInfo.getInstance().getSuccessorOrgId(var0);
      }

      String[] var2 = OrgInfo.getInstance().getOrgIdsOfOrgsWithOnlineUpdate();
      OrgsOnlineUpdateStatus var3 = OrgsOnlineUpdateStatusSettingsStoreDAO.load();
      var3.merge(var2);
      return var3.getOrgsWithEnabledOnlineUpdate().contains(var1);
   }

   public static synchronized String checkUpgrade(String var0, UpgradeFunction var1, String var2) {
      UpgradeLogger.getInstance().log(Thread.currentThread().getName() + " ### UpgradeManager : A(z)" + var2 + " szervezet " + var0 + " sablonfájl legfrisebb verzió szolgáltatása");

      try {
         if (isOnlineUpdateEnabled(var2)) {
            waitForVersionDataCacheReady();
         }
      } catch (Exception var4) {
         UpgradeLogger.getInstance().log(var4);
      }

      return RuntimeUpgrade.getInstance().checkUpgrade(var0, var1);
   }

   public static synchronized VersionData getUpdateableVersionData(BookModel var0) throws Exception {
      if (var0 != null && var0.docinfo != null) {
         VersionData var1 = new VersionData();
         var1.setName((String)var0.docinfo.get("id"));
         var1.setOrganization((String)var0.docinfo.get("org"));
         var1.setVersion(new Version((String)var0.docinfo.get("ver")));
         var1.setCategory("Template");
         UpgradeLogger.getInstance().log(Thread.currentThread().getName() + " ### UpgradeManager : A(z)" + var1.getOrganization() + " szervezet " + var1.getName() + " sablon verzióinformációinak szolgáltatása");
         if (isOnlineUpdateEnabled(var1.getOrganization())) {
            waitForVersionDataCacheReady();
         }

         return RuntimeUpgrade.getUpdateVersion(var1);
      } else {
         throw new Exception("Frissítési információk ellenőrzéséhez szükség van egy inicializált BookModel-re!");
      }
   }

   public static synchronized Hatalyossag isOperative(BookModel var0) throws Exception {
      UpgradeLogger.getInstance().log(Thread.currentThread().getName() + " ### UpgradeManager : A(z)" + var0.getPublisherOrgId() + " szervezet " + var0.docinfo.get("id") + " sablon hatályosság szolgáltatása");
      Hatalyossag var1 = Hatalyossag.HATALYOS;
      if (isOnlineUpdateEnabled(var0.getPublisherOrgId())) {
         waitForVersionDataCacheReady();
      }

      if (var0.isCheckValid()) {
         if (isOrganizationAvailableOnline(var0.getPublisherOrgId())) {
            var1 = RuntimeUpgrade.getInstance().isOperative(var0) ? Hatalyossag.HATALYOS : Hatalyossag.NEM_HATALYOS;
         } else {
            var1 = Hatalyossag.NEM_MEGALLAPITHATO;
         }
      }

      UpgradeLogger.getInstance().log(Thread.currentThread().getName() + " ### UpgradeManager : A(z)" + var0.getPublisherOrgId() + " szervezet " + var0.docinfo.get("id") + " sablon " + var1.toString());
      return var1;
   }

   private static boolean isOrganizationAvailableOnline(String var0) {
      String[] var1 = (String[])((String[])PropertyList.getInstance().get("orgs_notconnected.cache"));
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var0.equals(var1[var2])) {
               return false;
            }
         }

         return true;
      } else {
         UpgradeLogger.getInstance().log(Thread.currentThread().getName() + " ### UpgradeManager : " + var0 + " szervezetről nem lehetett megállapítani, hogy csatlakozik-e");
         return false;
      }
   }

   public static void downloadTemplate(String var0, String var1, String var2) throws UpgradeBusinessException, UpgradeTechnicalException {
      VersionData var3 = new VersionData();
      var3.setOrganization(var0);
      var3.setCategory("Template");
      var3.setSourceCategory("Filesystem");
      var3.setName(var1);
      var3.setVersion(new Version(var2));
      TemplateVersionDataProvider var4 = new TemplateVersionDataProvider();
      var4.collect();
      Iterator var5 = var4.getCollection().iterator();
      Version var6 = new Version("0.0");
      boolean var7 = false;

      while(var5.hasNext()) {
         VersionData var8 = (VersionData)var5.next();
         if (var3.getOrganization().equals(var8.getOrganization()) && var3.getName().equals(var8.getName()) && var3.getCategory().equals(var8.getCategory())) {
            if (!var7) {
               var7 = true;
            }

            if (var8.getVersion().compareTo(var6) > 0) {
               var6 = var8.getVersion();
            }
         }
      }

      if (!var7 || var6.compareTo(var3.getVersion()) < 0) {
         var7 = false;
         DownloadableComponents var12 = new DownloadableComponents(new String[]{var0});
         if (var12.getOrgsNotConnected().length > 0) {
            throw new UpgradeTechnicalException("A(z) " + var0 + " szervezethez nem lehet kapcsolódni. Kérem, nézze meg a hibalistát.");
         } else {
            NewComponents var9 = new NewComponents(var12);
            var5 = var9.getComponents().iterator();
            VersionData var10 = null;

            while(var5.hasNext()) {
               var10 = (VersionData)var5.next();
               if (var10.equals(var3) || var10.greaterThan(var3)) {
                  var7 = true;
                  break;
               }
            }

            if (!var7) {
               String var13 = String.format("Jelenleg a(z) %1$s szervezet nem publikál a(z) %2$s nyomtatványból legalább %3$s verziószámút!", var0, var1, var2);
               throw new UpgradeBusinessException(var13);
            } else {
               ArrayList var11 = new ArrayList();
               var11.add(var10);
               (new DownloadableComponents(var11)).install();
            }
         }
      }
   }

   public static UpgradableComponents getUpgradableComponents() {
      return new UpgradableComponents();
   }

   public static NewComponents getNewAndUpgradableComponents() {
      return new NewComponents();
   }

   public static DownloadableComponents getDonwloadableComponentsToReplicate() throws UpgradeBusinessException {
      String var0 = null;

      try {
         var0 = (String)((Vector)PropertyList.getInstance().get("prop.const.mode")).get(0);
      } catch (Exception var2) {
         UpgradeLogger.getInstance().log(" ### UpgradeManager : üzemmód konfigurációs paraméter nem található, online üzemmód aktiválása");
         ErrorList.getInstance().writeError("Frissítés", "Frissítés: üzemmód paraméter konfigurációs nem található, online üzemmód aktiválása", IErrorList.LEVEL_WARNING, var2, (Object)null);
      }

      if (var0 != null && "offline".equalsIgnoreCase(var0)) {
         throw new UpgradeBusinessException("Off-line üzemmódban a tükrözés nem támogatott");
      } else {
         return new DownloadableComponents();
      }
   }

   // $FF: synthetic method
   private static void lambda$waitForVersionDataCacheReady$13(JAutoUpdateInfoDialog var0) {
      var0.setVisible(false);
      var0.dispose();
   }

   // $FF: synthetic method
   private static void lambda$waitForVersionDataCacheReady$12(JAutoUpdateInfoDialog var0) {
      var0.setVisible(true);
   }
}
