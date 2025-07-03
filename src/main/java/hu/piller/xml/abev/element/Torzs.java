package hu.piller.xml.abev.element;

import hu.piller.xml.xes.element.EncryptedData;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Vector;

public class Torzs {
   public static final String TAG_TORZS = "Torzs";
   private EncryptedData encData;
   private Vector csatLista;

   public void printXML(String indent, OutputStream out) throws IOException {
      indent = indent + "\t";
      out.write((indent + "<abev:" + "Torzs" + ">").getBytes("iso-8859-2"));
      if (this.encData != null) {
         this.encData.printXML(indent, out);
      }

      if (this.csatLista != null) {
         Enumeration en = this.csatLista.elements();

         while(en.hasMoreElements()) {
            Csatolmany csat = (Csatolmany)en.nextElement();
            csat.printXML(indent, out);
         }
      }

      out.write((indent + "</abev:" + "Torzs" + ">").getBytes("iso-8859-2"));
   }

   public Vector getCsatLista() {
      return this.csatLista;
   }

   public void addCsatolmany(Csatolmany csat) {
      if (this.csatLista == null) {
         this.csatLista = new Vector();
      }

      this.csatLista.add(csat);
   }

   public EncryptedData getEncData() {
      return this.encData;
   }

   public void setEncData(EncryptedData encData) {
      this.encData = encData;
   }
}
