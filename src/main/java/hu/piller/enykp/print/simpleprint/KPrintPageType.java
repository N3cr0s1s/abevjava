package hu.piller.enykp.print.simpleprint;

public enum KPrintPageType {
   NORMAL("normál"),
   AN("AN"),
   AF("AF"),
   JOV("JOV"),
   AU("AU");

   private final String value;

   private KPrintPageType(String var3) {
      this.value = var3;
   }

   public String value() {
      return this.value;
   }

   public static KPrintPageType fromValue(String var0) {
      if (var0 != null && !var0.isEmpty()) {
         KPrintPageType[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            KPrintPageType var4 = var1[var3];
            if (var4.value.equals(var0)) {
               return var4;
            }
         }

         throw new IllegalArgumentException("A megadott értékhez nem tartozik KPrintPageType! érték: " + var0);
      } else {
         return NORMAL;
      }
   }
}
