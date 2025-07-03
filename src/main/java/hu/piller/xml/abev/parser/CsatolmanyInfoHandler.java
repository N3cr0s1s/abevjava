package hu.piller.xml.abev.parser;

import hu.piller.xml.XMLDocumentController;
import hu.piller.xml.XMLElemHandler;
import hu.piller.xml.abev.element.CsatolmanyInfo;
import hu.piller.xml.abev.element.DocMetaData;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class CsatolmanyInfoHandler extends XMLElemHandler {
   private DocMetaData metaData;
   private CsatolmanyInfo csatInfo;

   public CsatolmanyInfoHandler(XMLElemHandler parent, XMLDocumentController parser, DocMetaData metaData, String azon) {
      super(parent, parser);
      this.metaData = metaData;
      this.csatInfo = new CsatolmanyInfo();
      this.csatInfo.setAzon(azon);
   }

   public void startElement(String namespaceURI, String localName, String qName, Attributes attrs) throws SAXException {
      this.contents.reset();
   }

   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
      if (localName.equals("FileNev")) {
         this.csatInfo.setFileNev(this.contents.toString());
      }

      if (localName.equals("Megjegyzes")) {
         this.csatInfo.setMegjegyzes(this.contents.toString());
      }

      if (localName.equals("MimeTipus")) {
         this.csatInfo.setMimeTipus(this.contents.toString());
      }

      if (localName.equals("URI")) {
         this.csatInfo.setFileURI(this.contents.toString());
      }

      if (localName.equals("CsatolmanyInfo")) {
         this.metaData.addCsatInfo(this.csatInfo);
         this.parser.getReader().setContentHandler(this.parent);
      }

   }
}
