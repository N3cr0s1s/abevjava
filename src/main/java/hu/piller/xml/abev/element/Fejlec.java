package hu.piller.xml.abev.element;

import hu.piller.xml.XMLElem;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

public class Fejlec implements XMLElem {
   public static final String NAMESPACE = "abev";
   public static final String TAG_FEJLEC = "Fejlec";
   public static final String TAG_CIMZETT = "Cimzett";
   public static final String TAG_CIMZETTNYILVANOSKULCS = "CimzettNyilvanosKulcs";
   public static final String TAG_DOKTIPUSAZONOSITO = "DokTipusAzonosito";
   public static final String TAG_DOKTIPUSLEIRAS = "DokTipusLeiras";
   public static final String TAG_DOKTIPUSVERZIO = "DokTipusVerzio";
   public static final String TAG_FILENEV = "FileNev";
   public static final String TAG_MEGJEGYZES = "Megjegyzes";
   public static final String TAG_PARAMLISTA = "ParameterLista";
   public static final String TAG_PARAM = "Parameter";
   public static final String TAG_CSATOLMANY_INFO = "CsatolmanyInfo";
   public static final String ATTR_NEV = "Nev";
   public static final String ATTR_ERTEK = "Ertek";
   private DocMetaData metaData;

   public void setMetaData(DocMetaData metaData) {
      this.metaData = metaData;
   }

   public DocMetaData getMetaData() {
      return this.metaData;
   }

   public Fejlec() {
   }

   public Fejlec(DocMetaData metaData) {
      this.setMetaData(metaData);
   }

   public String toXML(String indent) throws IOException {
      indent = indent + "\t";
      StringBuffer str = new StringBuffer();
      str.append(indent).append("<").append("abev").append(":").append("Fejlec").append(">");
      if (this.metaData.getCimzett() != null) {
         str.append(indent).append("\t<").append("abev").append(":").append("Cimzett").append(">").append(this.metaData.getCimzett()).append("</").append("abev").append(":").append("Cimzett").append(">");
      }

      if (this.metaData.getDokTipusAzonosito() != null) {
         str.append(indent).append("\t<").append("abev").append(":").append("DokTipusAzonosito").append(">").append(this.metaData.getDokTipusAzonosito()).append("</").append("abev").append(":").append("DokTipusAzonosito").append(">");
      }

      if (this.metaData.getDokTipusLeiras() != null) {
         str.append(indent).append("\t<").append("abev").append(":").append("DokTipusLeiras").append(">").append(this.metaData.getDokTipusLeiras()).append("</").append("abev").append(":").append("DokTipusLeiras").append(">");
      }

      if (this.metaData.getDokTipusVerzio() != null) {
         str.append(indent).append("\t<").append("abev").append(":").append("DokTipusVerzio").append(">").append(this.metaData.getDokTipusVerzio()).append("</").append("abev").append(":").append("DokTipusVerzio").append(">");
      }

      if (this.metaData.getFileNev() != null) {
         str.append(indent).append("\t<").append("abev").append(":").append("FileNev").append(">").append(this.metaData.getFileNev()).append("</").append("abev").append(":").append("FileNev").append(">");
      }

      if (this.metaData.getMegjegyzes() != null) {
         str.append(indent).append("\t<").append("abev").append(":").append("Megjegyzes").append(">").append(this.metaData.getMegjegyzes()).append("</").append("abev").append(":").append("Megjegyzes").append(">");
      }

      Enumeration en;
      if (this.metaData.getParamList() != null) {
         str.append(indent).append("\t<").append("abev").append(":").append("ParameterLista").append(">");
         en = this.metaData.getParamList().keys();

         while(en.hasMoreElements()) {
            String key = (String)en.nextElement();
            String val = (String)this.metaData.getParamList().get(key);
            str.append(indent).append("\t\t<").append("abev").append(":").append("Parameter").append(" ").append("Nev").append("='").append(key).append("' ").append("Ertek").append("='").append(val).append("'/>");
         }

         str.append(indent).append("\t").append("</").append("abev").append(":").append("ParameterLista").append(">");
      }

      if (this.metaData.getCsatInfoLista() != null && !this.metaData.getCsatInfoLista().isEmpty()) {
         en = this.metaData.getCsatInfoLista().elements();

         while(en.hasMoreElements()) {
            str.append(((CsatolmanyInfo)en.nextElement()).toXML(indent));
         }
      }

      str.append(indent).append("</").append("abev").append(":").append("Fejlec").append(">");
      return str.toString();
   }

   public void printXML(String indent, OutputStream out) throws IOException {
      out.write(this.toXML(indent).getBytes("iso-8859-2"));
   }

   public String toString() {
      try {
         return this.toXML("");
      } catch (IOException var2) {
         var2.printStackTrace();
         return null;
      }
   }
}
