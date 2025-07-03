package hu.piller.enykp.alogic.settingspanel.printer;

import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import java.util.Hashtable;

public class PrinterSettingsStore {
   private static PrinterSettingsStore instance;
   private static String TABLE_KEY = "printer";

   public static PrinterSettingsStore getInstance() {
      if (instance == null) {
         instance = new PrinterSettingsStore();
      }

      return instance;
   }

   private PrinterSettingsStore() {
   }

   public Hashtable get() {
      return SettingsStore.getInstance().get(TABLE_KEY);
   }

   public String get(String var1) {
      return SettingsStore.getInstance().get(TABLE_KEY, var1);
   }

   public boolean set(Hashtable var1) {
      return SettingsStore.getInstance().set(TABLE_KEY, var1);
   }

   public boolean set(String var1, String var2) {
      return SettingsStore.getInstance().set(TABLE_KEY, var1, var2);
   }
}
