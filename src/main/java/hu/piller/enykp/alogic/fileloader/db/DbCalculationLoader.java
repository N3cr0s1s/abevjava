package hu.piller.enykp.alogic.fileloader.db;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.fileloader.xml.XmlLoader;
import hu.piller.enykp.alogic.filesaver.xml.CalculatedXmlSaver;
import hu.piller.enykp.alogic.filesaver.xml.ErrorListListener4XmlSave;
import hu.piller.enykp.alogic.fileutil.TemplateChecker;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.alogic.metainfo.MetaStore;
import hu.piller.enykp.datastore.EgyenlegData;
import hu.piller.enykp.datastore.StoreItem;
import hu.piller.enykp.extensions.db.DbFactory;
import hu.piller.enykp.extensions.db.IDbHandler;
import hu.piller.enykp.extensions.elogic.ElogicCaller;
import hu.piller.enykp.extensions.elogic.IELogicResult;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.print.generator.APdfCreator;
import hu.piller.enykp.print.generator.ExtendedPdfCreator;
import hu.piller.enykp.print.generator.OldPdfCreator;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.errordialog.TextWithIcon;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import me.necrocore.abevjava.NecroFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

public class DbCalculationLoader {
   private IDbHandler dbhandler;
   private BookModel bm;
   private ErrorListListener4XmlSave ell4xs;
   private boolean rolechanged;
   private static boolean tesztmode = false;
   private static final int ALIVE_SIGNAL_COUNT = 3;
   private int aliveSignalCount;
   private XmlLoader xmlLoader = new XmlLoader();
   private IErrorList el;
   private static final String STATUS_CALC_1 = "-101";
   private static final String STATUS_CALC_2 = "-102";
   private static final String STATUS_CALC_3 = "-103";
   private static final String STATUS_CALC_4 = "-104";
   private static final String STATUS_CALC_5 = "-105";
   private static final String STATUS_CALC_6 = "-106";
   private static final String STATUS_CALC_7 = "-107";
   private static final String STATUS_CALC_16 = "-116";
   private static final String STATUS_CALC_8 = "-108";
   private static final String STATUS_CALC_9 = "-109";
   private static final String STATUS_CALC_10 = "-110";
   private static final String STATUS_CALC_11 = "-111";
   private static final String STATUS_CALC_12 = "-112";
   private static final String STATUS_CALC_13 = "-113";
   private static final String STATUS_CALC_14 = "-114";
   private static final String STATUS_CALC_15 = "-115";
   private static final String STATUS_CALC_17 = "-117";
   private static final String STATUS_CALC_18 = "-118";
   private String programStatus = "";
   private static final String ATTR_VALUE_BEF = "befizetendő";
   private static final String ATTR_NAME_ADONEM = "vpos_adonem_kod";
   private static final String ATTR_NAME_EGYENLEG = "egyenleget_erinto";
   private static final String ATTR_NAME_RESZLET = "reszletfizetest_erinto";

   public DbCalculationLoader() {
      String var1 = null;

      try {
         IPropertyList var2 = PropertyList.getInstance();
         var1 = (String)((Vector)var2.get("prop.const.alive_signal_count")).get(0);
      } catch (Exception var4) {
         var1 = null;
      }

      try {
         this.aliveSignalCount = Integer.parseInt(var1);
         if (this.aliveSignalCount < 2) {
            this.aliveSignalCount = 2;
         }
      } catch (Exception var3) {
         this.aliveSignalCount = 3;
      }

      this.el = ErrorList.getInstance();
      this.ell4xs = new ErrorListListener4XmlSave(-1);
      ((IEventSupport)this.el).addEventListener(this.ell4xs);
      MainFrame.createXmlCalculationRunFile();
   }

