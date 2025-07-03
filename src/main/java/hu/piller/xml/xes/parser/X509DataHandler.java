package hu.piller.xml.xes.parser;

import hu.piller.xml.XMLElemHandler;
import hu.piller.xml.xes.XESDocumentController;
import hu.piller.xml.xes.element.KeyInfo;
import hu.piller.xml.xes.element.X509Data;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class X509DataHandler extends XMLElemHandler {
   private X509Data x509Data = new X509Data();
   private KeyInfo keyInfo;

   public X509DataHandler(XMLElemHandler parent, XESDocumentController parser) {
      super(parent, parser);
      this.keyInfo = new KeyInfo();
   }

   public X509DataHandler(XMLElemHandler parent, XESDocumentController parser, KeyInfo keyInfo) {
      super(parent, parser);
      this.keyInfo = keyInfo;
   }

   public void startElement(String namespaceURI, String localName, String qName, Attributes attrs) throws SAXException {
      this.contents.reset();
   }

   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
      if (localName.equals("X509SKI")) {
         this.x509Data.setSki(this.contents.toString());
      }

      if (localName.equals("X509Data")) {
         this.keyInfo.setKeyData(this.x509Data);
         this.parser.getReader().setContentHandler(this.parent);
      }

   }
}
