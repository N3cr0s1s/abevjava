package hu.piller.enykp.alogic.fileloader.db;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.fileloader.xml.XmlLoader;
import hu.piller.enykp.alogic.filesaver.xml.ErrorListListener4OnyaCheck;
import hu.piller.enykp.alogic.fileutil.ExtendedTemplateData;
import hu.piller.enykp.alogic.fileutil.HeadChecker;
import hu.piller.enykp.alogic.fileutil.TemplateChecker;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.extensions.db.DbFactory;
import hu.piller.enykp.extensions.db.IDbHandler;
import hu.piller.enykp.extensions.elogic.ELogicFactory;
import hu.piller.enykp.extensions.elogic.ElogicCaller;
import hu.piller.enykp.extensions.elogic.IELogicResult;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.interfaces.IResult;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.Map.Entry;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import me.necrocore.abevjava.NecroFile;
import org.xml.sax.InputSource;

public class OnyaDbLoader {
   private IDbHandler dbhandler;
   private BookModel bm;
   private String navAzon;
   private ErrorListListener4OnyaCheck ell4oc = new ErrorListListener4OnyaCheck();
   private boolean rolechanged;
   private static boolean tesztmode = false;
   private XmlLoader xmlLoader = new XmlLoader();
   private IErrorList el = ErrorList.getInstance();
   private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

   public OnyaDbLoader() {
      ((IEventSupport)this.el).addEventListener(this.ell4oc);
      MainFrame.createXmlCalculationRunFile();
   }

