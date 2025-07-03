package hu.piller.enykp.alogic.fileloader.kr;

import java.util.Hashtable;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class KrHeadHandler extends DefaultHandler {
   public static final String FORCED_STOP = "*FORCE_END*";
   private Hashtable data = new Hashtable();
   private boolean inOrg = false;
   private boolean inDocType = false;
   private boolean inDocVersion = false;

   public void characters(char[] var1, int var2, int var3) throws SAXException {
      String var4;
      if (this.inOrg) {
         var4 = new String(var1, var2, var3);
         this.data.put("org", var4);
      }

      if (this.inDocType) {
         var4 = new String(var1, var2, var3);
         this.data.put("DokTipusAzonosito", var4);
      }

      if (this.inDocVersion) {
         var4 = new String(var1, var2, var3);
         this.data.put("DokTipusVerzio", var4);
      }

   }

   public void endElement(String var1, String var2, String var3) throws SAXException {
      if (var3.equals("abev:Cimzett")) {
         this.inOrg = false;
      } else if (var3.equals("abev:DokTipusAzonosito")) {
         this.inDocType = false;
      } else {
         if (var3.equals("abev:DokTipusVerzio")) {
            this.inDocType = false;
         }

         if (var3.equals("abev:Fejlec")) {
            this.inOrg = false;
            this.inDocType = false;
            this.inDocType = false;
            throw new SAXException("*FORCE_END*");
         }
      }

   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      if (var3.equals("abev:Cimzett")) {
         this.inOrg = true;
      } else if (var3.equals("abev:DokTipusAzonosito")) {
         this.inDocType = true;
      } else if (var3.equals("abev:DokTipusVerzio")) {
         this.inDocVersion = true;
      } else if (var3.equals("abev:Parameter")) {
         this.attributes_done(var4);
      }

   }

   private void attributes_done(Attributes var1) {
      String var2 = "";
      String var3 = "";

      for(int var4 = 0; var4 < var1.getLength(); ++var4) {
         if ("Nev".equalsIgnoreCase(var1.getQName(var4))) {
            var2 = var1.getValue(var4);
         }

         if ("Ertek".equalsIgnoreCase(var1.getQName(var4))) {
            var3 = var1.getValue(var4);
         }
      }

      this.data.put(var2, var3);
   }

   public Hashtable getData() {
      return this.data;
   }
}
