package hu.piller.enykp.alogic.filesaver.xml;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.ebev.SendParams;
import hu.piller.enykp.alogic.fileloader.xml.XMLPost;
import hu.piller.enykp.alogic.filepanels.batch.BatchCheck;
import hu.piller.enykp.alogic.fileutil.DataChecker;
import hu.piller.enykp.alogic.fileutil.DatastoreKeyToXml;
import hu.piller.enykp.alogic.fileutil.HeadChecker;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.alogic.metainfo.MetaStore;
import hu.piller.enykp.alogic.orghandler.OrgHandler;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.alogic.templateutils.TemplateUtils;
import hu.piller.enykp.datastore.CachedCollection;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.datastore.StoreItem;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.interfaces.IResult;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Sha1Hash;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.errordialog.ErrorDialog;
import hu.piller.enykp.util.base.errordialog.TextWithIcon;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileOutputStream;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

public class ISOXmlSaver implements ActionListener, DBXmlSaver {
   private static final int HEAD_ADOSZAM_INDEX = 1;
   private static final int HEAD_ADOAZON_INDEX = 2;
   private static final int HEAD_NEV_INDEX = 3;
   private static final int HEAD_FROM_INDEX = 4;
   private static final int HEAD_TO_INDEX = 5;
   private static final int HEAD_EMPNAME_INDEX = 6;
   private static final int HEAD_EMPTAXID_INDEX = 7;
   private static final int HEAD_ALBIZONYLAT_INDEX = 11;
   private static final int HEAD_ALBIZONYLAT_AZONOSITO_INDEX = 12;
   private static final int HEAD_KOZOSSEGI_ADOSZAM = 14;
   private String filename;
   private boolean itsGonnaBeAllRight = true;
   private int hashPos = 0;
   private Hashtable copyFieldsTable;
   private StoreItemComparator sic = new StoreItemComparator();
   private Hashtable commonMetaData = new Hashtable();
   private Hashtable formMetaData = new Hashtable();
   private BookModel bookModel;
   private boolean action = false;
   private boolean debug = false;
   private String xmlns;

