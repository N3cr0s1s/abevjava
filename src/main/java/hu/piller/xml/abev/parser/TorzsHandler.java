package hu.piller.xml.abev.parser;

import hu.piller.xml.XMLElemHandler;
import hu.piller.xml.xes.XESDocumentController;
import hu.piller.xml.xes.parser.EncryptedDataHandler;
import java.io.IOException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class TorzsHandler extends XMLElemHandler {
   public TorzsHandler(XMLElemHandler parent, XESDocumentController parser) {
      super(parent, parser);
   }

   public void startElement(String namespaceURI, String localName, String qName, Attributes attrs) throws SAXException {
      if (localName.equals("EncryptedData")) {
         try {
            this.parser.getReader().setContentHandler(new EncryptedDataHandler(this, (XESDocumentController)this.parser));
         } catch (IOException var6) {
            throw new SAXException("decryption failed");
         }
      }

      if (localName.equals("Csatolmany")) {
         if (((BoritekParser3)this.parser).getMode() == BoritekParser3.PARSE_ALL) {
            if (attrs.getLength() > 0 && attrs.getLocalName(0).equals("Azonosito")) {
               this.parser.getReader().setContentHandler(new CsatolmanyHandler(this, (XESDocumentController)this.parser, attrs.getValue(0)));
            }
         } else {
            this.parser.getReader().setContentHandler(this.parent);
         }
      }

   }

   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
      if (localName.equals("Torzs")) {
         this.parser.getReader().setContentHandler(this.parent);
      }

   }
}
