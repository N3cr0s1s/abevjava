package hu.piller.xml.abev.element;

import hu.piller.xml.XMLElem;
import java.io.IOException;
import java.io.OutputStream;

public class Boritek implements XMLElem {
   public static final String TAG_BORITEK = "Boritek";
   private Fejlec fejlec;
   private Torzs torzs;

   public void setFejlec(Fejlec fejlec) {
      this.fejlec = fejlec;
   }

   public Fejlec getFejlec() {
      return this.fejlec;
   }

   public void setTorzs(Torzs torzs) {
      this.torzs = torzs;
   }

   public Torzs getTorzs() {
      return this.torzs;
   }

   public void printXML(String indent, OutputStream out) throws IOException {
      out.write("<?xml version=\"1.0\" encoding=\"iso-8859-2\"?>".getBytes("iso-8859-2"));
      out.write((indent + "<abev:Boritek xmlns:abev='http://iop.gov.hu/2006/01/boritek'>").getBytes("iso-8859-2"));
      this.fejlec.printXML(indent, out);
      if (this.torzs != null) {
         this.torzs.printXML(indent, out);
      }

      out.write((indent + "</abev:Boritek>").getBytes("iso-8859-2"));
   }
}
