package hu.piller.xml;

import hu.piller.xml.abev.parser.NullEntityResolver;
import org.xml.sax.XMLReader;

public abstract class XMLDocumentController {
   protected XMLReader reader;

   public XMLDocumentController(XMLReader reader) {
      this.reader = reader;
      this.reader.setEntityResolver(new NullEntityResolver());
   }

   public XMLReader getReader() {
      return this.reader;
   }
}
