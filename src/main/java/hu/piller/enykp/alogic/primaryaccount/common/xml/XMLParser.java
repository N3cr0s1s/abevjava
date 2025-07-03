package hu.piller.enykp.alogic.primaryaccount.common.xml;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import me.necrocore.abevjava.NecroFile;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class XMLParser extends DefaultXMLParser {
   private XMLParser.PathStore path_store = new XMLParser.PathStore();

   public XMLParser() throws SAXException {
   }

   public XMLParser(String var1) throws SAXException {
      super(var1);
   }

   public XMLParser.PathStore getPathStore() {
      return this.path_store;
   }

   public void parse(String var1) throws IOException, SAXException {
      super.parse((new NecroFile(var1)).toURI().toString());
   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
   }

   public void endElement(String var1, String var2, String var3) throws SAXException {
   }

   public void startDocument() throws SAXException {
   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
   }

   public void endDocument() throws SAXException {
   }

   protected final class PathStore {
      private Vector v = new Vector(30, 20);
      private String separator = "/";
      private String path = "";

      public void add(String var1) {
         this.v.add(var1);
         this.createPath();
      }

      public void remove() {
         this.v.remove(this.v.size() - 1);
         this.createPath();
      }

      public void clear() {
         this.v.removeAllElements();
      }

      private void createPath() {
         String var1 = "";
         int var2 = 0;

         for(int var3 = this.v.size(); var2 < var3; ++var2) {
            var1 = var1 + (var2 == 0 ? this.v.get(var2).toString() : this.separator + this.v.get(var2));
         }

         this.path = var1;
      }

      public String toString() {
         return this.path;
      }
   }
}
