package hu.piller.xml;

import org.xml.sax.SAXException;

public class MissingKeyException extends SAXException {
   public MissingKeyException(String message) {
      super(message);
   }
}
