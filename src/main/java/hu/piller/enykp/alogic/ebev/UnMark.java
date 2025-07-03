package hu.piller.enykp.alogic.ebev;

import hu.piller.enykp.alogic.filesaver.enykinner.EnykInnerSaver;
import hu.piller.enykp.alogic.fileutil.FileStatusChecker;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.framework.MainPanel;
import hu.piller.enykp.gui.framework.Menubar;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import me.necrocore.abevjava.NecroFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import javax.swing.JDialog;

public class UnMark {
   private BookModel bm;
   private SendParams sp;

   public UnMark(BookModel var1, SendParams var2) {
      this.bm = var1;
      this.sp = var2;
   }

   public Result doUnmark(boolean var1) {
      Result var2 = new Result();

      try {
         File var3 = this.bm.cc.getLoadedfile();
         if (var3 == null) {
            throw new FileNotFoundException("A fájl nincs elmentve.");
         }

         if (!var3.exists()) {
            throw new FileNotFoundException("A fájl nincs elmentve.");
         }

         FileStatusChecker var4 = FileStatusChecker.getInstance();

         try {
            var4.set((Object)null, this.bm.splitesaver.equalsIgnoreCase("true"));
         } catch (Exception var20) {
            var4.set((Object)null, false);
         }

         int var5 = var4.getStatus(var3.getAbsolutePath(), (String)((String)(this.bm.cc.docinfo.containsKey("krfilename") ? this.bm.cc.docinfo.get("krfilename") : "")));
         String var6 = "";
         if (this.bm.cc.docinfo.containsKey("krfilename")) {
            var6 = (String)this.bm.cc.docinfo.get("krfilename");
         }

         File var7;
         if (var6.equals("")) {
            var7 = new NecroFile(this.sp.destPath + PropertyList.USER_IN_FILENAME + (var3.getName().toLowerCase().endsWith(".frm.enyk") ? var3.getName().substring(0, var3.getName().toLowerCase().indexOf(".frm.enyk")) + ".kr" : var3.getName() + ".kr"));
         } else {
            var7 = new NecroFile(this.sp.destPath + PropertyList.USER_IN_FILENAME + var6);
         }

         boolean var8 = false;
         if (var5 != 1) {
            if (var5 == 2) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "A nyomtatványt már feladta elektronikusan, a visszavonás nem lehetséges.", "Üzenet", 1);
               var2.setOk(false);
               var2.errorList.add("A nyomtatványt már feladta elektronikusan, a visszavonás nem lehetséges.");
               return var2;
            }

            GuiUtil.showMessageDialog(MainFrame.thisinstance, "A nyomtatvány nem is volt küldésre megjelölt.", "Üzenet", 1);
            var2.setOk(false);
            var2.errorList.add("A nyomtatvány nem is volt küldésre megjelölt.");
            return var2;
         }

