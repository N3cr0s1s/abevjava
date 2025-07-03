package hu.piller.enykp.alogic.kihatas;

import java.util.Vector;

public class MegallapitasComboRecord {
   private String msvo_azon;
   private String nev;
   private String sorszam;
   private Vector adonemlista;

   public MegallapitasComboRecord(String var1, String var2, String var3, Vector var4) {
      this.msvo_azon = var1;
      this.nev = var2;
      this.sorszam = var3;
      this.adonemlista = var4;
   }

   public String getMsvo_azon() {
      return this.msvo_azon;
   }

   public void setMsvo_azon(String var1) {
      this.msvo_azon = var1;
   }

   public String getNev() {
      return this.nev;
   }

   public void setNev(String var1) {
      this.nev = var1;
   }

   public String getSorszam() {
      return this.sorszam;
   }

   public void setSorszam(String var1) {
      this.sorszam = var1;
   }

   public Vector getAdonemlista() {
      return this.adonemlista;
   }

   public void setAdonemlista(Vector var1) {
      this.adonemlista = var1;
   }

   public String getDisplayText() {
      String var1 = "" + this.sorszam;
      if (var1.length() == 1) {
         var1 = "  " + var1;
      }

      return var1 + ". " + this.nev;
   }
}
