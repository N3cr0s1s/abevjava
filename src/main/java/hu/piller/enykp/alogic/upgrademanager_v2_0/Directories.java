package hu.piller.enykp.alogic.upgrademanager_v2_0;

import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.util.base.PropertyList;
import java.io.File;

public class Directories {
   private static String replaceBackslash(String var0) {
      return var0 != null && var0.trim().length() != 0 ? var0.replaceAll("\\\\", "/") : var0;
   }

   public static String getAbevrootPath() {
      return replaceBackslash((String)PropertyList.getInstance().get("prop.sys.root"));
   }

   public static String getTemplatesPath() {
      return getAbevrootPath() + "/nyomtatvanyok";
   }

   public static String getHelpsPath() {
      return getAbevrootPath() + "/segitseg";
   }

   public static String getOrgresourcesPath() {
      return getAbevrootPath() + "/eroforrasok";
   }

   public static String getSchemasPath() {
      return getAbevrootPath() + "/xsd";
   }

   public static String getUpgradePath() {
      return getAbevrootPath() + "/upgrade";
   }

   public static String getAbevPath() {
      return getAbevrootPath() + "/abev";
   }

   public static String getNaploPath() {
      return replaceBackslash((String)PropertyList.getInstance().get("prop.usr.naplo"));
   }

   public static String getDownloadPath() {
      String var0 = SettingsStore.getInstance().get("upgrade", "download");
      if (var0 == null || "".equals(var0)) {
         var0 = (String)PropertyList.getInstance().get("prop.usr.frissitesek");
      }

      return replaceBackslash(var0);
   }

   public static String getSettingsPath() {
      return replaceBackslash((String)PropertyList.getInstance().get("prop.usr.root")) + File.separator + (String)PropertyList.getInstance().get("prop.usr.settings");
   }
}