         if (var7.exists()) {
            if (!var7.delete()) {
               throw new FileNotFoundException(var7.getAbsolutePath() + " fájl nem létezik.");
            }

            try {
               Result var25 = this.deleteCstFile(this.bm, var1);
               if (!var25.isOk()) {
                  var8 = true;
               }

               var2.errorList.addAll(var25.errorList);
            } catch (Exception var19) {
               Tools.eLog(var19, 0);
            }

            if (var8) {
               EbevTools.showExtendedResultDialog((JDialog)null, Tools.beautyPath(var7.getAbsolutePath()) + "\nfájl elektronikus feladásra történt kijelölése megszűnt.", var2.errorList, 1);
            } else {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, Tools.beautyPath(var7.getAbsolutePath()) + "\nfájl elektronikus feladásra történt kijelölése megszűnt.", "Visszavonás- üzenet", 1);
            }
         } else {
            File var9 = new NecroFile(this.sp.destPath);
            String var10 = var3.getName().indexOf(".") == -1 ? var3.getName() : var3.getName().substring(0, var3.getName().indexOf("."));
            UnMark.MarkedFileFilter var11 = new UnMark.MarkedFileFilter(PropertyList.USER_IN_FILENAME + var10);
            String[] var12 = var9.list(var11);
            int var14 = 0;

            while(true) {
               if (var14 >= var12.length) {
                  if (var8) {
                     EbevTools.showExtendedResultDialog((JDialog)null, Tools.beautyPath(var7.getAbsolutePath()) + "\nfájl elektronikus feladásra történt kijelölése megszűnt.", var2.errorList, 1);
                  } else {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, Tools.beautyPath(var7.getAbsolutePath()) + "\nfájl elektronikus feladásra történt kijelölése megszűnt.", "Visszavonás- üzenet", 1);
                  }
                  break;
               }

               File var15 = new NecroFile(this.sp.destPath + var12[var14]);
               var15.delete();

               try {
                  Result var13 = this.deleteCstFile(this.bm, var1);
                  if (!var13.isOk()) {
                     var8 = true;
                  }

                  var2.errorList.addAll(var13.errorList);
               } catch (Exception var18) {
                  Tools.eLog(var18, 0);
               }

               ++var14;
            }
         }

         this.bm.cc.docinfo.remove("krfilename");
         EnykInnerSaver var26 = new EnykInnerSaver(this.bm, true);
         var26.save(this.bm.cc.getLoadedfile().getAbsolutePath(), true);
      } catch (FileNotFoundException var21) {
         var2.setOk(false);
         var2.errorList.add(var21.getMessage());
         return var2;
      } catch (Exception var22) {
         var2.setOk(false);
         var2.errorList.add("programhiba : " + var22.getMessage());
         return var2;
      }

      if (var2.isOk()) {
         try {
            if (this.bm.isAvdhModel()) {
               Object[] var23 = this.getFileStatus();
               MainFrame.thisinstance.mp.getStatuspanel().statusname.setText((String)var23[0]);
               int var24 = (Integer)var23[1];
               if (var24 == 0) {
                  Menubar.thisinstance.setState(MainPanel.NORMAL);
                  MainFrame.thisinstance.mp.setReadonly(false);
                  this.cleanSourceFolder();
               } else {
                  Menubar.thisinstance.setState(MainPanel.READONLY);
               }
            } else {
               Menubar.thisinstance.setState((Object)null);
               MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("Módosítható");
               MainFrame.thisinstance.mp.setReadonly(false);
            }
         } catch (Exception var17) {
            Tools.eLog(var17, 0);
         }
      }

      return var2;
   }

   private Result deleteCstFile(BookModel var1, boolean var2) {
      Result var3 = new Result();
      String var4 = var1.cc.getLoadedfile().getAbsolutePath();

      for(int var5 = 0; var5 < var1.size(); ++var5) {
         String var6 = var4.substring(0, var4.toLowerCase().indexOf(".frm.enyk")) + "_" + var1.get(var5).name + ".cst";

         try {
            (new NecroFile(var6)).delete();
         } catch (Exception var14) {
            Tools.eLog(var14, 0);
         }
      }

      if (var2) {
         String var15 = PropertyList.getInstance().get("prop.usr.root") + "/" + PropertyList.getInstance().get("prop.usr.attachment") + "/" + var1.cc.getLoadedfile().getName();
         var15 = var15.substring(0, var15.toLowerCase().indexOf(".frm.enyk"));

         for(int var16 = 0; var16 < var1.forms.size(); ++var16) {
            FormModel var7 = (FormModel)var1.forms.get(var16);
            File var8 = new NecroFile(var15 + "/" + var7.id);
            if (var8.exists() && var8.isDirectory()) {
               File[] var9 = var8.listFiles(new FilenameFilter() {
                  public boolean accept(File var1, String var2) {
                     return var2.endsWith(".anyk.ASiC") || var2.endsWith(".urlap.anyk.xml");
                  }
               });

               for(int var10 = 0; var10 < var9.length; ++var10) {
                  File var11 = var9[var10];
                  if (!var11.delete()) {
                     try {
                        var3.errorList.add(var11.getAbsolutePath() + " fájl törlése nem sikerült.");
                        Ebev.log(1, var1.cc.getLoadedfile(), var11.getAbsolutePath() + " fájl törlése nem sikerült.");
                     } catch (Exception var13) {
                        System.out.println("Nem sikerült a log fájl írása. " + var11.getAbsolutePath() + " fájl törlése nem sikerült.");
                     }
                  }
               }
            }
         }

         var3.errorList.addAll(this.removeAsicFiles().errorList);
         if (var3.errorList.size() > 0) {
            var3.setOk(false);
         }
      }

      return var3;
   }

   private Object[] getFileStatus() {
      try {
         FileStatusChecker var1 = FileStatusChecker.getInstance();
         int var2 = var1.getExtendedStatus(this.bm.cc.getLoadedfile().getAbsolutePath(), (String)((String)(this.bm.cc.docinfo.containsKey("krfilename") ? this.bm.cc.docinfo.get("krfilename") : "")));
         String var3 = FileStatusChecker.getStringStatus(var2);
         return new Object[]{var3, var2};
      } catch (Exception var4) {
         return new Object[]{1, "Küldésre megjelölt"};
      }
   }

   public Result removeAsicFiles() {
      Result var1 = new Result();
      String var2 = this.bm.cc.getLoadedfile().getName();
      var2 = var2.substring(0, var2.length() - ".frm.enyk".length());
      String var3 = this.sp.srcPath + var2 + File.separator;
      File var4 = new NecroFile(var3 + "alairando");
      File[] var5 = var4.listFiles();
      int var6;
      if (var5 != null) {
         for(var6 = 0; var6 < var5.length; ++var6) {
            if (var5[var6].exists() && !var5[var6].delete()) {
               var1.errorList.add("A " + var5[var6] + " fájl törlése nem sikerült");
            }
         }
      }

      if (var4.exists() && !var4.delete()) {
         var1.errorList.add("A " + var4.getAbsolutePath() + " mappa törlése nem sikerült");
      }

      var4 = new NecroFile(var3 + "alairt");
      var5 = var4.listFiles();
      if (var5 != null) {
         for(var6 = 0; var6 < var5.length; ++var6) {
            if (var5[var6].exists() && !var5[var6].delete()) {
               var1.errorList.add("A " + var5[var6] + " fájl törlése nem sikerült");
            }
         }
      }

      if (var4.exists() && !var4.delete()) {
         var1.errorList.add("A " + var4.getAbsolutePath() + " mappa törlése nem sikerült");
      }

      var4 = new NecroFile(var3);
      if (var4.exists() && !var4.delete()) {
         var1.errorList.add("A " + var4.getAbsolutePath() + " mappa törlése nem sikerült");
      }

      return var1;
   }

   private void cleanSourceFolder() {
      String var1 = this.bm.cc.getLoadedfile().getName();
      var1 = var1.substring(0, var1.length() - ".frm.enyk".length());
      (new NecroFile(this.sp.srcPath + var1 + ".xml")).delete();
   }

   private class MarkedFileFilter implements FilenameFilter {
      private String filenmePrefix;

      public MarkedFileFilter(String var2) {
         this.filenmePrefix = var2.toLowerCase();
      }

      public boolean accept(File var1, String var2) {
         return var2.toLowerCase().startsWith(this.filenmePrefix) && var2.toLowerCase().endsWith("_p.kr");
      }
   }
}
