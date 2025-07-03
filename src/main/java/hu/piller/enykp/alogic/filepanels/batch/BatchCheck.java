package hu.piller.enykp.alogic.filepanels.batch;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.ebev.Ebev;
import hu.piller.enykp.alogic.filesaver.xml.ErrorListListener4XmlSave;
import hu.piller.enykp.alogic.templateutils.blacklist.BlacklistStore;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.interfaces.IResult;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.errordialog.TextWithIcon;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import hu.piller.enykp.util.test.XMLFileComparator;
import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Vector;

public class BatchCheck {
   public static final int TYPE_FILE = 0;
   public static final int TYPE_DATABASE = 1;
   public static final int RES_ERROR_ON_CHECK = 5;
   public static final int RES_CHECK_OK_NOERROR = 6;
   public static final int RES_CHECK_OK_WITHERROR = 7;
   private static final String[] messages = new String[]{"Minden rendben", "Listafájl v. könyvtár nem létezik", "Hiba a lista feldolgozásakor"};
   private static final String[] noError = new String[]{"programverzio", "nyomtatvanyverzio", "hash", "megjegyzes"};
   public static int errorCountFromBatch = -1;
   private int count = 0;
   private int countAll = 0;
   private int dataType = 0;
   private FileOutputStream fileOutput;
   private File fOut;
   private Loader4BatchCheck l4bc;

   public BatchCheck(int var1) throws Exception {
      this.dataType = var1;
   }

   public int doCheckOne(String var1, String var2) {
      try {
         PropertyList.getInstance().set("prop.dynamic.ilyenkor", "");
         this.openFileOutput4OneCheck(var2);
         this.l4bc = new FileLoader4BatchCheck();
         this.l4bc.setFileToList(var1);
         this.checkVector(var1);
         this.doOutput(0, "\nAz ellenőrzés sikeresen befejeződött.\nEllenőrzésre kijelölt fájl: " + this.countAll + " db\nSikeresen ellenőrzött: " + this.count + " db");
         this.closeFileOutput();
         PropertyList.getInstance().set("prop.dynamic.ilyenkor", (Object)null);
         byte var3 = 0;
         return var3;
      } catch (Exception var13) {
         var13.printStackTrace();
      } finally {
         try {
            this.closeFileOutput();
            PropertyList.getInstance().set("prop.dynamic.ilyenkor", (Object)null);
         } catch (Exception var12) {
            Tools.eLog(var12, 0);
         }

      }

      return -1;
   }

