package hu.piller.enykp.alogic.primaryaccount.common.xml;

import java.io.IOException;
import java.io.InputStream;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class DefaultXMLParser implements ContentHandler {
   private XMLReader xml_r;

   public DefaultXMLParser() throws SAXException {
      this.xml_r = XMLReaderFactory.createXMLReader();
      this.xml_r.setContentHandler(this);
   }

   public DefaultXMLParser(String var1) throws SAXException {
      this.xml_r = XMLReaderFactory.createXMLReader(var1);
      this.xml_r.setContentHandler(this);
   }

   public void parse(String var1) throws IOException, SAXException {
      this.xml_r.parse(var1);
   }

   public void parse(InputStream var1) throws IOException, SAXException {
      this.xml_r.parse(new InputSource(var1));
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
}
