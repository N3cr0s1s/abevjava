package hu.piller.enykp.alogic.fileloader.kr;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class KrHeadParser {
   String filename;
   String encoding;
   KrHeadHandler doch = new KrHeadHandler();

   public KrHeadParser(String var1, String var2) {
      this.filename = var1;
      this.encoding = var2 == null ? "iso-8859-2" : var2;
   }

   public void parse() throws IOException, SAXException, ParserConfigurationException {
      FileInputStream var1 = null;

      try {
         var1 = new FileInputStream(this.filename);
         InputSource var2 = new InputSource(var1);
         var2.setEncoding(this.encoding);
         SAXParserFactory var3 = SAXParserFactory.newInstance();
         var3.setNamespaceAware(true);
         var3.setValidating(true);
         SAXParser var4 = var3.newSAXParser();
         var4.parse(var2, this.doch);
      } finally {
         try {
            var1.close();
         } catch (Exception var10) {
         }

      }

   }

   public Hashtable getData() {
      return this.doch.getData();
   }
}