   public String doCheck(String var1) {
      if (this.dataType == 0) {
         try {
            this.checkListFromFile(var1);
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }

      return this.fOut.getAbsolutePath();
   }

   private void checkListFromFile(String var1) throws Exception {
      this.openFileOutput("check");
      this.l4bc = new FileLoader4BatchCheck();
      int var2 = this.l4bc.createList(var1);
      if (var2 != 0) {
         this.doOutput(var2, messages[var2]);
      } else {
         this.checkVector(var1);
      }

      this.doOutput(0, "\nAz ellenőrzés sikeresen befejeződött.\nEllenőrzésre kijelölt fájl: " + this.countAll + " db\nSikeresen ellenőrzött: " + this.count + " db");
      this.closeFileOutput();
   }

   private void checkVector(String var1) {
      File var2 = new NecroFile(var1);
      if (!var2.isDirectory()) {
         var1 = var2.getParent();
      }

      if (!var1.endsWith("\\") && !var1.endsWith("/")) {
         var1 = var1 + File.separator;
      }

      BookModel var3 = null;

      String var4;
      while((var4 = this.l4bc.nextId()) != null) {
         ++this.countAll;
         if (var4.toLowerCase().endsWith(".xml")) {
            if (var4.indexOf(File.separator) == -1) {
               var4 = var1 + var4;
            }

            BookModel var5 = this.l4bc.superLoad(var3, var4);
            var3 = var5;
            if (this.checkBookModel(var5, var4)) {
               CalculatorManager.getInstance().multiform_calc();

               try {
                  this.check(var4);
                  ++this.count;
               } catch (Exception var7) {
                  this.doOutput(5, "\n[" + var4 + "] Hiba történt a nyomtatvány ellenőrzésekor!");
               }
            }
         }
      }

   }

   public boolean checkBookModel(BookModel var1, String var2) {
      if (var1 == null) {
         this.doOutput(3, "\n[" + var2 + "] Hiba történt a nyomtatvány betöltésekor!");
         return false;
      } else {
         String var3;
         int var4;
         String[] var7;
         if (var1.hasError) {
            var3 = "\n[" + var2 + "] Hiba történt a nyomtatvány betöltésekor! (" + var1.errormsg + ")";
            if (var1.errorlist != null) {
               for(var4 = 0; var4 < var1.errorlist.size(); ++var4) {
                  var3 = var3 + "\n" + var1.errorlist.elementAt(var4);
               }
            }

            try {
               var7 = var1.errormsg.split("##");
               var3 = var3 + "\n" + var7[0] + "\n" + var7[1];
            } catch (Exception var6) {
               Tools.eLog(var6, 0);
            }

            this.doOutput(3, var3);
            return false;
         } else {
            var3 = "\n[" + var2 + "] A nyomtatvány betöltése az alábbi üzenetet adta:";
            if (var1.errormsg != null && var1.errormsg.length() > 0) {
               var7 = var1.errormsg.split(";");

               for(int var5 = 0; var5 < var7.length; ++var5) {
                  var3 = var3 + "\n" + var7[var5];
               }

               this.doOutput(3, var3);
               return false;
            } else {
               if (var1.warninglist != null) {
                  for(var4 = 0; var4 < var1.warninglist.size(); ++var4) {
                     var3 = var3 + "\n" + var1.warninglist.elementAt(var4);
                  }

                  this.doOutput(3, var3);
               }

               return true;
            }
         }
      }
   }

   public int check(String var1) throws Exception {
      IErrorList var2 = ErrorList.getInstance();
      ErrorListListener4XmlSave var3 = new ErrorListListener4XmlSave(-1);
      Vector var4 = null;
      boolean var5 = false;
      CalculatorManager var6 = CalculatorManager.getInstance();
      var3.clearErrorList();
      if (var4 != null) {
         var4.clear();
      }

      ((IEventSupport)var2).addEventListener(var3);
      CalculatorManager.xml = true;
      var6.do_check_all((IResult)null, var3);
      CalculatorManager.xml = false;
      ((IEventSupport)var2).removeEventListener(var3);
      var4 = var3.getErrorList();
      if (var4.size() > 0) {
         for(int var7 = 0; var7 < var4.size() && !var5; ++var7) {
            TextWithIcon var8 = (TextWithIcon)var4.get(var7);
            var5 = var8.ii != null;
         }
      }

      String var9 = "\n[" + var1 + "] A nyomtatvány ellenőrzése az alábbi eredményt adta: ";
      if (var5) {
         for(int var10 = 0; var10 < var4.size(); ++var10) {
            var9 = var9 + "\n" + var4.elementAt(var10);
         }

         if (var1.equals("")) {
            return this.getErrorCount(var4);
         }
      } else {
         var9 = var9 + "\n(Az ellenőrzés sikeres)";
      }

      this.doOutput(!var5 ? 6 : 7, var9);
      return -1;
   }

   private void openFileOutput(String var1) throws Exception {
      String var2 = (String)PropertyList.getInstance().get("prop.usr.naplo");
      if (!var2.endsWith("\\") && !var2.endsWith("/")) {
         var2 = var2 + File.separator;
      }

      this.fOut = new NecroFile(var2 + "jabev_batch_" + Tools.getTimeStringForFiles() + "." + var1);
      this.fileOutput = new NecroFileOutputStream(this.fOut);
   }

   private void openFileOutput4OneCheck(String var1) throws Exception {
      this.fOut = new NecroFile(var1);
      this.fileOutput = new NecroFileOutputStream(this.fOut);
   }

   private void closeFileOutput() {
      try {
         this.fileOutput.flush();
      } catch (Exception var3) {
         Tools.eLog(var3, 0);
      }

      try {
         this.fileOutput.close();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   private void doOutput(int var1, String var2) {
      try {
         this.fileOutput.write((var2 + " (" + var1 + ")\n").getBytes("ISO-8859-2"));
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   private void doDsignOutput(String var1, String var2) {
      try {
         if (!var2.equals("")) {
            this.compareFiles(var1, var2, this.fileOutput);
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public String doDsign(String var1) throws Exception {
      this.openFileOutput("diff");
      this.l4bc = new FileLoader4BatchCheck();
      int var2 = this.l4bc.createList(var1);
      if (var2 != 0) {
         this.doDsignOutput(messages[var2], "");
      } else {
         this.dsignVector(var1);
      }

      return this.fOut.getAbsolutePath();
   }

   private void dsignVector(String var1) {
      File var2 = new NecroFile(var1);
      if (!var2.isDirectory()) {
         var1 = var2.getParent();
      }

      if (!var1.endsWith("\\") && !var1.endsWith("/")) {
         var1 = var1 + File.separator;
      }

      BookModel var3 = null;

      String var4;
      while((var4 = this.l4bc.nextId()) != null) {
         ++this.countAll;
         if (var4.toLowerCase().endsWith(".xml")) {
            if (var4.indexOf(File.separator) == -1) {
               var4 = var1 + var4;
            }

            BookModel var5 = this.l4bc.superLoad(var3, var4);
            if (var5.isDisabledTemplate()) {
               try {
                  if (BlacklistStore.getInstance().handleGuiMessage(var5.getTemplateId(), var5.getOrgId())) {
                     return;
                  }
               } catch (Exception var8) {
               }
            }

            var3 = var5;
            if (this.checkBookModel(var5, var4)) {
               try {
                  errorCountFromBatch = this.check("");
                  Ebev var6 = new Ebev(var5);
                  Result var7 = var6.pass(true);
                  if (var7.isOk()) {
                     this.doDsignOutput(var4, (String)var7.errorList.elementAt(0));
                  } else {
                     this.doDsignOutput(var4, "");
                  }
               } catch (Exception var9) {
                  this.doDsignOutput(var4, "");
               }
            }
         }
      }

   }

   private void compareFiles(String var1, String var2, FileOutputStream var3) throws Exception {
      XMLFileComparator var4 = new XMLFileComparator();
      File var5 = new NecroFile(var1);
      File var6 = new NecroFile(var2);
      new Vector();
      new Vector();
      new Hashtable();
      new Hashtable();
      Vector var11 = new Vector();
      var11.add("/nyomtatvanyok/nyomtatvany");
      Vector var12 = var4.differences(var5, var6, var11);
      boolean var13 = false;
      File var14 = var5;
      if (!(Boolean)var12.elementAt(0)) {
         this.wf("*" + var1 + ";" + var2);
         this.wf("{1. fájl}");
         if (var12.elementAt(1) != null) {
            var3.write(var12.elementAt(1).toString().getBytes());
         }

         if (var12.elementAt(2) != null) {
            Vector var15 = (Vector)var12.elementAt(2);

            for(int var16 = 0; var16 < var15.size(); ++var16) {
               Vector var17 = (Vector)var15.elementAt(var16);
               if (this.createDiffText2(var17, var14)) {
                  var14 = var6;
               }
            }

            this.wf("");
         }
      }

   }

   private void createDiffText(Vector var1, Vector var2, Hashtable var3, Hashtable var4) {
      int var5;
      Object var6;
      String var7;
      for(var5 = 0; var5 < var1.size(); ++var5) {
         var6 = var1.elementAt(var5);
         if (var6.equals("dummykey")) {
            this.wf("");
         } else {
            var7 = var3.get(var6) + ";";
            if (var4.containsKey(var6)) {
               var7 = var7 + var4.get(var6);
               var4.remove(var6);
            }

            this.wf(var6.toString());
            this.wf(var7);
         }
      }

      for(var5 = 0; var5 < var2.size(); ++var5) {
         var6 = var2.elementAt(var5);
         if (var4.containsKey(var6)) {
            var7 = ";" + var4.get(var6);
            this.wf(var6.toString());
            this.wf(var7);
         }
      }

   }

   private boolean createDiffText2(Vector var1, File var2) {
      boolean var3 = false;
      if (var1.elementAt(0) != var2) {
         this.wf("{2. fájl}");
         var3 = true;
      }

      String var4 = "";

      for(int var5 = 1; var5 < var1.size(); ++var5) {
         var4 = var4 + this.getStringFromVector(var1, var5);
         var4 = var4 + " ";
      }

      var4 = var4.replaceAll("\\n", "");
      var4 = var4.replaceAll("\\r", "");
      var4 = var4.replaceAll("\\t", "");
      if (var4.replaceAll(" ", "").equals("")) {
         return var3;
      } else if (this.errorFilter(var4)) {
         return var3;
      } else {
         this.wf(var4);
         return var3;
      }
   }

   private void wf(String var1) {
      try {
         this.fileOutput.write((var1 + "\n").getBytes());
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   private String getStringFromVector(Vector var1, int var2) {
      return var1.elementAt(var2) != null ? var1.elementAt(var2).toString() : "";
   }

   private boolean errorFilter(String var1) {
      for(int var2 = 0; var2 < noError.length; ++var2) {
         if (var1.indexOf(noError[var2]) > -1) {
            return true;
         }
      }

      return false;
   }

   private int getErrorCount(Vector var1) {
      try {
         int var2 = 0;

         for(int var3 = 0; var3 < var1.size(); ++var3) {
            String var4;
            if (var1.elementAt(var3) instanceof String) {
               var4 = (String)var1.elementAt(var3);
            } else {
               var4 = ((TextWithIcon)var1.elementAt(var3)).text;
            }

            if (!var4.startsWith(" > ")) {
               ++var2;
            }
         }

         return var2;
      } catch (Exception var5) {
         return 0;
      }
   }
}
