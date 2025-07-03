package hu.piller.enykp.alogic.orghandler.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Vector;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class OrgInfoParser extends DefaultXMLParser {
   private Hashtable xml_result;
   private Hashtable current_tag;

   public OrgInfoParser() throws SAXException {
      this.init();
   }

   public OrgInfoParser(String var1) throws SAXException {
      super(var1);
      this.init();
   }

   private void init() {
      this.xml_result = new Hashtable();
   }

   public Hashtable parse(byte[] var1) {
      try {
         this.xml_result.clear();
         ByteArrayInputStream var2 = new ByteArrayInputStream(var1);
         super.parse((InputStream)var2);
         var2.close();
         this.xml_result = (Hashtable)((Vector)this.xml_result.get("children")).get(0);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return this.xml_result;
   }

   public void startDocument() throws SAXException {
      this.current_tag = this.xml_result;
   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      Hashtable var5 = this.current_tag;
      if (var5 != null) {
         Hashtable var7 = new Hashtable();
         var7.put("name", var2);
         var7.put("attributes", this.getAttributes(var4));
         Vector var6 = (Vector)var5.get("children");
         if (var6 == null) {
            var6 = new Vector(16);
            var5.put("children", var6);
         }

         var6.add(var7);
         var7.put("parent", var5);
         this.current_tag = var7;
      }

   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
      if (this.current_tag != null) {
         String var4 = (String)this.current_tag.get("value");
         if (var4 == null) {
            var4 = "";
         }

         var4 = var4 + new String(var1, var2, var3);
         this.current_tag.put("value", var4);
      }

   }

   public void endElement(String var1, String var2, String var3) throws SAXException {
      Hashtable var4 = this.current_tag;
      if (var4 != null) {
         this.current_tag = (Hashtable)var4.get("parent");
      }

   }

   public Hashtable getAttributes(Attributes var1) {
      int var2 = var1.getLength();
      Hashtable var3 = new Hashtable(var2);

      for(int var4 = 0; var4 < var2; ++var4) {
         var3.put(this.getString(var1.getLocalName(var4)), this.getString(var1.getValue(var4)));
      }

      return var3;
   }

   private String getString(String var1) {
      return var1 == null ? "" : var1;
   }
}
