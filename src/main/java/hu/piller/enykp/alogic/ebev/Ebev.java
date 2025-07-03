package hu.piller.enykp.alogic.ebev;

import hu.piller.enykp.alogic.filesaver.enykinner.EnykInnerSaver;
import hu.piller.enykp.alogic.fileutil.FileNameResolver;
import hu.piller.enykp.alogic.fileutil.FileStatusChecker;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.orghandler.OrgResource;
import hu.piller.enykp.alogic.orghandler.SuccessorException;
import hu.piller.enykp.alogic.templateutils.TemplateUtils;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.errordialog.ErrorDialog;
import hu.piller.enykp.util.base.errordialog.TextWithIcon;
import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JOptionPane;

public class Ebev {
   private BookModel bm;
   private Ebev.TWIComparator twic = new Ebev.TWIComparator();
   private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
   public static final int ACTION_MARK = 0;
   public static final int ACTION_UNMARK = 1;
   public static final int ACTION_PASS = 2;
   public static final int ACTION_POST_UK = 3;
   public static final int ACTION_POST_HK = 4;
   public static final int ACTION_POST_EK = 5;
   public static final int ACTION_PASS_AVDH = 10;
   public static final int ACTION_SIGN_AVDH = 11;
   public static final int ACTION_PASS_EXTERNAL = 12;
   public static final int ACTION_PASS_XCZ = 13;
   public static final int ACTION_RESET_AVDH = 14;
   public static final int ACTION_RESET_EXTERNAL = 15;
   public static final int ACTION_RESET_XCZ = 16;
   private static final String MARK = "Megjelölés feladásra";
   private static final String UNMARK = "Megjelölés visszavonás";
   private static final String PASS = "Átadás digitális aláírásra";
   private static final String POST_UK = "Küldés az Ügyfélkapura";
   private static final String POST_HK = "Küldés a Cég/Hivatali kapura";
   private static final String POST_EK = "Küldés a Perkapura";
   private static final String PASS_AVDH = "Átadás AVDH aláírásra";
   private static final String SIGN_AVDH = "AVDH aláírás";
   private static final String PASS_EXTERNAL = "Átadás külső aláírásra";
   private static final String PASS_XCZ = "XCZ fájl készítés külső adathordozóra";
   private static final String RESET_AVDH = "AVDH aláírás visszavonása";
   private static final String RESET_EXTERNAL = "Külső aláírás visszavonása";
   private static final String RESET_XCZ = "XCZ fájl visszavonása";
   IPropertyList mpl = PropertyList.getInstance();
   private SendParams sp;
   private boolean needOfficeUserAndPass;

   public Ebev(BookModel var1) {
      this.sp = new SendParams(this.mpl);
      this.bm = var1;
   }

   public void mark() {
      Result var1 = this.mark(false, false);
   }

   public Result mark(boolean var1, boolean var2) {
      return this.mark(var1, var2, (Vector)null);
   }

