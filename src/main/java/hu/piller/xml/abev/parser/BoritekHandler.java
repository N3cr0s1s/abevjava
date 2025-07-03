package hu.piller.xml.abev.parser;

import hu.piller.xml.FinishException;
import hu.piller.xml.XMLElemHandler;
import hu.piller.xml.xes.XESDocumentController;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class BoritekHandler extends XMLElemHandler {
   public BoritekHandler(BoritekParser3 parser) {
      super((XMLElemHandler)null, parser);
   }

   public void startElement(String namespaceURI, String localName, String qName, Attributes attrs) throws SAXException {
      if (localName.equals("Fejlec")) {
         this.parser.getReader().setContentHandler(new FejlecHandler(this, this.parser));
      }

      if (localName.equals("Torzs")) {
         if (((BoritekParser3)this.parser).getMode() == BoritekParser3.PARSE_HEADER) {
            throw new FinishException("fejlec ready");
         }

         this.parser.getReader().setContentHandler(new TorzsHandler(this, (XESDocumentController)this.parser));
      }

   }

   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
      if (localName.equals("CimzettNyilvanosKulcs")) {
         ((BoritekParser3)this.parser).getMetaData().setCimzettNyilvanosKulcs(this.contents.toString());
         if (((BoritekParser3)this.parser).getMode() == BoritekParser3.PARSE_HEADER) {
            throw new FinishException("fejlec ready");
         }
      }

      if (localName.equals("Boritek") && ((BoritekParser3)this.parser).getMode() == BoritekParser3.PARSE_HEADER) {
         throw new FinishException("fejlec ready");
      }
   }
}
