package hu.piller.enykp.alogic.primaryaccount;

import hu.piller.enykp.alogic.primaryaccount.common.DefaultEnvelope;
import hu.piller.enykp.alogic.primaryaccount.common.DefaultRecordFactory;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Tools {
   private static final String PRIMARY_ACCOUNTS_FOLDER = "primaryaccounts";
   private static String es;

   public static void checkPAFolderContent(File var0, File var1, String var2, DefaultRecordFactory var3) {
      es = "";
      Object var4;
      if (var0 == null) {
         var4 = PropertyList.getInstance().get("prop.usr.primaryaccounts");
         var0 = var4 == null ? null : new File(var4.toString());
      }

      if (var1 == null) {
         var4 = PropertyList.getInstance().get("prop.sys.root");
         Object var5 = PropertyList.getInstance().get("prop.sys.abev");
         if (var4 != null && var5 != null) {
            var1 = new File(var4.toString(), var5.toString());
            var1 = new File(var1, "primaryaccounts");
         }
      }

      boolean var8 = true;
      int var6 = _checkPAFolderContent(var0, var1, var2, var3);
      String var7;
      switch(var6) {
      case 0:
         var7 = null;
         var8 = false;
         break;
      case 1:
         var7 = "Elégtelen paraméterezés ! ";
         var7 = var7 + "Ellenőrizze a 'prop.usr.primaryaccounts', ";
         var7 = var7 + "'prop.sys.root' és 'prop.sys.abev' paramétereket ";
         var7 = var7 + "a konfigurációban! (" + var0 + "; " + var1;
         var7 = var7 + "; " + var2 + ")";
         break;
      case 2:
         var7 = "Nem talált törzsadat forrást ! ";
         var7 = var7 + "'prop.sys.root', 'prop.sys.abev' és törzsadat állomány ";
         var7 = var7 + "paraméterek megfelelők ? (" + var1 + "; " + var2 + ")";
         break;
      case 3:
         var7 = "Törzsadat állomány másolási hiba !";
         break;
      case 4:
         var7 = "File átnevezési hiba !";
         break;
      case 5:
         var7 = "File törlési hiba !";
         break;
      case 6:
         var7 = "Törzsadat file összeállítási hiba !";
         break;
      default:
         var7 = "Egyéb hiba ...";
      }

      if (var8) {
         ErrorList.getInstance().writeError("TA", "Törzsadat eszköz: " + var7 + " (" + es + ")", (Exception)null, (Object)null);
      }

   }

   private static int _checkPAFolderContent(File var0, File var1, String var2, DefaultRecordFactory var3) {
      if (var0 != null && var1 != null && var2 != null) {
         File var4 = new File(var1, var2);
         File var5 = new File(var0, var2);
         int var6 = 0;
         if (!var5.exists() && var4.exists() && !var5.equals(var4)) {
            if (!var4.exists()) {
               es = var4.toString();
               var6 = 2;
            }

            if (var6 == 0) {
               if (var5.exists()) {
                  File var7 = new File(var0, var2 + ".old");
                  File var8 = new File(var5.getParent(), var2 + ".ori");
                  if (var7.exists()) {
                     deleteFile(var7);
                  }

                  if (var8.exists()) {
                     deleteFile(var8);
                  }

                  var7.getParentFile().mkdirs();
                  var6 = copyFile(var4, var7);
                  if (var6 == 0) {
                     var6 = renameFile(var5, var8);
                  }

                  if (var6 == 0) {
                     var6 = compositeFile(var5, var8, var7, var3);
                  }

                  if (var6 == 0) {
                     deleteFile(var8);
                     deleteFile(var7);
                  }
               } else {
                  var5.getParentFile().mkdirs();
                  var6 = copyFile(var4, var5);
               }
            }

            if (var6 == 0) {
            }
         }

         return var6;
      } else {
         return 1;
      }
   }

   private static int compositeFile(File var0, File var1, File var2, DefaultRecordFactory var3) {
      byte var4 = 0;
      if (var3 != null) {
         try {
            var3.loadRecords((File)var1, (DefaultEnvelope)null);
            var3.appendRecords(var2, (DefaultEnvelope)null);
            var3.saveRecords(var0);
         } catch (Exception var6) {
            es = "Hiba az összeállítás során: " + var6.toString();
            var4 = 6;
         }
      } else {
         es = "Összeállító modul hiányzik";
         var4 = 6;
      }

      return var4;
   }

   private static int copyFile(File var0, File var1) {
      FileInputStream var2 = null;
      FileOutputStream var3 = null;
      byte var4 = 0;

      try {
         var2 = new FileInputStream(var0);
         var3 = new FileOutputStream(var1);
         byte[] var5 = new byte[4096];

         int var6;
         while((var6 = var2.read(var5)) > 0) {
            var3.write(var5, 0, var6);
         }

         var3.close();
         var3 = null;
         var2.close();
         var2 = null;
         es = "";
      } catch (Exception var9) {
         System.out.println("" + var9);
         if (var3 != null) {
            try {
               var3.close();
            } catch (IOException var8) {
               hu.piller.enykp.util.base.Tools.eLog(var9, 0);
            }
         }

         if (var2 != null) {
            try {
               var2.close();
            } catch (IOException var7) {
               hu.piller.enykp.util.base.Tools.eLog(var9, 0);
            }
         }

         es = var0 + " -> " + var1;
         var4 = 3;
      }

      return var4;
   }

   private static int deleteFile(File var0) {
      System.gc();

      try {
         Thread.sleep(500L);
      } catch (InterruptedException var2) {
      }

      boolean var1 = var0.delete();
      es = var1 ? "" : var0.toString();
      return var1 ? 0 : 5;
   }

   private static int renameFile(File var0, File var1) {
      System.gc();

      try {
         Thread.sleep(500L);
      } catch (InterruptedException var3) {
      }

      boolean var2 = var0.renameTo(var1);
      es = var2 ? "" : var0 + " -> " + var1;
      return var2 ? 0 : 4;
   }
}