   public void doneLoop() {
      String var1 = System.getProperty("java.version");
      Tools.log("Java verzió=" + var1);
      Tools.log("Verzió:3.44.0");
      long var2 = Runtime.getRuntime().freeMemory();
      Tools.log("Total memory=" + Runtime.getRuntime().totalMemory() + " byte");
      Tools.log("Max memory=" + Runtime.getRuntime().maxMemory() + " byte");
      Tools.log("Free memory=" + var2 + " byte  (" + var2 / 1024L / 1024L + " MB )");
      Tools.log("Processor db=" + Runtime.getRuntime().availableProcessors());
      Tools.log("recalc loop start");
      long var4 = System.nanoTime();
      long var6 = 0L;
      this.rolechanged = false;
      this.dbhandler = DbFactory.getDbHandler("hu.piller.enykpdb.defaultdbhandler.CalculationDbHandler");
      MainFrame.xmlCalculationMode = true;

      try {
         URI var8 = new URI(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
         String var9;
         if ((new NecroFile((new NecroFile(var8.getPath())).getParent() + "/temp_pic/bg.gif")).exists()) {
            var9 = (new NecroFile(var8.getPath())).getParent() + "/temp_pic/bg.gif";
            var9 = var9.startsWith("/") ? "file:" + var9 : "file:/" + var9;
            PropertyList.getInstance().set("prop.const.db.pdf_gen.bgImage", var9);
         } else {
            System.out.println("IMAGES - Ezt keresném, de nincs: " + (new NecroFile(var8.getPath())).getParent() + "/temp_pic/bg.gif");
         }

         if ((new NecroFile((new NecroFile(var8.getPath())).getParent() + "/temp_pic/navgenfejlec.png")).exists()) {
            var9 = (new NecroFile(var8.getPath())).getParent() + "/temp_pic/navgenfejlec.png";
            var9 = var9.startsWith("/") ? "file:" + var9 : "file:/" + var9;
            PropertyList.getInstance().set("prop.const.db.pdf_gen.nevGenFejlecImage", var9);
         } else {
            System.out.println("IMAGES - Ezt keresném, de nincs: " + (new NecroFile(var8.getPath())).getParent() + "/temp_pic/navgenfejlec.png");
         }
      } catch (URISyntaxException var23) {
         PropertyList.getInstance().set("prop.const.db.pdf_gen.bgImage", (Object)null);
         PropertyList.getInstance().set("prop.const.db.pdf_gen.nevGenFejlecImage", (Object)null);
      }

      while(true) {
         while(true) {
            this.programStatus = "";

            try {
               long var30 = System.currentTimeMillis();
               Hashtable var10 = null;

               try {
                  var10 = this.dbhandler.getNextTask((Hashtable)null);
               } catch (Exception var27) {
                  this.hiba(var27, "-101");
                  continue;
               }

               long var11 = System.currentTimeMillis();
               Tools.log("TIME WATCHER - load xml from db : " + (var11 - var30));
               String[] var10000 = new String[]{"", "", "", ""};

               String[] var13;
               try {
                  var13 = this.getNecesseryParams((String[])((String[])var10.get("JABEV_FUNCTIONS_ARRAY")));
               } catch (Exception var26) {
                  this.hiba(var26, "-102");
                  continue;
               }

               byte[] var14 = new byte[0];

               try {
                  var14 = (byte[])((byte[])var10.get("xmlData"));
               } catch (Exception var25) {
                  this.hiba(var25, "-103");
                  continue;
               }

               new Result();
               var30 = System.currentTimeMillis();

               Result var15;
               try {
                  var15 = this.doneXmlLoad(var13[0], var13[1], var14, var6);
                  if (!var15.isOk() && this.hasFatalError(var15.errorList)) {
                     this.hiba(new Exception("Súlyos hiba!"), "-110");
                  }
               } catch (Exception var24) {
                  this.hiba(var24, "-104");
                  continue;
               }

               var11 = System.currentTimeMillis();
               Tools.log("TIME WATCHER - load, check and recalc xml: " + (var11 - var30));
               Vector var16 = new Vector(var15.errorList);
               Object var17 = null;
               Object var18 = null;
               Object var19 = null;
               byte[] var31;
               byte[] var32;
               byte[] var33;
               if ("".equals(this.programStatus)) {
                  CalculatedXmlSaver var20 = new CalculatedXmlSaver(this.bm);
                  var15 = var20.save();
                  if (!var15.isOk()) {
                     this.hiba(new Exception(this.getErrorMessage(var15)), "-113");
                     var33 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<adonemEgyenlegek>\n</adonemEgyenlegek>".getBytes();
                     var15.errorList.clear();
                     var15.errorList.add("".getBytes());
                  } else {
                     try {
                        var33 = this.createEgyenlegXml(this.handleEgyenlegData());
                     } catch (Exception var22) {
                        var33 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<adonemEgyenlegek>\n</adonemEgyenlegek>".getBytes();
                     }
                  }

                  var31 = (byte[])((byte[])var15.errorList.get(0));
                  var15.errorList.clear();
                  if ("I".equals(var13[3])) {
                     var30 = System.currentTimeMillis();
                     var15 = this.print();
                     if (!var15.isOk()) {
                        var32 = "".getBytes();
                        if (var15.errorList.size() > 0 && "Header error!".equals(var15.errorList.get(0).toString())) {
                           this.hiba((Exception)null, "-118");
                        }
                     } else {
                        var32 = (byte[])((byte[])var15.errorList.get(0));
                     }

                     var11 = System.currentTimeMillis();
                     Tools.log("TIME WATCHER - create pdf : " + (var11 - var30));
                  } else {
                     var32 = "".getBytes();
                  }
               } else {
                  var31 = "".getBytes();
                  var32 = "".getBytes();
                  var33 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<adonemEgyenlegek>\n</adonemEgyenlegek>".getBytes();
               }

               ((IEventSupport)this.el).removeEventListener(this.ell4xs);

               try {
                  var15 = new Result();
                  var15.errorList.add(new BigDecimal(var13[2]));
                  var15.errorList.add(var13[0]);
                  var15.errorList.add(this.programStatus);
                  var15.errorList.add(var31);
                  var15.errorList.add(var32);
                  var15.errorList.add(var16);
                  var15.errorList.add(var33);
                  var30 = System.currentTimeMillis();
                  String var34 = this.dbhandler.xmlSave(var15, (Vector)null);
                  var11 = System.currentTimeMillis();
                  Tools.log("TIME WATCHER - save data to db : " + (var11 - var30));
                  if (!"0".equals(var34)) {
                     this.hiba(new Exception("A mentést végző tárolt eljárás hibával tért vissza: " + var34), "-115");
                     this.dbhandler.setGeneratorResult(-1000);
                     this.dbhandler.setGeneratorMessage("A mentést végző tárolt eljárás hibával tért vissza: " + var34);
                     this.dbhandler.setGeneratorException((Throwable)null);
                     continue;
                  }
               } catch (Exception var28) {
                  Tools.exception2SOut(var28);
                  this.hiba(new Exception("Hiba az adatok mentésekor"), "-117");
                  this.dbhandler.setGeneratorResult(-1000);
                  this.dbhandler.setGeneratorMessage("Hiba az adatok mentésekor");
                  this.dbhandler.setGeneratorException(var28);
                  continue;
               }

               var11 = System.nanoTime();
               if (this.hasEndSignal()) {
                  Tools.log("End Signal! System.exit(0)");
                  MainFrame.xmlCalculationMode = false;
                  System.exit(0);
               }
            } catch (Exception var29) {
               var29.printStackTrace();
            }
            break;
         }

         ++var6;
      }
   }

   private Result doneXmlLoad(String var1, String var2, byte[] var3, long var4) {
      Result var6 = new Result();

      try {
         if (this.dbhandler == null) {
            this.dbhandler = DbFactory.getDbHandler("hu.piller.enykpdb.defaultdbhandler.CalculationDbHandler");
         }

         this.el.clear();
         boolean var7 = false;
         this.ell4xs.clearErrorList();
         long var8 = System.nanoTime();
         MainFrame.opmode = "1";
         MainFrame.role = "0";
         String var10 = this.initBookModel(var1);
         if (!"".equals(var10)) {
            this.hiba(new Exception(var10), "-105");
            var6.errorList.insertElementAt(var10, 0);
            var6.setOk(false);
            return var6;
         }

         BookModel var11 = this.xmlLoader.load(new ByteArrayInputStream(var3), var1, "", "", this.bm, var2);
         if (var11.hasError) {
            this.hiba(new Exception(var11.errormsg), "-106");
            var6.errorList.insertElementAt(var11.errormsg, 0);
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
         var6 = this.doCalcAndCheck(var4);
         if (!var6.isOk()) {
            return var6;
         }

         long var19 = System.nanoTime();
         IELogicResult var20 = ElogicCaller.eventBatchAfterCheck(this.bm);
         if (var20.getStatus() != 0) {
            this.hiba(new Exception("ElogicCaller.eventBatchAfterCheck status: " + var20.getStatus()), "-112");
            var6.errorList.insertElementAt("ElogicCaller.eventBatchAfterCheck status: " + var20.getStatus(), 0);
            var6.setOk(false);
            return var6;
         }

         var8 = System.nanoTime();
      } catch (Exception var18) {
         this.hiba(var18, "-111");
         var6.errorList.insertElementAt("Hiba az adatok betöltésekor/ellenőrzésekor!", 0);
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
               Tools.log("Nem található megfelelő nyomtatványsablon: " + var4);
               return "Nem található megfelelő nyomtatványsablon: " + var4;
            }

            if ("".equals(var10)) {
               Tools.log("Nem található megfelelő nyomtatványsablon: " + var4);
               return "Nem található megfelelő nyomtatványsablon (1): " + var4;
            }

            var7 = new NecroFile(var10);
         } catch (Exception var15) {
            Tools.exception2SOut(var15);
            return "Nem található megfelelő nyomtatványsablon (2): " + var1;
         }

         long var16 = System.nanoTime();
         if (var7 == null) {
            return "Nem található megfelelő nyomtatványsablon (3): " + var1;
         } else if (!var7.exists()) {
            return "Nem található megfelelő nyomtatványsablon (4): " + var1;
         } else {
            if (this.bm != null && this.bm.loadedfile.getAbsolutePath().equals(var7.getAbsolutePath())) {
               if (this.rolechanged) {
                  Tools.log("Sablonvaltas rolvaltozas miatt!");
                  if (this.bm != null) {
                     this.bm.destroy();
                  }

                  this.bm = new BookModel(var7);
               } else {
                  try {
                     this.bm.reempty();
                  } catch (Exception var14) {
                     this.bm.hasError = true;
                     this.bm.errormsg = "Hibas volt az elozo beolvasas! (batch mod)";
                  }
               }
            } else {
               Tools.log("Sablon betoltese!");
               if (this.bm != null) {
                  this.bm.destroy();
               }

               this.bm = new BookModel(var7);
            }

            if (this.bm.hasError) {
               Tools.log("BookModel error=" + this.bm.errormsg);
               int var17 = this.bm.calculator.getLastElogicCallerStatus();
               this.bm.destroy();
               this.bm = null;
               return var17 != 0 ? "Hibás Elogic hívás status: " + var17 : "Hiba a bookmodel készítésekor: " + this.bm.errormsg;
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

   private Result doCalcAndCheck(long var1) {
      Result var3 = new Result();
      boolean var4 = false;

      try {
         CalculatorManager.getInstance().multiform_calc();
      } catch (Exception var7) {
         this.hiba(new Exception("Hiba a számított mezők újraszámításakor"), "-107");
         var3.setOk(false);
         return var3;
      }

      try {
         CalculatorManager.getInstance().feltetelesErtekPreCheck();
      } catch (Exception var6) {
         this.hiba(new Exception("Hiba a FeltételesÉrték és FeltételesÉrték2 függvények előszámításánál"), "-116");
         var3.setOk(false);
         return var3;
      }

      IELogicResult var5 = ElogicCaller.eventBatchBeforeDataLoad(this.bm);
      if (var5.getStatus() != 0) {
         this.hiba(new Exception("ElogicCaller.eventBatchBeforeDataLoad return: " + var5.getMessage()), "-108");
         var3.setOk(false);
         return var3;
      } else {
         ((IEventSupport)this.el).addEventListener(this.ell4xs);
         int var8 = CalculatorManager.getInstance().do_fly_check_all_one(this.ell4xs);
         if (var8 != 0) {
            this.hiba(new Exception("do_fly_check_all_one return: " + var8), "-109");
            var3.setOk(false);
            return var3;
         } else {
            this.errlistsave2db(var3);
            return var3;
         }
      }
   }

   private boolean hasEndSignal() {
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
         var2 = new ExtendedPdfCreator(this.bm, true);
      } else {
         var2 = new OldPdfCreator(this.bm, true);
      }

      try {
         var1 = ((APdfCreator)var2).createAndCheck();
      } catch (Exception var4) {
         this.hiba(var4, "-114");
         var1.setOk(false);
         var1.errorList.clear();
         var1.errorList.add(var4.getMessage());
      }

      return var1;
   }

   private String[] getNecesseryParams(String[] var1) {
      String[] var2 = new String[4];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         String var4 = var1[var3];
         if (var4.startsWith("bizonylattipus=")) {
            var2[0] = var4.substring(15);
         }

         if (var4.startsWith("encoding=")) {
            var2[1] = var4.substring(9);
         }

         if (var4.startsWith("ajanlat_verzio=")) {
            var2[2] = var4.substring(15);
         }

         if (var4.startsWith("needPDF=")) {
            var2[3] = var4.substring(8);
         }
      }

      return var2;
   }

   private void hiba(Exception var1, String var2) {
      Tools.log("HIBA - status : " + var2);
      this.programStatus = this.programStatus + "<status value=\"" + var2 + "\">";
      if (var1 != null) {
         try {
            StackTraceElement[] var3 = var1.getStackTrace();

            for(int var4 = 0; var4 < var3.length; ++var4) {
               StackTraceElement var5 = var3[var4];
               this.programStatus = this.programStatus + "   " + var5;
            }
         } catch (Exception var6) {
            Tools.log("Az exception nem írható ki (ex)!");
         } catch (Error var7) {
            Tools.log("Az exception nem írható ki (err)!");
         }
      }

      this.programStatus = this.programStatus + "</status>\n";
      if (var1 != null) {
         Tools.exception2SOut(var1);
      }

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

   private HashMap handleEgyenlegData() throws NumberFormatException {
      MetaStore var1 = MetaInfo.getInstance().getMetaStore(this.bm.main_document_id);
      Hashtable var2 = var1.getFieldAttributes("vpos_adonem_kod", true);
      Hashtable var3 = var1.getFieldAttributes("egyenleget_erinto", true);
      Hashtable var4 = var1.getFieldAttributes("reszletfizetest_erinto", true);
      HashMap var5 = new HashMap();
      HashMap var6 = Tools.getWP(this.bm, var2);
      Iterator var7 = var6.keySet().iterator();

      while(var7.hasNext()) {
         String var8 = (String)var7.next();
         String var9 = ((String[])var6.get(var8))[0];
         String var10 = ((String[])var6.get(var8))[1];
         EgyenlegData var11;
         if (var5.containsKey(var9)) {
            var11 = (EgyenlegData)var5.get(var9);
         } else {
            var11 = new EgyenlegData(var9, var10);
         }

         Vector var12 = this.bm.get_dfield_values(var8);
         if (var3.containsKey(var8)) {
            Iterator var13;
            String var14;
            if ("befizetendő".equals(var3.get(var8))) {
               if (var4.containsKey(var8)) {
                  var13 = var12.iterator();

                  while(var13.hasNext()) {
                     var14 = (String)var13.next();
                     var11.add2Reszletfizetendo(var14);
                  }
               } else {
                  var13 = var12.iterator();

                  while(var13.hasNext()) {
                     var14 = (String)var13.next();
                     var11.add2Befizetendo(var14);
                  }
               }
            } else {
               var13 = var12.iterator();

               while(var13.hasNext()) {
                  var14 = (String)var13.next();
                  var11.add2Visszaigenyelheto(var14);
               }
            }
         }

         if (!var11.isEmpty()) {
            System.out.println("TALÁLTAM EGYENLEGET !");
            var5.put(var9, var11);
         }
      }

      return var5;
   }

   public byte[] createEgyenlegXml(HashMap<String, EgyenlegData> var1) throws IOException {
      ByteArrayOutputStream var3 = new ByteArrayOutputStream();

      try {
         var3.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<adonemEgyenlegek>\n".getBytes("utf-8"));
         Iterator var4 = var1.keySet().iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            var3.write(((EgyenlegData)var1.get(var5)).toXmlTag().getBytes("utf-8"));
         }

         var3.write("</adonemEgyenlegek>".getBytes("utf-8"));
         byte[] var2 = var3.toByteArray();
         return var2;
      } finally {
         try {
            var3.close();
         } catch (Exception var11) {
         }

      }
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
