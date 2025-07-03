package hu.piller.xml.xes.parser;

import hu.piller.xml.XMLElemHandler;
import hu.piller.xml.abev.parser.BoritekParser3;
import hu.piller.xml.xes.XESDocumentController;
import hu.piller.xml.xes.element.EncryptedKey;
import hu.piller.xml.xes.element.KeyInfo;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class KeyInfoHandler extends XMLElemHandler {
   KeyInfo keyInfo;
   EncryptedKey encKey;
   boolean embedded;

   public KeyInfoHandler(XMLElemHandler parent, XESDocumentController parser) {
      super(parent, parser);
      this.keyInfo = new KeyInfo();
   }

   public KeyInfoHandler(XMLElemHandler parent, XESDocumentController parser, EncryptedKey encKey) {
      super(parent, parser);
      this.encKey = encKey;
      this.keyInfo = encKey.getKeyInfo();
   }

   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
      if (localName.equals("EncryptedKey")) {
         this.parser.getReader().setContentHandler(new EncryptedKeyHandler(this, (XESDocumentController)this.parser, this.keyInfo));
      }

      if (localName.equals("X509Data")) {
         this.parser.getReader().setContentHandler(new X509DataHandler(this, (XESDocumentController)this.parser, this.keyInfo));
      }

      if (localName.equals("PGPData")) {
         this.parser.getReader().setContentHandler(new PGPDataHandler(this, (XESDocumentController)this.parser, this.keyInfo));
      }

   }

   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
      if (localName.equals("KeyInfo")) {
         if (this.keyInfo.getEncKey() != null) {
            ((BoritekParser3)this.parser).addKeyInfo(this.keyInfo);
         }

         this.parser.getReader().setContentHandler(this.parent);
      }

   }
}