   public Result mark(boolean var1, boolean var2, Vector var3) {
      Result var4 = new Result();
      if (var3 != null) {
         if (var3.size() > 0) {
            if (!var1) {
               ErrorDialog var5 = new ErrorDialog(MainFrame.thisinstance, "Ellenőrzés", true, true, var3, "Hibalista", true, this.bm.cc.getLoadedfile());
               if (var5.isProcessStoppped()) {
                  var4 = new Result();
                  var4.setOk(false);
                  var4.errorList.add("Felhasználói megszakítás");
                  return var4;
               }
            }
         } else if (!var1) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "A nyomtatvány ellenőrzése megtörtént\nAz ellenőrzés nem talált hibát.", "Nyomtatvány ellenőrzés", 1);
         }
      }

      if (var3 != null && Tools.hasFatalError(var3)) {
         var4.setOk(false);
         var4.errorList.add("A nyomtatvány súlyos hibát is tartalmaz, a művelet nem folytatható.");
         if (!var1) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "A nyomtatvány súlyos hibát is tartalmaz, a művelet nem folytatható.", "Hiba", 0);
         }

         return var4;
      } else {
         var4 = this.check(true, var1);
         if (!var4.isOk()) {
            if (!var1) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, var4.errorList.elementAt(0), "Naplózás", 0);
            }

            return var4;
         } else {
            boolean var9 = false;

            try {
               var9 = this.bm.splitesaver.equalsIgnoreCase("true");
            } catch (Exception var7) {
               var9 = false;
            }

            if (var9) {
               Mark2SendParts var6 = new Mark2SendParts(this.bm, this.sp, var1);
               if (var3 != null) {
                  var6.needCheck = false;
               }

               var4 = var6.mark();
            } else {
               Mark2Send var10 = new Mark2Send(this.bm, this.sp, var1, var2);
               if (var3 != null) {
                  var10.needCheck = false;
               }

               var4 = var10.mark(var1, true);
            }

            if (var4.isOk()) {
               try {
                  log(0, this.bm.cc.getLoadedfile());
               } catch (Exception var8) {
                  if (!var1) {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, var8.getMessage(), "Naplózás", 0);
                  } else {
                     System.out.println("Figyelmeztetés! Nem sikerült a feladás naplózása (nem hiba).");
                  }
               }
            }

            return var4;
         }
      }
   }

   public void unmark(boolean var1) {
      UnMark var2 = new UnMark(this.bm, this.sp);
      Result var3 = var2.doUnmark(var1);
      if (var3.isOk()) {
         try {
            log(1, this.bm.cc.getLoadedfile(), " sikeres!");
         } catch (Exception var6) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, var6.getMessage(), "Naplózás", 0);
         }
      } else {
         EbevTools.showResultDialog(var3.errorList);

         try {
            log(1, this.bm.cc.getLoadedfile(), " hibákkal!");
         } catch (Exception var5) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, var5.getMessage(), "Naplózás", 0);
         }
      }

   }

   public void pass() {
      this.pass(false);
   }

   public Result pass(boolean var1) {
      Result var2 = this.check(false, false, var1);
      if (!var1 && !var2.isOk()) {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, var2.errorList.get(0), "Átadás", 0);
      }

      if (!var2.isOk()) {
         return var2;
      } else {
         boolean var3 = false;

         try {
            var3 = this.bm.splitesaver.equalsIgnoreCase("true");
         } catch (Exception var5) {
            var3 = false;
         }

         if (var3) {
            Pass2DSignParts var4 = new Pass2DSignParts(this.bm, this.sp, this.mpl);
            var2 = var4.pass(var1);
         } else {
            Pass2DSign var6 = new Pass2DSign(this.bm, this.sp, this.mpl);
            var2 = var6.pass(var1);
         }

         if (!var2.isOk()) {
            if (var2.errorList.size() == 0) {
               var2.errorList.add("Hiba történt a művelet elvégzésekor.");
            }

            if (!var1) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, var2.errorList.get(0), "Átadás", 0);
            }
         }

         return var2;
      }
   }

   public void post(boolean var1) {
      this.needOfficeUserAndPass = var1;
      Send2Mohu var2 = new Send2Mohu();
      FileStatusChecker.getInstance().stopBatchMode();
      Vector var3 = FileStatusChecker.getInstance().getFilenames(this.bm.cc.getLoadedfile().getAbsolutePath(), (String)((String)(this.bm.cc.docinfo.containsKey("krfilename") ? this.bm.cc.docinfo.get("krfilename") : "")));
      if (this.checkFileSize(var3, !var1) <= 0) {
         String[] var4 = new String[var3.size()];
         var3.toArray(var4);
         var2.send(false, this.sp, var4, this.bm, var1);
      }
   }

   public Result export(String var1) {
      if (var1 != null && (new File(var1)).exists() && GuiUtil.showOptionDialog((Component)null, "Fájl már létezik. Felülírjuk?", "XML mentés", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) == 1) {
         return new Result();
      } else {
         Pass2DSign var3 = new Pass2DSign(this.bm, this.sp, this.mpl);
         Result var2 = var3.export(var1);
         if (!var2.isOk()) {
            if (var2.errorList.size() == 0) {
               var2.errorList.add("Hiba történt a művelet elvégzésekor.");
            } else {
               var2.errorList.insertElementAt("Hiba történt a művelet elvégzésekor.", 0);
            }

            if (var1 == null) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, var2.errorList.get(0), "XML Export", 0);
            }
         }

         return var2;
      }
   }

   private Result check(boolean var1, boolean var2) {
      return this.check(var1, false, var2);
   }

   private Result check(boolean var1, boolean var2, boolean var3) {
      MainFrame var4 = MainFrame.thisinstance;
      Result var5 = new Result();

      try {
         if (!this.checkPath(this.sp.srcPath, true)) {
            throw new EbevParameterException("Hiányzó vagy hibás 'prop.usr.ds_src' paraméter!");
         }

         if (!this.checkPath(this.sp.destPath, true)) {
            throw new EbevParameterException("Hiányzó vagy hibás 'prop.usr.ds_dest' paraméter!");
         }

         if (!this.checkPath(this.sp.root, true)) {
            throw new EbevParameterException("Hiányzó vagy hibás 'prop.usr.root' paraméter!");
         }
      } catch (EbevParameterException var12) {
         if (!var2 && !var3) {
            GuiUtil.showMessageDialog(var4, var12.getMessage(), "Átadás", 0);
         }

         var5.setOk(false);
         var5.errorList.add(var12.getMessage());
         return var5;
      }

      String var13;
      if (EbevTools.eSendDisabled(this.bm)) {
         var13 = "A nyomtatvány jelen kitöltöttséggel közvetlenül nem adható fel elektronikusan.";
         var5.setOk(false);
         var5.errorList.add(var13);
         return var5;
      } else if (this.bm.temtype != null && this.bm.temtype.equals("control")) {
         var13 = "Ilyen típusú nyomtatvány közvetlenül nem adható fel elektronikusan.\nAz eBEV rendszeren keresztül elektronikusan küldhető formátumú adatállományok\na nyomtatvány bezárását követően az \"Adatok -> Kontroll állomány létrehozása\"\nmenüpontban készíthetők el.";
         if (!var2 && !var3) {
            GuiUtil.showMessageDialog(var4, var13, "Megjelölés", 0);
         }

         var5.setOk(false);
         var5.errorList.add(var13);
         return var5;
      } else if (this.bm.epost != null && this.bm.epost.equals("notinternet")) {
         if (!var2 && !var3) {
            GuiUtil.showMessageDialog(var4, "Ilyen típusú nyomtatvány közvetlenül nem adható fel elektronikusan!", "Megjelölés", 0);
         }

         var5.setOk(false);
         var5.errorList.add("Ilyen típusú nyomtatvány közvetlenül nem adható fel elektronikusan!");
         return var5;
      } else if (this.bm.get_main_index() != 0) {
         if (!var2 && !var3) {
            GuiUtil.showMessageDialog(var4, "A nyomtatványban nem az első helyen áll a fődokumentum, ezért nem adható fel elektronikusan!", "Megjelölés", 0);
         }

         var5.setOk(false);
         var5.errorList.add(new TextWithIcon("A nyomtatványban nem az első helyen áll a fődokumentum, ezért nem adható fel elektronikusan!", 1));
         return var5;
      } else {
         int var6 = Tools.loadedFileCheck(this.bm.cc.getLoadedfile());
         if (var6 != 0) {
            var5 = this.isSavable();
            if (!var5.isOk()) {
               if (!var2 && !var3) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, var5.errorList.get(0), "Mentés hiba", 0);
               }

               return var5;
            }

            String var7;
            if (var6 == 1) {
               FileNameResolver var8 = new FileNameResolver(this.bm);
               var7 = var8.generateFileName();
            } else {
               var7 = this.bm.cc.getLoadedfile().getAbsolutePath();
            }

            try {
               EnykInnerSaver var15 = new EnykInnerSaver(this.bm);
               var5 = var15.save(var7, -1, var3);
               File var9 = null;
               if (var5.isOk()) {
                  var9 = (File)var5.errorList.get(0);
               }

               if (var9 == null) {
                  var5.setOk(false);
                  var5.errorList.add("Hiba a mentéskor!");
                  return var5;
               }

               this.bm.cc.setLoadedfile(var9);
            } catch (Exception var11) {
               var5.setOk(false);
               var5.errorList.add("Hiba a mentéskor (2)!");
               return var5;
            }
         } else if (!var2 && !this.bm.cc.getLoadedfile().getAbsolutePath().endsWith(".xml") && this.bm.dirty) {
            if (!var1 && !var3) {
               if (JOptionPane.showOptionDialog(var4, "A nyomtatvány tartalma megváltozott, de nem került mentésre!\nMegjelöljük a módosított nyomtatványt?", "Átadás", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) == 1) {
                  var5.setOk(false);
                  var5.errorList.add("A megjelölést megszakította!");
                  return var5;
               }
            } else {
               try {
                  EnykInnerSaver var14 = new EnykInnerSaver(this.bm);
                  var14.save(this.bm.cc.getLoadedfile().getAbsolutePath(), var3);
               } catch (Exception var10) {
                  var5.setOk(false);
                  var5.errorList.add("Hiba a mentéskor! (4)");
                  return var5;
               }
            }
         }

         if (!this.bm.cc.getLoadedfile().exists()) {
            var5.setOk(false);
            var5.errorList.add("A nyomtatvány nem adható fel! Hiányzó fájl: " + this.bm.cc.getLoadedfile().getAbsolutePath());
         }

         return var5;
      }
   }

   private boolean checkPath(String var1, boolean var2) {
      try {
         File var3 = new File(var1);
         if (!var3.exists()) {
            return false;
         } else {
            return var2 ? var3.isDirectory() : true;
         }
      } catch (Exception var4) {
         return false;
      }
   }

   private Result isSavable() {
      TemplateUtils var1 = TemplateUtils.getInstance();
      Result var2 = new Result();

      try {
         Object[] var3 = (Object[])((Object[])var1.isSavable(this.bm));
         var2.setOk((Boolean)var3[0]);
         if (!var2.isOk()) {
            var2.errorList.add(var3[1]);
         }

         return var2;
      } catch (Exception var4) {
         var2.setOk(false);
         var2.errorList.add(var4.getMessage());
         return var2;
      }
   }

   public static void log(int var0, File var1) throws Exception {
      log(var0, var1, (String)null);
   }

   public static void log(int var0, File var1, String var2) throws Exception {
      try {
         String var3 = (String)PropertyList.getInstance().get("prop.usr.krdir");
         if (!var3.endsWith("\\") && !var3.endsWith("/")) {
            var3 = var3 + File.separator;
         }

         if (!(new File(var3)).isDirectory()) {
            throw new FileNotFoundException("nem található krdir");
         } else {
            String var4 = sdf.format(Calendar.getInstance().getTime()) + ";" + var1.getAbsolutePath() + ";" + System.getProperty("user.name") + ";";
            switch(var0) {
            case 0:
               var4 = var4 + "Megjelölés feladásra";
               break;
            case 1:
               var4 = var4 + "Megjelölés visszavonás";
               break;
            case 2:
            case 6:
            case 7:
            case 8:
            case 9:
            default:
               var4 = var4 + "Átadás digitális aláírásra";
               break;
            case 3:
               var4 = var4 + "Küldés az Ügyfélkapura";
               break;
            case 4:
               var4 = var4 + "Küldés a Cég/Hivatali kapura";
               break;
            case 5:
               var4 = var4 + "Küldés a Perkapura";
               break;
            case 10:
               var4 = var4 + "Átadás AVDH aláírásra";
               break;
            case 11:
               var4 = var4 + "AVDH aláírás";
               break;
            case 12:
               var4 = var4 + "Átadás külső aláírásra";
               break;
            case 13:
               var4 = var4 + "XCZ fájl készítés külső adathordozóra";
               break;
            case 14:
               var4 = var4 + "AVDH aláírás visszavonása";
               break;
            case 15:
               var4 = var4 + "Külső aláírás visszavonása";
               break;
            case 16:
               var4 = var4 + "XCZ fájl visszavonása";
            }

            File var5 = new File(var3 + "feladas_naplo.log");
            PrintWriter var6 = null;
            if (var2 != null) {
               var4 = var4 + ";" + var2;
            }

            try {
               var6 = new PrintWriter(new FileWriter(var5, true));
               var6.println(var4);
               var6.close();
            } catch (Exception var10) {
               try {
                  var6.close();
               } catch (Exception var9) {
                  Tools.eLog(var9, 0);
               }
            }

         }
      } catch (Exception var11) {
         throw new Exception("Hiba a művelet naplózásakor: " + var11.getMessage());
      }
   }

   private Result checkIfDisabled() {
      Result var1 = new Result();
      int var2 = this.bm.getCalcelemindex();

      for(int var3 = 0; var3 < this.bm.cc.size(); ++var3) {
         this.bm.setCalcelemindex(var3);
         Result var4 = Tools.checkDisabled((Elem)this.bm.cc.get(var3), this.bm);
         if (!var4.isOk()) {
            var1.setOk(false);
            var1.errorList.addAll(var4.errorList);
         }
      }

      this.bm.setCalcelemindex(var2);
      Collections.sort(var1.errorList, this.twic);
      return var1;
   }

   private int checkFileSize(Vector var1, boolean var2) {
      Vector var3 = new Vector();
      long var4 = 524288000L;
      String var6 = "500 Mbyte";
      if (!var2) {
         var4 = 524288000L;
         var6 = "500 Mbyte";
      }

      String var7;
      String var9;
      if (PropertyList.getInstance().get("prop.dynamic.gateTypeEPER") != null) {
         try {
            var7 = (String)this.bm.docinfo.get("org");
            if (OrgInfo.getInstance().hasSuccessor(var7)) {
               var7 = OrgInfo.getInstance().getSuccessorOrgId(var7);
            }

            OrgResource var8 = (OrgResource)((Hashtable)OrgInfo.getInstance().getOrgList()).get(var7);
            var9 = var8.getPreferredUplodaMaxSize();
            if ("".equals(var9) || "0".equals(var9)) {
               return 0;
            }

            var6 = var9 + " Mbyte";

            try {
               var4 = Long.parseLong(var9) * 1024L * 1024L;
            } catch (NumberFormatException var11) {
               if (var2) {
                  var4 = 524288000L;
                  var6 = "500 Mbyte";
               } else {
                  var4 = 524288000L;
                  var6 = "500 Mbyte";
               }
            }

            if (var2) {
               if (var4 > 524288000L) {
                  var4 = 524288000L;
                  var6 = "500 Mbyte";
               }
            } else if (var4 > 524288000L) {
               var4 = 524288000L;
               var6 = "500 Mbyte";
            }
         } catch (SuccessorException var12) {
            ErrorList.getInstance().writeError(new Integer(23112), "Hiba a mérethatár megállapításakor, nem alkalmazunk korlátot.", var12, (Object)null);
            return 0;
         }
      }

      var7 = "az Ügyfélkapu";
      if (this.needOfficeUserAndPass) {
         var7 = "a Cég/Hivatali Kapu";
      }

      for(int var13 = 0; var13 < var1.size(); ++var13) {
         var9 = (String)var1.elementAt(var13);
         File var10 = new File(var9);
         if (var10.length() > var4) {
            var3.add(var9);
            var3.add(" - A dokumentum mérete meghaladja az " + var6 + "-ot. Ekkora állományt " + var7 + " nem tud fogadni!");
         }
      }

      if (var3.size() > 0) {
         new ErrorDialog(MainFrame.thisinstance, "Az alábbi fájlok nem küldhetők be", true, false, var3);
      }

      return var3.size();
   }

   private class TWIComparator implements Comparator {
      private TWIComparator() {
      }

      public int compare(Object var1, Object var2) {
         return var1.toString().compareTo(var2.toString());
      }

      // $FF: synthetic method
      TWIComparator(Object var2) {
         this();
      }
   }
}
