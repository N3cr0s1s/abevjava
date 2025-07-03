package hu.piller.enykp.alogic.ebev.extendedsign;

import hu.piller.enykp.alogic.ebev.Ebev;
import hu.piller.enykp.alogic.ebev.EbevTools;
import hu.piller.enykp.alogic.ebev.SendParams;
import hu.piller.enykp.alogic.filesaver.enykinner.EnykInnerSaver;
import hu.piller.enykp.alogic.filesaver.xml.EnykXmlSaver;
import hu.piller.enykp.alogic.fileutil.FileNameResolver;
import hu.piller.enykp.alogic.signer.AnykCsatolmanyLenyomat;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import java.awt.Component;
import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JDialog;

public class KrPreparation {
   public static final String EXTERNAL_SIGN_SOURCE = "alairando";
   public static final String EXTERNAL_SIGN_TARGET = "alairt";
   public static final String HASH_XML_POSTFIX = "_lenyomat";
   SendParams sp = new SendParams(PropertyList.getInstance());
   private BookModel bm;
   private String loadedFileAzon;

   public KrPreparation(BookModel var1) throws Exception {
      this.bm = var1;
      String var2 = null;
      if (var1.cc.getLoadedfile() == null) {
         FileNameResolver var3 = new FileNameResolver(var1);
         var2 = var3.generateFileName();
      } else if (var1.dirty) {
         var2 = var1.cc.getLoadedfile().getAbsolutePath();
      }

      if (var2 != null) {
         try {
            EnykInnerSaver var6 = new EnykInnerSaver(var1);
            File var4 = var6.save(var2, -1);
            if (var4 != null) {
               var1.cc.setLoadedfile(var4);
               var1.dirty = false;
            }

            if (var4 == null) {
               throw new Exception();
            }
         } catch (Exception var5) {
            GuiUtil.showMessageDialog((Component)null, "A nyomtatvány mentése nem sikerült, a művelet nem folytatható", "Hiba", 0);
            ErrorList.getInstance().writeError(new Long(4001L), "A 'nyomtatvány mentése külső aláírással történő hitelesítéshez' művelet előtti mentés nem sikerült!", IErrorList.LEVEL_ERROR, var5, (Object)null);
         }
      }

      this.loadedFileAzon = var1.cc.getLoadedfile().getName();
      this.loadedFileAzon = this.loadedFileAzon.substring(0, this.loadedFileAzon.length() - ".frm.enyk".length());
   }

