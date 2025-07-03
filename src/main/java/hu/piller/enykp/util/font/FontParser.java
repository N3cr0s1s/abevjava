package hu.piller.enykp.util.font;

import hu.piller.enykp.util.base.ErrorList;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class FontParser implements ContentHandler {
   public static final String ERR_XML_PARSER_CREATE = "XML olvasó létrehozási hiba";
   public static final String ERR_XML_PARSER_START = "XML olvasó indítási hiba";
   public static final String ERR_XML_PARSER_MAIN = "Sikertelen az állomány beolvasása";
   public static final String ERR_XML_PARSER = "XML Olvasási hiba";
   public static final String ERR_XML_PARSER_EXCEPTION = "XML Olvasási hiba, az objektumépítő hibára futott";
   public static final int RESULT_INITIAL_SIZE = 10;
   private boolean debugOn = false;
   private XMLReader xml_r;
   private Vector objectpath = new Vector();
   private Object acttable;
   private Object newtable;
   private boolean root;
   private boolean stepUp;
   private StringBuffer datastore = new StringBuffer();

   public FontParser() {
      try {
         this.xml_r = XMLReaderFactory.createXMLReader();
         this.xml_r.setContentHandler(this);
      } catch (SAXException var2) {
         this.errAdmin("9000", "XML olvasó létrehozási hiba", var2, (Object)null);
      }

   }

   public FontParser(String var1) {
      try {
         this.xml_r = XMLReaderFactory.createXMLReader(var1);
         this.xml_r.setContentHandler(this);
      } catch (SAXException var3) {
         this.errAdmin("9000", "XML olvasó létrehozási hiba", var3, var1);
      }

   }

   public Object parse(InputSource var1) throws SAXException, IOException {
      this.xml_r.parse(var1);
      return this.objectpath.firstElement();
   }

   public Object parse(String var1) {
      InputSource var2 = new InputSource();
      var2.setSystemId(var1);

      try {
         return this.parse(var2);
      } catch (IOException var4) {
         this.errAdmin("9001", "Sikertelen az állomány beolvasása", var4, var1);
      } catch (SAXException var5) {
         this.errAdmin("9003", "Sikertelen az állomány beolvasása", var5, var1);
      }

      return null;
   }

   public Object parse(byte[] var1) {
      InputSource var2 = new InputSource();
      var2.setByteStream(new ByteArrayInputStream(var1));

      try {
         return this.parse(var2);
      } catch (IOException var4) {
         this.errAdmin("9008", "XML olvasó indítási hiba", var4, var1);
      } catch (SAXException var5) {
         this.errAdmin("9009", "Sikertelen az állomány beolvasása", var5, var1);
      }

      return null;
   }

   public Object parse(File var1) {
      try {
         InputSource var2 = new InputSource();
         var2.setByteStream(new BufferedInputStream(new FileInputStream(var1)));
         return this.parse(var2);
      } catch (IOException var3) {
         this.errAdmin("9010", "XML olvasó indítási hiba", var3, var1.getPath());
      } catch (SAXException var4) {
         this.errAdmin("9011", "Sikertelen az állomány beolvasása", var4, var1.getPath());
      }

      return null;
   }

   public void endDocument() throws SAXException {
      this.log("ed");
   }

   public void startDocument() throws SAXException {
      this.log("sd");
      this.objectpath.removeAllElements();
      this.acttable = new Hashtable(10);
      this.root = true;
   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
      String var4 = String.valueOf(var1, var2, var3);
      if (var4.trim().length() != 0) {
         this.datastore.append(var4.trim());
      }
   }

   public void ignorableWhitespace(char[] var1, int var2, int var3) throws SAXException {
      this.log("whitesp");
   }

   public void endPrefixMapping(String var1) throws SAXException {
      this.log("endPrefixMapping");
   }

   public void skippedEntity(String var1) throws SAXException {
      this.log("skippedEntity");
   }

   public void setDocumentLocator(Locator var1) {
      this.log("skippedEntity");
   }

   public void processingInstruction(String var1, String var2) throws SAXException {
      this.log("processingInstruction");
   }

   public void startPrefixMapping(String var1, String var2) throws SAXException {
      this.log("startPrefixMapping");
   }

   public void endElement(String var1, String var2, String var3) throws SAXException {
      this.log("endElement-qName:" + var3);
      if (this.datastore.length() > 0) {
         ((Vector)this.acttable).add(this.datastore.toString());
         this.datastore.delete(0, this.datastore.length());
      }

      if (!this.stepUp) {
         this.stepUp = true;
      } else {
         if (this.objectpath.size() > 1) {
            this.objectpath.remove(this.objectpath.size() - 1);
            this.acttable = this.objectpath.lastElement();
         }

      }
   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      this.datastore.delete(0, this.datastore.length());

      try {
         this.log("startElement-qName:" + var3);
         this.stepUp = true;
         if (var4.getLength() == 0 && this.acttable instanceof Vector) {
            this.stepUp = false;
         } else {
            if (!this.root) {
               if (var4.getLength() == 0) {
                  this.newtable = new Vector();
               } else {
                  this.newtable = new Hashtable(10);
               }

               if (this.acttable instanceof Vector) {
                  ((Vector)this.acttable).add(this.newtable);
               } else {
                  ((Hashtable)this.acttable).put(var3, this.newtable);
               }

               this.acttable = this.newtable;
            }

            this.objectpath.add(this.acttable);

            for(int var6 = 0; var6 < var4.getLength(); ++var6) {
               Object[] var5 = new Object[]{var4.getValue(var6)};
               this.log(var4.getQName(var6) + "-" + var5[0]);
               ((Hashtable)this.acttable).put(var4.getQName(var6), var5[0]);
            }

            this.root = false;
         }
      } catch (Exception var7) {
         this.errAdmin("9010", "XML Olvasási hiba", var7, var2);
         throw new SAXException(var7);
      }
   }

   public void log(Object var1) {
      if (this.debugOn) {
         System.out.println(this.toString());
      }

   }

   private String errAdmin(String var1, String var2, Exception var3, Object var4) {
      if (this.debugOn && var3 != null) {
         var3.printStackTrace();
      }

      String var5 = var2 + ":" + (var4 == null ? "" : var4.toString());
      ErrorList.getInstance().writeError(var1, var5, (Exception)null, var4);
      return var1 + " " + var5;
   }
}
