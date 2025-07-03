package hu.piller.enykp.alogic.filesaver.xml;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.ebev.SendParams;
import hu.piller.enykp.alogic.fileloader.xml.XMLPost;
import hu.piller.enykp.alogic.filepanels.batch.BatchCheck;
import hu.piller.enykp.alogic.fileutil.DataChecker;
import hu.piller.enykp.alogic.fileutil.DatastoreKeyToXml;
import hu.piller.enykp.alogic.fileutil.FileNameResolver;
import hu.piller.enykp.alogic.fileutil.HeadChecker;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.alogic.metainfo.MetaStore;
import hu.piller.enykp.alogic.orghandler.OrgHandler;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.alogic.templateutils.TemplateUtils;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.datastore.StoreItem;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.model.FormModel;
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
import java.awt.Frame;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

public class EnykXmlSaverParts {
   private static final int HEAD_ADOSZAM_INDEX = 1;
   private static final int HEAD_ADOAZON_INDEX = 2;
   private static final int HEAD_NEV_INDEX = 3;
   private static final int HEAD_FROM_INDEX = 4;
   private static final int HEAD_TO_INDEX = 5;
   private static final int HEAD_EMPNAME_INDEX = 6;
   private static final int HEAD_EMPTAXID_INDEX = 7;
   private static final int HEAD_KOZOSSEGI_ADOSZAM = 14;
   private String filename;
   private boolean itsGonnaBeAllRight = true;
   private int hashPos = 0;
   private StoreItemComparator sic = new StoreItemComparator();
   private Hashtable commonMetaData = new Hashtable();
   private Hashtable formMetaData = new Hashtable();
   private BookModel bm;
   private boolean debug = false;
   private boolean abev = true;
   private String xmlns;

