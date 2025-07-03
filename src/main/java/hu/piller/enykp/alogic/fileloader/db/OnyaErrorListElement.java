package hu.piller.enykp.alogic.fileloader.db;

import java.util.ArrayList;

public class OnyaErrorListElement {
   public static final String NOT_ERROR = "-1";
   public static final String WARNING = "1";
   public static final String ERROR = "2";
   public static final String FATAL_ERROR = "3";
   public static final int SINGLE_BIZONYLAT = -1;
   public static final String TECHICAL_EXCEPTION = "p010";
   public static final String TECHNICAL_EXCEPTION_MESSAGE = "Hiba történt az ellenőrzéskor!";
   private String hibaKod;
   private String hibaSzoveg;
   private String hibaTipus;
   private String hibaSzint;
   private String hibaSzoveg2;
   private int bizonylatResz;
   private ArrayList<String> fids = new ArrayList();
   private boolean newPageStart;

   public OnyaErrorListElement(String var1, String var2, String var3, String var4) {
      this.hibaKod = var1;
      this.hibaSzoveg = var2;
      this.hibaTipus = var3;
      this.hibaSzint = var4;
      this.newPageStart = false;
      this.bizonylatResz = -1;
   }

   public OnyaErrorListElement(String var1, String var2, String var3, String var4, int var5) {
      this.hibaKod = var1;
      this.hibaSzoveg = var2;
      this.hibaTipus = var3;
      this.hibaSzint = var4;
      this.newPageStart = false;
      this.bizonylatResz = var5;
   }

   public OnyaErrorListElement(String var1, String var2, String var3, String var4, String var5, int var6) {
      this.hibaKod = var1;
      this.hibaSzoveg = var2;
      this.hibaTipus = var3;
      this.hibaSzint = var4;
      this.newPageStart = false;
      this.hibaSzoveg2 = var5;
      this.bizonylatResz = var6;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof OnyaErrorListElement)) {
         return false;
      } else {
         OnyaErrorListElement var2 = (OnyaErrorListElement)var1;
         return this.hibaKod.equals(var2.hibaKod) && this.hibaTipus.equals(var2.hibaTipus) && this.hibaSzint.equals(var2.hibaSzint);
      }
   }

   public void addFid(String var1) {
      if (var1 != null) {
         if (!this.fids.contains(var1)) {
            this.fids.add(var1);
         }

      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("\t\t<ae:hibaElem ");
      var1.append("szint=\"").append(this.hibaSzint).append("\"").append(" hibaszoveg=\"").append(this.getXmlHibaszoveg(this.hibaSzoveg)).append("\"").append(" tipus=\"").append(this.hibaTipus).append("\"").append(" kod=\"").append(this.getXmlHibaszoveg(this.hibaKod)).append("\"").append(">\n");

      for(int var2 = 0; var2 < this.fids.size(); ++var2) {
         var1.append("\t\t\t<ae:fid>").append((String)this.fids.get(var2)).append("</ae:fid>\n");
      }

      if (this.bizonylatResz != -1) {
         var1.append("\t\t\t<ae:bizonylatResz>").append(this.bizonylatResz).append("</ae:bizonylatResz>");
      }

      var1.append("\t\t</ae:hibaElem>\n");
      return var1.toString();
   }

   public boolean isNewPageStart() {
      return this.newPageStart;
   }

   public void setNewPageStart(boolean var1) {
      this.newPageStart = var1;
   }

   public boolean isFatalError() {
      return "2".equals(this.hibaSzint);
   }

   public boolean isRealError() {
      if ("-1".equals(this.hibaSzint)) {
         return false;
      } else {
         return !this.newPageStart;
      }
   }

   public String getHibaKod() {
      return this.hibaKod;
   }

   public String getHibaSzoveg() {
      return this.hibaSzoveg;
   }

   public void setHibaTipus(String var1) {
      this.hibaTipus = var1;
   }

   private String getXmlHibaszoveg(String var1) {
      if (var1 == null) {
         return "";
      } else {
         String var2 = "";
         var2 = var1.replaceAll("\n", " _ ").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "'");
         return var2;
      }
   }

   public ArrayList<String> getFids() {
      return this.fids;
   }
}
