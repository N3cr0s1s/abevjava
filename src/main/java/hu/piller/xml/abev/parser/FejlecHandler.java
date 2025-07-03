package hu.piller.xml.abev.parser;

import hu.piller.xml.XMLDocumentController;
import hu.piller.xml.XMLElemHandler;
import hu.piller.xml.abev.element.DocMetaData;
import java.util.Vector;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class FejlecHandler extends XMLElemHandler {
   private DocMetaData metaData = new DocMetaData();
   private Vector tagPath = new Vector();
   private BoritekParser3 parserCallBack;

   public FejlecHandler(XMLElemHandler parent, XMLDocumentController bp) {
      super(parent, bp);
   }

   public void startElement(String namespaceURI, String localName, String qName, Attributes attrs) throws SAXException {
      this.tagPath.add(localName);
      this.contents.reset();
      if (localName.equals("Parameter") && attrs.getLength() == 2 && attrs.getLocalName(0).equals("Nev") && attrs.getLocalName(1).equals("Ertek")) {
         this.metaData.addParam(attrs.getValue(0), attrs.getValue(1));
      }

      if (localName.equals("CsatolmanyInfo") && attrs.getLength() > 0 && attrs.getLocalName(0).equals("Azonosito")) {
         this.parser.getReader().setContentHandler(new CsatolmanyInfoHandler(this, this.parser, this.metaData, attrs.getValue(0)));
      }

   }

   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
      if (localName.equals("Fejlec")) {
         ((BoritekParser3)this.parser).setDocMetaData(this.metaData);
         this.parser.getReader().setContentHandler(this.parent);
      } else {
         if (this.tagPath.lastElement().equals(localName)) {
            this.tagPath.removeElementAt(this.tagPath.size() - 1);
         }

         if (localName.equals("Cimzett")) {
            this.metaData.setCimzett(this.contents.toString());
         }

         if (localName.equals("DokTipusAzonosito")) {
            this.metaData.setDokTipusAzonosito(this.contents.toString());
         }

         if (localName.equals("DokTipusLeiras")) {
            this.metaData.setDokTipusLeiras(this.contents.toString());
         }

         if (localName.equals("DokTipusVerzio")) {
            this.metaData.setDokTipusVerzio(this.contents.toString());
         }

         if (localName.equals("FileNev")) {
            this.metaData.setFileNev(this.contents.toString());
         }

         if (localName.equals("Megjegyzes")) {
            this.metaData.setMegjegyzes(this.contents.toString());
         }

      }
   }

   public DocMetaData getDocMetaData() {
      return this.metaData;
   }
}
