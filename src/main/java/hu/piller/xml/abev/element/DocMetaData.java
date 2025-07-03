package hu.piller.xml.abev.element;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

public class DocMetaData {
   private String cimzett;
   private String dokTipusAzonosito;
   private String dokTipusLeiras;
   private String dokTipusVerzio;
   private String fileNev;
   private String megjegyzes;
   private Properties paramList = new Properties();
   private String cimzettNyilvanosKulcs;
   public static final String EMPTY_HASH = "0000000000000000000000000000000000000000";
   private String docHash = "";
   private Hashtable csatInfoLista;

   public DocMetaData() {
      this.paramList.put("dokhash", "0000000000000000000000000000000000000000");
   }

   public String getDocHash() {
      return this.docHash;
   }

   public void setDocHash(String hashValue) {
      this.docHash = hashValue;
      this.paramList.put("dochash", hashValue);
   }

   public String getCimzettNyilvanosKulcs() {
      return this.cimzettNyilvanosKulcs;
   }

   public void setCimzettNyilvanosKulcs(String cimzettNyilvanosKulcs) {
      this.cimzettNyilvanosKulcs = cimzettNyilvanosKulcs;
   }

   public Properties getParamList() {
      return this.paramList;
   }

   public void setParamLista(Properties paramList) {
      this.paramList = paramList;
   }

   public void addParam(String paramName, String paramValue) {
      if (this.paramList == null) {
         this.paramList = new Properties();
      }

      this.paramList.setProperty(paramName, paramValue);
   }

   public String getCimzett() {
      return this.cimzett;
   }

   public void setCimzett(String cimzett) {
      this.cimzett = cimzett;
   }

   public String getDokTipusAzonosito() {
      return this.dokTipusAzonosito;
   }

   public void setDokTipusAzonosito(String dokTipusAzonosito) {
      this.dokTipusAzonosito = dokTipusAzonosito;
   }

   public String getDokTipusLeiras() {
      return this.dokTipusLeiras;
   }

   public void setDokTipusLeiras(String dokTipusLeiras) {
      this.dokTipusLeiras = dokTipusLeiras;
   }

   public String getDokTipusVerzio() {
      return this.dokTipusVerzio;
   }

   public void setDokTipusVerzio(String dokTipusVerzio) {
      this.dokTipusVerzio = dokTipusVerzio;
   }

   public String getFileNev() {
      return this.fileNev;
   }

   public void setFileNev(String fileNev) {
      this.fileNev = fileNev;
   }

   public String getMegjegyzes() {
      return this.megjegyzes;
   }

   public void setMegjegyzes(String megjegyzes) {
      this.megjegyzes = megjegyzes;
   }

   public boolean equals(Object obj) {
      if (!obj.getClass().getName().equalsIgnoreCase(this.getClass().getName())) {
         return false;
      } else {
         DocMetaData ma = (DocMetaData)obj;
         if (ma.getCimzett() != null && !ma.getCimzett().equals(this.getCimzett()) || this.getCimzett() != null && ma.getCimzett() == null) {
            return false;
         } else if (ma.getDokTipusAzonosito() != null && !ma.getDokTipusAzonosito().equals(this.getDokTipusAzonosito()) || this.getDokTipusAzonosito() != null && ma.getDokTipusAzonosito() == null) {
            return false;
         } else if ((ma.getDokTipusLeiras() == null || ma.getDokTipusLeiras().equals(this.getDokTipusLeiras())) && (this.getDokTipusLeiras() == null || ma.getDokTipusLeiras() != null)) {
            if (ma.getDokTipusVerzio() != null && !ma.getDokTipusVerzio().equals(this.getDokTipusVerzio()) || this.getDokTipusVerzio() != null && ma.getDokTipusVerzio() == null) {
               return false;
            } else if (ma.getFileNev() != null && !ma.getFileNev().equals(this.getFileNev()) || this.getFileNev() != null && ma.getFileNev() == null) {
               return false;
            } else if ((ma.getMegjegyzes() == null || ma.getMegjegyzes().equals(this.getMegjegyzes())) && (this.getMegjegyzes() == null || ma.getMegjegyzes() != null)) {
               if (ma.getParamList() == null ^ this.getParamList() == null) {
                  return false;
               } else {
                  if (ma.getParamList() != null) {
                     Properties params = ma.getParamList();
                     Enumeration en = params.propertyNames();

                     while(en.hasMoreElements()) {
                        String key = (String)en.nextElement();
                        String val = params.getProperty(key);
                        if (!this.getParamList().getProperty(key).equals(val)) {
                           return false;
                        }
                     }
                  }

                  return true;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   public String toString() {
      try {
         return (new Fejlec(this)).toXML("\n");
      } catch (IOException var2) {
         return null;
      }
   }

   public void addCsatInfo(CsatolmanyInfo csatInfo) {
      String azon = csatInfo.getAzon();
      if (this.csatInfoLista == null) {
         this.csatInfoLista = new Hashtable();
      }

      this.csatInfoLista.put(azon, csatInfo);
   }

   public void setCsatInfoLista(Hashtable csatInfoLista) {
      this.csatInfoLista = csatInfoLista;
   }

   public Hashtable getCsatInfoLista() {
      return this.csatInfoLista;
   }
}
