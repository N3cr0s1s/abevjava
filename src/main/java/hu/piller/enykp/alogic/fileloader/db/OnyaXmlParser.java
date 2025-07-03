package hu.piller.enykp.alogic.fileloader.db;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class OnyaXmlParser extends DefaultHandler {
   private boolean needParse = false;
   private String messageId;

   public String getMessageId() {
      return this.messageId;
   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      if ("nav:MessageID".equalsIgnoreCase(var3)) {
         this.needParse = true;
      }

   }

   public void endElement(String var1, String var2, String var3) throws SAXException {
      if ("nav:MessageID".equalsIgnoreCase(var3)) {
         this.needParse = false;
      }

   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
      if (this.needParse) {
         this.messageId = new String(var1, var2, var3);
      }

   }
}
