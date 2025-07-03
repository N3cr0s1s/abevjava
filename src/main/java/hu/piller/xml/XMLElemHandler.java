package hu.piller.xml;

import java.io.CharArrayWriter;
import java.io.IOException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public abstract class XMLElemHandler extends DefaultHandler {
   protected XMLDocumentController parser;
   protected XMLElemHandler parent;
   protected CharArrayWriter contents;
   private static char[] ampChars = new char[]{'&', 'a', 'm', 'p', ';'};
   private static char[] ltChars = new char[]{'&', 'l', 't', ';'};
   private static char[] gtChars = new char[]{'&', 'g', 't', ';'};

   public XMLElemHandler(XMLElemHandler parent, XMLDocumentController parser) {
      this.parser = parser;
      this.parent = parent;
      this.contents = new CharArrayWriter();
   }

   public void characters(char[] ch, int start, int length) throws SAXException {
      try {
         for(int i = start; i < start + length; ++i) {
            switch(ch[i]) {
            case '&':
               this.contents.write(ampChars);
               break;
            case '<':
               this.contents.write(gtChars);
               break;
            case '>':
               this.contents.write(ltChars);
               break;
            default:
               this.contents.write(ch[i]);
            }
         }
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   public abstract void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException;

   public abstract void endElement(String var1, String var2, String var3) throws SAXException;
}
