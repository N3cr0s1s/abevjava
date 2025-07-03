package hu.piller.enykp.alogic.masterdata.converter.internal;

import java.util.Hashtable;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PAHandler extends DefaultHandler {
   private String type;
   private Hashtable<String, Hashtable<String, String>> elements = new Hashtable();
   private Hashtable<String, String> curElement;

   public void setType(String var1) {
      this.elements.clear();
      this.type = var1;
   }

   public String getType() {
      return this.type;
   }

   public Hashtable<String, Hashtable<String, String>> getElements() {
      return this.elements;
   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      if (var2.equals(this.type)) {
         this.curElement = new Hashtable();
         String var5 = "";

         for(int var6 = 0; var6 < var4.getLength(); ++var6) {
            if ("id".equalsIgnoreCase(var4.getLocalName(var6))) {
               var5 = var4.getValue(var6);
            } else {
               this.curElement.put(var4.getLocalName(var6), var4.getValue(var6));
            }
         }

         if (!"".equals(var5)) {
            this.elements.put(var5, this.curElement);
         }
      }

   }
}