   public void doneLoop() {
      this.doStartLog();
      this.rolechanged = false;
      this.dbhandler = DbFactory.getDbHandler("hu.piller.enykpdb.defaultdbhandler.OnyaDbHandler");

      while(true) {
         while(true) {
            while(true) {
               while(true) {
                  while(true) {
                     while(true) {
                        while(true) {
                           while(true) {
                              try {
                                 long var1 = System.currentTimeMillis();
                                 Hashtable var3 = null;
                                 Result var4 = new Result();
                                 this.navAzon = "";

                                 try {
                                    var3 = this.dbhandler.getNextTask((Hashtable)null);
                                 } catch (Exception var15) {
                                    this.createWebTechnicalException(var15, "getNextTask", var4);
                                    continue;
                                 }

                                 long var5 = System.currentTimeMillis();
                                 Tools.log("TIME WATCHER - load xml from db : " + (var5 - var1));
                                 String[] var10000 = new String[]{"", "", "", ""};

                                 String[] var7;
                                 try {
                                    var7 = this.getNecesseryParams((String[])((String[])var3.get("JABEV_FUNCTIONS_ARRAY")));
                                 } catch (Exception var16) {
                                    this.createWebTechnicalException(var16, "getNecesseryParams", var4);
                                    continue;
                                 }

                                 byte[] var8 = new byte[0];

                                 try {
                                    var8 = (byte[])((byte[])var3.get("xmlData"));
                                 } catch (Exception var17) {
                                    this.createWebTechnicalException(var17, "xmlData", var4);
                                    continue;
                                 }

                                 var1 = System.currentTimeMillis();
                                 String var9 = "";

                                 try {
                                    var9 = this.getRequestedMessageIdFromXml(var7[1], var8);
                                 } catch (Exception var18) {
                                    var4.errorList.add("ERROR");
                                    var4.errorList.add(var7[0]);
                                    var4.errorList.add("ERROR");
                                    var4.errorList.add(this.formatter.format(new Date(var5)));
                                    this.createWebTechnicalException(var18, "requestMessageId", var4);
                                    continue;
                                 }

                                 OnyaXmlLoadResult var10 = null;

                                 try {
                                    this.ell4oc.setGarnituraKod((String)var3.get("garnituraKod"));
                                 } catch (Exception var13) {
                                    this.ell4oc.setGarnituraKod("");
                                 }

                                 try {
                                    var10 = this.checkXml(var7[0], var7[1], var8);
                                    var10.setRequestMessageId(var9);
                                    var4.errorList.add(var9);
                                    var4.errorList.add(var7[0]);
                                    var4.errorList.add(this.bm.docinfo.get("ver"));
                                    var4.errorList.add(this.formatter.format(new Date(var5)));
                                 } catch (Exception var19) {
                                    this.createWebTechnicalException(var19, "parseAndCheck", var4);
                                    continue;
                                 }

                                 var5 = System.currentTimeMillis();
                                 Tools.log("TIME WATCHER - load, check and recalc xml: " + (var5 - var1));
                                 ((IEventSupport)this.el).removeEventListener(this.ell4oc);

                                 try {
                                    var4.errorList.add(var10.getErrorList());
                                    var1 = System.currentTimeMillis();
                                    Vector var11 = new Vector();
                                    var11.add(this.navAzon);
                                    String var12 = this.dbhandler.xmlSave(var4, var11);
                                    var5 = System.currentTimeMillis();
                                    Tools.log("TIME WATCHER - save data to db : " + (var5 - var1));
                                    if (!"0".equals(var12)) {
                                       this.dbhandler.setGeneratorResult(-1000);
                                       this.dbhandler.setGeneratorMessage(var12);
                                       this.dbhandler.setGeneratorException((Throwable)null);
                                       continue;
                                    }
                                 } catch (Exception var14) {
                                    Tools.exception2SOut(var14);
                                    this.dbhandler.setGeneratorResult(-1000);
                                    this.dbhandler.setGeneratorMessage("Hiba az adatok mentĂ©sekor");
                                    this.dbhandler.setGeneratorException(var14);
                                    continue;
                                 }

                                 var5 = System.nanoTime();
                                 if (this.hasEndSignal()) {
                                    Tools.log("End Signal! System.exit(0)");
                                    MainFrame.onyaCheckMode = false;
                                    System.exit(0);
                                 }
                              } catch (Exception var20) {
                                 var20.printStackTrace();
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private OnyaXmlLoadResult checkXml(String var1, String var2, byte[] var3) throws Exception {
      OnyaXmlLoadResult var4 = new OnyaXmlLoadResult();

      try {
         if (this.dbhandler == null) {
            this.dbhandler = DbFactory.getDbHandler("hu.piller.enykpdb.defaultdbhandler.OnyaDbHandler");
         }

         this.el.clear();
         this.ell4oc.clearErrorList();
         MainFrame.opmode = "1";
         MainFrame.role = "1";
         String var5 = this.initBookModel(var1);
         if (!"".equals(var5)) {
            throw new Exception(var5);
         } else {
            BookModel var6 = this.xmlLoader.load(new ByteArrayInputStream(var3), var1, "", "", this.bm, var2);
            if (var6.hasError) {
               throw new Exception(var6.errormsg);
            } else {
               this.bm = var6;
               this.bm.cc.setActiveObject(this.bm.cc.get(0));
               this.bm.setCalcelemindex(0);
               this.setNavAzon();
               this.ell4oc.setMaxElemCount(this.bm.cc.size());
               Vector var7 = this.doCheck();
               this.filterErrorList(var7);
               var4.setErrorList(var7);
               var7 = null;
               IELogicResult var8 = ElogicCaller.eventBatchAfterCheck(this.bm);
               if (var8.getStatus() != 0) {
                  throw new Exception("ElogicCaller.eventBatchAfterCheck status: " + var8.getStatus());
               } else {
                  return var4;
               }
            }
         }
      } catch (Exception var9) {
         throw new Exception("Hiba az adatok betĂ¶ltĂ©sekor/ellenĹ‘rzĂ©sekor! " + var9.toString());
      }
   }

   private String getRequestedMessageIdFromXml(String var1, byte[] var2) throws Exception {
      try {
         ByteArrayInputStream var3 = new ByteArrayInputStream(var2);
         SAXParserFactory var4 = SAXParserFactory.newInstance();
         var4.setNamespaceAware(true);
         var4.setValidating(true);
         SAXParser var5 = var4.newSAXParser();
         OnyaXmlParser var6 = new OnyaXmlParser();
         InputSource var7 = new InputSource(var3);
         var7.setEncoding(var1);
         var5.parse(var7, var6);
         var3.close();
         return var6.getMessageId();
      } catch (Exception var8) {
         throw new Exception("Hiba az adatok betĂ¶ltĂ©sekor (messageId)! " + var8.toString());
      }
   }

   private String initBookModel(String var1) {
      if (this.bm != null && this.bm.getTemplateId().equals(var1)) {
         this.bm.reempty();
         return "";
      } else {
         TemplateChecker.getInstance().setNeedDialog4Download(false);
         String var2 = var1;
         String var3 = "";
         Object var4 = null;
         File var5 = null;

         try {
            ExtendedTemplateData var6 = TemplateChecker.getInstance().getTemplateFileNames(var2, var3, (String)var4);
            String var7 = var6.getTemplateFileNames()[0];
            if (var7 == null) {
               Tools.log("Nem talĂˇlhatĂł megfelelĹ‘ nyomtatvĂˇnysablon: " + var2);
               return "Nem talĂˇlhatĂł megfelelĹ‘ nyomtatvĂˇnysablon: " + var2;
            }

            if ("".equals(var7)) {
               Tools.log("Nem talĂˇlhatĂł megfelelĹ‘ nyomtatvĂˇnysablon: " + var2);
               return "Nem talĂˇlhatĂł megfelelĹ‘ nyomtatvĂˇnysablon (1): " + var2;
            }

            var5 = new NecroFile(var7);
         } catch (Exception var9) {
            Tools.exception2SOut(var9);
            return "Nem talĂˇlhatĂł megfelelĹ‘ nyomtatvĂˇnysablon (2): " + var1;
         }

         if (var5 == null) {
            return "Nem talĂˇlhatĂł megfelelĹ‘ nyomtatvĂˇnysablon (3): " + var1;
         } else if (!var5.exists()) {
            return "Nem talĂˇlhatĂł megfelelĹ‘ nyomtatvĂˇnysablon (4): " + var1;
         } else {
            if (this.bm != null && this.bm.loadedfile.getAbsolutePath().equals(var5.getAbsolutePath())) {
               if (this.rolechanged) {
                  Tools.log("Sablonvaltas rolvaltozas miatt!");
                  if (this.bm != null) {
                     this.bm.destroy();
                  }

                  this.bm = new BookModel(var5);
               } else {
                  try {
                     this.bm.reempty();
                  } catch (Exception var8) {
                     this.bm.hasError = true;
                     this.bm.errormsg = "Hibas volt az elozo beolvasas! (batch mod)";
                  }
               }
            } else {
               Tools.log("Sablon betoltese!");
               if (this.bm != null) {
                  this.bm.destroy();
               }

               this.bm = new BookModel(var5);
            }

            if (this.bm.hasError) {
               Tools.log("BookModel error=" + this.bm.errormsg);
               int var10 = this.bm.calculator.getLastElogicCallerStatus();
               this.bm.destroy();
               this.bm = null;
               return var10 != 0 ? "HibĂˇs Elogic hĂ\u00advĂˇs status: " + var10 : "Hiba a bookmodel kĂ©szĂ\u00adtĂ©sekor: " + this.bm.errormsg;
            } else {
               this.bm.setAdozovaljavit((String)null);
               this.bm.setTesztmode(tesztmode);
               this.bm.setBarcode((String)null);
               this.bm.setBizt_azon((String)null);
               this.bm.setEvent_azon((String)null);
               this.bm.setIgazgatosagi_kod((String)null);
               this.bm.cc.setNoCacheMode();
               this.bm.setCalcelemindex(0);
               return "";
            }
         }
      }
   }

   private Vector<OnyaErrorListElement> doCheck() throws Exception {
      try {
         ErrorList.getInstance().clear();
         IErrorList var1 = ErrorList.getInstance();
         ((IEventSupport)var1).addEventListener(this.ell4oc);
         IELogicResult var2 = ElogicCaller.eventAfterDataLoad(this.bm);
         if (var2.getStatus() != 0) {
            throw new Exception("ElogicCaller.eventAfterDataLoad return: " + var2.getMessage());
         }

         CalculatorManager.getInstance().do_check_all((IResult)null, this.ell4oc);
         ((IEventSupport)var1).removeEventListener(this.ell4oc);
      } catch (Exception var3) {
         throw new Exception("Hiba az ellenĹ‘rzĂ©skor : " + var3.toString());
      }

      return this.ell4oc.getErrorListForDBBatch();
   }

   private boolean hasEndSignal() {
      return !MainFrame.xmlCalculationRunFileExists();
   }

   private String[] getNecesseryParams(String[] var1) throws Exception {
      String[] var2 = new String[4];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         String var4 = var1[var3];
         if (var4.startsWith("bizonylattipus=")) {
            var2[0] = var4.substring(15);
         }

         if (var4.startsWith("encoding=")) {
            var2[1] = var4.substring(9);
         }
      }

      return var2;
   }

   private void createWebTechnicalException(Exception var1, String var2, Result var3) {
      Vector var4 = new Vector();
      OnyaErrorListElement var5 = new OnyaErrorListElement("p010", "__" + System.currentTimeMillis() + "__" + "Hiba történt az ellenőrzéskor!" + " (" + var1.toString() + ")", "B", "2", -1);
      var4.add(var5);
      var3.setOk(false);
      int var7;
      if (var3.errorList.size() < 4) {
         var3.errorList.clear();

         try {
            var3.errorList.add("");
            var3.errorList.add(this.bm.main_document_id);
            var3.errorList.add(this.bm.docinfo.get("ver"));
            var3.errorList.add(this.formatter.format(new Date(System.currentTimeMillis())));
         } catch (Exception var10) {
            var3.errorList.clear();

            for(var7 = 0; var7 < 4; ++var7) {
               var3.errorList.add("");
            }
         }
      }

      var3.errorList.add(var4);
      Tools.log("OnyaDbLoader Exception: " + var2);
      Tools.exception2SOut(var1);

      try {
         String var6 = var1.toString();

         for(var7 = 0; var7 < var1.getStackTrace().length; ++var7) {
            StackTraceElement var8 = var1.getStackTrace()[var7];
            var6 = var6 + var8.toString() + "\n";
         }

         this.dbhandler.setVPMessage(var6);
      } catch (Exception var9) {
         this.dbhandler.setVPMessage(var9.toString());
      }

      Vector var11 = new Vector();
      var11.add(this.navAzon);
      this.dbhandler.xmlSave(var3, var11);
   }

   private void filterErrorList(Vector<OnyaErrorListElement> var1) throws Exception {
      this.filterDuplication(var1);
      Vector var2 = new Vector();

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         OnyaErrorListElement var4 = (OnyaErrorListElement)var1.elementAt(var3);
         if (!"".equals(var4.getHibaKod())) {
            var2.add(var4.getHibaKod());
            Tools.log(var4.getHibaKod() + " | " + var4.getHibaSzoveg());
         }
      }

      ErrorList.getInstance().setErrorCodes(var2);
      ElogicCaller.eventBatchFilterErrorCodes(this.bm);
      Vector var6 = ELogicFactory.getELogic().getErrorCodeList();
      if (var6 == null) {
         throw new Exception("Nem sikerĂĽlt a hibalista szĹ±rĹ‘ megszerzĂ©se");
      } else {
         for(int var7 = var1.size() - 1; var7 > -1; --var7) {
            OnyaErrorListElement var5 = (OnyaErrorListElement)var1.elementAt(var7);
            if (!var6.contains(var5.getHibaKod()) && !"p010".equalsIgnoreCase(var5.getHibaKod())) {
               var1.remove(var7);
               Tools.log("REMOVE2 : " + var5.getHibaKod() + " | " + var5.getHibaSzoveg());
            }
         }

      }
   }

   private void filterDuplication(Vector<OnyaErrorListElement> var1) {
      HashMap var2 = new HashMap();

      for(int var3 = var1.size() - 1; var3 > -1; --var3) {
         OnyaErrorListElement var4 = (OnyaErrorListElement)var1.elementAt(var3);
         if (!var4.isRealError()) {
            var1.remove(var3);
            Tools.log("REMOVE1 : " + var4.getHibaKod() + " | " + var4.getHibaSzoveg());
         }

         String var5 = var4.getHibaSzoveg().replaceAll(" ", "");
         if (var2.containsKey(var5)) {
            this.handleFidList((OnyaErrorListElement)var2.get(var5), var4.getFids());
         } else {
            var2.put(var5, var4);
         }
      }

      var1.removeAllElements();
      Iterator var6 = var2.entrySet().iterator();

      while(var6.hasNext()) {
         Entry var7 = (Entry)var6.next();
         var1.add((OnyaErrorListElement) var7.getValue());
      }

   }

   private void handleFidList(OnyaErrorListElement var1, ArrayList<String> var2) {
      for(int var3 = 0; var3 < var2.size(); ++var3) {
         if (!var1.getFids().contains(var2.get(var3))) {
            var1.addFid((String)var2.get(var3));
         }
      }

   }

   private void doStartLog() {
      Tools.log("Java verziĂł=" + System.getProperty("java.version"));
      Tools.log("VerziĂł:3.44.0");
      long var1 = Runtime.getRuntime().freeMemory();
      Tools.log("Total memory=" + Runtime.getRuntime().totalMemory() + " byte");
      Tools.log("Max memory=" + Runtime.getRuntime().maxMemory() + " byte");
      Tools.log("Free memory=" + var1 + " byte  (" + var1 / 1024L / 1024L + " MB )");
      Tools.log("Processor db=" + Runtime.getRuntime().availableProcessors());
      Tools.log("recalc loop start");
   }

   private void setNavAzon() {
      Elem var1 = (Elem)this.bm.cc.get(0);
      String[] var2 = (String[])((String[])HeadChecker.getInstance().getHeadData(this.bm.get(var1.getType()).id, (IDataStore)var1.getRef()));
      this.navAzon = var2[1];
      if (this.navAzon == null || "".equals(this.navAzon)) {
         this.navAzon = var2[2];
      }

      if (this.navAzon == null) {
         this.navAzon = "";
      }

   }

   private Hashtable getDataFromString() {
      Hashtable var1 = new Hashtable();
      String[] var2 = new String[]{"bizonylattipus=21KIVA", "encoding=iso8859-2"};
      byte[] var3 = this.getXml();
      var1.put("xmlData", var3);
      var1.put("garnituraKod", "65");
      var1.put("JABEV_FUNCTIONS_ARRAY", var2);
      return var1;
   }

   private byte[] getXml() {
      return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<nyomtatvanyok xmlns=\"http://www.apeh.hu/abev/nyomtatvanyok/2005/01\">\n    <abev>\n        <hibakszama>1</hibakszama>\n        <hash>dd1216c4dfaedd55972c32000f7ce532e3b38111</hash>\n        <programverzio>v.10.0.0</programverzio>\n    </abev>\n    <nyomtatvany>\n        <nyomtatvanyinformacio>\n            <nyomtatvanyazonosito>21KIVA</nyomtatvanyazonosito>\n            <nyomtatvanyverzio>0.1</nyomtatvanyverzio>\n            <adozo>\n                <nev>ALOPET KKT</nev>\n                <adoszam>29077896143</adoszam>\n            </adozo>\n            <idoszak>\n                <tol>20210101</tol>\n                <ig>20210331</ig>\n            </idoszak>\n        </nyomtatvanyinformacio>\n        <mezok>\n            <mezo eazon=\"0A0001C001A\">29077896143</mezo>\n            <mezo eazon=\"0A0001C004A\">ALOPET KKT</mezo>\n            <mezo eazon=\"0A0001D001A\">20210101</mezo>\n            <mezo eazon=\"0A0001D002A\">20210331</mezo>\n            <mezo eazon=\"0A0001D005A\">O</mezo>\n            <mezo eazon=\"0A0001D006A\">N</mezo>\n            <mezo eazon=\"0B0001B001A\">29077896143</mezo>\n            <mezo eazon=\"0B0001B002A\">ALOPET KKT</mezo>\n            <mezo eazon=\"0B0001C0001AA\">1000</mezo>\n            <mezo eazon=\"0B0001C0001BA\">1000</mezo>\n            <mezo eazon=\"0B0001C0001CA\">1000</mezo>\n            <mezo eazon=\"0B0001C0001DA\">3000</mezo>\n            <mezo eazon=\"0B0001C0002AA\">10</mezo>\n            <mezo eazon=\"0B0001C0002BA\">10</mezo>\n            <mezo eazon=\"0B0001C0002CA\">10</mezo>\n            <mezo eazon=\"0B0001C0002DA\">30</mezo>\n            <mezo eazon=\"0B0001C0005AA\">990</mezo>\n            <mezo eazon=\"0B0001C0005BA\">990</mezo>\n            <mezo eazon=\"0B0001C0005CA\">990</mezo>\n            <mezo eazon=\"0B0001C0005DA\">2970</mezo>\n            <mezo eazon=\"0B0001C0006AA\">109</mezo>\n            <mezo eazon=\"0B0001C0006BA\">109</mezo>\n            <mezo eazon=\"0B0001C0006CA\">109</mezo>\n            <mezo eazon=\"0B0001C0006DA\">327</mezo>\n            <mezo eazon=\"0H0001B001A\">29077896143</mezo>\n            <mezo eazon=\"0H0001B002A\">ALOPET KKT</mezo>\n            <mezo eazon=\"0H0001D0001AA\">3000</mezo>\n            <mezo eazon=\"0H0001D0001BA\">2970</mezo>\n            <mezo eazon=\"0H0001D0001CA\">-30</mezo>\n            <mezo eazon=\"0H0001D0002AA\">330</mezo>\n            <mezo eazon=\"0H0001D0002BA\">327</mezo>\n            <mezo eazon=\"0H0001D0002CA\">-3</mezo>\n            <mezo eazon=\"0I0001B002A\">29077896143</mezo>\n            <mezo eazon=\"0I0001B003A\">ALOPET KKT</mezo>\n        </mezok>\n    </nyomtatvany>\n</nyomtatvanyok>".getBytes();
   }

   private byte[] getXml2() {
      return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<nyomtatvanyok xmlns=\"http://www.apeh.hu/abev/nyomtatvanyok/2005/01\">\n    <abev>\n        <hibakszama>1</hibakszama>\n        <hash>b01c6bd3ce8ea6b6f41d9ba073bbf21b21a2c23e</hash>\n        <programverzio>v.10.0.0</programverzio>\n    </abev>\n    <nyomtatvany>\n        <nyomtatvanyinformacio>\n            <nyomtatvanyazonosito>NY</nyomtatvanyazonosito>\n            <nyomtatvanyverzio>36.3</nyomtatvanyverzio>\n            <adozo>\n                <nev>ABRASITS ZOLTANNE</nev>\n                <adoazonosito>8268773235</adoazonosito>\n            </adozo>\n        </nyomtatvanyinformacio>\n        <mezok>\n            <mezo eazon=\"0A0001C001A\">8268773235</mezo>\n            <mezo eazon=\"0A0001C006A\">ABRASITS ZOLTANNE</mezo>\n            <mezo eazon=\"0A0001D0001AA\">2008</mezo>\n        </mezok>\n    </nyomtatvany>\n</nyomtatvanyok>".getBytes();
   }
}
