package hu.piller.xml.abev.parser;

import java.io.IOException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class NullEntityResolver implements EntityResolver {
   public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
      throw new ExternalEntityException("No external entity allowed!!! " + (publicId == null ? "" : "publicid=" + publicId) + (systemId == null ? "" : "systemid=" + systemId));
   }
}