   public boolean external() {
      String var1 = this.sp.srcPath + this.loadedFileAzon + File.separator;
      (new File(var1)).mkdir();
      (new File(var1 + "alairando")).mkdir();
      (new File(var1 + "alairt")).mkdir();
      String var2 = var1 + "alairando" + File.separator + this.loadedFileAzon;
      Result var3 = this.saveAnykFileAsXml(var2 + ".xml", true);
      String var4;
      if (!var3.isOk()) {
         if (!(new File(var2 + ".xml")).delete()) {
            var4 = (String)var3.errorList.get(0);
            var4 = var4 + "\nA " + var2 + ".xml" + " fájl törlése nem sikerült, kérjük törölje más módon!";
            var3.errorList.insertElementAt(var4, 0);
         }

         GuiUtil.showMessageDialog(MainFrame.thisinstance, var3.errorList.get(0), "Átadás digitális aláírásra", 0);
         return false;
      } else if (var3.errorList.size() > 0 && "Felhasználói megszakítás".equals(var3.errorList.elementAt(0))) {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "A " + var2 + ".xml" + " fájl törlése nem sikerült, kérjük törölje más módon!", "Átadás digitális aláírásra", 0);
         return false;
      } else {
         var4 = this.doHashAnykFile(var2);
         if (var4 != null) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, var4, "Átadás digitális aláírásra", 0);
            return false;
         } else {
            String var5 = this.sp.srcPath + this.loadedFileAzon + File.separator + "\n";
            var5 = var5 + var2.substring(var5.length() - 1);
            var5 = Tools.beautyPath(var5);
            if (GuiUtil.showOptionDialog(MainFrame.thisinstance, "A nyomtatvány lenyomatát a \n" + var5 + "_lenyomat" + ".xml" + "\nnéven mentettük!\n" + "Kérjük ezt a fájlt írja alá az aláíró programjával, majd helyezze át a \n" + var1 + "alairt" + "\nmappába!\n" + "Megnyissuk a lenyomatot tartalmazó mappát?", "Kérdés", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) == 0) {
               this.openSourceFolder(var1 + "alairando");
            }

            try {
               Ebev.log(12, this.bm.cc.getLoadedfile());
            } catch (Exception var7) {
               ErrorList.getInstance().writeError(new Long(4009L), this.bm.cc.getLoadedfile() + " - Hiba a nyomtatvány mentése külső aláírással történő hitelesítéshez naplózásakor", IErrorList.LEVEL_ERROR, var7, (Object)null);
            }

            return true;
         }
      }
   }

   public String avdh(boolean var1) {
      Result var2 = this.saveAnykFileAsXml(this.sp.srcPath + File.separator + this.loadedFileAzon + ".kr", var1);
      if (var2.errorList.size() > 0 && "Felhasználói megszakítás".equals(var2.errorList.elementAt(0))) {
         return null;
      } else if (!var2.isOk()) {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, var2.errorList.get(0), "Átadás digitális aláírásra", 0);
         return null;
      } else {
         return this.loadedFileAzon;
      }
   }

   private Result saveAnykFileAsXml(String var1, boolean var2) {
      Result var3 = new Result();

      try {
         EnykXmlSaver var4 = new EnykXmlSaver(this.bm);
         var3 = var4.save(var1 + ".xml", !var2, true, this.sp, var1);
         if (!var3.isOk() && "Felhasználói megszakítás".equals(var3.errorList.elementAt(0))) {
            var3.setOk(true);
         }
      } catch (Exception var5) {
         ErrorList.getInstance().writeError(new Long(4009L), var1 + ".xml" + "Hiba a nyomtatvány mentésekor - nem készíthető el a lenyomat!", IErrorList.LEVEL_ERROR, var5, (Object)null);
         var3.setOk(false);
         var3.errorList.insertElementAt("Hiba a nyomtatvány mentésekor - nem készíthető el a lenyomat!", 0);
      }

      return var3;
   }

   private String doHashAnykFile(String var1) {
      File var2 = new File(var1 + "_lenyomat" + ".xml");
      FileWriter var3 = null;

      label71: {
         String var5;
         try {
            var3 = new FileWriter(var2);
            var3.write(this.createHashFromXml(new File(var1 + ".xml")));
            break label71;
         } catch (Exception var15) {
            ErrorList.getInstance().writeError(new Long(4009L), var1 + ".xml" + "Hiba a nyomtatvány mentésekor - nem készíthető el a lenyomat!", IErrorList.LEVEL_ERROR, var15, (Object)null);
            var5 = "Hiba a lenyomat készítésekor!";
         } finally {
            try {
               var3.close();
            } catch (Exception var14) {
               Tools.eLog(var14, 0);
            }

         }

         return var5;
      }

      File var4 = new File(this.sp.srcPath + this.loadedFileAzon + ".xml");
      if (var4.exists()) {
         var4.delete();
      }

      (new File(var1 + ".xml")).renameTo(var4);
      return null;
   }

   private String createHashFromXml(File var1) {
      try {
         AnykCsatolmanyLenyomat var3 = AnykCsatolmanyLenyomat.create(var1);
         String var2 = var3.toXml();
         return var2;
      } catch (Exception var4) {
         return null;
      }
   }

   private void openSourceFolder(String var1) {
      try {
         Desktop.getDesktop().open(new File(var1));
      } catch (Exception var3) {
         ErrorList.getInstance().writeError(new Long(4009L), "Nem sikerült a " + var1 + " mappa megnyitása!", IErrorList.LEVEL_ERROR, var3, (Object)null);
      }

   }

   public void reset() {
      this.reset(true);
   }

   public void reset(boolean var1) {
      String var2 = this.sp.srcPath + this.loadedFileAzon + File.separator;
      String var3 = "";
      Vector var4 = new Vector();
      File var5 = new File(var2 + "alairando");
      File[] var6 = var5.listFiles();
      if (var6 != null) {
         for(int var7 = 0; var7 < var6.length; ++var7) {
            if (var6[var7].exists() && !var6[var7].delete()) {
               var3 = var3 + "\nA " + var6[var7] + " fájl törlése nem sikerült";
            } else {
               var4.add(var6[var7]);
            }
         }
      }

      File var9 = new File(var2 + "alairando");
      if (var9.exists() && !var9.delete()) {
         var3 = var3 + "\nA " + var9.getAbsolutePath() + " mappa törlése nem sikerült";
      }

      var5 = new File(var2 + "alairt");
      var6 = var5.listFiles();
      if (var6 != null) {
         for(int var8 = 0; var8 < var6.length; ++var8) {
            if (var6[var8].exists() && !var6[var8].delete()) {
               var3 = var3 + "\nA " + var6[var8] + " fájl törlése nem sikerült";
            } else {
               var4.add(var6[var8]);
            }
         }
      }

      var9 = new File(var2 + "alairt");
      if (var9.exists() && !var9.delete()) {
         var3 = var3 + "\nA " + var9.getAbsolutePath() + " mappa törlése nem sikerült";
      }

      var9 = new File(var2);
      if (var9.exists() && !var9.delete()) {
         var3 = var3 + "\nA " + var9.getAbsolutePath() + " mappa törlése nem sikerült";
      }

      if (var1) {
         if (!"".equals(var3)) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "A visszavonáskor az alábbi hibák keletkeztek:" + var3 + "\nKérjük kézzel törölje a fenti fájlokat/mappákat!");
         } else {
            EbevTools.showExtendedResultDialog((JDialog)null, "A hitelesítés visszavonása megtörtént.\nA visszavonáskor az alábbi fájlokat töröltük:", var4, 1);
         }
      } else {
         Vector var10 = this.deleteAttachmentAsics();
         if (var10.size() > 0) {
            EbevTools.showExtendedResultDialog((JDialog)null, "A hibás műveletet visszavontuk.\nAz alábbi fájlok törlése nem sikerült, kérjük törölje más módon a fájlokat!", var10, 0);
         }
      }

   }

   public String resetXCZ() {
      String var1 = "";

      try {
         File var2 = new File(this.sp.krdir + "fizikai_adathordozo" + File.separator + this.loadedFileAzon + ".xcz");
         if (!var2.delete()) {
            var1 = this.sp.krdir + "fizikai_adathordozo" + File.separator + this.loadedFileAzon + ".xcz" + " fájl törlése nem sikerült!\nKérjük más módon törölje!";
         }

         var2 = new File(this.sp.destPath + File.separator + this.loadedFileAzon + ".xcz" + "_status");
         if (!var2.delete()) {
            var1 = var1 + "\n" + this.sp.destPath + File.separator + this.loadedFileAzon + ".xcz" + "_status fájl törlése nem sikerült!\nKérjük más módon törölje!";
         }

         if ("".equals(var1)) {
            var1 = "A(z) " + this.sp.krdir + "fizikai_adathordozo" + File.separator + this.loadedFileAzon + ".xcz" + " fájlt töröltük.";
         }

         try {
            Ebev.log(16, this.bm.cc.getLoadedfile());
         } catch (Exception var4) {
            ErrorList.getInstance().writeError(new Long(4001L), "A külső adathordozóra történő másolás (xcz) visszavonása nem sikerült!", IErrorList.LEVEL_ERROR, var4, (Object)null);
         }
      } catch (Exception var5) {
         var1 = "Hiba a fájlok törlésekor!";
         ErrorList.getInstance().writeError(new Integer(57773), "Hiba az külső adathordozóra történő másolás (xcz) visszavonásakor", 2, var5, (Object)null);
      }

      return var1;
   }

   public void moveXmlToTargetDir(String var1) {
      String var2 = this.sp.srcPath + this.loadedFileAzon + File.separator;
      (new File(var2)).mkdir();
      (new File(var2 + "alairt")).mkdir();
      File var3 = new File(this.sp.srcPath + File.separator + var1 + ".xml" + ".anyk.ASiC");
      boolean var4 = var3.renameTo(new File(var2 + "alairt" + File.separator + var1 + ".urlap.anyk.ASiC"));
      System.out.println(var4);
   }

   private Vector deleteAttachmentAsics() {
      Vector var1 = new Vector();
      String var2 = (String)PropertyList.getInstance().get("prop.usr.attachment");
      var2 = this.sp.root + var2;
      if (!var2.endsWith("\\") && !var2.endsWith("/")) {
         var2 = var2 + File.separator;
      }

      var2 = var2 + this.loadedFileAzon + File.separator + this.bm.get_formid();
      File[] var3 = (new File(var2)).listFiles();
      if (var3 == null) {
         return var1;
      } else {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4].getName().toLowerCase().endsWith(".anyk.ASiC".toLowerCase()) && !var3[var4].delete()) {
               var1.add(var3[var4]);
            }
         }

         return var1;
      }
   }
}
