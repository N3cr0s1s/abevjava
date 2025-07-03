package hu.piller.enykp.alogic.fileloader.xml;

import hu.piller.enykp.util.base.PropertyList;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class DefaultXMLParser implements ContentHandler, ErrorHandler {
   private XMLReader xml_r;
   private final Object parse_sync = new Object();
   public static boolean silent = false;
   public static boolean fullcheck = true;
   public static boolean validation = true;

   public DefaultXMLParser() throws SAXException {
      this.xml_r = null;

      try {
         SAXParserFactory var1 = SAXParserFactory.newInstance();
         var1.setValidating(false);
         var1.setNamespaceAware(true);
         SchemaFactory var2 = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

         try {
            var1.setSchema(var2.newSchema(this.getSources()));
         } catch (Exception var4) {
            var4.printStackTrace();
         }

         SAXParser var3 = var1.newSAXParser();
         this.xml_r = var3.getXMLReader();
      } catch (ParserConfigurationException var5) {
         var5.printStackTrace();
      }

   }

   private Source[] getSources() {
      File var1 = new File(PropertyList.getInstance().get("prop.sys.root") + "/xsd");
      File[] var2 = var1.listFiles(new FileFilter() {
         public boolean accept(File var1) {
            String var2 = var1.getAbsolutePath().toLowerCase();
            return var2.endsWith(".xsd");
         }
      });
      int var3 = var2.length;
      Source[] var4 = new Source[var3];

      for(int var5 = 0; var5 < var3; ++var5) {
         var4[var5] = new StreamSource(var2[var5]);
      }

      return var4;
   }

   public DefaultXMLParser(String var1) throws SAXException {
      this.xml_r = XMLReaderFactory.createXMLReader(var1);
      this.xml_r.setContentHandler(this);
   }

   public void parse(String var1) throws IOException, SAXException {
      synchronized(this.parse_sync) {
         this.xml_r.setErrorHandler(this);
         this.xml_r.parse(var1);
      }
   }

   public void parse(InputStream var1) throws IOException, SAXException {
      synchronized(this.parse_sync) {
         this.xml_r.setErrorHandler(this);
         this.xml_r.parse(new InputSource(var1));
      }
   }

   public void parse(InputSource var1) throws IOException, SAXException {
      synchronized(this.parse_sync) {
         this.xml_r.setErrorHandler(this);
         this.xml_r.parse(var1);
      }
   }

   public void setContentHandler(ContentHandler var1) {
      this.xml_r.setContentHandler(var1);
   }

   public void endDocument() throws SAXException {
   }

   public void startDocument() throws SAXException {
   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
   }

   public void ignorableWhitespace(char[] var1, int var2, int var3) throws SAXException {
   }

   public void endPrefixMapping(String var1) throws SAXException {
   }

   public void skippedEntity(String var1) throws SAXException {
   }

   public void setDocumentLocator(Locator var1) {
   }

   public void processingInstruction(String var1, String var2) throws SAXException {
   }

   public void startPrefixMapping(String var1, String var2) throws SAXException {
   }

   public void endElement(String var1, String var2, String var3) throws SAXException {
   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
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
      if (!this.isSilent()) {
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

   private boolean isSilent() {
      return silent;
   }

   private boolean isSchemaFullChecking() {
      return fullcheck;
   }

   private boolean isValidation() {
      return validation;
   }

   private String getXMLSchema() {
      return null;
   }

   private String getTargetNamespace() {
      return null;
   }
}
