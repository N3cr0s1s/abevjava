package hu.piller.xml.xes.parser;

import hu.piller.xml.XMLElemHandler;
import hu.piller.xml.xes.XESDocumentController;
import hu.piller.xml.xes.element.KeyInfo;
import hu.piller.xml.xes.element.PGPData;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class PGPDataHandler extends XMLElemHandler {
   private PGPData pgpData = new PGPData();
   private KeyInfo keyInfo;

   public PGPDataHandler(XMLElemHandler parent, XESDocumentController parser) {
      super(parent, parser);
      this.keyInfo = new KeyInfo();
   }

   public PGPDataHandler(XMLElemHandler parent, XESDocumentController parser, KeyInfo keyInfo) {
      super(parent, parser);
      this.keyInfo = keyInfo;
   }

   public void startElement(String namespaceURI, String localName, String qName, Attributes attrs) throws SAXException {
      this.contents.reset();
   }

   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
      if (localName.equals("PGPKeyPacket")) {
         this.pgpData.setPGPKeyPacket(this.contents.toString());
      }

      if (localName.equals("PGPData")) {
         this.keyInfo.setKeyData(this.pgpData);
         this.parser.getReader().setContentHandler(this.parent);
      }

   }
}