   public EnykXmlSaverParts(BookModel var1, int var2) throws Exception {
      this.bm = var1;
      this.debug = (Boolean)PropertyList.getInstance().get("prop.dynamic.debug");
      this.abev = OrgHandler.getInstance().isNotGeneralOrg((String)var1.docinfo.get("org"));
      this.xmlns = OrgHandler.getInstance().getXMLNS((String)var1.docinfo.get("org"));

      try {
         this.initData(var2);
      } catch (Exception var4) {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "Hiányzó adatok! " + var4.getMessage(), "Nyomtatvány mentése", 0);
         throw var4;
      }
   }

   public String getFileNameSuffix() {
      return ".xml";
   }

   public Result save(String var1, boolean var2, boolean var3, SendParams var4, String var5) {
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
         } catch (Exception var27) {
            Tools.eLog(var27, 0);
         }

         if (!this.preCheck()) {
            var6.setOk(false);
            var6.errorList.add("Ellenőrzés hiba");
            return var6;
         } else {
            FileOutputStream var7 = null;

            label254: {
               Result var8;
               try {
                  this.itsGonnaBeAllRight = true;
                  if (var5 == null) {
                     this.setRightFilename(var1);
                  } else {
                     this.filename = var5;
                  }

                  var6 = this.checkData();
                  if (var6.isOk()) {
                     var6.errorList.removeAllElements();
                     Vector var30 = new Vector();

                     for(int var9 = 0; var9 < this.bm.cc.size(); ++var9) {
                        String var10;
                        if (var4 != null && var5 == null) {
                           var10 = this.createMultipartFilenme(var4.importPath + var1, var9);
                        } else {
                           var10 = this.createMultipartFilenme(this.filename, var9);
                        }

                        var7 = new FileOutputStream(var10);
                        Elem var11 = (Elem)this.bm.cc.get(var9);
                        FormModel var12 = this.bm.get(var11.getType());
                        this.setNewData(var12, var9);
                        this.saveHeaderInfo(var7);
                        var6 = this.saveNyomtatvanyData(var7, (Elem)this.bm.cc.get(var9));
                        if (!var6.isOk()) {
                           if (!var2) {
                              GuiUtil.showErrorDialog((Frame)null, "Hiba az XML mentése során", true, true, var6.errorList);
                           }

                           throw new Exception();
                        }

                        this.saveFooterInfo(var7);
                        this.hashPos += 6;
                        File var13;
                        if (var4 == null) {
                           var13 = new File(var10);
                           this.createHash(var10, false);
                           var30.add(var13.getName());
                        } else {
                           var13 = new File(var10);
                           this.createHash(var10, true);
                           var30.add(var13.getName());
                        }

                        try {
                           var7.close();
                        } catch (Exception var26) {
                           Tools.eLog(var26, 0);
                        }
                     }

                     var6.errorList.removeAllElements();
                     var6.errorList.addAll(var30);
                     break label254;
                  }

                  if (!var2) {
                     new ErrorDialog(MainFrame.thisinstance, "Nyomtatvány mentése nem lehetséges", true, true, var6.errorList, this.bm.cc.getLoadedfile());
                  }

                  var8 = var6;
               } catch (Exception var28) {
                  this.itsGonnaBeAllRight = false;
                  var28.printStackTrace();
                  break label254;
               } finally {
                  try {
                     var7.close();
                  } catch (Exception var25) {
                     Tools.eLog(var25, 0);
                  }

               }

               return var8;
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
         File var2 = new File(var1);
         if (var2.exists()) {
            return;
         }
      }

      this.filename = this.getDsPath() + this.filename;
   }

   private Result checkData() {
      DataChecker var1 = DataChecker.getInstance();
      return var1.superCheck(this.bm, true);
   }

   private void saveHeaderInfo(FileOutputStream var1) throws IOException {
      StringBuffer var2 = new StringBuffer();
      var2.append("<?xml version=\"1.0\" encoding=\"").append("utf-8").append("\"?>\r\n").append("  <nyomtatvanyok xmlns=\"" + this.xmlns + "\"").append(">\r\n").append("  <abev>\r\n").append("    <hibakszama>").append(this.commonMetaData.get("hibakszama")).append("</hibakszama>\r\n").append("    <hash>Piller KFT                              </hash>\r\n").append("    <programverzio>").append(this.commonMetaData.get("programverzio")).append("</programverzio>\r\n").append("  </abev>\r\n");
      this.hashPos = var2.indexOf("<hash>");
      var1.write(var2.toString().getBytes("utf-8"));
   }

   private Result saveNyomtatvanyData(FileOutputStream var1, Elem var2) throws Exception {
      Result var3 = new Result();

      try {
         try {
            this.initFormMetaData(var2, var2.getEtc().get("orignote"));
         } catch (Exception var5) {
            var3.errorList.add(var5.getMessage());
            var3.setOk(false);
            this.bm.resetCalc();
            return var3;
         }

         this.saveNyomtatvanyHeadData(var1);
         this.saveData(var1, var2);
      } catch (Exception var6) {
         this.bm.resetCalc();
         throw var6;
      }

      this.bm.resetCalc();
      return var3;
   }

   private void saveNyomtatvanyHeadData(FileOutputStream var1) throws Exception {
      StringBuffer var2 = new StringBuffer();
      var2.append("  <nyomtatvany>\r\n").append("    <nyomtatvanyinformacio>\r\n").append("      <nyomtatvanyazonosito>").append(this.formMetaData.get("nyomtatvanyazonosito")).append("</nyomtatvanyazonosito>\r\n").append("      <nyomtatvanyverzio>").append(this.formMetaData.get("ver")).append("</nyomtatvanyverzio>\r\n");
      if (OrgHandler.getInstance().isNotGeneralOrg((String)this.commonMetaData.get("org"))) {
         var2.append("      <adozo>\r\n").append(!this.formMetaData.get("nev").equals("") ? "        <nev>" + this.formMetaData.get("nev") + "</nev>\r\n" : "").append(!this.formMetaData.get("adoszam").equals("") ? "        <adoszam>" + this.formMetaData.get("adoszam") + "</adoszam>\r\n" : "").append(this.formMetaData.get("adoazonosito").equals("") && !this.debug ? "" : "        <adoazonosito>" + this.formMetaData.get("adoazonosito") + "</adoazonosito>\r\n").append("      </adozo>\r\n");
         if (!this.formMetaData.get("tol").equals("") || !this.formMetaData.get("ig").equals("")) {
            var2.append("      <idoszak>\r\n");
            if (!this.formMetaData.get("tol").equals("")) {
               var2.append("        <tol>").append(this.formMetaData.get("tol")).append("</tol>\r\n");
            }

            if (!this.formMetaData.get("ig").equals("")) {
               var2.append("        <ig>").append(this.formMetaData.get("ig")).append("</ig>\r\n");
            }

            var2.append("      </idoszak>\r\n");
         }
      }

      var2.append(this.getNoteTag());
      var2.append("    </nyomtatvanyinformacio>\r\n");
      var1.write(var2.toString().getBytes("utf-8"));
   }

   private String getNoteTag() {
      return this.formMetaData.get("megjegyzes") != null && !"".equals(this.formMetaData.get("megjegyzes")) ? "        <megjegyzes>" + this.formMetaData.get("megjegyzes") + "</megjegyzes>\r\n" : "";
   }

   private void saveFooterInfo(FileOutputStream var1) throws IOException {
      var1.write("</nyomtatvanyok>\r\n".getBytes("utf-8"));
   }

   private void saveData(FileOutputStream var1, Elem var2) throws IOException {
      String var3 = var2.getType();
      Hashtable var4 = this.bm.get(var3).fids;
      Hashtable var5 = this.bm.get_enabled_fields(var2);
      MetaInfo var6 = MetaInfo.getInstance();
      MetaStore var7 = var6.getMetaStore(var3);
      Hashtable var8 = var7.getFieldMetas();
      Iterator var9 = ((IDataStore)var2.getRef()).getCaseIdIterator();
      Vector var10 = new Vector();

      while(var9.hasNext()) {
         StoreItem var11 = (StoreItem)var9.next();
         if (var11.value != null && !var11.value.equals("") && var11.index >= 0 && !var11.code.endsWith("H") && var5.containsKey(var11.code)) {
            var10.add(var11);
         }
      }

      Collections.sort(var10, this.sic);
      var1.write("    <mezok>\n".getBytes("utf-8"));

      for(int var17 = 0; var17 < var10.size(); ++var17) {
         StoreItem var12 = (StoreItem)var10.elementAt(var17);
         if (var12.value != null && !var12.value.equals("")) {
            Hashtable var13 = ((DataFieldModel)var4.get(var12.code)).features;
            Hashtable var14 = (Hashtable)var8.get(var12.code);
            String var15 = this.savable(var13, var14, var12.toString(), (String)var12.value);
            if (var15 != null) {
               String var16 = DatastoreKeyToXml.convert(var12.toString());
               if (var16.indexOf("_") <= -1) {
                  var1.write(("      <mezo eazon=\"" + var16 + "\">" + DatastoreKeyToXml.htmlConvert(var15) + "</mezo>\n").getBytes("utf-8"));
               }
            }
         }
      }

      var1.write("    </mezok>\n".getBytes("utf-8"));
      var1.write("  </nyomtatvany>\n".getBytes("utf-8"));
   }

   private void deleteFile() {
      File var1 = new File(this.filename);
      var1.delete();
   }

   private boolean preCheck() {
      return true;
   }

   private void initFormMetaData(Elem var1, Object var2) throws Exception {
      this.formMetaData.clear();
      this.formMetaData.put("nyomtatvanyazonosito", var1.getType());

      try {
         this.formMetaData.put("ver", this.bm.get(var1.getType()).get_docinfodoc().get("ver"));
      } catch (Exception var14) {
         throw new Exception("Hibás sablon! Nem határozható meg a nyomtatvány verziója.");
      }

      this.formMetaData.put("hanydarab", "1");
      if (var2 != null && !"".equals(var2)) {
         this.formMetaData.put("megjegyzes", var2);
      }

      String[] var3 = (String[])((String[])HeadChecker.getInstance().getHeadData(this.bm.get(var1.getType()).id, (IDataStore)var1.getRef()));

      try {
         this.formMetaData.put("nev", var3[3]);
      } catch (Exception var13) {
         this.formMetaData.put("nev", "");
      }

      try {
         this.formMetaData.put("adoszam", var3[1]);
         if (var3[1].equals("")) {
            throw new Exception();
         }
      } catch (Exception var12) {
         try {
            if (!var3[14].equals("")) {
               this.formMetaData.put("adoszam", var3[14]);
            }
         } catch (Exception var11) {
            this.formMetaData.put("adoszam", "");
         }
      }

      try {
         this.formMetaData.put("adoazonosito", var3[2]);
      } catch (Exception var10) {
         this.formMetaData.put("adoazonosito", "");
      }

      try {
         this.formMetaData.put("empname", var3[6]);
      } catch (Exception var9) {
         this.formMetaData.put("empname", "");
      }

      try {
         this.formMetaData.put("emptaxid", var3[7]);
      } catch (Exception var8) {
         this.formMetaData.put("emptaxid", "");
      }

      try {
         this.formMetaData.put("tol", var3[4]);
      } catch (Exception var7) {
         this.formMetaData.put("tol", "");
      }

      try {
         this.formMetaData.put("ig", var3[5]);
      } catch (Exception var6) {
         this.formMetaData.put("ig", "");
      }

      DatastoreKeyToXml.valueHtmlConvert(this.formMetaData);
   }

   private void initData(int var1) throws Exception {
      this.commonMetaData.put("hibakszama", "-1");
      Elem var2 = (Elem)this.bm.cc.get(var1);
      FormModel var3 = this.bm.get(var2.getType());

      try {
         this.commonMetaData.put("ver", var3.get_docinfodoc().get("ver"));
      } catch (Exception var5) {
         throw new Exception("Hibás sablon! Nem határozható meg a nyomtatvány verziója.");
      }

      this.commonMetaData.put("programverzio", "v.3.44.0");
      this.commonMetaData.put("org", OrgHandler.getInstance().getReDirectedOrgId((String)this.bm.docinfo.get("org")));
      if (this.bm.get_main_document() == null) {
         throw new Exception("A nyomtatvány nem tartalmaz fődokumentumot.");
      } else {
         DatastoreKeyToXml.valueHtmlConvert(this.commonMetaData);
      }
   }

   private String getKRDir() throws Exception {
      String var1 = Tools.fillPath((String)PropertyList.getInstance().get("prop.usr.krdir"));
      if (!(new File(var1)).isDirectory()) {
         throw new FileNotFoundException("nem található krdir");
      } else {
         return var1;
      }
   }

   private String savable(Hashtable var1, Hashtable var2, String var3, String var4) {
      if (var1 != null) {
         try {
            if (((String)var1.get("datatype")).equalsIgnoreCase("check")) {
               if (var4.equalsIgnoreCase("true")) {
                  return "X";
               }

               return null;
            }

            if (((String)var1.get("visible")).equalsIgnoreCase("no")) {
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
            if (((String)var1.get("datatype")).equalsIgnoreCase("check")) {
               if (var3.equalsIgnoreCase("true")) {
                  return "X";
               }

               return null;
            }

            if (((String)var1.get("visible")).equalsIgnoreCase("no")) {
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
      var5.write(var4.getBytes("utf-8"));
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
            var6 = new ErrorListListener4XmlSave(var1 ? -1 : this.bm.cc.size());
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
                  var4 = new ErrorDialog(MainFrame.thisinstance, "Nyomtatvány-ellenőrzés hibalista", true, true, var8, "A nyomtatvány az alábbi hibákat tartalmazza:", true, this.bm.cc.getLoadedfile());
               } else {
                  var8.clear();
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "A nyomtatvány ellenőrzése megtörtént\nAz ellenőrzés nem talált hibát.", "Nyomtatvány ellenőrzés", 1);
               }

               if (var2 && this.hasFatalError(var8)) {
                  var3.setOk(false);
                  var3.errorList.add("A nyomtatvány súlyos hibát is tartalmaz, a művelet nem folytatható.");
                  Result var25 = var3;
                  return var25;
               }
            }

            for(int var24 = 0; var24 < var8.size(); ++var24) {
               var3.errorList.add(var8.elementAt(var24));
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
            } else {
               var3.setOk(true);
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
         Object[] var3 = (Object[])((Object[])var1.isSavable(this.bm));
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
      String var6 = OrgHandler.getInstance().getReDirectedOrgId((String)this.bm.docinfo.get("org"));
      Object[] var7 = new Object[]{var5, var6, var2, new Integer(var3), this.bm.get_datastore()};
      Vector var8 = var4.headCheck(var7, this.bm);
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

         if (var2.get("gui", "digitális_aláírás").equals("")) {
            throw new Exception();
         }

         var1 = Tools.fillPath(var2.get("gui", "digitális_aláírás"));
      } catch (Exception var4) {
         var1 = Tools.fillPath(this.getKRDir() + PropertyList.getInstance().get("prop.usr.ds_src"));
      }

      return var1;
   }

   private boolean hasFatalError(Vector var1) {
      int var2 = 0;

      boolean var3;
      for(var3 = false; var2 < var1.size() && !var3; ++var2) {
         if (((TextWithIcon)var1.get(var2)).imageType == 1) {
            var3 = true;
         }
      }

      return var3;
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
      if (var1 != null && !((String)var1).equals("")) {
         var2.put(var3, var1);
      }

   }

   private void setNewData(FormModel var1, int var2) {
      this.commonMetaData.put("id", var1.id);
      String[] var3 = (String[])((String[])HeadChecker.getInstance().getHeadData(var1.id, (IDataStore)((Elem)this.bm.cc.get(var2)).getRef()));

      try {
         this.commonMetaData.put("nev", var3[3]);
      } catch (Exception var9) {
         this.commonMetaData.put("nev", "");
      }

      try {
         this.commonMetaData.put("adoszam", var3[1]);
      } catch (Exception var8) {
         this.commonMetaData.put("adoszam", "");
      }

      try {
         this.commonMetaData.put("adoazonosito", var3[2]);
      } catch (Exception var7) {
         this.commonMetaData.put("adoazonosito", "");
      }

      try {
         this.commonMetaData.put("tol", var3[4]);
      } catch (Exception var6) {
         this.commonMetaData.put("tol", "");
      }

      try {
         this.commonMetaData.put("ig", var3[5]);
      } catch (Exception var5) {
         this.commonMetaData.put("ig", "");
      }

   }

   public Result saveFile(BookModel var1, String var2, boolean var3) throws Exception {
      String var4;
      if (var1.cc.getLoadedfile() != null) {
         var4 = var1.cc.getLoadedfile().getAbsolutePath();
      } else {
         FileNameResolver var6 = new FileNameResolver(var1);
         var4 = var6.generateFileName();
      }

      if (var4.indexOf(".frm.enyk") > -1) {
         var4 = var4.substring(0, var4.toLowerCase().indexOf(".frm.enyk")) + var2;
      }

      var4 = PropertyList.USER_IN_FILENAME + (new File(var4)).getName();
      Result var5 = this.save(var4, var3, true, (SendParams)null, this.filename);
      return !var5.isOk() ? var5 : var5;
   }

   private String createMultipartFilenme(String var1, int var2) {
      int var3 = var1.lastIndexOf(".");
      return var1.substring(0, var3) + "_" + var2 + "_p" + var1.substring(var3);
   }
}
