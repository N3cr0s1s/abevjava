package hu.piller.xml;

import org.xml.sax.SAXException;

public class FinishException extends SAXException {
   public FinishException(Exception e) {
      super(e);
   }

   public FinishException(String message) {
      super(message);
   }

   public FinishException(String message, Exception e) {
      super(message, e);
   }
}
