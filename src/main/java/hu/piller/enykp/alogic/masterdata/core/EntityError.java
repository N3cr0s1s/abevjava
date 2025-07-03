package hu.piller.enykp.alogic.masterdata.core;

public class EntityError {
   private String name;
   private String blockName;
   private int block_seq;
   private String mdKey;
   private String mdVal;
   private String errorMsg;

   public EntityError(String var1, String var2, int var3, String var4, String var5, String var6) {
      this.name = var1;
      this.blockName = var2;
      this.block_seq = var3;
      this.mdKey = var4;
      this.mdVal = var5;
      this.errorMsg = var6;
   }

   public String getName() {
      return this.name;
   }

   public String getBlockName() {
      return this.blockName;
   }

   public int getBlock_seq() {
      return this.block_seq;
   }

   public String getErrorMsg() {
      return this.errorMsg;
   }

   public String getMDKey() {
      return this.mdKey;
   }

   public String getMDVal() {
      return this.mdVal;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         EntityError var2 = (EntityError)var1;
         if (this.block_seq != var2.block_seq) {
            return false;
         } else {
            label72: {
               if (this.blockName != null) {
                  if (this.blockName.equals(var2.blockName)) {
                     break label72;
                  }
               } else if (var2.blockName == null) {
                  break label72;
               }

               return false;
            }

            if (this.errorMsg != null) {
               if (!this.errorMsg.equals(var2.errorMsg)) {
                  return false;
               }
            } else if (var2.errorMsg != null) {
               return false;
            }

            label58: {
               if (this.mdKey != null) {
                  if (this.mdKey.equals(var2.mdKey)) {
                     break label58;
                  }
               } else if (var2.mdKey == null) {
                  break label58;
               }

               return false;
            }

            if (this.mdVal != null) {
               if (!this.mdVal.equals(var2.mdVal)) {
                  return false;
               }
            } else if (var2.mdVal != null) {
               return false;
            }

            if (this.name != null) {
               if (!this.name.equals(var2.name)) {
                  return false;
               }
            } else if (var2.name != null) {
               return false;
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = this.name != null ? this.name.hashCode() : 0;
      var1 = 29 * var1 + (this.blockName != null ? this.blockName.hashCode() : 0);
      var1 = 29 * var1 + this.block_seq;
      var1 = 29 * var1 + (this.mdKey != null ? this.mdKey.hashCode() : 0);
      var1 = 29 * var1 + (this.mdVal != null ? this.mdVal.hashCode() : 0);
      var1 = 29 * var1 + (this.errorMsg != null ? this.errorMsg.hashCode() : 0);
      return var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer("");
      var1.append("[entity: ");
      var1.append(this.name);
      var1.append(", block: ");
      var1.append(this.blockName);
      var1.append(", bseq: ");
      var1.append(this.block_seq);
      var1.append(", mdKey: ");
      var1.append(this.mdKey);
      var1.append(", mdVal");
      var1.append(this.mdVal);
      var1.append(", msg: ");
      var1.append(this.errorMsg);
      var1.append("]");
      return var1.toString();
   }
}
