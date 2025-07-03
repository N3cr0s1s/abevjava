package hu.piller.enykp.alogic.calculator.matrices.matrixproviderimpl;

import hu.piller.enykp.alogic.calculator.matrices.MatrixMeta;
import java.util.HashMap;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MatricesHandler extends DefaultHandler {
   private HashMap<String, MatrixMeta> matrices;
   private MatrixMeta currentMeta;
   private String currVal;

   public MatrixMeta getMatrixMeta(String var1) {
      return (MatrixMeta)this.matrices.get(var1.toUpperCase());
   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      if (var3.equals("matrix")) {
         this.currentMeta = new MatrixMeta();
      }

   }

   public void endElement(String var1, String var2, String var3) throws SAXException {
      if (var3.equals("matrix")) {
         this.matrices.put(this.currentMeta.getName().toUpperCase(), this.currentMeta);
      } else if (var3.equals("name")) {
         this.currentMeta.setName(this.currVal);
      } else if (var3.equals("file")) {
         this.currentMeta.setFile(this.currVal);
      } else if (var3.equals("encoding")) {
         this.currentMeta.setEncoding(this.currVal);
      } else if (var3.equals("delimiter")) {
         this.currentMeta.setDelimiter(this.currVal);
      }

   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
      this.currVal = new String(var1, var2, var3);
   }

   public void startDocument() throws SAXException {
      super.startDocument();
      this.matrices = new HashMap();
   }

   public void endDocument() throws SAXException {
      super.endDocument();
   }
}
