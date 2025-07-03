package hu.piller.enykp.alogic.settingspanel.upgrade.onlineupdate.dao;

import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.alogic.settingspanel.upgrade.onlineupdate.model.OrgsOnlineUpdateStatus;
import hu.piller.enykp.alogic.settingspanel.upgrade.onlineupdate.model.Status;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class OrgsOnlineUpdateStatusSettingsStoreDAO {
   private static final String SETTINGS_TABLE_ID = "table_onlineupdate";
   private static final String SETTINGS_DATA_ID = "serialized";
   private static final String SEPARATOR_DATA = ";";
   private static final String SEPARATOR_ORGVAL = "=";

   public static OrgsOnlineUpdateStatus load() {
      OrgsOnlineUpdateStatus var0 = new OrgsOnlineUpdateStatus();
      if (SettingsStore.getInstance().get("table_onlineupdate") != null) {
         String var1 = (String)SettingsStore.getInstance().get("table_onlineupdate").get("serialized");
         if (var1 != null) {
            Map var2 = deserialize(var1);
            Iterator var3 = var2.entrySet().iterator();

            while(var3.hasNext()) {
               Entry var4 = (Entry)var3.next();
               var0.addOrgStatus((String)var4.getKey(), (Status)var4.getValue());
            }
         }
      }

      return var0;
   }

   public static void save(OrgsOnlineUpdateStatus var0) {
      SettingsStore.getInstance().set("table_onlineupdate", "serialized", serialize(var0.getOrgsOnlineUpdateStatus()));
   }

   private static Map<String, Status> deserialize(String var0) {
      HashMap var1 = new HashMap();
      String[] var2 = var0.split(";");
      if (var2 != null) {
         String[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            String[] var7 = var6.split("=");
            var1.put(var7[0], Status.valueOf(var7[1]));
         }
      }

      return var1;
   }

   private static String serialize(Map<String, Status> var0) {
      StringBuffer var1 = new StringBuffer();
      int var2 = 0;
      Iterator var3 = var0.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         var1.append(String.format("%1$s%2$s%3$s%4$s", var4.getKey(), "=", var4.getValue(), var2++ < var0.size() - 1 ? SEPARATOR_DATA : ""));
      }

      return var1.toString();
   }
}
