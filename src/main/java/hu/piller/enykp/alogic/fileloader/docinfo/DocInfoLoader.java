package hu.piller.enykp.alogic.fileloader.docinfo;

import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.ILoadManager;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import me.necrocore.abevjava.NecroFile;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class DocInfoLoader implements ILoadManager {
   static final String RESOURCE_NAME = "DocInfo Betöltő";
   static final Long RESOURCE_ERROR_ID = new Long(1000L);
   private static final String KEY_TS_TYPE = "type";
   private static final String KEY_TS_SAVED = "saved";
   public static final String KEY_TS_DOC = "doc";
   public static final String KEY_TS_DOCINFO = "docinfo";
   private int load_type = 0;
   private Hashtable head_data;
   private Hashtable body_data;

   public String getId() {
      return "docinfo_data_loader_v1";
   }

   public String getDescription() {
      return "DocInfo állomány";
   }

   public Hashtable getHeadData(String var1, String var2) {
      String var3 = (String)PropertyList.getInstance().get("prop.sys.root");
      String var4 = (String)PropertyList.getInstance().get("prop.sys.helps");
      String var5 = var1 + "_" + var2;
      String var6 = var3 + File.separator + "segitseg" + File.separator + this.createFileName(var5);

      URL var7;
      try {
         var7 = new URL(var6);
      } catch (MalformedURLException var11) {
         try {
            var6 = var4 + File.separator + this.createFileName(var5);
            var7 = new URL(var6);
         } catch (MalformedURLException var10) {
            return null;
         }
      }

      File var8 = new NecroFile(var7.getFile());
      return this.getHeadData(var8);
   }

   public Hashtable getHeadData(File var1) {
      Hashtable var2;
      if (var1 != null && var1.toString().endsWith(this.getFileNameSuffix())) {
         try {
            this.load_type = 1;
            this.load(var1.toString(), (String)null, (String)null, (String)null);
         } finally {
            this.load_type = 0;
         }

         if (this.head_data != null) {
            var2 = new Hashtable(4);
            var2.put("type", "single");
            var2.put("saved", "");
            Hashtable var3 = new Hashtable();
            var3.put("id", Tools.getString(this.head_data.get("id"), ""));
            var3.put("name", Tools.getString(this.head_data.get("name"), ""));
            var3.put("ver", Tools.getString(this.head_data.get("ver"), ""));
            var3.put("org", Tools.getString(this.head_data.get("org_id"), ""));
            var3.put("category", Tools.getString(this.head_data.get("category"), ""));
            var3.put("check_valid", Tools.getString(this.head_data.get("check_valid"), ""));
            var3.put("tax_number", "");
            var3.put("account_name", "");
            var3.put("person_name", "");
            var3.put("from_date", "");
            var3.put("to_date", "");
            var3.put("info", "");
            var3.put("note", "");
            var2.put("docinfo", var3);
            var2.put("doc", new Vector(0));
         } else {
            var2 = null;
         }
      } else {
         var2 = null;
      }

      return var2;
   }

   public BookModel load(String var1, String var2, String var3, String var4, BookModel var5) {
      return this.load(var1, var2, var3, var4);
   }

   public BookModel load(String var1, String var2, String var3, String var4) {
      BookModel var5 = null;

      try {
         var5 = new BookModel();
         if (var5.errormsg == null) {
            var5.errormsg = "";
         }

         if (var5.hasError) {
            return var5;
         }

         this.loadDocInfoFile(var1);
      } catch (Exception var7) {
         if (var5 == null) {
            this.writeError("Betöltési hiba !\n", var7);
         } else {
            var5.errormsg = var5.errormsg + (var5.errormsg.length() == 0 ? "" : ";") + "Betöltési hiba !\n" + var7;
         }
      }

      return var5;
   }

   public String getFileNameSuffix() {
      return ".docver";
   }

   public String createFileName(String var1) {
      return var1 == null ? null : (var1.trim().endsWith(".docver") ? var1.trim() : var1.trim() + ".docver");
   }

   private void loadDocInfoFile(String var1) throws ParserConfigurationException, SAXException, IOException {
      SAXParserFactory var2 = SAXParserFactory.newInstance();
      SAXParser var3 = var2.newSAXParser();
      FileInputStream var4 = null;

      try {
         var4 = new FileInputStream(var1);
         DocInfoLoader.DocInfoXmlHandler var5 = new DocInfoLoader.DocInfoXmlHandler(this.load_type);
         var3.parse(var4, var5);
         this.head_data = var5.getHeadData();
         this.body_data = var5.getBodyData();
         var5.release();
      } finally {
         if (var4 != null) {
            var4.close();
         }

      }

   }

   private void writeError(String var1, Exception var2) {
      ErrorList.getInstance().writeError(RESOURCE_ERROR_ID, "DocInfo Betöltő: " + (var1 == null ? "" : var1), var2, (Object)null);
   }

   private static class DocInfoXmlHandler extends DefaultHandler {
      private int load_type;
      private Hashtable head_data;
      private Hashtable body_data;
      public boolean is_silent_mode = false;

      public DocInfoXmlHandler(int var1) {
         this.load_type = var1;
      }

      public Hashtable getHeadData() {
         return this.head_data == null ? null : new Hashtable(this.head_data);
      }

      public Hashtable getBodyData() {
         return null;
      }

      public void release() {
         this.head_data = null;
         this.body_data = null;
      }

      public void startDocument() throws SAXException {
         this.head_data = null;
      }

      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         if (var3.equalsIgnoreCase("docinfo")) {
            this.head_data = this.getAttributes(var4, this.head_data);
         }

      }

      public void characters(char[] var1, int var2, int var3) throws SAXException {
      }

      public void endElement(String var1, String var2, String var3) throws SAXException {
      }

      public void endDocument() throws SAXException {
      }

      public void warning(SAXParseException var1) throws SAXException {
         this.printInfo(var1);
      }

      public void error(SAXParseException var1) throws SAXException {
         this.printInfo(var1);
      }

      public void fatalError(SAXParseException var1) throws SAXException {
         this.printInfo(var1);
      }

      private void printInfo(SAXParseException var1) throws SAXException {
         if (!this.is_silent_mode) {
            if (var1.getPublicId() != null) {
               System.out.println("    pid: " + var1.getPublicId());
            }

            if (var1.getSystemId() != null) {
               System.out.println("    sid: " + var1.getSystemId());
            }

            System.out.println("    sor: " + var1.getLineNumber());
            System.out.println("    oszlop: " + var1.getColumnNumber());
            System.out.println("    üzenet: " + var1.getMessage());
            System.out.println("    hely: " + System.getProperty("user.dir"));
         }

         throw var1;
      }

      private Hashtable getAttributes(Attributes var1, Hashtable var2) {
         Hashtable var3 = var2 == null ? new Hashtable() : var2;
         if (var1 != null) {
            int var4 = 0;

            for(int var5 = var1.getLength(); var4 < var5; ++var4) {
               var3.put(var1.getQName(var4), var1.getValue(var4));
            }
         }

         return var3;
      }
   }
}
