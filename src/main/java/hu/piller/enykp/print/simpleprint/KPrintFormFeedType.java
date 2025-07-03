package hu.piller.enykp.print.simpleprint;

public enum KPrintFormFeedType {
   NO("no"),
   FIRST("first"),
   ALL("all");

   private final String value;

   private KPrintFormFeedType(String var3) {
      this.value = var3;
   }

   public String value() {
      return this.value;
   }

   public static KPrintFormFeedType fromValue(String var0) {
      if (var0 != null && !var0.isEmpty()) {
         KPrintFormFeedType[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            KPrintFormFeedType var4 = var1[var3];
            if (var4.value.equals(var0)) {
               return var4;
            }
         }

         throw new IllegalArgumentException("A megadott értékhez nem tartozik KPrintFormFeedType! érték: " + var0);
      } else {
         return NO;
      }
   }
}
