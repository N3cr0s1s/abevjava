package hu.piller.enykp.alogic.signer;

public class Hash {
   private String value;
   private HashType type;

   private Hash() {
   }

   public static Hash create(String var0, HashType var1) {
      Hash var2 = new Hash();
      var2.value = var0;
      var2.type = var1;
      return var2;
   }

   public String getValue() {
      return this.value;
   }

   public HashType getType() {
      return this.type;
   }

   public String toString() {
      return "Hash [value=" + this.value + ", type=" + this.type + "]";
   }

   public int hashCode() {
      byte var2 = 1;
      int var3 = 31 * var2 + (this.type == null ? 0 : this.type.hashCode());
      var3 = 31 * var3 + (this.value == null ? 0 : this.value.hashCode());
      return var3;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         Hash var2 = (Hash)var1;
         if (this.type != var2.type) {
            return false;
         } else {
            if (this.value == null) {
               if (var2.value != null) {
                  return false;
               }
            } else if (!this.value.equals(var2.value)) {
               return false;
            }

            return true;
         }
      }
   }
}
