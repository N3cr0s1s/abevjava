package hu.piller.enykp.alogic.masterdata.core;

public class BlockDefinition {
   private String blockName;
   private int min;
   private int max;
   private String masterDataNames;
   private BlockLayoutDef layoutDef;

   public BlockDefinition(String var1, int var2, int var3, String var4, BlockLayoutDef var5) {
      this.blockName = var1;
      this.min = var2;
      this.max = var3;
      this.masterDataNames = var4;
      this.layoutDef = var5;
   }

   public String getBlockName() {
      return this.blockName;
   }

   public int getMin() {
      return this.min;
   }

   public int getMax() {
      return this.max;
   }

   public BlockLayoutDef getBlockLayoutDef() {
      return this.layoutDef;
   }

   public String[] getMasterDataNames() {
      return this.masterDataNames.split(";");
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("[");
      var1.append(this.blockName);
      var1.append(" min: ");
      var1.append(this.min);
      var1.append(" max: ");
      var1.append(this.max);
      var1.append(" , torzsadat: ");
      var1.append(this.masterDataNames);
      var1.append("\n");
      var1.append(this.layoutDef);
      var1.append("\n]");
      return var1.toString();
   }
}
