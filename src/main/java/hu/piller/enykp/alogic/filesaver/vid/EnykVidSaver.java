package hu.piller.enykp.alogic.filesaver.vid;

import hu.piller.enykp.alogic.fileutil.DataChecker;
import hu.piller.enykp.alogic.fileutil.DatastoreKeyToXml;
import hu.piller.enykp.alogic.fileutil.HeadChecker;
import hu.piller.enykp.alogic.orghandler.OrgHandler;
import hu.piller.enykp.alogic.templateutils.TemplateUtils;
import hu.piller.enykp.datastore.CachedCollection;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.datastore.StoreItem;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.interfaces.ISaveManager;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.errordialog.ErrorDialog;
import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;

public class EnykVidSaver implements ISaveManager {
   private static final int HEAD_ADOSZAM_INDEX = 1;
   private static final int HEAD_ADOAZON_INDEX = 2;
   private static final int HEAD_NEV_INDEX = 3;
   private static final int HEAD_FROM_INDEX = 4;
   private static final int HEAD_TO_INDEX = 5;
   private static final int HEAD_EMPNAME_INDEX = 6;
   private static final int HEAD_EMPTAXID_INDEX = 7;
   private static final int HEAD_ALBIZONYLAT_INDEX = 11;
   private static final int HEAD_ALBIZONYLAT_AZONOSITO_INDEX = 12;
   private String filename;
   private boolean calculated = true;
   private boolean abev = true;
   private Hashtable commonMetaData = new Hashtable();
   private Hashtable formMetaData = new Hashtable();
   private BookModel bookModel;
   private boolean silentMode;
   private Hashtable notSaved = new Hashtable();
   private String xmlns;

   public EnykVidSaver(BookModel var1, boolean var2) throws Exception {
      this.bookModel = var1;
      this.silentMode = var2;

      try {
         this.initData();
      } catch (Exception var4) {
         if (!var2) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "Hiányzó adatok!", "Nyomtatvány mentése", 0);
         }

