package hu.piller.enykp.alogic.filesaver.enykinner;

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
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.interfaces.ISaveManager;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.errordialog.ErrorDialog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

public class EnykInnerSaver implements ISaveManager {
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
   private String xmlns;

   public EnykInnerSaver(BookModel var1) throws Exception {
      this.bookModel = var1;

      try {
         this.initData();
      } catch (Exception var3) {
         throw var3;
      }
   }

   public EnykInnerSaver(BookModel var1, boolean var2) throws Exception {
      this.bookModel = var1;

      try {
         this.initData();
      } catch (Exception var4) {
         if (!var2) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "Hiányzó adatok!", "Nyomtatvány mentése", 0);
         }

         throw var4;
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
      return ".frm.enyk";
   }

   public String getDescription() {
      return "";
   }

   public boolean save(String var1) {
      if (this.save(var1, -1, false) != null) {
         this.bookModel.dirty = false;
         return true;
      } else {
         return false;
      }
   }

   public File save(String var1, int var2) {
      if (var2 != -1) {
         this.removeCountAttachmentAndKrFilenameInfo();
      }

      Result var3 = this.save(var1, var2, false);
      if (var3.isOk()) {
         if (this.bookModel.cc.getLoadedfile() != null && this.bookModel.cc.getLoadedfile().getAbsolutePath().equals(var1)) {
            this.bookModel.dirty = false;
         }

         return (File)var3.errorList.get(0);
      } else {
         return null;
      }
   }

   public boolean save(String var1, boolean var2) {
      if (this.save(var1, -1, var2) != null) {
         this.bookModel.dirty = false;
         return true;
      } else {
         return false;
      }
   }

   public Result save(String var1, int var2, boolean var3) {
      Result var4 = new Result();
      if (var2 == -1) {
         var4 = this.isSavable();
         if (!var4.isOk()) {
            if (!var3) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, var4.errorList.get(0), "Mentés hiba", 0);
            }

            return var4;
         }
      }

      if (!this.preCheck()) {
         var4.setOk(false);
         var4.errorList.add("Hiányzó mentés adatok.");
         return var4;
      } else {
         FileOutputStream var5 = null;
         boolean var6 = false;

         try {
            var4.setOk(true);
            this.setRightFilename(var1);
            var4 = this.checkData(var2);
            if (!var4.isOk()) {
               if (!var3) {
                  new ErrorDialog(MainFrame.thisinstance, "Nyomtatvány mentése nem lehetséges", true, true, var4.errorList, "A nyomtatvány az alábbi hibákat tartalmazza:", false);
                  Result var7 = var4;
                  return var7;
               }

               var6 = true;
            }

            var5 = new FileOutputStream(this.filename);
            this.saveHeaderInfo(var5);
            this.saveAbevInfo(var5);
            this.saveNyomtatvanyData(var5, this.bookModel.cc, var2);
            this.saveFooterInfo(var5);
         } catch (Exception var20) {
            var4.setOk(false);
            if (var20.getMessage() == null) {
               var4.errorList.add(var20.toString());
            } else {
               var4.errorList.add(var20.getMessage());
            }
         } finally {
            try {
               var5.close();
            } catch (Exception var18) {
               Tools.eLog(var18, 0);
            }

         }

         if (!var4.isOk()) {
            try {
               if (var6) {
                  this.deleteFile();
               }
            } catch (Exception var19) {
               Tools.eLog(var19, 0);
            }
         }

         if (!var4.isOk()) {
            if (!var3) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "Hiba történt a nyomtatvány mentésekor!\n" + var4.errorList.get(0), "Nyomtatvány mentése", 0);
            }
         } else if (!var3) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "A nyomtatványt\n" + Tools.beautyPath(this.filename) + "\nnéven mentettük.", "Nyomtatvány mentése", 1);
         }

         if (var4.isOk()) {
            var4.errorList.clear();
            var4.errorList.add(new File(this.filename));
         }

         if (!var6) {
            var4.errorList.add(new File(this.filename));
         }

         return var4;
      }
   }

   public Object getErrorList() {
      return null;
   }

   private void setRightFilename(String var1) {
      File var2;
      if (var1.endsWith(this.getFileNameSuffix())) {
         this.filename = var1;
         var2 = new File(var1);
         if (var2.exists()) {
            return;
         }
      } else {
         this.filename = var1 + this.getFileNameSuffix();
      }

      if ((new File(this.filename)).getParent() == null) {
         this.filename = PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.saves") + File.separator + this.filename;
         var2 = new File(this.filename);

         for(int var3 = 1; var2.exists(); ++var3) {
            this.filename = PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.saves") + File.separator + var1 + "_" + var3 + this.getFileNameSuffix();
            var2 = new File(this.filename);
         }

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

   private void saveNyomtatvanyData(FileOutputStream var1, CachedCollection var2, int var3) throws Exception {
      int var4 = 0;
      int var5 = var2.size();
      if (var3 != -1) {
         var4 = var3;
         var5 = var3 + 1;
      }

      int var6 = -1;
      if (var3 == -1 && this.bookModel.cc.size() != 1) {
         var6 = this.bookModel.get_main_index();
         this.saveNyomtatvany(var2, var6, var1);
      }

      for(int var7 = var4; var7 < var5; ++var7) {
         if (var7 != var6) {
            this.saveNyomtatvany(var2, var7, var1);
         }
      }

   }

   private String getNoteTag() {
      return this.formMetaData.get("megjegyzes") != null && !"".equals(this.formMetaData.get("megjegyzes")) ? "        <megjegyzes>" + this.formMetaData.get("megjegyzes") + "</megjegyzes>\r\n" : "";
   }

   private StringBuffer getHeadTag() {
      StringBuffer var1 = new StringBuffer("  <head filetype=\"zn1810\">\r\n");
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
      if (this.commonMetaData.containsKey("category")) {
         var1.append(" category=\"").append(this.commonMetaData.get("category")).append("\"");
      }

      if (this.commonMetaData.containsKey("krfilename")) {
         var1.append(" krfilename=\"").append(this.commonMetaData.get("krfilename")).append("\"");
      }

      if (this.commonMetaData.containsKey("attachment_count")) {
         var1.append(" attachment_count=\"").append(this.commonMetaData.get("attachment_count")).append("\"");
      }

      if (this.commonMetaData.containsKey("avdh_cst")) {
         var1.append(" ").append("avdh_cst").append("=\"").append(this.commonMetaData.get("avdh_cst")).append("\"");
      }

      var1.append(" />\r\n");
      var1.append("  </head>\r\n");
      return var1;
   }

   private void saveFooterInfo(FileOutputStream var1) throws IOException {
      var1.write("  </nyomtatvanyok>\r\n</file>\r\n".getBytes("utf-8"));
   }

   private void saveData(FileOutputStream var1, Elem var2) throws IOException {
      String var3 = var2.getType();
      Hashtable var4 = this.bookModel.get(var3).fids;
      Iterator var5 = ((IDataStore)var2.getRef()).getCaseIdIterator();
      var1.write("      <mezok>\n".getBytes("utf-8"));

      while(var5.hasNext()) {
         StoreItem var6 = (StoreItem)var5.next();
         if (var6.value != null && !var6.value.equals("") && var6.index >= 0) {
            Hashtable var7 = ((DataFieldModel)var4.get(var6.code)).features;
            String var8 = this.savable(var7, (String)var6.value);
            if (var8 != null) {
               var1.write(("        <mezo eazon=\"" + var6.toString() + "\">" + DatastoreKeyToXml.htmlConvert(var8) + "</mezo>\n").getBytes("utf-8"));
               if (var6.getDetails() != null) {
                  this.getSavableDetails(var1, var6.getDetails(), var6.getDetailSum());
               }
            }
         }
      }

      var1.write("      </mezok>\n".getBytes("utf-8"));
      var1.write("    </nyomtatvany>\n".getBytes("utf-8"));
   }

   private void getSavableDetails(FileOutputStream var1, Vector<Vector> var2, String var3) throws IOException {
      var1.write(("          <reszletek osszeg=\"" + var3 + "\">\n").getBytes("utf-8"));

      for(int var4 = 0; var4 < var2.size(); ++var4) {
         Vector var5 = (Vector)var2.elementAt(var4);
         var1.write(("            <reszlet ertek=\"" + var5.elementAt(1) + "\">" + DatastoreKeyToXml.htmlConvert(var5.elementAt(0).toString()) + "</reszlet>\n").getBytes("utf-8"));
      }

      var1.write("          </reszletek>\n".getBytes("utf-8"));
   }

   private void deleteFile() {
      File var1 = new File(this.filename);
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
      if (var3 != null && !"".equals(var3)) {
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

      try {
         if (this.bookModel.cc.docinfo.containsKey("category")) {
            this.commonMetaData.put("category", this.bookModel.cc.docinfo.get("category"));
         } else if (this.bookModel.docinfo.containsKey("category")) {
            this.commonMetaData.put("category", this.bookModel.docinfo.get("category"));
         }

         if (this.bookModel.cc.docinfo.containsKey("krfilename")) {
            this.commonMetaData.put("krfilename", this.bookModel.cc.docinfo.get("krfilename"));
         }

         if (this.bookModel.cc.docinfo.containsKey("attachment_count")) {
            this.commonMetaData.put("attachment_count", this.bookModel.cc.docinfo.get("attachment_count"));
         }
      } catch (Exception var14) {
         Tools.eLog(var14, 0);
      }

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
      } catch (Exception var13) {
         this.commonMetaData.put("nev", "");
      }

      try {
         this.commonMetaData.put("adoszam", var2[1]);
      } catch (Exception var12) {
         this.commonMetaData.put("adoszam", "");
      }

      try {
         this.commonMetaData.put("adoazonosito", var2[2]);
      } catch (Exception var11) {
         this.commonMetaData.put("adoazonosito", "");
      }

      try {
         this.commonMetaData.put("tol", var2[4]);
      } catch (Exception var10) {
         this.commonMetaData.put("tol", "");
      }

      try {
         this.commonMetaData.put("ig", var2[5]);
      } catch (Exception var9) {
         this.commonMetaData.put("ig", "");
      }

      try {
         Object var3;
         try {
            var3 = this.bookModel.get_main_elem().getEtc().get("orignote");
            if (var3 == null) {
               var3 = "";
            }
         } catch (Exception var7) {
            var3 = "";
         }

         this.commonMetaData.put("note", var3);
      } catch (Exception var8) {
         Tools.eLog(var8, 0);
      }

      try {
         this.calculated = !(Boolean)PropertyList.getInstance().get("prop.dynamic.dirty2");
      } catch (Exception var6) {
         this.calculated = true;
      }

      try {
         if (this.bookModel.isAvdhModel()) {
            this.commonMetaData.put("avdh_cst", this.bookModel.docinfo.get("avdh_cst"));
         } else {
            this.commonMetaData.put("avdh_cst", "");
         }
      } catch (Exception var5) {
         this.commonMetaData.put("avdh_cst", "");
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

   private void saveNyomtatvany(CachedCollection var1, int var2, FileOutputStream var3) throws Exception {
      this.initFormMetaData((Elem)var1.get(var2), var1.size(), ((Elem)var1.get(var2)).getEtc().get("orignote"));
      String var4 = HeadChecker.getInstance().getHeaderTagType(((Elem)var1.get(var2)).getType(), this.bookModel);
      StringBuilder var5 = new StringBuilder();
      var5.append("    <nyomtatvany");
      if (((Elem)var1.get(var2)).getEtc().containsKey("sn")) {
         var5.append(" sn=\"").append(((Elem)var1.get(var2)).getEtc().get("sn")).append("\"");
      }

      var5.append(">\r\n").append("      <nyomtatvanyinformacio>\r\n").append("        <nyomtatvanyazonosito>").append(this.formMetaData.get("nyomtatvanyazonosito")).append("</nyomtatvanyazonosito>\r\n").append("        <nyomtatvanyverzio>").append(this.formMetaData.get("ver")).append("</nyomtatvanyverzio>\r\n");
      if (this.abev) {
         var5.append("        <adozo>\r\n").append("          <nev>").append(this.commonMetaData.get("nev")).append("</nev>\r\n").append(!this.commonMetaData.get("adoszam").equals("") ? "          <adoszam>" + this.commonMetaData.get("adoszam") + "</adoszam>\r\n" : "").append(!this.commonMetaData.get("adoazonosito").equals("") ? "          <adoazonosito>" + this.commonMetaData.get("adoazonosito") + "</adoazonosito>\r\n" : "").append("        </adozo>\r\n");
         if (var4.equals("1")) {
            var5.append("      <munkavallalo>\r\n").append("        <nev>").append(this.formMetaData.get("empname")).append("</nev>\r\n").append("        <adoazonosito>").append(this.formMetaData.get("emptaxid")).append("</adoazonosito>\r\n").append("      </munkavallalo>\r\n");
         } else if (var4.equals("2")) {
            var5.append("      <albizonylatazonositas>\r\n").append("        <megnevezes>").append(this.formMetaData.get("megnevezes")).append("</megnevezes>\r\n").append("        <azonosito>").append(this.formMetaData.get("azonosito")).append("</azonosito>\r\n").append("      </albizonylatazonositas>\r\n");
         }

         var5.append("        <idoszak>\r\n").append("          <tol>").append(this.formMetaData.get("tol")).append("</tol>\r\n").append("          <ig>").append(this.formMetaData.get("ig")).append("</ig>\r\n").append("        </idoszak>\r\n");
      }

      var5.append(this.getNoteTag());
      var5.append("      </nyomtatvanyinformacio>\r\n");
      var3.write(var5.toString().getBytes("utf-8"));
      Elem var6 = (Elem)this.bookModel.cc.get(var2);
      this.saveData(var3, var6);
   }

   private void removeCountAttachmentAndKrFilenameInfo() {
      try {
         this.commonMetaData.remove("krfilename");
         this.commonMetaData.remove("attachment_count");
      } catch (Exception var2) {
         Tools.eLog(var2, 0);
      }

   }
}
