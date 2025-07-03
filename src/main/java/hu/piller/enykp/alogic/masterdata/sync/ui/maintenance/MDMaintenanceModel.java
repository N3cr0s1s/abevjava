package hu.piller.enykp.alogic.masterdata.sync.ui.maintenance;

public class MDMaintenanceModel {
   public static final int DATA_ROW = 0;
   public static final int HEADER_ROW = 1;
   private int type;
   private long id;
   private String blockName;
   private int seq = 1;
   private String name;
   private String nameToShow;
   private String localValue;
   private String navValue;
   private boolean navValid;
   private String validValue;

   public int getType() {
      return this.type;
   }

   public void setType(int var1) {
      this.type = var1;
   }

   public long getId() {
      return this.id;
   }

   public void setId(long var1) {
      this.id = var1;
   }

   public String getBlockName() {
      return this.blockName;
   }

   public void setBlockName(String var1) {
      this.blockName = var1;
   }

   public int getSeq() {
      return this.seq;
   }

   public void setSeq(int var1) {
      this.seq = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getNameToShow() {
      return this.nameToShow;
   }

   public void setNameToShow(String var1) {
      this.nameToShow = var1;
   }

   public String getLocalValue() {
      return this.localValue;
   }

   public void setLocalValue(String var1) {
      this.localValue = var1;
   }

   public String getNavValue() {
      return this.navValue;
   }

   public void setNavValue(String var1) {
      this.navValue = var1;
   }

   public boolean isNavValid() {
      return this.navValid;
   }

   public void setNavValid(boolean var1) {
      this.navValid = var1;
   }

   public String getValidValue() {
      return this.validValue;
   }

   public void setValidValue(String var1) {
      this.validValue = var1;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         MDMaintenanceModel var2 = (MDMaintenanceModel)var1;
         if (this.id != var2.id) {
            return false;
         } else if (this.navValid != var2.navValid) {
            return false;
         } else if (this.seq != var2.seq) {
            return false;
         } else if (!this.blockName.equals(var2.blockName)) {
            return false;
         } else if (!this.localValue.equals(var2.localValue)) {
            return false;
         } else if (!this.name.equals(var2.name)) {
            return false;
         } else if (!this.navValue.equals(var2.navValue)) {
            return false;
         } else {
            return this.validValue.equals(var2.validValue);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      long var1 = this.id;
      var1 = 31L * var1 + (long)this.blockName.hashCode();
      var1 = 31L * var1 + (long)this.seq;
      var1 = 31L * var1 + (long)this.name.hashCode();
      var1 = 31L * var1 + (long)this.localValue.hashCode();
      var1 = 31L * var1 + (long)this.navValue.hashCode();
      var1 = 31L * var1 + (long)(this.navValid ? 1 : 0);
      var1 = 31L * var1 + (long)this.validValue.hashCode();
      return (int)var1;
   }
}