         throw var4;
      }
   }

   public Hashtable getNotSavedData() {
      return this.notSaved;
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
      return ".frm.enyk";
   }

   public String getDescription() {
      return "";
   }

   public boolean save(String var1) {
      if (this.save(var1, -1, "", "", (Hashtable)null) != null) {
         this.bookModel.dirty = false;
         return true;
      } else {
         return false;
      }
   }

   public Result save(String var1, int var2, String var3, String var4, Hashtable var5) {
      Result var6 = new Result();
      if (this.commonMetaData.containsKey("nyomtatvanynev")) {
         this.commonMetaData.put("nyomtatvanynev", var3);
      }

      if (this.commonMetaData.containsKey("id")) {
         this.commonMetaData.put("id", var4);
      }

      if (var2 == -1) {
         var6 = this.isSavable();
         if (!var6.isOk()) {
            if (!this.silentMode) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, var6.errorList.get(0), "Mentés hiba", 0);
            }

            return var6;
         }
      }

      if (!this.preCheck()) {
         var6.setOk(false);
         var6.errorList.add("Hiányzó mentés adatok.");
         return var6;
      } else {
         FileOutputStream var7 = null;
         boolean var8 = false;

         try {
            var6.setOk(true);
            this.setRightFilename(var1);
            var6 = this.checkData(var2);
            if (!var6.isOk()) {
               if (!this.silentMode) {
                  new ErrorDialog(MainFrame.thisinstance, "Nyomtatvány mentése nem lehetséges", true, true, var6.errorList, "A nyomtatvány az alábbi hibákat tartalmazza:", false);
                  Result var9 = var6;
                  return var9;
               }

               var8 = true;
            }

            var7 = new NecroFileOutputStream(this.filename);
            this.saveHeaderInfo(var7);
            this.saveAbevInfo(var7);
            this.saveNyomtatvanyData(var7, this.bookModel.cc, var2, var5);
            this.saveFooterInfo(var7);
         } catch (Exception var22) {
            var6.setOk(false);
            if (var22.getMessage() == null) {
               var6.errorList.add(var22.toString());
            } else {
               var6.errorList.add(var22.getMessage());
            }
         } finally {
            try {
               var7.close();
            } catch (Exception var20) {
               Tools.eLog(var20, 0);
            }

         }

         if (!var6.isOk()) {
            try {
               if (var8) {
                  this.deleteFile();
               }
            } catch (Exception var21) {
               Tools.eLog(var21, 0);
            }
         }

         if (!var6.isOk()) {
            if (!this.silentMode) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "Hiba történt a nyomtatvány mentésekor!\n" + var6.errorList.get(0), "Nyomtatvány mentése", 0);
            }
         } else if (!this.silentMode) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "A nyomtatványt\n" + Tools.beautyPath(this.filename) + "\nnéven mentettük.", "Nyomtatvány mentése", 1);
         }

         if (var6.isOk()) {
            var6.errorList.clear();
            var6.errorList.add(new NecroFile(this.filename));
         }

         if (!var8) {
            var6.errorList.add(new NecroFile(this.filename));
         }

         return var6;
      }
   }

   public Object getErrorList() {
      return null;
   }

   private void setRightFilename(String var1) {
      if (var1.endsWith(this.getFileNameSuffix())) {
         this.filename = var1;
         File var2 = new NecroFile(var1);
         if (var2.exists()) {
            return;
         }
      } else {
         this.filename = var1 + this.getFileNameSuffix();
      }

      if ((new NecroFile(this.filename)).getParent() == null) {
         this.filename = PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.saves") + File.separator + this.filename;
      }
   }

   private Result checkData(int var1) {
      DataChecker var2 = DataChecker.getInstance();
      return var2.superCheck(this.bookModel, true, var1);
   }

   private void saveHeaderInfo(FileOutputStream var1) throws IOException {
      StringBuffer var2 = new StringBuffer();
      String var3 = (String)this.commonMetaData.get("template");
      if (var3.endsWith(".tem.enyk")) {
         var3 = var3.substring(0, var3.length() - ".tem.enyk".length());
      }

      var2.append(" template=\"").append(var3).append("\"");
      if (this.commonMetaData.containsKey("nyomtatvanynev")) {
         var2.append(" name=\"").append(this.commonMetaData.get("nyomtatvanynev")).append("\"");
      }

      if (this.commonMetaData.containsKey("id")) {
         var2.append(" id=\"").append(this.commonMetaData.get("id")).append("\"");
      }

      StringBuffer var4 = new StringBuffer();
      var4.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n").append("<file>\r\n").append(this.getHeadTag()).append("  <nyomtatvanyok xmlns=\"").append(this.xmlns).append("\"").append(var2).append(">\r\n");
      var1.write(var4.toString().getBytes("utf-8"));
   }

   public void saveAbevInfo(FileOutputStream var1) throws IOException {
      StringBuffer var2 = new StringBuffer();
      var2.append("    <abev>\r\n").append("      <hibakszama>").append(this.commonMetaData.get("hibakszama")).append("</hibakszama>\r\n").append(this.abev ? "      <hash>                                        </hash>\r\n" : "").append("      <programverzio>").append(this.commonMetaData.get("programverzio")).append("</programverzio>\r\n").append("    </abev>\r\n");
      var1.write(var2.toString().getBytes("utf-8"));
   }

   private void saveNyomtatvanyData(FileOutputStream var1, CachedCollection var2, int var3, Hashtable var4) throws Exception {
      int var5 = 0;
      int var6 = var2.size();
      if (var3 != -1) {
         var5 = var3;
         var6 = var3 + 1;
      }

      int var7 = -1;
      if (var3 == -1 && this.bookModel.cc.size() != 1) {
         var7 = this.bookModel.get_main_index();
         this.saveNyomtatvany(var2, var7, var1, var4);
      }

      for(int var8 = var5; var8 < var6; ++var8) {
         if (var8 != var7) {
            this.saveNyomtatvany(var2, var8, var1, var4);
         }
      }

   }

   private String getNoteTag() {
      return this.formMetaData.get("megjegyzes") != null && !"".equals(this.formMetaData.get("megjegyzes")) ? "        <megjegyzes>" + this.formMetaData.get("megjegyzes") + "</megjegyzes>\r\n" : "";
   }

   private StringBuffer getHeadTag() {
      StringBuffer var1 = new StringBuffer("  <head filetype=\"zn1811\">\r\n");
      var1.append("    <type>").append(this.commonMetaData.get("type")).append("</type>\r\n");
      var1.append("    <saved>").append((new SimpleDateFormat("yyyyMMddHHmmssSS")).format(Calendar.getInstance().getTime())).append("</saved>\r\n");
      var1.append("    <docinfo name=\"").append(this.commonMetaData.get("nyomtatvanynev")).append("\" ");
      var1.append("id=\"").append(this.commonMetaData.get("id")).append("\" ");
      var1.append("count=\"").append(this.commonMetaData.get("count")).append("\" ");
      var1.append("note=\"").append(this.commonMetaData.get("note") != null ? this.commonMetaData.get("note") : "").append("\" ");
      var1.append("ver=\"").append(this.commonMetaData.get("programverzio")).append("\" ");
      var1.append("org=\"").append(this.commonMetaData.get("org")).append("\" ");
      var1.append("templatever=\"").append(this.commonMetaData.get("ver")).append("\" ");
      var1.append("tax_number=\"").append(this.commonMetaData.get("adoszam")).append("\" ");
      var1.append("from_date=\"").append(this.commonMetaData.get("tol")).append("\" ");
      var1.append("to_date=\"").append(this.commonMetaData.get("ig")).append("\" ");
      var1.append("person_name=\"").append(this.commonMetaData.get("nev")).append("\" ");
      var1.append("account_name=\"").append(this.commonMetaData.get("adoazonosito")).append("\" ");
      var1.append("calculated=\"").append(this.calculated ? "true" : "false").append("\" ");
      var1.append("seq=\"").append(this.bookModel.cc.sequence).append("\"");
      var1.append(" />\r\n");
      var1.append("  </head>\r\n");
      return var1;
   }

   private void saveFooterInfo(FileOutputStream var1) throws IOException {
      var1.write("  </nyomtatvanyok>\r\n</file>\r\n".getBytes("utf-8"));
   }

   private void saveData(FileOutputStream var1, Elem var2) throws IOException {
      Hashtable var3 = new Hashtable();
      String var4 = var2.getType();
      FormModel var5 = this.bookModel.get(var4);
      Hashtable var6 = var5.fids;
      Iterator var7 = ((IDataStore)var2.getRef()).getCaseIdIterator();
      var1.write("      <mezok>\n".getBytes("utf-8"));

      while(var7.hasNext()) {
         StoreItem var8 = (StoreItem)var7.next();
         if (var8.value != null && !var8.value.equals("") && var8.index >= 0) {
            Hashtable var9 = ((DataFieldModel)var6.get(var8.code)).features;
            String var10 = this.savable(var9, (String)var8.value);
            if (var10 != null) {
               int var11 = this.rightVid(var9, var5, var8.code);
               if (var11 == 0) {
                  var1.write(("        <mezo vid=\"" + var8.toString().substring(0, var8.toString().indexOf("_")) + "_" + var9.get("vid") + "\">" + DatastoreKeyToXml.htmlConvert(var10) + "</mezo>\n").getBytes("utf-8"));
               } else if (var11 == -1) {
                  var3.put(var8.toString(), DatastoreKeyToXml.htmlConvert(var10));
               }
            }
         }
      }

      var1.write("      </mezok>\n".getBytes("utf-8"));
      var1.write("    </nyomtatvany>\n".getBytes("utf-8"));
      this.notSaved.put(var2, var3);
   }

   private int rightVid(Hashtable var1, FormModel var2, String var3) {
      if (!var1.containsKey("vid")) {
         return -1;
      } else if (var2.get_short_inv_fields_ht().containsKey(var3)) {
         return 1;
      } else {
         return var1.get("vid").equals("") ? -1 : 0;
      }
   }

   private void deleteFile() {
      File var1 = new NecroFile(this.filename);
      var1.delete();
   }

   private boolean preCheck() {
      if (this.commonMetaData.get("ver").equals("")) {
         return false;
      } else {
         return !this.commonMetaData.get("template").equals("");
      }
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
      } catch (Exception var14) {
         this.formMetaData.put("nev", "");
      }

      try {
         this.formMetaData.put("adoszam", var4[1]);
      } catch (Exception var13) {
         this.formMetaData.put("adoszam", "");
      }

      try {
         this.formMetaData.put("adoazonosito", var4[2]);
      } catch (Exception var12) {
         this.formMetaData.put("adoazonosito", "");
      }

      try {
         this.formMetaData.put("empname", var4[6]);
      } catch (Exception var11) {
         this.formMetaData.put("empname", "");
      }

      try {
         this.formMetaData.put("emptaxid", var4[7]);
      } catch (Exception var10) {
         this.formMetaData.put("emptaxid", "");
      }

      try {
         this.formMetaData.put("tol", var4[4]);
      } catch (Exception var9) {
         this.formMetaData.put("tol", "");
      }

      try {
         this.formMetaData.put("ig", var4[5]);
      } catch (Exception var8) {
         this.formMetaData.put("ig", "");
      }

      try {
         this.formMetaData.put("megnevezes", var4[11]);
      } catch (Exception var7) {
         this.formMetaData.put("megnevezes", "");
      }

      try {
         this.formMetaData.put("azonosito", var4[12]);
      } catch (Exception var6) {
         this.formMetaData.put("azonosito", "");
      }

      DatastoreKeyToXml.valueHtmlConvert(this.formMetaData);
   }

   private void initData() throws Exception {
      this.abev = OrgHandler.getInstance().isNotGeneralOrg((String)this.bookModel.docinfo.get("org"));
      this.xmlns = OrgHandler.getInstance().getXMLNS((String)this.bookModel.docinfo.get("org"));
      this.commonMetaData.put("hibakszama", "-1");
      this.commonMetaData.put("type", this.bookModel.head.get("type"));
      this.commonMetaData.put("ver", this.bookModel.docinfo.get("ver"));
      this.commonMetaData.put("id", this.bookModel.id);
      this.commonMetaData.put("nyomtatvanynev", this.bookModel.name);
      this.commonMetaData.put("programverzio", "v.3.44.0");
      this.commonMetaData.put("org", OrgHandler.getInstance().getReDirectedOrgId((String)this.bookModel.docinfo.get("org")));
      this.commonMetaData.put("count", Integer.toString(this.bookModel.cc.size()));
      this.commonMetaData.put("template", this.bookModel.loadedfile.getName().substring(0, this.bookModel.loadedfile.getName().indexOf(".tem.enyk")));
      IDataStore var1 = this.bookModel.get_main_document();
      if (var1 == null) {
         var1 = this.bookModel.get_datastore();
      }

      String[] var2;
      if (this.bookModel.get_main_index() == -1) {
         var2 = (String[])((String[])HeadChecker.getInstance().getHeadData(((Elem)this.bookModel.cc.get(0)).getType(), var1));
      } else {
         var2 = (String[])((String[])HeadChecker.getInstance().getHeadData(this.bookModel.main_document_id, var1));
      }

      try {
         this.commonMetaData.put("nev", var2[3]);
      } catch (Exception var12) {
         this.commonMetaData.put("nev", "");
      }

      try {
         this.commonMetaData.put("adoszam", var2[1]);
      } catch (Exception var11) {
         this.commonMetaData.put("adoszam", "");
      }

      try {
         this.commonMetaData.put("adoazonosito", var2[2]);
      } catch (Exception var10) {
         this.commonMetaData.put("adoazonosito", "");
      }

      try {
         this.commonMetaData.put("tol", var2[4]);
      } catch (Exception var9) {
         this.commonMetaData.put("tol", "");
      }

      try {
         this.commonMetaData.put("ig", var2[5]);
      } catch (Exception var8) {
         this.commonMetaData.put("ig", "");
      }

      try {
         Object var3;
         try {
            var3 = this.bookModel.get_main_elem().getEtc().get("orignote");
            if (var3 == null) {
               var3 = "";
            }
         } catch (Exception var6) {
            var3 = "";
         }

         this.commonMetaData.put("note", var3);
      } catch (Exception var7) {
         Tools.eLog(var7, 0);
      }

      try {
         this.calculated = !(Boolean)PropertyList.getInstance().get("prop.dynamic.dirty2");
      } catch (Exception var5) {
         this.calculated = true;
      }

      DatastoreKeyToXml.valueHtmlConvert(this.commonMetaData);
   }

   private String savable(Hashtable var1, String var2) {
      if (var1 != null) {
         try {
            if (((String)var1.get("datatype")).equalsIgnoreCase("check")) {
               if (var2.equalsIgnoreCase("true")) {
                  return "X";
               }

               return null;
            }
         } catch (Exception var4) {
            Tools.eLog(var4, 0);
         }
      }

      return var2;
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

   private void saveNyomtatvany(CachedCollection var1, int var2, FileOutputStream var3, Hashtable var4) throws Exception {
      this.initFormMetaData((Elem)var1.get(var2), var1.size(), ((Elem)var1.get(var2)).getEtc().get("orignote"));
      if (var4 != null && var4.containsKey(this.formMetaData.get("nyomtatvanyazonosito"))) {
         this.formMetaData.put("nyomtatvanyazonosito", var4.get(this.formMetaData.get("nyomtatvanyazonosito")));
      }

      String var5 = HeadChecker.getInstance().getHeaderTagType(((Elem)var1.get(var2)).getType(), this.bookModel);
      StringBuilder var6 = new StringBuilder();
      ((Elem)var1.get(var2)).getEtc().put("nev_vid", "");
      ((Elem)var1.get(var2)).getEtc().put("id_vid", "");
      var6.append("    <nyomtatvany");
      if (((Elem)var1.get(var2)).getEtc().containsKey("sn")) {
         var6.append(" sn=\"").append(((Elem)var1.get(var2)).getEtc().get("sn")).append("\"");
      }

      var6.append(">\r\n").append("      <nyomtatvanyinformacio>\r\n").append("        <nyomtatvanyazonosito>").append(this.formMetaData.get("nyomtatvanyazonosito")).append("</nyomtatvanyazonosito>\r\n").append("        <nyomtatvanyverzio>").append(this.formMetaData.get("ver")).append("</nyomtatvanyverzio>\r\n");
      if (this.abev) {
         var6.append("        <adozo>\r\n").append("          <nev>").append(this.commonMetaData.get("nev")).append("</nev>\r\n").append(!this.commonMetaData.get("adoszam").equals("") ? "          <adoszam>" + this.commonMetaData.get("adoszam") + "</adoszam>\r\n" : "").append(!this.commonMetaData.get("adoazonosito").equals("") ? "          <adoazonosito>" + this.commonMetaData.get("adoazonosito") + "</adoazonosito>\r\n" : "").append("        </adozo>\r\n");
         if (var5.equals("1")) {
            var6.append("      <munkavallalo>\r\n").append("        <nev>").append(this.formMetaData.get("empname")).append("</nev>\r\n").append("        <adoazonosito>").append(this.formMetaData.get("emptaxid")).append("</adoazonosito>\r\n").append("      </munkavallalo>\r\n");
            ((Elem)var1.get(var2)).getEtc().put("nev_vid", this.formMetaData.get("empname"));
            ((Elem)var1.get(var2)).getEtc().put("id_vid", this.formMetaData.get("emptaxid"));
         } else if (var5.equals("2")) {
            var6.append("      <albizonylatazonositas>\r\n").append("        <megnevezes>").append(this.formMetaData.get("megnevezes")).append("</megnevezes>\r\n").append("        <azonosito>").append(this.formMetaData.get("azonosito")).append("</azonosito>\r\n").append("      </albizonylatazonositas>\r\n");
            ((Elem)var1.get(var2)).getEtc().put("nev_vid", this.formMetaData.get("megnevezes"));
            ((Elem)var1.get(var2)).getEtc().put("id_vid", this.formMetaData.get("azonosito"));
         }

         var6.append("        <idoszak>\r\n").append("          <tol>").append(this.formMetaData.get("tol")).append("</tol>\r\n").append("          <ig>").append(this.formMetaData.get("ig")).append("</ig>\r\n").append("        </idoszak>\r\n");
      }

      var6.append(this.getNoteTag());
      var6.append("      </nyomtatvanyinformacio>\r\n");
      var3.write(var6.toString().getBytes("utf-8"));
      Elem var7 = (Elem)this.bookModel.cc.get(var2);
      this.saveData(var3, var7);
   }
}
