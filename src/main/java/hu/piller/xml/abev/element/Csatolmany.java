package hu.piller.xml.abev.element;

import hu.piller.xml.XMLElem;
import hu.piller.xml.xes.element.EncryptedData;
import java.io.IOException;
import java.io.OutputStream;

public class Csatolmany implements XMLElem {
   private String csatInfoAzon;
   private EncryptedData encData;
   public static final String ATT_AZON = "Azonosito";
   public static final String TAG_CSAT = "Csatolmany";

   public Csatolmany() {
   }

   public Csatolmany(String csatInfoAzon, EncryptedData encData) {
      this.csatInfoAzon = csatInfoAzon;
      this.encData = encData;
   }

   public void printXML(String indent, OutputStream out) throws IOException {
      indent = indent + "\t";
      out.write((indent + "<abev:" + "Csatolmany" + " " + "Azonosito" + "='" + this.csatInfoAzon + "'>").getBytes("iso-8859-2"));
      if (this.getEncData() != null) {
         this.getEncData().printXML(indent, out);
      }

      out.write((indent + "</abev:" + "Csatolmany" + ">").getBytes("iso-8859-2"));
   }

   public String getCsatInfo() {
      return this.csatInfoAzon;
   }

   public void setCsatInfo(String csatInfoAzon) {
      this.csatInfoAzon = csatInfoAzon;
   }

   public EncryptedData getEncData() {
      return this.encData;
   }

   public void setEncData(EncryptedData encData) {
      this.encData = encData;
   }
}