   public ISOXmlSaver(BookModel var1) throws Exception {
      this.bookModel = var1;
      this.debug = (Boolean)PropertyList.getInstance().get("prop.dynamic.debug");
      if (var1.cc.size() > 1) {
         this.copyFieldsTable = new Hashtable();
      }

      this.xmlns = OrgHandler.getInstance().getXMLNS((String)var1.docinfo.get("org"));

      try {
         this.initData();
      } catch (Exception var3) {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "Hiányzó adatok! " + var3.getMessage(), "Nyomtatvány mentése", 0);
         throw var3;
      }
   }

   public void save(OutputStream var1) {
   }

   public Object getHelper(OutputStream var1) {
      return null;
   }

   public OutputStream getStream(String var1) {
      return null;
   }

   public String createFileName(String var1) {
      return null;
   }

   public String getFileNameSuffix() {
      return ".xml";
   }

   public String getDescription() {
      return "";
   }

   public boolean save(String var1) {
      return this.save(var1, false);
   }

   public boolean save(String var1, boolean var2) {
      return this.save(var1, var2, false).isOk();
   }

   public Result save(String var1, boolean var2, boolean var3) {
      return this.save(var1, var2, var3, (SendParams)null);
   }

   public Result save(String var1, boolean var2, boolean var3, SendParams var4) {
      return this.save(var1, var2, var3, var4, (String)null);
   }

   public Result save(String var1, boolean var2, boolean var3, SendParams var4, String var5) {
      this.action = false;
      Result var6 = this.isSavable();
      if (!var6.isOk()) {
         if (!var2) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, var6.errorList.get(0), "Mentés hiba", 0);
         }

         return var6;
      } else {
         if (!var2) {
            var6 = this.check(var2, var3);
            if (!var6.isOk()) {
               return var6;
            }
         }

         this.commonMetaData.put("hibakszama", "" + this.getErrorCount(var6.errorList));

         try {
            if (BatchCheck.errorCountFromBatch != -1) {
               this.commonMetaData.put("hibakszama", "" + BatchCheck.errorCountFromBatch);
            }
         } catch (Exception var13) {
            Tools.eLog(var13, 0);
         }

         if (!this.preCheck()) {
            var6.setOk(false);
            var6.errorList.add("Ellenőrzés hiba");
            return var6;
         } else {
            Object var7 = null;

            try {
               this.itsGonnaBeAllRight = true;
               if (var5 == null) {
                  this.setRightFilename(var1);
               } else {
                  this.filename = var5;
               }

               Result var8 = this.checkData();
               if (!var8.isOk()) {
                  if (!var2) {
                     new ErrorDialog(MainFrame.thisinstance, "Nyomtatvány mentése nem lehetséges", true, true, var8.errorList, this.bookModel.cc.getLoadedfile());
                  }

                  return var8;
               }

               var6.errorList.addAll(var8.errorList);
               if (this.bookModel.dbpkgloader != null) {
                  var7 = new ByteArrayOutputStream();
               } else if (var4 != null && var5 == null) {
                  var7 = new NecroFileOutputStream(var4.importPath + var1);
               } else {
                  var7 = new NecroFileOutputStream(this.filename);
               }

               this.saveHeaderInfo((OutputStream)var7);
               var8 = this.saveNyomtatvanyData((OutputStream)var7, this.bookModel.cc);
               if (!var8.isOk()) {
                  if (!var2) {
                     GuiUtil.showErrorDialog((Frame)null, "Hiba az XML mentése során", true, true, var8.errorList);
                  }

                  throw new Exception();
               }

               this.saveFooterInfo((OutputStream)var7);
               ((OutputStream)var7).close();
               this.hashPos += 6;
               if (this.bookModel.dbpkgloader == null) {
                  File var9;
                  long var10;
                  if (var4 == null) {
                     var9 = new NecroFile(this.filename);
                     var10 = var9.length();
                     this.createHash(this.filename, false);
                     var9 = new NecroFile(this.filename);
                     var10 = var9.length();
                  } else {
                     if (var5 == null) {
                        var9 = new NecroFile(var4.importPath + var1);
                     } else {
                        var9 = new NecroFile(var5);
                     }

                     var10 = var9.length();
                     if (var5 == null) {
                        this.createHash(var4.importPath + var1, true);
                     } else {
                        this.createHash(var5, true);
                     }

                     if (var5 == null) {
                        var9 = new NecroFile(var4.importPath + var1);
                     } else {
                        var9 = new NecroFile(var5);
                     }

                     var10 = var9.length();
                  }
               }
            } catch (Exception var14) {
               try {
                  ((OutputStream)var7).close();
               } catch (Exception var12) {
                  Tools.eLog(var12, 0);
               }

               this.itsGonnaBeAllRight = false;
               Tools.exception2SOut(var14);
            }

            if (!this.itsGonnaBeAllRight) {
               this.deleteFile();
            }

            if (!this.itsGonnaBeAllRight && !var2) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "Hiba történt a nyomtatvány mentésekor!", "Nyomtatvány mentése", 0);
            }

            var6.setOk(this.itsGonnaBeAllRight);
            if (!this.itsGonnaBeAllRight) {
               var6.errorList.add("Hiba történt a nyomtatvány mentésekor!");
            }

            if (this.bookModel.dbpkgloader != null && var6.isOk()) {
               var6.errorList.insertElementAt(((ByteArrayOutputStream)var7).toByteArray(), 0);
            }

            return var6;
         }
      }
   }

   public Object getErrorList() {
      return null;
   }

   private void setRightFilename(String var1) throws Exception {
      if (!var1.endsWith(this.getFileNameSuffix()) && !var1.endsWith(".kr")) {
         this.filename = PropertyList.USER_IN_FILENAME + var1 + this.getFileNameSuffix();
      } else {
         this.filename = var1;
         File var2 = new NecroFile(var1);
         if (var2.exists() && var2.isAbsolute()) {
            return;
         }
      }

      this.filename = this.getDsPath() + this.filename;
   }

   private Result checkData() {
      DataChecker var1 = DataChecker.getInstance();
      return var1.superCheck(this.bookModel, true);
   }

   private void saveHeaderInfo(OutputStream var1) throws IOException {
      StringBuffer var2 = new StringBuffer();
      var2.append("<?xml version=\"1.0\" encoding=\"").append("ISO-8859-2").append("\"?>\r\n").append("  <nyomtatvanyok xmlns=\"" + this.xmlns + "\"").append(">\r\n").append("  <abev>\r\n").append("    <hibakszama>").append(this.commonMetaData.get("hibakszama")).append("</hibakszama>\r\n");
      if (this.bookModel.dbpkgloader == null) {
         var2.append("    <hash>Piller KFT                              </hash>\r\n");
      } else {
         var2.append("    <hash>aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa</hash>\r\n");
      }

      var2.append("    <programverzio>").append(this.commonMetaData.get("programverzio")).append("</programverzio>\r\n").append("  </abev>\r\n");
      this.hashPos = var2.indexOf("<hash>");
      var1.write(var2.toString().getBytes("ISO-8859-2"));
   }

   private Result saveNyomtatvanyData(OutputStream var1, CachedCollection var2) throws Exception {
      int var3 = this.bookModel.getCalcelemindex();
      Result var4 = new Result();

      try {
         if (this.bookModel.get_main_index() == -1) {
            throw new Exception("A nyomtatványban nincs fődokumentum");
         }

         this.bookModel.setCalcelemindex(this.bookModel.get_main_index());
         Elem var5 = this.saveMainDocument(var2, var1);

         for(int var6 = 0; var6 < var2.size(); ++var6) {
            if (var2.get(var6) != var5) {
               this.initFormMetaData((Elem)var2.get(var6), var2.size(), ((Elem)var2.get(var6)).getEtc().get("orignote"));
               String var7 = HeadChecker.getInstance().getHeaderTagType(((Elem)var2.get(var6)).getType(), this.bookModel);
               this.saveNyomtatvanyHeadData(var1, var7);
               Elem var8 = (Elem)this.bookModel.cc.get(var6);
               this.bookModel.setCalcelemindex(var6);
               this.saveData(var1, var8, false);
            }
         }
      } catch (Exception var9) {
         this.bookModel.setCalcelemindex(var3);
         this.bookModel.resetCalc();
         throw var9;
      }

      this.bookModel.setCalcelemindex(var3);
      this.bookModel.resetCalc();
      return var4;
   }

   private Elem saveMainDocument(CachedCollection var1, OutputStream var2) throws Exception {
      Elem var3 = this.bookModel.get_main_elem();
      this.initFormMetaData(var3, 1, var3.getEtc().get("orignote"));
      Result var4 = new Result();
      this.headCheck(var4, var3.getType(), 1);
      String var5 = HeadChecker.getInstance().getHeaderTagType(var3.getType(), this.bookModel);
      this.saveNyomtatvanyHeadData(var2, var5);
      this.saveData(var2, var3, true);
      return var3;
   }

   private void saveNyomtatvanyHeadData(OutputStream var1, String var2) throws Exception {
      StringBuffer var3 = new StringBuffer();
      var3.append("  <nyomtatvany>\r\n").append("    <nyomtatvanyinformacio>\r\n").append("      <nyomtatvanyazonosito>").append(this.formMetaData.get("nyomtatvanyazonosito")).append("</nyomtatvanyazonosito>\r\n").append("      <nyomtatvanyverzio>").append(this.formMetaData.get("ver")).append("</nyomtatvanyverzio>\r\n");
      if (OrgHandler.getInstance().isNotGeneralOrg((String)this.commonMetaData.get("org"))) {
         var3.append("      <adozo>\r\n").append(!"".equals(this.formMetaData.get("nev")) ? "        <nev>" + this.formMetaData.get("nev") + "</nev>\r\n" : "").append(!"".equals(this.formMetaData.get("adoszam")) ? "        <adoszam>" + this.formMetaData.get("adoszam") + "</adoszam>\r\n" : "").append("".equals(this.formMetaData.get("adoazonosito")) && !this.debug ? "" : "        <adoazonosito>" + this.formMetaData.get("adoazonosito") + "</adoazonosito>\r\n").append("      </adozo>\r\n");
         if ("1".equals(var2)) {
            var3.append("      <munkavallalo>\r\n").append("        <nev>").append(this.formMetaData.get("empname")).append("</nev>\r\n").append("        <adoazonosito>").append(this.formMetaData.get("emptaxid")).append("</adoazonosito>\r\n").append("      </munkavallalo>\r\n");
         } else if ("2".equals(var2)) {
            var3.append("      <albizonylatazonositas>\r\n").append("        <megnevezes>").append(this.formMetaData.get("megnevezes")).append("</megnevezes>\r\n").append("        <azonosito>").append(this.formMetaData.get("azonosito")).append("</azonosito>\r\n").append("      </albizonylatazonositas>\r\n");
         }

         if (!"".equals(this.formMetaData.get("tol")) || !"".equals(this.formMetaData.get("ig"))) {
            var3.append("      <idoszak>\r\n");
            if (!"".equals(this.formMetaData.get("tol"))) {
               var3.append("        <tol>").append(this.formMetaData.get("tol")).append("</tol>\r\n");
            }

            if (!"".equals(this.formMetaData.get("ig"))) {
               var3.append("        <ig>").append(this.formMetaData.get("ig")).append("</ig>\r\n");
            }

            var3.append("      </idoszak>\r\n");
         }
      }

      var3.append(this.getNoteTag());
      var3.append("    </nyomtatvanyinformacio>\r\n");
      var1.write(var3.toString().getBytes("ISO-8859-2"));
   }

   private String getNoteTag() {
      return this.formMetaData.get("megjegyzes") != null && !"".equals(this.formMetaData.get("megjegyzes")) ? "        <megjegyzes>" + this.formMetaData.get("megjegyzes") + "</megjegyzes>\r\n" : "";
   }

   private void saveFooterInfo(OutputStream var1) throws IOException {
      var1.write("</nyomtatvanyok>\r\n".getBytes("ISO-8859-2"));
   }

   private void saveData(OutputStream var1, Elem var2, boolean var3) throws IOException {
      String var4 = var2.getType();
      Hashtable var5 = this.bookModel.get(var4).fids;
      Hashtable var6 = this.bookModel.get_enabled_fields(var2);
      MetaInfo var7 = MetaInfo.getInstance();
      MetaStore var8 = var7.getMetaStore(var4);
      Hashtable var9 = var8.getFieldMetas();
      Iterator var10 = ((IDataStore)var2.getRef()).getCaseIdIterator();
      Vector var11 = new Vector();

      while(var10.hasNext()) {
         StoreItem var12 = (StoreItem)var10.next();
         if (var12.value != null && !"".equals(var12.value) && var12.index >= 0 && !var12.code.endsWith("H") && var6.containsKey(var12.code)) {
            var11.add(var12);
         }
      }

      Collections.sort(var11, this.sic);
      var1.write("    <mezok>\n".getBytes("ISO-8859-2"));

      for(int var18 = 0; var18 < var11.size(); ++var18) {
         StoreItem var13 = (StoreItem)var11.elementAt(var18);
         if (var13.value != null && !"".equals(var13.value)) {
            Hashtable var14 = ((DataFieldModel)var5.get(var13.code)).features;
            Hashtable var15 = (Hashtable)var9.get(var13.code);
            String var16 = var3 ? this.savable2(var14, var15, (String)var13.value) : this.savable(var14, var15, var13.toString(), (String)var13.value);
            if (var16 != null) {
               String var17 = DatastoreKeyToXml.convert(var13.toString());
               if (var17.indexOf("_") <= -1) {
                  var1.write(("      <mezo eazon=\"" + var17 + "\">" + DatastoreKeyToXml.htmlConvert(var16) + "</mezo>\n").getBytes("ISO-8859-2"));
               }
            }
         }
      }

      var1.write("    </mezok>\n".getBytes("ISO-8859-2"));
      var1.write("  </nyomtatvany>\n".getBytes("ISO-8859-2"));
   }

   private void deleteFile() {
      File var1 = new NecroFile(this.filename);
      var1.delete();
   }

   private boolean preCheck() {
      return true;
   }

   private void initFormMetaData(Elem var1, int var2, Object var3) {
      this.formMetaData.clear();
      this.formMetaData.put("nyomtatvanyazonosito", var1.getType());
      this.formMetaData.put("ver", this.commonMetaData.get("ver"));
      this.formMetaData.put("hanydarab", Integer.toString(var2));
      if (var3 != null) {
         this.formMetaData.put("megjegyzes", var3);
      }

      String[] var4 = (String[])((String[])HeadChecker.getInstance().getHeadData(this.bookModel.get(var1.getType()).id, (IDataStore)var1.getRef()));

      try {
         this.formMetaData.put("nev", var4[3]);
      } catch (Exception var16) {
         this.formMetaData.put("nev", "");
      }

      try {
         this.formMetaData.put("adoszam", var4[1]);
         if ("".equals(var4[1])) {
            throw new Exception();
         }
      } catch (Exception var15) {
         try {
            if (!"".equals(var4[14])) {
               this.formMetaData.put("adoszam", var4[14]);
            }
         } catch (Exception var14) {
            this.formMetaData.put("adoszam", "");
         }
      }

      try {
         this.formMetaData.put("adoazonosito", var4[2]);
      } catch (Exception var13) {
         this.formMetaData.put("adoazonosito", "");
      }

      try {
         this.formMetaData.put("empname", var4[6]);
      } catch (Exception var12) {
         this.formMetaData.put("empname", "");
      }

      try {
         this.formMetaData.put("emptaxid", var4[7]);
      } catch (Exception var11) {
         this.formMetaData.put("emptaxid", "");
      }

      try {
         this.formMetaData.put("tol", var4[4]);
      } catch (Exception var10) {
         this.formMetaData.put("tol", "");
      }

      try {
         this.formMetaData.put("ig", var4[5]);
      } catch (Exception var9) {
         this.formMetaData.put("ig", "");
      }

      try {
         this.formMetaData.put("megnevezes", var4[11]);
      } catch (Exception var8) {
         this.formMetaData.put("megnevezes", "");
      }

      try {
         this.formMetaData.put("azonosito", var4[12]);
      } catch (Exception var7) {
         this.formMetaData.put("azonosito", "");
      }

      DatastoreKeyToXml.valueHtmlConvert(this.formMetaData);
   }

   private void initData() throws Exception {
      this.commonMetaData.put("hibakszama", "-1");
      this.commonMetaData.put("ver", this.bookModel.docinfo.get("ver"));
      this.commonMetaData.put("id", this.bookModel.id);
      this.commonMetaData.put("programverzio", "v.3.44.0");
      this.commonMetaData.put("org", OrgHandler.getInstance().getReDirectedOrgId((String)this.bookModel.docinfo.get("org")));
      if (this.bookModel.get_main_document() == null) {
         throw new Exception("A nyomtatvány nem tartalmaz fődokumentumot.");
      } else {
         String[] var1 = (String[])((String[])HeadChecker.getInstance().getHeadData(this.bookModel.get_main_formmodel().id, this.bookModel.get_main_document()));

         try {
            this.commonMetaData.put("nev", var1[3]);
         } catch (Exception var7) {
            this.commonMetaData.put("nev", "");
         }

         try {
            this.commonMetaData.put("adoszam", var1[1]);
         } catch (Exception var6) {
            this.commonMetaData.put("adoszam", "");
         }

         try {
            this.commonMetaData.put("adoazonosito", var1[2]);
         } catch (Exception var5) {
            this.commonMetaData.put("adoazonosito", "");
         }

         try {
            this.commonMetaData.put("tol", var1[4]);
         } catch (Exception var4) {
            this.commonMetaData.put("tol", "");
         }

         try {
            this.commonMetaData.put("ig", var1[5]);
         } catch (Exception var3) {
            this.commonMetaData.put("ig", "");
         }

         DatastoreKeyToXml.valueHtmlConvert(this.commonMetaData);
      }
   }

   private String getKRDir() throws Exception {
      String var1 = Tools.fillPath((String)PropertyList.getInstance().get("prop.usr.krdir"));
      if (!(new NecroFile(var1)).isDirectory()) {
         throw new FileNotFoundException("nem található krdir");
      } else {
         return var1;
      }
   }

   private String savable(Hashtable var1, Hashtable var2, String var3, String var4) {
      if (var1 != null) {
         try {
            if ("check".equalsIgnoreCase((String)var1.get("datatype"))) {
               if ("true".equalsIgnoreCase(var4)) {
                  return "X";
               }

               return null;
            }

            if ("no".equalsIgnoreCase((String)var1.get("visible"))) {
               return null;
            }
         } catch (Exception var6) {
            Tools.eLog(var6, 0);
         }
      }

      return var4;
   }

   private String savable2(Hashtable var1, Hashtable var2, String var3) {
      if (var1 != null) {
         try {
            if ("check".equals((String)var1.get("datatype"))) {
               if ("true".equalsIgnoreCase(var3)) {
                  return "X";
               }

               return null;
            }

            if ("no".equalsIgnoreCase((String)var1.get("visible"))) {
               return null;
            }
         } catch (Exception var5) {
            Tools.eLog(var5, 0);
         }
      }

      return var3;
   }

   public void createHash(String var1, boolean var2) throws Exception {
      Sha1Hash var3 = new Sha1Hash();
      String var4 = var3.toHexString(var3.createByteHash(var1));
      RandomAccessFile var5 = new RandomAccessFile(var1, "rw");
      var5.seek((long)this.hashPos);
      var5.write(var4.getBytes("ISO-8859-2"));
      var5.close();
   }

   public Result check(boolean var1) {
      return this.check(var1, false);
   }

   public Result check(boolean var1, boolean var2) {
      Result var3 = new Result();
      if (XMLPost.xmleditnemjo) {
         return var3;
      } else {
         ErrorDialog var4 = null;
         IErrorList var5 = null;
         ErrorListListener4XmlSave var6 = null;

         try {
            CalculatorManager var7 = CalculatorManager.getInstance();
            var5 = ErrorList.getInstance();
            var6 = new ErrorListListener4XmlSave(var1 ? -1 : this.bookModel.cc.size());
            ((IEventSupport)var5).addEventListener(var6);
            var7.do_check_all((IResult)null, var6);
            Vector var8 = var6.getErrorList();
            if (var8.size() > 0) {
               TextWithIcon var9 = (TextWithIcon)var8.get(var8.size() - 1);
               if (var9.ii == null) {
                  var8.remove(var8.size() - 1);
               }
            }

            if (!var1) {
               if (var6.getRealErrorExtra() > 0) {
                  var4 = new ErrorDialog(MainFrame.thisinstance, "Nyomtatvány-ellenőrzés hibalista", true, true, var8, "A nyomtatvány az alábbi hibákat tartalmazza:", true, this.bookModel.cc.getLoadedfile());
               } else {
                  var8.clear();
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "A nyomtatvány ellenőrzése megtörtént\nAz ellenőrzés nem talált hibát.", "Nyomtatvány ellenőrzés", 1);
               }

               if (var2 && MainFrame.opmode != "1" && Tools.hasFatalError(var8)) {
                  var3.setOk(false);
                  var3.errorList.add("A nyomtatvány súlyos hibát is tartalmaz, a művelet nem folytatható.");
                  Result var24 = var3;
                  return var24;
               }
            } else {
               if (var6.getFatalError() > 0) {
                  var3.setOk(false);
               }

               if (var1) {
                  var3.errorList.insertElementAt("A nyomtatvány súlyos hibát tartalmaz, a művelet nem folytatható", 0);
               }
            }

            for(int var25 = 0; var25 < var8.size(); ++var25) {
               var3.errorList.add(var8.elementAt(var25));
            }
         } catch (Exception var22) {
            Tools.eLog(var22, 0);
         } finally {
            try {
               ((IEventSupport)var5).removeEventListener(var6);
            } catch (Exception var20) {
               Tools.eLog(var20, 0);
            }

         }

         try {
            if (var4 != null) {
               var3.setOk(!var4.isProcessStoppped());
               if (var4.isProcessStoppped()) {
                  var3.errorList.clear();
                  var3.errorList.add("Felhasználói megszakítás");
               }
            }

            return var3;
         } catch (Exception var21) {
            var3.setOk(false);
            if (var4 == null || var4.isProcessStoppped()) {
               var3.errorList.clear();
               var3.errorList.add("Hiba a mentéskor");
            }

            return var3;
         }
      }
   }

   private Result isSavable() {
      TemplateUtils var1 = TemplateUtils.getInstance();
      Result var2 = new Result();

      try {
         Object[] var3 = (Object[])((Object[])var1.isSavable(this.bookModel));
         var2.setOk((Boolean)var3[0]);
         if (!var2.isOk()) {
            var2.errorList.add(var3[1]);
         }

         return var2;
      } catch (Exception var4) {
         var2.setOk(false);
         var2.errorList.add(var4.toString());
         return var2;
      }
   }

   private void headCheck(Result var1, String var2, int var3) {
      HeadChecker var4 = HeadChecker.getInstance();
      Hashtable var5 = new Hashtable();
      this.putInto(this.formMetaData.get("nev"), var5, "nev");
      this.putInto(this.formMetaData.get("adoszam"), var5, "adoszam");
      this.putInto(this.formMetaData.get("adoazonosito"), var5, "adoazonosito");
      this.putInto(this.formMetaData.get("empname"), var5, "empname");
      this.putInto(this.formMetaData.get("emptaxid"), var5, "emptaxid");
      this.putInto(this.formMetaData.get("tol"), var5, "tol");
      this.putInto(this.formMetaData.get("ig"), var5, "ig");
      String var6 = OrgHandler.getInstance().getReDirectedOrgId((String)this.bookModel.docinfo.get("org"));
      Object[] var7 = new Object[]{var5, var6, var2, new Integer(var3), this.bookModel.get_datastore()};
      Vector var8 = var4.headCheck(var7, this.bookModel);
      if (var8.size() != 0) {
         if (var4.hadFatalError()) {
            var1.setOk(false);
         }

         var1.errorList.addAll(var8);
      }

   }

   private String getDsPath() throws Exception {
      String var1 = "";
      SettingsStore var2 = SettingsStore.getInstance();

      try {
         if (var2.get("gui", "digitális_aláírás") == null) {
            throw new Exception();
         }

         if ("".equals(var2.get("gui", "digitális_aláírás"))) {
            throw new Exception();
         }

         var1 = Tools.fillPath(var2.get("gui", "digitális_aláírás"));
      } catch (Exception var4) {
         var1 = Tools.fillPath(this.getKRDir() + PropertyList.getInstance().get("prop.usr.ds_src"));
      }

      return var1;
   }

   public void actionPerformed(ActionEvent var1) {
      this.action = true;
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

   private void putInto(Object var1, Hashtable var2, String var3) {
      if (var1 != null && !"".equals((String)var1)) {
         var2.put(var3, var1);
      }

   }
}
