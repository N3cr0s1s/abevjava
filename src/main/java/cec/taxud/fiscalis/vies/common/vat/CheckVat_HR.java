package cec.taxud.fiscalis.vies.common.vat;

public class CheckVat_HR implements ValidationRoutine {
   static final int LENGTH = 11;

   private boolean checkSumOk(String var1) {
      int var2 = 10;
      boolean var3 = false;

      int var4;
      for(var4 = 0; var4 < 10; ++var4) {
         int var6 = StringUtils.digitAt(var1, var4) + var2;
         int var5 = var6 % 10;
         if (var5 == 0) {
            var5 = 10;
         }

         var2 = 2 * var5 % 11;
      }

      var4 = 11 - var2;
      return var4 == 10 && StringUtils.digitAt(var1, 10) == 0 || var4 == StringUtils.digitAt(var1, 10);
   }

   public boolean check(String var1) {
      return var1.length() != 11 ? false : this.checkSumOk(var1);
   }
}
