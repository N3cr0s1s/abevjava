package hu.piller.enykp.alogic.fileutil;

import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.errordialog.ErrorDialog;
import me.necrocore.abevjava.NecroFile;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Vector;

public class DeleteInPublicMode {
   private static DeleteInPublicMode ourInstance = new DeleteInPublicMode();
   private DeleteInPublicMode.ExitFileFilter eff;
   private String[] dirs;

   public static DeleteInPublicMode getInstance() {
      return ourInstance;
   }

   private DeleteInPublicMode() {
      try {
         String var1 = Tools.fullService("prop.usr.root", true);
         String var2 = var1 + Tools.fullService("prop.usr.import", false);
         String var3 = var1 + Tools.fullService("prop.usr.saves", false);
         String var4 = var1 + Tools.fullService("prop.usr.archive", false);
         String var5 = var1 + Tools.fullService("prop.usr.attachment", false);
         String var6 = Tools.fullService("prop.usr.krdir", true);
         String var7 = var6 + Tools.fullService("prop.usr.ds_src", false);
         String var8 = var6 + Tools.fullService("prop.usr.ds_sent", false);
         String var9 = var6 + Tools.fullService("prop.usr.ds_dest", false);
         String var10 = Tools.fullService("prop.usr.primaryaccounts", true);
         String var11 = Tools.fullService("prop.usr.naplo", true);
         String var12 = var1 + Tools.fullService("prop.usr.tmp", false);
         String var13 = var1 + Tools.fullService("prop.usr.kontroll", false);
         String var14 = SettingsStore.getInstance().get("printer", "print.settings.pdfdir");
         if (var14 == null) {
            var14 = (String)PropertyList.getInstance().get("prop.usr.naplo");
         }

         try {
            if (SettingsStore.getInstance().get("gui", "digitális_aláírás") != null && !SettingsStore.getInstance().get("gui", "digitális_aláírás").equals("")) {
               var7 = Tools.fillPath(SettingsStore.getInstance().get("gui", "digitális_aláírás"));
            }
         } catch (Exception var16) {
            var7 = var6 + Tools.fullService("prop.usr.ds_src", false);
         }

         this.dirs = new String[]{var3, var4, var10, var2, var5, var6, var11, var14, var12, var13, var7, var8, var9};
         this.eff = new DeleteInPublicMode.ExitFileFilter();
      } catch (Exception var17) {
         ourInstance = null;
         var17.printStackTrace();
      }

   }

   public boolean exit(boolean var1) {
      Vector var2 = new Vector();
      Result var3 = new Result();

      for(int var4 = 0; var4 < this.dirs.length; ++var4) {
         File var5 = new NecroFile(this.dirs[var4]);
         this.eff.type = var4;
         File[] var6 = var5.listFiles(this.eff);

         try {
            if (var1) {
               for(int var7 = 0; var7 < var6.length; ++var7) {
                  try {
                     Tools.emptyDir(var6[var7], this.eff, var3);
                  } catch (Exception var11) {
                     Tools.eLog(var11, 0);
                  }
               }
            } else {
               var2.addAll(new Vector(Arrays.asList(var6)));
            }
         } catch (Exception var13) {
            var13.printStackTrace();
            Tools.eLog(var13, 0);
         }
      }

      if (!var1 && var2.size() > 0) {
         var3.errorList.clear();
         var2 = this.delDup(var2);
         ErrorDialog var14 = new ErrorDialog(MainFrame.thisinstance, "Nyilvános mód - adatállományok listája", true, false, var2, "Az alábbi állományokat és mappák tartalmát töröljük!", true);
         if (var14.isProcessStoppped()) {
            return false;
         } else {
            for(int var15 = 0; var15 < this.dirs.length; ++var15) {
               File var16 = new NecroFile(this.dirs[var15]);
               this.eff.type = var15;
               File[] var17 = var16.listFiles(this.eff);

               try {
                  for(int var8 = 0; var8 < var17.length; ++var8) {
                     try {
                        Tools.emptyDir(var17[var8], this.eff, var3);
                     } catch (Exception var10) {
                        Tools.eLog(var10, 0);
                     }
                  }
               } catch (Exception var12) {
                  Tools.eLog(var12, 0);
               }
            }

            if (var3.errorList.size() > 0) {
               var2.clear();
               var2 = this.delDup(var3.errorList);
               new ErrorDialog(MainFrame.thisinstance, "Nyilvános mód - adatállományok listája", true, false, var2, "Az alábbi állományokat nem sikerült törölni! Ha szükséges, gondoskodjon törlésükről!", false);
            }

            return true;
         }
      } else {
         return true;
      }
   }

   private Vector delDup(Vector var1) {
      Vector var2 = new Vector();

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         if (!var2.contains(var1.elementAt(var3))) {
            var2.add(var1.elementAt(var3));
         }
      }

      return var2;
   }

   private class ExitFileFilter implements FilenameFilter {
      public int type;

      private ExitFileFilter() {
      }

      public boolean accept(File var1, String var2) {
         String[] var3 = null;
         String var4 = "";
         switch(this.type) {
         case 0:
            var3 = new String[]{".frm.enyk", ".cst", ".atc"};
            break;
         case 1:
            var3 = new String[]{".frm.enyk"};
            break;
         case 2:
            var3 = new String[]{".xml"};
            break;
         case 3:
            var3 = new String[]{".dat", ".abv", ".kat", ".elk", ".xml", ".imp", ".xkr"};
            break;
         case 4:
            var3 = new String[]{""};
            break;
         case 5:
            var3 = new String[]{".log"};
            break;
         case 6:
            var3 = new String[]{""};
            break;
         case 7:
            var3 = new String[]{""};
            break;
         case 8:
            var3 = new String[]{""};
            break;
         case 9:
            var3 = new String[]{"_0", ".kif", ".txt"};
            break;
         default:
            var3 = new String[]{".kr", ".kat", ".elk", ".mf", ".xml"};
         }

         if (!var2.toLowerCase().startsWith(var4)) {
            return false;
         } else {
            for(int var5 = 0; var5 < var3.length; ++var5) {
               if (var2.toLowerCase().endsWith(var3[var5])) {
                  return true;
               }
            }

            return false;
         }
      }

      // $FF: synthetic method
      ExitFileFilter(Object var2) {
         this();
      }
   }
}
