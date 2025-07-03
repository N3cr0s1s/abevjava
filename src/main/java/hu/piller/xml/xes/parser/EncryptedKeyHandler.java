package hu.piller.xml.xes.parser;

import hu.piller.xml.XMLElemHandler;
import hu.piller.xml.xes.XESDocumentController;
import hu.piller.xml.xes.element.EncryptedKey;
import hu.piller.xml.xes.element.KeyInfo;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class EncryptedKeyHandler extends XMLElemHandler {
   private EncryptedKey encKey;
   private KeyInfo keyInfo;

   public EncryptedKeyHandler(XMLElemHandler parent, XESDocumentController parser) {
      super(parent, parser);
   }

   public EncryptedKeyHandler(XMLElemHandler parent, XESDocumentController parser, KeyInfo keyInfo) {
      super(parent, parser);
      this.keyInfo = keyInfo;
      this.encKey = new EncryptedKey();
   }

   public void startElement(String namespaceURI, String localName, String qName, Attributes attrs) throws SAXException {
      this.contents.reset();
      if (localName.equals("KeyInfo")) {
         this.parser.getReader().setContentHandler(new KeyInfoHandler(this, (XESDocumentController)this.parser, this.encKey));
      }

   }

   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
      if (localName.equals("CipherValue")) {
         this.encKey.setCipherValue(this.contents.toString());
      }

      if (localName.equals("EncryptedKey")) {
         if (this.keyInfo != null) {
            this.keyInfo.setEncKey(this.encKey);
         }

         this.parser.getReader().setContentHandler(this.parent);
      }

   }

   protected EncryptedKey getEncryptedKey() {
      return this.encKey;
   }
}
