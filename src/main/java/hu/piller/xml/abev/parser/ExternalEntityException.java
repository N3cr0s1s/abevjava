package hu.piller.xml.abev.parser;

import org.xml.sax.SAXException;

public class ExternalEntityException extends SAXException {
   public ExternalEntityException(Exception e) {
      super(e);
   }

   public ExternalEntityException(String message) {
      super(message);
   }

   public ExternalEntityException(String message, Exception e) {
      super(message, e);
   }
}
