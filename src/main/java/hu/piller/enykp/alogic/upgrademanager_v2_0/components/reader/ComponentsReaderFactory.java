package hu.piller.enykp.alogic.upgrademanager_v2_0.components.reader;

import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import java.util.Vector;

public class ComponentsReaderFactory {
   public static ComponentsReader getInstalledComponentsReader() {
      return new InstalledComponentsReader();
   }

   public static ComponentsReader getDownloadableComponentsReader() {
      return getDownloadableComponentsReader((String[])null);
   }

   public static ComponentsReader getDownloadableComponentsReader(String[] var0) {
      String var1 = null;

      try {
         var1 = (String)((Vector)PropertyList.getInstance().get("prop.const.mode")).get(0);
      } catch (Exception var4) {
         Tools.eLog(var4, 0);
      }

      Object var2;
      if (var1 != null && "offline".equalsIgnoreCase(var1)) {
         var2 = new OfflineDownloadableComponentsReader();
      } else {
         var2 = new OnlineDownloadableComponentsReader();
      }

      ((ComponentsReader)var2).setOrgs(var0);
      return (ComponentsReader)var2;
   }
}
