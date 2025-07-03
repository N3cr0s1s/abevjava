package hu.piller.enykp.alogic.fileloader.xml;

import hu.piller.enykp.alogic.fileutil.DatastoreKeyToXml;
import hu.piller.enykp.extensions.db.DbFactory;
import hu.piller.enykp.extensions.db.IDbHandler;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.ILoadManager;
import hu.piller.enykp.util.base.PropertyList;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlQuickloader implements ILoadManager {
   public static final int CLOB = 0;
   public static final int BLOB = 1;
   public static final int FIELDS = 2;
   String loaderid;
   String suffix;
   String description;
   IDbHandler dbhandler;
   private boolean onlyhead;
   private int type;
   String param;
   Hashtable parht;
   HashSet ex_hs;
   Hashtable mainheadht;
   Hashtable headht;
   Hashtable dataht;
   String mezokey;
   String mezovalue;
   boolean firstform;
   boolean in_mv;
   public static final String IDLE = "0";
   public static final String TASK = "1";
   public static final String STOP = "2";
   public static final int MILLIS = 600000;

   public XmlQuickloader(int var1, String var2) {
      this.type = var1;
      this.param = var2;
      this.loaderid = "quick_xml_data_loader_v1";
      this.suffix = ".xml";
      this.description = "XML állomány";
      this.onlyhead = false;

      try {
         this.dbhandler = DbFactory.getDbHandler();
      } catch (Exception var4) {
         this.dbhandler = null;
      }

      this.ex_hs = new HashSet();
      this.ex_hs.add("abev");
      this.ex_hs.add("nyomtatvanyinformacio");
      this.ex_hs.add("adozo");
      this.ex_hs.add("munkavallalo");
      this.ex_hs.add("idoszak");
      this.ex_hs.add("nyomtatvanyok");
      this.ex_hs.add("mezok");
   }

   public String getId() {
      return this.loaderid;
   }

   public String getDescription() {
      return this.description;
   }

   public Hashtable getHeadData(File var1) {
      if (this.dbhandler == null) {
         return null;
      } else {
         new Hashtable();
         return null;
      }
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
      return var1.endsWith(this.suffix) ? var1 : var1 + this.suffix;
   }

   public Hashtable qload() {
      Hashtable var1 = new Hashtable();
      if (this.dbhandler == null) {
         var1.put("errormsg", "Nincs megfelelő adatbáziskezelő!");
         var1.put("stop", "stop");
         return var1;
      } else {
         try {
            Hashtable var2 = new Hashtable();
            var2.put("TASKTYPE", "GETNEXTTASK");
            this.parht = this.dbhandler.getNextTask(var2);
            String[] var3 = (String[])((String[])this.parht.get("JABEV_FUNCTIONS_ARRAY"));

            try {
               if (var3[0].equals("0")) {
                  var1.put("idle", "idle");
                  return var1;
               }

               if (var3[0].equals("2")) {
                  var1.put("stop", "stop");
                  return var1;
               }
            } catch (Exception var5) {
            }

            switch(this.type) {
            case 0:
            case 2:
               this.parht.put("LOB_TYPE", "C");
               return this.done_clob(var1);
            case 1:
               this.parht.put("LOB_TYPE", "B");
               return this.done_blob(var1);
            }
         } catch (Exception var6) {
            var1.put("stop", "stop");
            var1.put("errormsg", var6.getMessage() == null ? var6.toString() : var6.getMessage());
         }

         return var1;
      }
   }

   private Hashtable done_clob(Hashtable var1) {
      try {
         return var1;
      } catch (Exception var3) {
         var3.printStackTrace();
         var1.put("errormsg", var3.getMessage() == null ? var3.toString() : var3.getMessage());
         return var1;
      }
   }

   private Hashtable done_blob(Hashtable var1) {
      try {
         return var1;
      } catch (Exception var3) {
         var3.printStackTrace();
         var1.put("errormsg", var3.getMessage() == null ? var3.toString() : var3.getMessage());
         return var1;
      }
   }

   public void load(InputStream var1, String var2) {
      try {
         XmlQuickloader.bodyhandler var3 = new XmlQuickloader.bodyhandler();
         InputSource var4 = new InputSource(var1);
         var4.setEncoding(var2);
         DefaultXMLParser var5 = new DefaultXMLParser();
         DefaultXMLParser.silent = true;
         var5.setContentHandler(var3);
         var5.parse(var4);
         var1.close();
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   private boolean check(String var1) {
      return !this.ex_hs.contains(var1);
   }

   public void loop_qload() {
      boolean var1 = true;
      int var2 = 0;
      System.out.println("Start:" + new Date());
      long var3 = System.currentTimeMillis();

      while(true) {
         Hashtable var5 = this.qload();
         ++var2;
         if (var2 % 1000 == 0 && var1) {
            System.out.println("count=" + var2 + "    time=" + (System.currentTimeMillis() - var3) / 1000L + " sec");
         }

         if (var5.get("stop") != null) {
            return;
         }

         if (var5.get("idle") != null) {
            try {
               int var6 = 600000;
               Integer var7 = (Integer)PropertyList.getInstance().get("prop.dynamic.db.idle");
               if (var7 != null) {
                  var6 = var7 * 60 * 1000;
               }

               Thread.sleep((long)var6);
            } catch (InterruptedException var8) {
            }
         }
      }
   }

   class bodyhandler extends DefaultHandler {
      public void characters(char[] var1, int var2, int var3) throws SAXException {
         XmlQuickloader.this.mezovalue = XmlQuickloader.this.mezovalue + DatastoreKeyToXml.plainConvert(new String(var1, var2, var3));
      }

      public void endPrefixMapping(String var1) throws SAXException {
         super.endPrefixMapping(var1);
      }

      public void error(SAXParseException var1) throws SAXException {
         super.error(var1);
      }

      public void fatalError(SAXParseException var1) throws SAXException {
         super.fatalError(var1);
      }

      public void ignorableWhitespace(char[] var1, int var2, int var3) throws SAXException {
         super.ignorableWhitespace(var1, var2, var3);
      }

      public void notationDecl(String var1, String var2, String var3) throws SAXException {
         super.notationDecl(var1, var2, var3);
      }

      public void processingInstruction(String var1, String var2) throws SAXException {
         super.processingInstruction(var1, var2);
      }

      public void setDocumentLocator(Locator var1) {
         super.setDocumentLocator(var1);
      }

      public void skippedEntity(String var1) throws SAXException {
         super.skippedEntity(var1);
      }

      public void startDocument() throws SAXException {
         XmlQuickloader.this.mainheadht = new Hashtable();
         XmlQuickloader.this.headht = null;
         XmlQuickloader.this.in_mv = false;
         XmlQuickloader.this.firstform = true;
      }

      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         XmlQuickloader.this.mezovalue = "";
         if (var3.equals("mezo")) {
            XmlQuickloader.this.mezokey = var4.getValue("eazon");
         } else if (var3.equals("nyomtatvany")) {
            XmlQuickloader.this.headht = new Hashtable();
            XmlQuickloader.this.dataht = new Hashtable();
         } else if (var3.equals("munkavallalo")) {
            XmlQuickloader.this.in_mv = true;
         }

      }

      public void startPrefixMapping(String var1, String var2) throws SAXException {
         super.startPrefixMapping(var1, var2);
      }

      public void unparsedEntityDecl(String var1, String var2, String var3, String var4) throws SAXException {
         super.unparsedEntityDecl(var1, var2, var3, var4);
      }

      public void warning(SAXParseException var1) throws SAXException {
         super.warning(var1);
      }
   }
}
