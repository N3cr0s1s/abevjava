package hu.piller.enykp.alogic.orghandler.xml;

import java.io.IOException;
import java.io.InputStream;
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

   public DefaultXMLParser() throws SAXException {
      this.xml_r = XMLReaderFactory.createXMLReader();
   }

   public DefaultXMLParser(String var1) throws SAXException {
      this.xml_r = XMLReaderFactory.createXMLReader(var1);
      this.xml_r.setContentHandler(this);
   }

   public void parse(String var1) throws IOException, SAXException {
      synchronized(this.parse_sync) {
         this.xml_r.setContentHandler(this);
         this.xml_r.setErrorHandler(this);
         this.xml_r.parse(var1);
      }
   }

   public void parse(InputStream var1) throws IOException, SAXException {
      synchronized(this.parse_sync) {
         this.xml_r.setContentHandler(this);
         this.xml_r.setErrorHandler(this);
         this.xml_r.parse(new InputSource(var1));
      }
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
      if (var1.getPublicId() != null) {
         System.out.println("    pid: " + var1.getPublicId());
      }

      if (var1.getSystemId() != null) {
         System.out.println("    sid: " + var1.getSystemId());
      }

      System.out.println("    sor: " + var1.getLineNumber());
      System.out.println("    oszlop: " + var1.getColumnNumber());
      System.out.println("    üzenet: " + var1.getMessage());
      throw var1;
   }
}
