package hu.piller.enykp.alogic.fileloader.db;

import hu.piller.enykp.alogic.ebev.SendParams;
import hu.piller.enykp.alogic.fileloader.xml.XmlLoader;
import hu.piller.enykp.alogic.filesaver.xml.DBXmlSaver;
import hu.piller.enykp.alogic.filesaver.xml.EnykXmlSaver;
import hu.piller.enykp.alogic.filesaver.xml.ISOXmlSaver;
import hu.piller.enykp.alogic.fileutil.TemplateChecker;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.datastore.GUI_Datastore;
import hu.piller.enykp.extensions.db.DbFactory;
import hu.piller.enykp.extensions.db.IDbHandler;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.ILoadManager;
import hu.piller.enykp.util.base.Result;
import me.necrocore.abevjava.NecroFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JOptionPane;

public class DbPkgLoader implements ILoadManager {
   String loaderid;
   String suffix;
   String description;
   IDbHandler dbhandler;
   public BookModel bm;
   String cbid;
   String pkg;
   String blobtype;
   String encoding = "utf-8";

   public DbPkgLoader() {
   }

   public DbPkgLoader(String var1, String var2, String var3, String var4) {
      this.cbid = var1;
      this.pkg = var2;
      this.blobtype = var3;
      this.loaderid = "db_data_loader_v1";
      this.suffix = "";
      this.description = "Adatbázis forrás";
      this.encoding = var4;

      try {
         this.dbhandler = DbFactory.getDbHandler();
      } catch (Exception var6) {
         this.dbhandler = null;
      }

      this.bm = null;
   }

   public BookModel load() {
      try {
         String var1 = this.dbhandler.xmlInit(this.pkg, this.cbid);
         if (var1 != null) {
            BookModel var19 = new BookModel();
            var19.hasError = true;
            var19.errormsg = var1;
            return var19;
         }

         Hashtable var2 = this.dbhandler.xmlLoad(this.blobtype);
         XmlLoader var3 = new XmlLoader();
         var3.silentheadcheck = true;
         String var4 = (String)var2.get("status");
         if ("-1".equals(var4)) {
            BookModel var21 = new BookModel();
            var21.hasError = true;
            var21.errormsg = (String)var2.get("error");
            return var21;
         }

         if ("1".equals(var4)) {
            try {
               String var20 = (String)var2.get("formData");
               File var23 = new NecroFile(TemplateChecker.getInstance().getTemplateFileNames(var20).getTemplateFileNames()[0]);
               BookModel var24 = new BookModel(var23);
               return var24;
            } catch (Exception var17) {
               var17.printStackTrace();
               BookModel var22 = new BookModel();
               var22.hasError = true;
               var22.errormsg = var17.getMessage();
               return var22;
            }
         }

         byte[] var5 = (byte[])((byte[])var2.get("xmlData"));
         String var6 = "";
         String var7 = "";
         String var8 = "";
         ByteArrayInputStream var9 = new ByteArrayInputStream(var5);
         byte var10 = 0;
         this.encoding = "";
         int var11 = var9.read();
         if (var11 == 255) {
            this.encoding = "UTF-16LE";
         }

         if (var11 == 254) {
            this.encoding = "UTF-16BE";
         }

         if (var11 == 239) {
            var10 = 3;
         }

         if (this.encoding.equals("")) {
            byte[] var12 = new byte[128];
            var9.read(var12);
            String var13 = new String(var12);
            int var14 = var13.indexOf("encoding=");
            int var15 = var13.indexOf("\"", var14);
            int var16 = var13.indexOf("\"", var15 + 1);
            if (var15 == -1) {
               var15 = var13.indexOf("'", var14);
               var16 = var13.indexOf("'", var15 + 1);
            }

            this.encoding = var13.substring(var15 + 1, var16);
         }

         var9.close();
         ByteArrayInputStream var25 = new ByteArrayInputStream(var5);
         if (var10 != 0) {
            var25.skip((long)var10);
         }

         Hashtable var26 = var3.getHeadData(var25, this.encoding);
         var25.close();
         var26 = (Hashtable)var26.get("docinfo");
         var6 = (String)var26.get("id");
         var7 = (String)var26.get("ver");
         var8 = (String)var26.get("org");
         var25 = new ByteArrayInputStream(var5);
         if (var10 != 0) {
            var25.skip((long)var10);
         }

         this.bm = var3.load(var25, var6, var7, var8, this.encoding, false);
         var25.close();
         this.bm.cc.setSavepoints();
      } catch (Exception var18) {
         var18.printStackTrace();
      }

      return this.bm;
   }

   public String getId() {
      return this.loaderid;
   }

   public String getDescription() {
      return this.description;
   }

   public Hashtable getHeadData(File var1) {
      return null;
   }

   public BookModel load(String var1, String var2, String var3, String var4) {
      return null;
   }

   public BookModel load(String var1, String var2, String var3, String var4, BookModel var5) {
      return null;
   }

   public String getFileNameSuffix() {
      return this.suffix;
   }

   public String createFileName(String var1) {
      return null;
   }

   public int save(BookModel var1) {
      try {
         Object var2;
         if (this.encoding.equals("ISO-8859-2")) {
            var2 = new ISOXmlSaver(var1);
         } else {
            if (!this.encoding.equals("utf-8")) {
               JOptionPane.showMessageDialog(MainFrame.thisinstance, "Csak utf-8 vagy ISO-8859-2 kódolású xmlt tudunk készíteni.", "Hibaüzenet", 0);
               return 1;
            }

            var2 = new EnykXmlSaver(var1);
         }

         Result var3 = ((DBXmlSaver)var2).save("", false, true, (SendParams)null, (String)null);
         if (!var3.isOk()) {
            String var11 = "A nyomtatvány ebben a formában nem menthető.";
            JOptionPane.showMessageDialog(MainFrame.thisinstance, var11, "Hibaüzenet", 0);
            return 1;
         }

         int var4 = var1.cc.size();
         Vector var5 = new Vector();

         for(int var6 = 0; var6 < var4; ++var6) {
            Elem var7 = (Elem)var1.cc.get(var6);
            GUI_Datastore var8 = (GUI_Datastore)var7.getRef();
            Hashtable var9 = var8.getChangedValues();
            var9.put("formid", var7.getType());
            var9.put("formindex", new Integer(var6));
            var5.add(var9);
         }

         String var12 = this.dbhandler.xmlSave(var3, var5);
         if (var12 != null) {
            JOptionPane.showMessageDialog(MainFrame.thisinstance, var12, "Hibaüzenet", 0);
            return 1;
         }
      } catch (Exception var10) {
         var10.printStackTrace();
      }

      return 0;
   }

   public void close() {
      try {
         String var1 = this.dbhandler.xmlClose();
         if (var1 != null) {
            JOptionPane.showMessageDialog(MainFrame.thisinstance, var1, "Hibaüzenet", 0);
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
