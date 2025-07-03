package hu.piller.enykp.alogic.kontroll;

public class KData {
   public String azonosito;
   public String informacio;
   public String mentve;
   public String megjegyzes;
   public String filename;

   public KData(String var1, String var2, String var3, String var4, String var5) {
      this.azonosito = var1;
      this.informacio = var2;
      this.megjegyzes = var3;
      this.mentve = var4;
      this.filename = var5;
   }

   public KData() {
   }
}
