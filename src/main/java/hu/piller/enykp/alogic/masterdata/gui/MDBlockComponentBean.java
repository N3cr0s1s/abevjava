package hu.piller.enykp.alogic.masterdata.gui;

public class MDBlockComponentBean {
   private String blockType;
   private String[] mdKeys;
   private String[][] data;

   public MDBlockComponentBean(String var1, String[] var2) {
      this.blockType = var1;
      this.mdKeys = var2;
      this.data = new String[0][var2.length];
   }

   public void setMDValue(int var1, String var2, String var3) {
      int var4 = this.findIdxByMDKey(var2);
      if (var4 >= 0) {
         this.data[var1 - 1][var4] = var3;
      }

   }

   public String getMDValue(int var1, String var2) {
      int var3 = this.findIdxByMDKey(var2);
      return var1 <= this.data.length && this.data.length > 0 && var3 >= 0 && var1 - 1 >= 0 ? this.data[var1 - 1][var3] : "";
   }

   public String getBlockType() {
      return this.blockType;
   }

   public int getSize() {
      return this.data.length;
   }

   private int findIdxByMDKey(String var1) {
      for(int var2 = 0; var2 < this.mdKeys.length; ++var2) {
         if (this.mdKeys[var2].equals(var1)) {
            return var2;
         }
      }

      return -1;
   }

   public void addEmptyDataRecord() {
      String[][] var1 = new String[this.data.length + 1][this.mdKeys.length];

      int var2;
      int var3;
      for(var2 = 0; var2 < this.data.length; ++var2) {
         for(var3 = 0; var3 < this.mdKeys.length; ++var3) {
            var1[var2][var3] = this.data[var2][var3];
         }
      }

      for(var3 = 0; var3 < this.mdKeys.length; ++var3) {
         var1[var2][var3] = "";
      }

      this.data = var1;
   }

   public void removeDataRecord(int var1) {
      if (var1 <= this.data.length) {
         --var1;
         String[][] var2 = new String[this.data.length - 1][this.mdKeys.length];

         int var3;
         int var4;
         for(var3 = 0; var3 < var1; ++var3) {
            for(var4 = 0; var4 < this.mdKeys.length; ++var4) {
               var2[var3][var4] = this.data[var3][var4];
            }
         }

         for(var3 = var1 + 1; var3 < this.data.length; ++var3) {
            for(var4 = 0; var4 < this.mdKeys.length; ++var4) {
               var2[var3 - 1][var4] = this.data[var3][var4];
            }
         }

         this.data = var2;
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer("");
      var1.append("Blokknév: ");
      var1.append(this.blockType);
      var1.append("\n");
      int var2 = 0;
      String[][] var3 = this.data;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String[] var6 = var3[var5];
         var1.append("blokk sorsz: ");
         var1.append(var2++);
         var1.append("\n");

         for(int var7 = 0; var7 < this.mdKeys.length; ++var7) {
            var1.append(this.mdKeys[var7]);
            var1.append(": ");
            var1.append(var6[var7]);
            var1.append("\n");
         }

         var1.append("\n");
      }

      return var1.toString();
   }
}
