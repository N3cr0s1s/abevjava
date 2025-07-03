package hu.piller.enykp.alogic.fileloader.xml;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.filesaver.xml.CalculatedXmlSaver;
import hu.piller.enykp.alogic.filesaver.xml.ErrorListListener4XmlSave;
import hu.piller.enykp.alogic.fileutil.TemplateChecker;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.alogic.metainfo.MetaStore;
import hu.piller.enykp.datastore.StoreItem;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.print.generator.APdfCreator;
import hu.piller.enykp.print.generator.ExtendedPdfCreator;
import hu.piller.enykp.print.generator.OldPdfCreator;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.errordialog.TextWithIcon;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class CalculationLoader {
   private BookModel bm;
   private ErrorListListener4XmlSave ell4xs = new ErrorListListener4XmlSave(-1);
   private static boolean tesztmode = false;
   private XmlLoader xmlLoader = new XmlLoader();
   private IErrorList el = ErrorList.getInstance();
   private String programStatus = "";
   private String outputId;
   private boolean needToCreatePdf = true;
   private static final String PATH = "e:\\temp\\xmlCalculation\\result\\";

   public CalculationLoader() {
      ((IEventSupport)this.el).addEventListener(this.ell4xs);
      this.outputId = "" + System.currentTimeMillis();
   }

   public CalculationLoaderResult doneLoop(String var1, InputStream var2) throws UnsupportedEncodingException {
      String var4 = System.getProperty("java.version");
      Tools.log("Java verzió=" + var4);
      Tools.log("Verzió:3.44.0");
      long var5 = Runtime.getRuntime().freeMemory();
      Tools.log("Total memory=" + Runtime.getRuntime().totalMemory() + " byte");
      Tools.log("Max memory=" + Runtime.getRuntime().maxMemory() + " byte");
      Tools.log("Free memory=" + var5 + " byte  (" + var5 / 1024L / 1024L + " MB )");
      Tools.log("Processor db=" + Runtime.getRuntime().availableProcessors());
      Tools.log("recalc loop start");
      long var7 = System.nanoTime();
      long var9 = 0L;
      MainFrame.xmlCalculationMode = true;

      try {
         URI var11 = new URI(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
         String var12;
         if ((new File((new File(var11.getPath())).getParent() + "/temp_pic/bg.gif")).exists()) {
            var12 = (new File(var11.getPath())).getParent() + "/temp_pic/bg.gif";
            var12 = var12.startsWith("/") ? "file:" + var12 : "file:/" + var12;
            PropertyList.getInstance().set("prop.const.db.pdf_gen.bgImage", var12);
         } else {
            System.out.println("IMAGES - Ezt keresném, de nincs: " + (new File(var11.getPath())).getParent() + "/temp_pic/bg.gif");
         }

         if ((new File((new File(var11.getPath())).getParent() + "/temp_pic/navgenfejlec.png")).exists()) {
            var12 = (new File(var11.getPath())).getParent() + "/temp_pic/navgenfejlec.png";
            var12 = var12.startsWith("/") ? "file:" + var12 : "file:/" + var12;
            PropertyList.getInstance().set("prop.const.db.pdf_gen.nevGenFejlecImage", var12);
         } else {
            System.out.println("IMAGES - Ezt keresném, de nincs: " + (new File(var11.getPath())).getParent() + "/temp_pic/navgenfejlec.png");
         }
      } catch (URISyntaxException var25) {
         PropertyList.getInstance().set("prop.const.db.pdf_gen.bgImage", (Object)null);
         PropertyList.getInstance().set("prop.const.db.pdf_gen.nevGenFejlecImage", (Object)null);
      }

      this.programStatus = "";
      long var26 = System.currentTimeMillis();
      byte[] var13 = this.getDataFromFile(var2);
      if (var13 == null) {
         CalculationLoaderResult var27 = new CalculationLoaderResult();
         var27.setStatus("-101");
         return var27;
      } else {
         long var14 = System.currentTimeMillis();
         Tools.log("TIME WATCHER - load xml from stream : " + (var14 - var26));
         var26 = System.currentTimeMillis();
         Result var16 = this.doneXmlLoad(var1, "utf-8", var13, var9);
         if (!var16.isOk() && this.hasFatalError(var16.errorList)) {
            this.hiba(new Exception("Súlyos hiba!"), "-110");
         }

         var14 = System.currentTimeMillis();
         Tools.log("TIME WATCHER - load, check and recalc xml: " + (var14 - var26));
         Vector var17 = new Vector(var16.errorList);
         Object var18 = null;
         Object var19 = null;
         Object var20 = null;
         byte[] var28;
         byte[] var29;
         if ("".equals(this.programStatus)) {
            CalculatedXmlSaver var21 = new CalculatedXmlSaver(this.bm);
            var16 = var21.save();
            if (!var16.isOk()) {
               this.hiba(new Exception(this.getErrorMessage(var16)), "-105");
            }

            var28 = (byte[])((byte[])var16.errorList.get(0));
            var16.errorList.clear();
            if (this.needToCreatePdf) {
               var26 = System.currentTimeMillis();
               var16 = this.print();
               if (!var16.isOk()) {
                  var29 = null;
               } else {
                  var29 = (byte[])((byte[])var16.errorList.get(0));
               }

               var14 = System.currentTimeMillis();
               Tools.log("TIME WATCHER - create pdf : " + (var14 - var26));
            } else {
               var29 = null;
            }
         } else {
            var28 = null;
            var29 = null;
         }

         ((IEventSupport)this.el).removeEventListener(this.ell4xs);

         byte[] var30;
         try {
            new Result();
            var26 = System.currentTimeMillis();
            var30 = this.errorListXMLGenerator(var17);
            var14 = System.currentTimeMillis();
         } catch (Exception var24) {
            var24.printStackTrace();
            byte[] var22 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<hibak>\n<hiba kod=\"-107\" hibaszoveg=\"Hiba a nyomtatvány hibalista készítésekor\" szint=\"2\"/>\n</hibak>\n".getBytes("utf-8");
            CalculationLoaderResult var23 = new CalculationLoaderResult(var28, var22, var29);
            var23.setStatus("-107");
            return var23;
         }

         CalculationLoaderResult var31 = new CalculationLoaderResult(var28, var30, var29);
         if (var28 == null || var29 == null || var30 == null) {
            if (this.programStatus.length() > 3) {
               var31.setStatus(this.programStatus.substring(0, 4));
            } else {
               var31.setStatus("-105");
            }
         }

         return var31;
      }
   }

   private Result doneXmlLoad(String var1, String var2, byte[] var3, long var4) {
      Result var6 = new Result();

      try {
         this.el.clear();
         boolean var7 = false;
         this.ell4xs.clearErrorList();
         long var8 = System.nanoTime();
         MainFrame.opmode = "1";
         MainFrame.role = "0";
         String var10 = this.initBookModel(var1);
         if (!"".equals(var10)) {
            var6.errorList.insertElementAt(var10, 0);
            var6.setOk(false);
            return var6;
         }

         BookModel var11 = this.xmlLoader.load(new ByteArrayInputStream(var3), var1, "", "", this.bm, var2);
         if (var11.hasError) {
            this.hiba(new Exception(var11.errormsg), "-103");
            var6.setOk(false);
            return var6;
         }

         this.bm = var11;
         this.bm.cc.setActiveObject(this.bm.cc.get(0));
         this.bm.setCalcelemindex(0);

         try {
            MetaStore var12 = MetaInfo.getInstance().getMetaStore(this.bm.get_main_formmodel().id);
            Vector var13 = var12.getFilteredFieldMetas_And(new Vector(Arrays.asList("panids")));
            if (var13.size() > 0) {
               for(int var14 = 0; var14 < var13.size(); ++var14) {
                  Hashtable var15 = (Hashtable)var13.elementAt(var14);
                  String var16 = (String)var15.get("panids");
                  if (var16.indexOf("Adózó adóazonosító jele") > -1) {
                     var14 = var13.size();
                     System.out.println("Adoazonosito: " + this.bm.get_main_document().get("0_" + var15.get("fid")));
                  }
               }
            }
         } catch (Exception var17) {
            System.out.println("Adoazonosito: " + var17.toString());
         }

         this.setCreationDateIntoDataStore();
         var6 = this.doCalcAndCheck();
         if (!var6.isOk()) {
            return var6;
         }
      } catch (Exception var18) {
         this.hiba(var18, "-103");
         var6.errorList.insertElementAt("Hiba az xml betöltésekor!", 0);
         var6.setOk(false);
      }

      return var6;
   }

   private void errlistsave2db(Result var1) {
      Vector var2 = this.ell4xs.getErrorListForDBBatch();
      Vector var3 = new Vector(var2);
      this.ell4xs.clearErrorList();
      if (var3.size() != 0) {
         try {
            this.cleanErrorList(var3);
         } catch (Exception var5) {
            Tools.eLog(var5, 0);
         }
      }

      var1.errorList.clear();
      var1.setErrorList(var3);
      if (var3.size() > 0) {
         var1.setOk(false);
      }

   }

   private void cleanErrorList(Vector var1) {
      for(int var2 = var1.size() - 1; var2 >= 0; --var2) {
         TextWithIcon var3 = (TextWithIcon)var1.elementAt(var2);
         if (var3.ii == null) {
            var1.remove(var2);
         } else {
            for(int var4 = 0; var4 < PropertyList.ERRORLIST_PILL_SPECIAL_ERRORS.length; ++var4) {
               if (PropertyList.ERRORLIST_PILL_SPECIAL_ERRORS[var4].equals(var3.officeErrorCode)) {
                  var1.remove(var2);
               }
            }
         }
      }

   }

   private String initBookModel(String var1) {
      if (this.bm != null && this.bm.getTemplateId().equals(var1)) {
         this.bm.reempty();
         return "";
      } else {
         long var2 = System.nanoTime();
         TemplateChecker.getInstance().setNeedDialog4Download(false);
         String var4 = var1;
         String var5 = "";
         Object var6 = null;
         File var7 = null;
         long var8 = System.nanoTime();

         try {
            String var10 = TemplateChecker.getInstance().getTemplateFileNames(var4, var5, (String)var6).getTemplateFileNames()[0];
            if (var10 == null) {
               this.hiba(new Exception("Nem található megfelelő nyomtatványsablon: " + var4), "-102");
               return "Nem található megfelelő nyomtatványsablon: " + var4;
            }

            if ("".equals(var10)) {
               this.hiba(new Exception("Nem található megfelelő nyomtatványsablon: " + var4), "-102");
               return "Nem található megfelelő nyomtatványsablon (1): " + var4;
            }

            var7 = new File(var10);
         } catch (Exception var14) {
            this.hiba(var14, "-102");
            return "Nem található megfelelő nyomtatványsablon (2): " + var1;
         }

         long var15 = System.nanoTime();
         if (var7 == null) {
            this.hiba(new Exception("Nem található megfelelő nyomtatványsablon: " + var1), "-102");
            return "Nem található megfelelő nyomtatványsablon (3): " + var1;
         } else if (!var7.exists()) {
            this.hiba(new Exception("Nem található megfelelő nyomtatványsablon: " + var1), "-102");
            return "Nem található megfelelő nyomtatványsablon (4): " + var1;
         } else {
            if (this.bm != null) {
               this.bm.destroy();
            }

            this.bm = new BookModel(var7);
            if (this.bm.hasError) {
               this.hiba(new Exception("Hiba a nyomtatványsablon betöltésekor : " + this.bm.errormsg), "-102");
               this.bm.destroy();
               this.bm = null;
               return "Hiba a nyomtatványsablon betöltésekor";
            } else {
               this.bm.setAdozovaljavit((String)null);
               this.bm.setTesztmode(tesztmode);
               this.bm.setBarcode((String)null);
               this.bm.setBizt_azon((String)null);
               this.bm.setEvent_azon((String)null);
               this.bm.setIgazgatosagi_kod((String)null);
               this.bm.cc.setNoCacheMode();
               long var12 = System.nanoTime();
               this.bm.setCalcelemindex(0);
               return "";
            }
         }
      }
   }

   private Result doCalcAndCheck() {
      ((IEventSupport)this.el).addEventListener(this.ell4xs);
      Result var1 = new Result();
      boolean var2 = false;

      try {
         CalculatorManager.getInstance().multiform_calc();
      } catch (Exception var5) {
         this.hiba(new Exception("Hiba a számított mezők újraszámításakor"), "-104");
         var1.setOk(false);
         return var1;
      }

      try {
         CalculatorManager.getInstance().feltetelesErtekPreCheck();
      } catch (Exception var4) {
         this.hiba(new Exception("Hiba a FeltételesÉrték és FeltételesÉrték2 függvények előszámításánál"), "-104");
         var1.setOk(false);
         return var1;
      }

      PropertyList.getInstance().set("prop.dynamic.dirty2", Boolean.TRUE);
      int var6 = CalculatorManager.getInstance().do_fly_check_all_one(this.ell4xs);
      if (var6 != 0) {
         this.hiba(new Exception("do_fly_check_all_one return: " + var6), "-104");
         var1.setOk(false);
         return var1;
      } else {
         this.errlistsave2db(var1);
         return var1;
      }
   }

   private void copyErrorList(Vector var1, Vector var2) {
      for(int var3 = 0; var3 < var1.size(); ++var3) {
         var2.insertElementAt(var1.elementAt(var3), var3);
      }

   }

   public boolean hasEndSignal() {
      return !MainFrame.xmlCalculationRunFileExists();
   }

   public boolean isTestMode() {
      return MainFrame.ubevtesztmode;
   }

   private Result print() {
      Result var1 = new Result();
      this.bm.cc.setActiveObject(this.bm.cc.get(0));
      Object var2;
      if (this.bm.isNewStylePageBreak()) {
         System.out.println("new page attrs set");
         var2 = new ExtendedPdfCreator(this.bm, true);
      } else {
         System.out.println("NO new page attrs set");
         var2 = new OldPdfCreator(this.bm, true);
      }

      try {
         var1 = ((APdfCreator)var2).createAndCheck();
      } catch (Exception var4) {
         this.hiba(var4, "-106");
         var1.errorList.add("Hiba a pdf készítésekor!");
         var1.setOk(false);
      }

      return var1;
   }

   private void hiba(Exception var1, String var2) {
      Tools.log("HIBA - status : " + var2);
      this.programStatus = var2 + ";" + var1.getMessage();
      Tools.exception2SOut(var1);
   }

   private String getErrorMessage(Result var1) {
      String var2 = "";

      for(int var3 = 0; var3 < var1.errorList.size(); ++var3) {
         if (var1.errorList.get(var3) instanceof String) {
            var2 = var2 + var1.errorList.get(var3) + "\n";
         }
      }

      return var2;
   }

   private void setCreationDateIntoDataStore() {
      Hashtable var1 = MetaInfo.getInstance().getFieldAttributes(this.bm.get_main_formmodel().id, "panids", true);
      String var2 = this.getKeyByValue(var1, "Készítés dátuma");
      if (var2 != null) {
         String var3 = (new SimpleDateFormat("yyyyMMdd")).format(new Date());
         StoreItem var4 = new StoreItem(var2, 0, var3);
         this.bm.get_datastore().set(var4, var3);
      }
   }

   private String getKeyByValue(Hashtable var1, String var2) {
      Enumeration var3 = var1.keys();

      Object var4;
      do {
         if (!var3.hasMoreElements()) {
            return null;
         }

         var4 = var3.nextElement();
      } while(!var1.get(var4).equals(var2));

      return (String)var4;
   }

   private byte[] getDataFromFile(InputStream var1) {
      ByteArrayOutputStream var2 = null;

      Object var5;
      try {
         var2 = new ByteArrayOutputStream();
         byte[] var4 = new byte[2048];

         int var3;
         while((var3 = var1.read(var4)) != -1) {
            var2.write(var4, 0, var3);
         }

         return var2.toByteArray();
      } catch (Exception var19) {
         var5 = null;
      } finally {
         try {
            var1.close();
         } catch (Exception var18) {
            Tools.eLog(var18, 0);
         }

         try {
            var2.close();
         } catch (Exception var17) {
            Tools.eLog(var17, 0);
         }

      }

      return (byte[])var5;
   }

   private byte[] errorListXMLGenerator(Vector var1) throws Exception {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();

      try {
         var2.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<hibak>\n".getBytes("utf-8"));
         if (!"".equals(this.programStatus)) {
            var2.write(("<hiba kod=\"" + this.programStatus.substring(0, 4) + "\" hibaszoveg=\"" + this.programStatus.substring(5) + "\" szint=\"3\"/>\n").getBytes("utf-8"));
         }

         for(int var3 = 0; var3 < var1.size(); ++var3) {
            if (var1.elementAt(var3) instanceof TextWithIcon) {
               TextWithIcon var4 = (TextWithIcon)var1.elementAt(var3);
               if (var4.officeErrorCode == null) {
                  var4.officeErrorCode = "p001";
               }

               var2.write(("<hiba kod=\"" + var4.officeErrorCode + "\" hibaszoveg=\"" + var4.text.replaceAll("\n", " _ ").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "'") + "\" szint=\"" + (var4.imageType + 2) + "\"/>\n").getBytes("utf-8"));
            }
         }

         var2.write("</hibak>\n".getBytes("utf-8"));
      } finally {
         try {
            var2.close();
         } catch (Exception var10) {
         }

      }

      return var2.toByteArray();
   }

   public String xmlSave(CalculationLoaderResult var1) {
      FileOutputStream var2 = null;

      try {
         var2 = new FileOutputStream("e:\\temp\\xmlCalculation\\result\\" + this.outputId + "_data.xml");
         var2.write(var1.getCalculatedXMLData());
      } catch (Exception var56) {
         var56.printStackTrace();
      } finally {
         try {
            var2.close();
         } catch (Exception var51) {
            Tools.eLog(var51, 0);
         }

      }

      try {
         var2 = new FileOutputStream("e:\\temp\\xmlCalculation\\result\\" + this.outputId + ".pdf");
         var2.write(var1.getPdfData());
      } catch (Exception var54) {
         var54.printStackTrace();
      } finally {
         try {
            var2.close();
         } catch (Exception var50) {
            Tools.eLog(var50, 0);
         }

      }

      try {
         var2 = new FileOutputStream("e:\\temp\\xmlCalculation\\result\\" + this.outputId + "_errorList.xml");
         var2.write(var1.getErrorListXMLData());
      } catch (Exception var52) {
         var52.printStackTrace();
      } finally {
         try {
            var2.close();
         } catch (Exception var49) {
            Tools.eLog(var49, 0);
         }

      }

      return "";
   }

   private boolean hasFatalError(Vector var1) {
      if (var1 == null) {
         return false;
      } else if (var1.size() == 0) {
         return false;
      } else {
         for(int var2 = 0; var2 < var1.size(); ++var2) {
            Object var3 = var1.elementAt(var2);
            if (var3 instanceof TextWithIcon && ((TextWithIcon)var3).imageType == 1) {
               return true;
            }
         }

         return false;
      }
   }
}
