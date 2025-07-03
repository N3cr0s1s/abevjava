package hu.piller.enykp.alogic.settingspanel;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileOutputStream;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

public class SettingsStore {
   public static final String TABLE_GUI = "gui";
   public static final String TABLE_ORIGINAL_GUI = "original_gui";
   public static final String TABLE_ORIGINAL_OPENPANEL = "original_filepanel_open";
   public static final String TABLE_ORIGINAL_NEWPANEL = "original_filepanel_new";
   public static final String TABLE_UBEVGUI = "ubevgui";
   public static final String TABLE_NEWPANEL = "abevnewpanel";
   public static final String TABLE_OPENPANEL = "abevopenpanel";
   public static final String TABLE_PRINTER = "printer";
   public static final String TABLE_KR = "kr";
   public static final String TABLE_UPGRADE = "upgrade";
   public static final String TABLE_FILE_MASZK = "file_maszk";
   public static final String TABLE_FILE_ATTACHMENTS = "attachments";
   public static final String TABLE_USER_PATHS = "userpaths";
   private static final String ERR_FILE_READ = "Hiba a paraméter állomány betöltése során";
   private static final String MSG_FILE_CREATE = "Az állomány törlésre és újra létrehozásra kerül: ";
   private static final String ERR_FILE_WRITE = "Hiba a paraméter állomány mentése során";
   private static final String ERR_PAR_SETTING = "Hiba a paraméterek beállítása során";
   private static SettingsStore instance;
   private static final String CONST_FILE_INFO = "AbevJava beállítások";
   public static final String SETTINGS_FILE = "settings.enyk";
   private File settingsFile;
   Hashtable store;

   public static SettingsStore getInstance() {
      if (instance == null) {
         instance = new SettingsStore();
      }

      return instance;
   }

   private SettingsStore() {
      this.load();
   }

   public void load() {
      boolean var1 = false;
      this.store = new Hashtable();
      IPropertyList var2 = PropertyList.getInstance();
      this.settingsFile = new NecroFile((new NecroFile((String)var2.get("prop.usr.root"), (String)var2.get("prop.usr.settings"))).getAbsolutePath(), "settings.enyk");

      try {
         Properties var3 = this.loadPropertyFile(this.settingsFile);

         String var6;
         String var7;
         String var8;
         for(Enumeration var13 = var3.propertyNames(); var13.hasMoreElements(); this.set(var6, var7, var8)) {
            String var5 = (String)var13.nextElement();
            var6 = this.getTable(var5);
            var7 = this.getKey(var5);
            var8 = (String)var3.get(var5);

            try {
               if (var5.equals("gui.kaubrowser") && (var8.equalsIgnoreCase("true") || var8.equalsIgnoreCase("false") || var8.equalsIgnoreCase("KAU_UK"))) {
                  var8 = "KAU_UKP";
                  var1 = true;
               }
            } catch (Exception var11) {
            }
         }
      } catch (Exception var12) {
         Exception var4 = var12;

         try {
            ErrorList.getInstance().writeError(new Integer(12007), "Hiba a paraméter állomány betöltése során", var4, this.settingsFile.getAbsolutePath());
            GuiUtil.showMessageDialog((Component)null, "Az állomány törlésre és újra létrehozásra kerül: \n" + this.settingsFile.getAbsolutePath(), "Hiba a paraméter állomány betöltése során", 1);
            this.settingsFile.delete();
         } catch (Exception var10) {
            Tools.eLog(var10, 1);
         }
      }

      if (var1) {
         this.save();
      }

   }

   private String getTable(String var1) {
      return var1.substring(0, var1.indexOf("."));
   }

   private String getKey(String var1) {
      return var1.substring(var1.indexOf(".") + 1, var1.length());
   }

   private Properties loadPropertyFile(File var1) throws IOException {
      FileInputStream var2 = new FileInputStream(var1);
      Properties var3 = new Properties();
      var3.load(var2);
      var2.close();
      return var3;
   }

   public int list() {
      int var1 = 0;
      Enumeration var2 = this.store.keys();

      while(var2.hasMoreElements()) {
         String var3 = (String)var2.nextElement();
         Hashtable var4 = (Hashtable)this.store.get(var3);

         for(Enumeration var5 = var4.keys(); var5.hasMoreElements(); ++var1) {
            String var6 = (String)var5.nextElement();
         }
      }

      return var1;
   }

   public boolean save() {
      System.out.println("settings saved");

      try {
         Properties var1 = new Properties();
         Enumeration var2 = this.store.keys();

         while(var2.hasMoreElements()) {
            String var3 = (String)var2.nextElement();
            Hashtable var4 = (Hashtable)this.store.get(var3);
            Enumeration var5 = var4.keys();

            while(var5.hasMoreElements()) {
               String var6 = (String)var5.nextElement();
               String var7 = var3 + "." + var6;
               var1.put(var7, var4.get(var6));
            }
         }

         FileOutputStream var9 = new NecroFileOutputStream(this.settingsFile);
         var1.store(var9, "AbevJava beállítások");
         return true;
      } catch (IOException var8) {
         ErrorList.getInstance().writeError(new Integer(12006), "Hiba a paraméter állomány mentése során", var8, this.settingsFile.getAbsolutePath());
         return false;
      }
   }

   public String get(String var1, String var2) {
      Hashtable var3 = (Hashtable)this.store.get(var1);
      return var3 == null ? null : (String)var3.get(var2);
   }

   public Hashtable get(String var1) {
      return (Hashtable)this.store.get(var1);
   }

   public boolean set(String var1, Hashtable var2) {
      this.store.put(var1, var2);
      return true;
   }

   public boolean set(String var1, String var2, String var3) {
      if (var1 != null && var2 != null && var1.length() != 0 && var2.length() != 0) {
         String var4 = var3;
         if (var3 == null) {
            var4 = "";
         }

         if (var2.equals("kaubrowser")) {
            System.out.println("authmethod set to: " + var3);
         }

         Hashtable var5 = (Hashtable)this.store.get(var1);
         if (var5 == null) {
            var5 = new Hashtable();
            this.store.put(var1, var5);
         }

         if (!var2.endsWith("xPos") && (!var1.equals("gui") || !var2.equals("x"))) {
            if (var2.endsWith("yPos") || var1.equals("gui") && var2.equals("y")) {
               try {
                  if (!MainFrame.visible(0, Integer.valueOf(var4))) {
                     System.out.println("az ablak kilógna a látható területről, y koord.: 0");
                     var4 = "0";
                  }
               } catch (Exception var7) {
                  var4 = "0";
               }
            }
         } else {
            try {
               if (!MainFrame.visible(Integer.valueOf(var4), 0)) {
                  System.out.println("az ablak kilógna a látható területről, x koord.: 0");
                  var4 = "0";
               }
            } catch (Exception var8) {
               var4 = "0";
            }
         }

         var5.put(var2, var4);
         return true;
      } else {
         ErrorList.getInstance().writeError(new Integer(12008), "Hiba a paraméterek beállítása során", (Exception)null, "tábla:" + var1 + " kulcs:" + var2);
         return false;
      }
   }

   public Hashtable remove(String var1) {
      return (Hashtable)this.store.remove(var1);
   }
}
