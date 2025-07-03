package hu.piller.enykp.alogic.filesaver.xml;

import hu.piller.enykp.alogic.fileutil.DatastoreKeyToXml;
import hu.piller.enykp.alogic.fileutil.HeadChecker;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.alogic.metainfo.MetaStore;
import hu.piller.enykp.alogic.orghandler.OrgHandler;
import hu.piller.enykp.datastore.CachedCollection;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.datastore.StoreItem;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

public class CalculatedXmlSaver {
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
   private StoreItemComparator sic = new StoreItemComparator();
   private Hashtable commonMetaData = new Hashtable();
   private Hashtable formMetaData = new Hashtable();
   private BookModel bookModel;
   private String xmlns;

   public CalculatedXmlSaver(BookModel var1) {
      this.bookModel = var1;
      this.xmlns = OrgHandler.getInstance().getXMLNS((String)var1.docinfo.get("org"));
      this.initData();
   }

   public Result save() {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      Result var2 = new Result();

      try {
         this.saveHeaderInfo(var1);
         var2 = this.saveNyomtatvanyData(var1, this.bookModel.cc);
         if (!var2.isOk()) {
            throw new Exception();
         }

         this.saveFooterInfo(var1);
         var2.errorList.add(var1.toByteArray());
      } catch (Exception var12) {
         var2.setOk(false);
         var2.errorList.clear();
         var2.errorList.add("Hiba az elkészített xml mentésekor: " + var12.toString());
         Tools.eLog(var12, 1);
      } finally {
         try {
            var1.close();
         } catch (Exception var11) {
            Tools.eLog(var11, 0);
         }

      }

      return var2;
   }

   private void saveHeaderInfo(OutputStream var1) throws IOException {
      StringBuffer var2 = new StringBuffer();
      var2.append("<?xml version=\"1.0\" encoding=\"").append("utf-8").append("\"?>\r\n").append("  <nyomtatvanyok xmlns=\"" + this.xmlns + "\"").append(">\r\n").append("  <abev>\r\n").append("    <hibakszama>").append(this.commonMetaData.get("hibakszama")).append("</hibakszama>\r\n").append("    <hash>5c6447315576e736b370fb0a7540a5d4124ff711</hash>\r\n").append("    <programverzio>").append(this.commonMetaData.get("programverzio")).append("</programverzio>\r\n").append("  </abev>\r\n");
      var1.write(var2.toString().getBytes("utf-8"));
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
         var3.append("      <adozo>\r\n").append(!"".equals(this.formMetaData.get("nev")) ? "        <nev>" + this.formMetaData.get("nev") + "</nev>\r\n" : "").append(!"".equals(this.formMetaData.get("adoszam")) ? "        <adoszam>" + this.formMetaData.get("adoszam") + "</adoszam>\r\n" : "").append(!"".equals(this.formMetaData.get("adoazonosito")) ? "        <adoazonosito>" + this.formMetaData.get("adoazonosito") + "</adoazonosito>\r\n" : "").append("      </adozo>\r\n");
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
      var1.write(var3.toString().getBytes("utf-8"));
   }

   private String getNoteTag() {
      return this.formMetaData.get("megjegyzes") != null && !"".equals(this.formMetaData.get("megjegyzes")) ? "        <megjegyzes>" + this.formMetaData.get("megjegyzes") + "</megjegyzes>\r\n" : "";
   }

   private void saveFooterInfo(OutputStream var1) throws IOException {
      var1.write("</nyomtatvanyok>\r\n".getBytes("utf-8"));
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
      var1.write("    <mezok>\n".getBytes("utf-8"));

      for(int var18 = 0; var18 < var11.size(); ++var18) {
         StoreItem var13 = (StoreItem)var11.elementAt(var18);
         if (var13.value != null && !"".equals(var13.value)) {
            Hashtable var14 = ((DataFieldModel)var5.get(var13.code)).features;
            Hashtable var15 = (Hashtable)var9.get(var13.code);
            String var16 = var3 ? this.savable2(var14, var15, (String)var13.value) : this.savable(var14, var15, var13.toString(), (String)var13.value);
            if (var16 != null) {
               String var17 = DatastoreKeyToXml.convert(var13.toString());
               if (var17.indexOf("_") <= -1) {
                  var1.write(("      <mezo eazon=\"" + var17 + "\">" + DatastoreKeyToXml.htmlConvert(var16) + "</mezo>\n").getBytes("utf-8"));
               }
            }
         }
      }

      var1.write("    </mezok>\n".getBytes("utf-8"));
      var1.write("  </nyomtatvany>\n".getBytes("utf-8"));
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

   private void initData() {
      try {
         this.commonMetaData.put("hibakszama", "-1");
         this.commonMetaData.put("ver", this.bookModel.docinfo.get("ver"));
         this.commonMetaData.put("id", this.bookModel.id);
         this.commonMetaData.put("programverzio", "v.3.44.0");
         this.commonMetaData.put("org", OrgHandler.getInstance().getReDirectedOrgId((String)this.bookModel.docinfo.get("org")));
         if (this.bookModel.get_main_document() == null) {
            this.bookModel.hasError = true;
            this.bookModel.errormsg = "A nyomtatvány nem tartalmaz fődokumentumot.";
         }

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
      } catch (Exception var8) {
         Tools.eLog(var8, 0);
         this.bookModel.hasError = true;
         this.bookModel.errormsg = "Hiba a mentés inicializálásakor";
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
            if ("check".equalsIgnoreCase((String)var1.get("datatype"))) {
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

   private void putInto(Object var1, Hashtable var2, String var3) {
      if (var1 != null && !"".equals((String)var1)) {
         var2.put(var3, var1);
      }

   }
}
