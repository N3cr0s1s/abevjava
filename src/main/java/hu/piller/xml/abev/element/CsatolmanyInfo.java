package hu.piller.xml.abev.element;

import hu.piller.xml.XMLElem;
import java.io.IOException;
import java.io.OutputStream;

public class CsatolmanyInfo implements XMLElem {
   private static int n = 5;
   public static final String NAMESPACE = "abev";
   private String azon;
   private String fileNev;
   private String fileURI;
   private String mimeTipus;
   private String megjegyzes;
   public static final String ATT_AZON = "Azonosito";
   public static final String TAG_CSATOLMANY_INFO = "CsatolmanyInfo";
   public static final String TAG_FILE_NEV = "FileNev";
   public static final String TAG_URI = "URI";
   public static final String TAG_MIME_TIPUS = "MimeTipus";
   public static final String TAG_MEGJEGZYES = "Megjegyzes";

   public CsatolmanyInfo() {
      this.azon = "" + n++;
   }

   public CsatolmanyInfo(String azon, String fileNev, String fileURI, String mimeTipus, String megjegyzes) {
      this.azon = "" + n++;
      if (azon != null && azon.length() > 0) {
         this.azon = azon;
      }

      this.megjegyzes = megjegyzes;
      this.fileNev = fileNev;
      this.fileURI = fileURI;
      this.mimeTipus = mimeTipus;
   }

   public String getFileNev() {
      return this.fileNev;
   }

   public void setFileNev(String fileNev) {
      this.fileNev = fileNev;
   }

   public String getFileURI() {
      return this.fileURI;
   }

   public void setFileURI(String fileURI) {
      this.fileURI = fileURI;
   }

   public String getMimeTipus() {
      return this.mimeTipus;
   }

   public void setMimeTipus(String mimeTipus) {
      this.mimeTipus = mimeTipus;
   }

   public String getMegjegyzes() {
      return this.megjegyzes;
   }

   public void setMegjegyzes(String megjegyzes) {
      this.megjegyzes = megjegyzes;
   }

   public String getAzon() {
      return this.azon != null ? this.azon : "" + this.hashCode();
   }

   public void setAzon(String azon) {
      this.azon = azon;
   }

   public String toXML(String indent) throws IOException {
      indent = indent + "\t";
      StringBuffer str = new StringBuffer();
      str.append(indent).append("<").append("abev").append(":").append("CsatolmanyInfo").append(" Azonosito='" + this.azon).append("'>");
      if (this.getFileNev() != null && this.getFileNev().length() > 0) {
         str.append(indent).append("\t<").append("abev").append(":").append("FileNev").append(">").append(this.getFileNev()).append("</").append("abev").append(":").append("FileNev").append(">");
      }

      if (this.getFileURI() != null && this.getFileURI().length() > 0) {
         str.append(indent).append("\t<").append("abev").append(":").append("URI").append(">").append(this.getFileURI()).append("</").append("abev").append(":").append("URI").append(">");
      }

      if (this.getMimeTipus() != null && this.getMimeTipus().length() > 0) {
         str.append(indent).append("\t<").append("abev").append(":").append("MimeTipus").append(">").append(this.getMimeTipus()).append("</").append("abev").append(":").append("MimeTipus").append(">");
      }

      if (this.getMegjegyzes() != null && this.getMegjegyzes().length() > 0) {
         str.append(indent).append("\t<").append("abev").append(":").append("Megjegyzes").append(">").append(this.getMegjegyzes()).append("</").append("abev").append(":").append("Megjegyzes").append(">");
      }

      str.append(indent).append("</").append("abev").append(":").append("CsatolmanyInfo").append(">");
      return str.toString();
   }

   public void printXML(String indent, OutputStream out) throws IOException {
      out.write(this.toXML(indent).getBytes("iso-8859-2"));
   }

   public String toString() {
      try {
         return this.toXML("\n");
      } catch (IOException var2) {
         var2.printStackTrace();
         return null;
      }
   }
}
