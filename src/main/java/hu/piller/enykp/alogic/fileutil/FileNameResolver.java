package hu.piller.enykp.alogic.fileutil;

import hu.piller.enykp.alogic.settingspanel.SettingsPanel;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.PropertyList;
import java.io.File;

public class FileNameResolver {
   public static final String MASK_FORM_ID = "nyomtatvány azonosító";
   public static final String MASK_TAX_ID = "adószám vagy adóazonosító jel";
   public static final String MASK_NAME_ID = "név (cégnév vagy személynév)";
   public static final String MASK_DATE_FROM_ID = "bevallási időszak kezdete";
   public static final String MASK_DATE_TO_ID = "bevallási időszak vége";
   public static final String MASK_HK_ID = "Hivatali kapu azonosító";
   public static final String INVALID_FILE_CHARS = "\\/:*?\"<>|,;";
   private static final String VALID_SPEC_CHARS = "áéíóöőúüűÁÉÍÓÖŐÚÜŰ";
   private static final String CHANGE_FILE_CHARS = "őöűüŐÖŰÜ";
   public static final String EKEZETES_FILE_CHARS = "áéíóöőúüűÁÉÍÓÖŐÚÜŰ";
   public static final String EKEZETLEN_FILE_CHARS = "aeiooouuuAEIOOOUUU";
   BookModel gui_info;
   String sourceFilename;

   public FileNameResolver(BookModel var1) {
      this.gui_info = var1;
      this.sourceFilename = null;
   }

   public FileNameResolver(BookModel var1, String var2) {
      this.gui_info = var1;

      try {
         if (this.checkOriginalFilenameBySettings()) {
            File var3 = new File(var2);
            if (!var3.exists()) {
               this.sourceFilename = null;
            } else {
               this.sourceFilename = var3.getName();
               int var4 = this.sourceFilename.lastIndexOf(".");
               if (var4 > -1) {
                  this.sourceFilename = this.sourceFilename.substring(0, var4);
               }
            }
         } else {
            this.sourceFilename = null;
         }
      } catch (Exception var5) {
         this.sourceFilename = null;
      }

   }

   public String generateFileName() {
      String var1;
      if (this.sourceFilename != null) {
         var1 = this.sourceFilename;
      } else {
         var1 = this.getFileMask();
      }

      String var2 = this.normalizeString(var1);
      String var3 = "" + System.nanoTime();
      String var4 = var2 + (var3.length() > 0 ? (var2.length() > 0 ? "_" : "") + var3 : "");
      System.out.println("Generált állománynév = " + var4);
      return var4;
   }

   public String normalizeString(String var1) {
      var1 = this.removeInvalidFileChars(var1);
      int var5 = 0;

      char var2;
      int var6;
      for(var6 = "őöűüŐÖŰÜ".length(); var5 < var6; var5 += 2) {
         var2 = "őöűüŐÖŰÜ".charAt(var5);
         char var3;
         if (var5 + 1 < var6) {
            var3 = "őöűüŐÖŰÜ".charAt(var5 + 1);
         } else {
            var3 = '_';
         }

         var1 = var1.replace(var2, var3);
      }

      char var7 = '_';
      var5 = 0;

      for(var6 = var1.length(); var5 < var6; ++var5) {
         var2 = var1.charAt(var5);
         boolean var4 = var2 >= '0' && var2 <= '9' || var2 >= 'a' && var2 <= 'z' || var2 >= 'A' && var2 <= 'Z' || "áéíóöőúüűÁÉÍÓÖŐÚÜŰ".indexOf(var2) > -1;
         if (!var4) {
            var1 = var1.replace(var2, var7);
         }
      }

      return var1;
   }

   public String getFileMask() {
      String var1 = "";
      IPropertyList var2 = this.getMasterPropertyList();
      if (var2 != null) {
         IDataStore var3 = this.gui_info.get_main_document();
         if (var3 == null) {
            var3 = this.gui_info.get_datastore();
         }

         Object var4 = HeadChecker.getInstance().getHeadData(this.gui_info.main_document_id, var3);
         if (var4 instanceof Object[]) {
            Object[] var5 = (Object[])((Object[])var4);
            String var6 = (new SettingsPanel()).getFileMask();
            String[] var7 = var6.split("#");
            int var10 = 0;

            for(int var11 = var7.length; var10 < var11; ++var10) {
               String var8 = var7[var10];
               String var9 = "";
               if (var8.equalsIgnoreCase("nyomtatvány azonosító")) {
                  var9 = this.gui_info.main_document_id;
               } else if (var8.equalsIgnoreCase("adószám vagy adóazonosító jel")) {
                  String var12 = this.getString(var5[1]);
                  String var13 = this.getString(var5[2]);
                  if (var12.length() > 0) {
                     var9 = var12;
                  } else {
                     var9 = var13;
                  }
               } else if (var8.equalsIgnoreCase("név (cégnév vagy személynév)")) {
                  var9 = this.getString(var5[3]);
               } else if (var8.equalsIgnoreCase("bevallási időszak kezdete")) {
                  var9 = this.getString(var5[4]);
               } else if (var8.equalsIgnoreCase("bevallási időszak vége")) {
                  var9 = this.getString(var5[5]);
               } else if (var8.equalsIgnoreCase("Hivatali kapu azonosító")) {
                  var9 = this.getString(var5[15]);
               }

               var9 = var9.trim();
               var1 = var1 + (var9.length() > 0 && var1.length() > 0 ? "_" : "");
               var1 = var1 + var9;
            }

            var1 = this.removeInvalidFileChars(var1);
         }
      }

      return var1;
   }

   public String removeInvalidFileChars(String var1) {
      if (var1 == null) {
         return null;
      } else {
         StringBuffer var2 = new StringBuffer(var1.length());
         int var3 = 0;

         for(int var4 = var1.length(); var3 < var4; ++var3) {
            char var5 = var1.charAt(var3);
            if ("\\/:*?\"<>|,;".indexOf(var5) <= -1) {
               var2.append(var5);
            }
         }

         return var2.toString();
      }
   }

   private IPropertyList getMasterPropertyList() {
      return PropertyList.getInstance();
   }

   private String getString(Object var1) {
      return var1 == null ? "" : var1.toString();
   }

   public static String ektelen(String var0) {
      if (var0 == null) {
         return null;
      } else {
         StringBuffer var1 = new StringBuffer(var0.length());
         int var2 = 0;

         for(int var3 = var0.length(); var2 < var3; ++var2) {
            char var4 = var0.charAt(var2);
            int var5 = "áéíóöőúüűÁÉÍÓÖŐÚÜŰ".indexOf(var4);
            if (var5 > -1) {
               var1.append("aeiooouuuAEIOOOUUU".charAt(var5));
            } else {
               var1.append(var4);
            }
         }

         return var1.toString();
      }
   }

   private static String ektelen_and_remove_invalid(String var0) {
      if (var0 == null) {
         return null;
      } else {
         StringBuffer var1 = new StringBuffer(var0.length());
         int var2 = 0;

         for(int var3 = var0.length(); var2 < var3; ++var2) {
            char var4 = var0.charAt(var2);
            if ("\\/:*?\"<>|,;".indexOf(var4) <= -1) {
               var1.append(var4);
            }
         }

         return ektelen(var1.toString());
      }
   }

   private boolean checkOriginalFilenameBySettings() {
      try {
         return Boolean.valueOf(SettingsStore.getInstance().get("file_maszk", "import_use_original_filename"));
      } catch (Exception var2) {
         return false;
      }
   }
}
