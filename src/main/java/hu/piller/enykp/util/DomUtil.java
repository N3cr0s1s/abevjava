package hu.piller.enykp.util;

import java.io.StringWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

public class DomUtil {
   public static void print(Document var0) {
      System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Print Document >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
      System.out.println(documentToString(var0));
      System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Print Document <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
   }

   public static String documentToString(Document var0) {
      String var1;
      try {
         TransformerFactory var2 = TransformerFactory.newInstance();
         Transformer var3 = var2.newTransformer();
         var3.setOutputProperty("omit-xml-declaration", "yes");
         StringWriter var4 = new StringWriter();
         var3.transform(new DOMSource(var0), new StreamResult(var4));
         var1 = var4.toString();
      } catch (TransformerException var5) {
         var5.printStackTrace();
         var1 = "ERROR " + var5.getMessage();
      }

      return var1;
   }
}
