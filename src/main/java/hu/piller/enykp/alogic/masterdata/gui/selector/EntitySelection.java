package hu.piller.enykp.alogic.masterdata.gui.selector;

public class EntitySelection {
   private long entity_id;
   private EntitySelection.BlockSelection[] selectedBlocks = new EntitySelection.BlockSelection[0];

   public void setEntityId(long var1) {
      this.entity_id = var1;
   }

   public long getEntityId() {
      return this.entity_id;
   }

   public void addBlockSelection(String var1, int var2) {
      EntitySelection.BlockSelection[] var3 = new EntitySelection.BlockSelection[this.selectedBlocks.length + 1];
      int var4 = 0;
      EntitySelection.BlockSelection[] var5 = this.selectedBlocks;
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         EntitySelection.BlockSelection var8 = var5[var7];
         var3[var4++] = var8;
      }

      var3[var4] = new EntitySelection.BlockSelection(var1, var2);
      this.selectedBlocks = var3;
   }

   public int[] getSelectedBlockSeqs(String var1) {
      int[] var2 = new int[this.numBlocksWithBlockName(var1)];
      int var3 = 0;
      EntitySelection.BlockSelection[] var4 = this.selectedBlocks;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         EntitySelection.BlockSelection var7 = var4[var6];
         if (var7.getBlockName().equals(var1)) {
            var2[var3++] = var7.getSeq();
         }
      }

      return var2;
   }

   private int numBlocksWithBlockName(String var1) {
      int var2 = 0;
      EntitySelection.BlockSelection[] var3 = this.selectedBlocks;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EntitySelection.BlockSelection var6 = var3[var5];
         if (var6.getBlockName().equals(var1)) {
            ++var2;
         }
      }

      return var2;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("entity_id: ");
      var1.append(this.entity_id);
      var1.append("\nblokk(ok):\n");
      EntitySelection.BlockSelection[] var2 = this.selectedBlocks;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EntitySelection.BlockSelection var5 = var2[var4];
         var1.append(var5);
         var1.append("\n");
      }

      return var1.toString();
   }

   public class BlockSelection {
      private String blockName;
      private int seq;

      public BlockSelection(String var2, int var3) {
         this.blockName = var2;
         this.seq = var3;
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

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append("[blokk: ");
         var1.append(this.blockName);
         var1.append(", seq: ");
         var1.append(this.seq);
         var1.append("]");
         return var1.toString();
      }
   }
}
