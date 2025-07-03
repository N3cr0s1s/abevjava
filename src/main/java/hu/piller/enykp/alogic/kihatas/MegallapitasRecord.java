package hu.piller.enykp.alogic.kihatas;

public class MegallapitasRecord {
   public static final String TOROLTSEGJEL = "T";
   public static final String MODOSULTJEL = "M";
   public static final String UJJEL = "";
   private String kias_azon;
   private String msvo_azon;
   private String adonemkod;
   private String toroltsegjel;

   public MegallapitasRecord(String var1, String var2, String var3, String var4) {
      this.kias_azon = var1;
      this.msvo_azon = var2;
      this.adonemkod = var3;
      this.toroltsegjel = var4;
   }

   public String getKias_azon() {
      return this.kias_azon == null ? "" : this.kias_azon;
   }

   public void setKias_azon(String var1) {
      this.kias_azon = var1;
   }

   public String getMsvo_azon() {
      return this.msvo_azon;
   }

   public void setMsvo_azon(String var1) {
      this.msvo_azon = var1;
   }

   public String getAdonemkod() {
      return this.adonemkod;
   }

   public void setAdonemkod(String var1) {
      this.adonemkod = var1;
   }

   public String getToroltsegjel() {
      return this.toroltsegjel;
   }

   public void setToroltsegjel(String var1) {
      this.toroltsegjel = var1;
   }

   public boolean isDeleted() {
      return "T".equals(this.toroltsegjel);
   }

   public boolean isNew() {
      return "".equals(this.toroltsegjel);
   }

   public boolean isModified() {
      return "M".equals(this.toroltsegjel);
   }

   public MegallapitasRecord make_copy() {
      MegallapitasRecord var1 = new MegallapitasRecord(this.kias_azon, this.msvo_azon, this.adonemkod, this.toroltsegjel);
      return var1;
   }

   public String toString() {
      return "(" + this.kias_azon + "," + this.msvo_azon + "," + this.adonemkod + "," + this.toroltsegjel + ")";
   }
}
