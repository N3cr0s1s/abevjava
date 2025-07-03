package hu.piller.enykp.alogic.masterdata.gui.entityfilter;

public class SelectorPartRule {
   private SelectorPartRule.BlockMapping[] mappings = new SelectorPartRule.BlockMapping[0];
   private String entityType;
   private String partName;

   public SelectorPartRule() {
   }

   public SelectorPartRule(String var1, String var2) {
      this.entityType = var1;
      this.partName = var2;
   }

   public void setPartName(String var1) {
      this.partName = var1;
   }

   public String getPartName() {
      return this.partName;
   }

   public void setEntityType(String var1) {
      this.entityType = var1;
   }

   public String getEntityType() {
      return this.entityType;
   }

   public SelectorPartRule.BlockMapping addBlockMapping() {
      SelectorPartRule.BlockMapping[] var1 = new SelectorPartRule.BlockMapping[this.mappings.length + 1];
      int var2 = 0;
      SelectorPartRule.BlockMapping[] var3 = this.mappings;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         SelectorPartRule.BlockMapping var6 = var3[var5];
         var1[var2++] = var6;
      }

      var1[var2] = new SelectorPartRule.BlockMapping();
      this.mappings = var1;
      return this.mappings[var2];
   }

   public SelectorPartRule.BlockMapping[] getBlockMappings() {
      return this.mappings;
   }

   public class BlockMapping {
      private String blockName;
      private String mdName;

      public String getBlockName() {
         return this.blockName;
      }

      public void setBlockName(String var1) {
         this.blockName = var1;
      }

      public String getMdName() {
         return this.mdName;
      }

      public void setMdName(String var1) {
         this.mdName = var1;
      }
   }
}
