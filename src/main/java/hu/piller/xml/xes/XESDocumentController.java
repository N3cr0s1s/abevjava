package hu.piller.xml.xes;

import hu.piller.xml.FinishException;
import hu.piller.xml.XMLDocumentController;
import hu.piller.xml.xes.element.KeyInfo;
import java.io.IOException;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public abstract class XESDocumentController extends XMLDocumentController {
   public XESDocumentController(XMLReader reader) {
      super(reader);
   }

   public abstract void addKeyInfo(KeyInfo var1) throws IOException, FinishException, SAXException;
}
