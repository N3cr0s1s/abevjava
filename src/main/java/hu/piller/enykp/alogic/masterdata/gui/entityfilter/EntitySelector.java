package hu.piller.enykp.alogic.masterdata.gui.entityfilter;

public class EntitySelector {
   private long entity_id;
   private String entityType;
   private String[] selectorNames;
   private String[] selectorValues;

   public EntitySelector(String var1, long var2) {
      this.entity_id = var2;
      this.entityType = var1;
      this.selectorNames = new String[0];
      this.selectorValues = new String[0];
   }

   public long getEntityId() {
      return this.entity_id;
   }

   public String getEntityType() {
      return this.entityType;
   }

   public void setSelectorValue(String var1, String var2) {
      int var3;
      for(var3 = this.getIdxOfName(var1); var3 == -1; var3 = this.getIdxOfName(var1)) {
         this.selectorNames = this.expandAddValue(this.selectorNames, var1);
         this.selectorValues = this.expandAddValue(this.selectorValues, "");
      }

      this.selectorValues[var3] = var2;
   }

   public String getSelectorValue(String var1) {
      String var2 = "";
      int var3 = this.getIdxOfName(var1);
      if (var3 > -1) {
         var2 = this.selectorValues[var3];
      }

      return var2;
   }

   private String[] expandAddValue(String[] var1, String var2) {
      String[] var3 = new String[var1.length + 1];
      int var4 = 0;
      String[] var5 = var1;
      int var6 = var1.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String var8 = var5[var7];
         var3[var4++] = var8;
      }

      var3[var4] = var2 == null ? "" : var2;
      return var3;
   }

   private int getIdxOfName(String var1) {
      int var2 = -1;

      for(int var3 = 0; var3 < this.selectorNames.length; ++var3) {
         if (var1.equals(this.selectorNames[var3])) {
            var2 = var3;
            break;
         }
      }

      return var2;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("type: ");
      var1.append(this.entityType);
      var1.append(", id: ");
      var1.append(this.entity_id);
      var1.append(" {");
      String[] var2 = this.selectorNames;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         var1.append(var5);
         var1.append("=");
         var1.append(this.getSelectorValue(var5));
         var1.append(";");
      }

      var1.append("}");
      return var1.toString();
   }
}
